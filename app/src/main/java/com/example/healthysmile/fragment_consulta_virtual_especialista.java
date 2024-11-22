package com.example.healthysmile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.util.ArrayList;
import java.util.List;

public class fragment_consulta_virtual_especialista extends Fragment {

    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private FirebaseFirestore db;
    private String chatId;
    private Usuario paciente;

    private EditText messageInput;
    private Button sendButton;

    private Socket socket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consulta_virtual_especialista, container, false);

        // Obtener los datos del paciente desde SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("idPaciente", null);
        String nombre = sharedPreferences.getString("nombrePaciente", null);
        String correo = sharedPreferences.getString("correoPaciente", null);
        String foto = sharedPreferences.getString("fotoPaciente", null);
        paciente = new Usuario(id, nombre, correo, null, "Paciente", foto);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Configurar RecyclerView
        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesRecyclerView.setAdapter(messageAdapter);

        // Obtener el chatId (este debe ser dinámico en función de la conversación)
        chatId = "chatId";  // Aquí debes obtener dinámicamente el chatId

        // Cargar mensajes desde Firestore
        cargarMensajes();

        // Configurar el campo de entrada y el botón de enviar mensaje
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String textoMensaje = messageInput.getText().toString();
            if (!textoMensaje.isEmpty()) {
                // Enviar el mensaje por WebSocket
                enviarMensajeSocket(textoMensaje);
            } else {
                Toast.makeText(getContext(), "No puedes enviar un mensaje vacío", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar WebSocket
        configurarSocket();

        return view;
    }

    // Método para configurar WebSocket
    private void configurarSocket() {
        try {
            // Conectar al servidor WebSocket (asegúrate de usar la IP correcta del servidor en producción)
            socket = IO.socket("http://localhost:3000");
            socket.connect();

            // Escuchar el evento de recibir un mensaje
            socket.on("recibirMensaje", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String mensaje = (String) args[0];
                    getActivity().runOnUiThread(() -> {
                        // Actualizar la UI con el mensaje recibido
                        Message newMessage = new Message("especialistaId", paciente.getIdUsuario(), mensaje, new java.util.Date());
                        messageList.add(newMessage);
                        messageAdapter.notifyDataSetChanged();
                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);  // Hacer scroll hasta el último mensaje
                    });
                }
            });

        } catch (Exception e) {
            Log.e("SocketIO", "Error al conectar con WebSocket", e);
        }
    }

    private void cargarMensajes() {
        db.collection("chats").document(chatId).collection("messages")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Message message = document.toObject(Message.class);
                            messageList.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();  // Actualiza el adapter
                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);  // Hacer scroll al último mensaje
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al cargar mensajes", e);
                });
    }



    // Método para enviar un mensaje por WebSocket
    private void enviarMensajeSocket(String texto) {
        String destinatarioId = "especialistaId";  // Este ID debe ser dinámico (de algún modo obtienes el ID del especialista)

        // Crear un nuevo mensaje
        Message message = new Message(paciente.getIdUsuario(), destinatarioId, texto, new java.util.Date());

        // Enviar el mensaje a través de WebSocket
        if (socket != null) {
            socket.emit("enviarMensaje", texto);
        }

        // Guardar el mensaje en Firestore
        db.collection("chats").document(chatId).collection("messages")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    messageInput.setText("");  // Limpiar el campo de texto
                    Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();

                    // Recargar los mensajes para que se visualicen en la interfaz
                    cargarMensajes();  // Recargar los mensajes
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desconectar el socket cuando se destruya el fragmento
        if (socket != null) {
            socket.disconnect();
        }
    }
}
