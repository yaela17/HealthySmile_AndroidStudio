package com.example.healthysmile.gui.consulta.chat;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.model.entities.Message;
import com.example.healthysmile.gui.extraAndroid.adaptadores.MessageAdapter;
import com.example.healthysmile.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_consulta_virtual_especialista extends Fragment {

    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private FirebaseFirestore db;

    private EditText messageInput;
    private Button sendButton;

    long idUsuarioChat, idEspecialistaChat;
    String tipoUsuario, nombreReceptorChat;

    private ListenerRegistration messagesListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_virtual_especialista, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        idUsuarioChat = sharedPreferences.getLong("idUsuario", 0);
        idEspecialistaChat = sharedPreferences.getLong("idEspecialistaChat", 0);
        nombreReceptorChat = sharedPreferences.getString("nombreReceptorChat","null");
        tipoUsuario = sharedPreferences.getString("tipoUsuario","Paciente");

        if(tipoUsuario.equals("Especialista")){
            idUsuarioChat = sharedPreferences.getLong("idEspecialistaChat",0);
            idEspecialistaChat = sharedPreferences.getLong("idEspecialista",0);
        }

        long idEmisorActual = 0;
        if(tipoUsuario.equals("Especialista")){
            idEmisorActual = idEspecialistaChat;
        }else
            if(tipoUsuario.equals("Paciente")){
                idEmisorActual = idUsuarioChat;
            }
        Log.d("ConsultaVirtual", "idUsuarioChat: " + idUsuarioChat);
        Log.d("ConsultaVirtual", "idEspecialistaChat: " + idEspecialistaChat);
        Log.d("ConsultaVirtual", "nombreReceptorChat: " + nombreReceptorChat);
        Log.d("ConsultaVirtual", "tipoUsuario: " + tipoUsuario);
        Log.d("ConsultaVirtual", "idEmisorActual: " + idEmisorActual);


        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Configurar RecyclerView
        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        messageAdapter = new MessageAdapter(messageList,idEmisorActual);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesRecyclerView.setAdapter(messageAdapter);

        // Cargar mensajes desde Firestore
        cargarMensajes();

        // Configurar el campo de entrada y el botón de enviar mensaje
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String textoMensaje = messageInput.getText().toString();
            if (!textoMensaje.isEmpty()) {
                enviarMensajeSocket(textoMensaje);
            } else {
                Toast.makeText(getContext(), "No puedes enviar un mensaje vacío", Toast.LENGTH_SHORT).show();
            }
        });

        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(nombreReceptorChat);
            }
        }

        return view;
    }

    private void cargarMensajes() {
        db.collection("chats")
                .whereEqualTo("idUsuario", idUsuarioChat)
                .whereEqualTo("idEspecialista", idEspecialistaChat)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot chatDocument = querySnapshot.getDocuments().get(0);
                        String chatId = chatDocument.getId();
                        Log.d("Consulta", "Chat encontrado: " + chatId);

                        // Escuchar cambios en tiempo real en la colección de mensajes
                        messagesListener = db.collection("chats")
                                .document(chatId)
                                .collection("mensajes")
                                .orderBy("fecha", Query.Direction.ASCENDING)
                                .addSnapshotListener((snapshots, e) -> {
                                    if (e != null) {
                                        Log.e("Firestore", "Error en el listener de mensajes", e);
                                        return;
                                    }

                                    if (snapshots != null) {
                                        messageList.clear(); // Limpiar la lista antes de actualizarla
                                        for (DocumentSnapshot document : snapshots.getDocuments()) {
                                            Message message = document.toObject(Message.class);
                                            messageList.add(message);
                                        }
                                        messageAdapter.notifyDataSetChanged();
                                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                                    }
                                });
                    } else {
                        Log.d("Consulta", "No se encontró el chat.");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error al cargar mensajes", e));
    }


    private void enviarMensajeSocket(String texto) {
        db.collection("chats")
                .whereEqualTo("idUsuario", idUsuarioChat)
                .whereEqualTo("idEspecialista", idEspecialistaChat)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot chatDocument = querySnapshot.getDocuments().get(0);
                        String chatId = chatDocument.getId();
                        guardarMensaje(chatId, texto);
                    } else {
                        // Crear un nuevo chat si no existe
                        Map<String, Object> nuevoChat = new HashMap<>();
                        nuevoChat.put("idUsuario", idUsuarioChat);
                        nuevoChat.put("idEspecialista", idEspecialistaChat);

                        db.collection("chats").add(nuevoChat)
                                .addOnSuccessListener(documentReference -> {
                                    guardarMensaje(documentReference.getId(), texto);
                                    cargarMensajesNuevoChat(documentReference.getId());
                                });
                    }
                });
    }

    private void guardarMensaje(String chatId, String texto) {
        Map<String, Object> nuevoMensaje = new HashMap<>();
        nuevoMensaje.put("mensaje", texto);
        nuevoMensaje.put("fecha", new java.util.Date());
        if(tipoUsuario.equals("Paciente")){
            nuevoMensaje.put("emisor", idUsuarioChat);
            nuevoMensaje.put("destinatario", idEspecialistaChat);
        }else
            if(tipoUsuario.equals("Especialista")){
                nuevoMensaje.put("emisor", idEspecialistaChat);
                nuevoMensaje.put("destinatario", idUsuarioChat);
            }

        db.collection("chats").document(chatId).collection("mensajes")
                .add(nuevoMensaje)
                .addOnSuccessListener(documentReference -> {
                    messageInput.setText(""); // Limpiar el campo de texto
                    Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarMensajesNuevoChat(String chatId) {
        // Escuchar cambios en tiempo real en la colección de mensajes del nuevo chat
        messagesListener = db.collection("chats")
                .document(chatId)
                .collection("mensajes")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Error en el listener de mensajes", e);
                        return;
                    }

                    if (snapshots != null) {
                        messageList.clear();
                        for (DocumentSnapshot document : snapshots.getDocuments()) {
                            Message message = document.toObject(Message.class);
                            messageList.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();
                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (messagesListener != null) {
            messagesListener.remove();
        }
    }

}
