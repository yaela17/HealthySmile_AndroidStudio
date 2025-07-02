package com.example.healthysmile.gui.consulta.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.model.entities.MensajeLocalDB;
import com.example.healthysmile.model.entities.Message;
import com.example.healthysmile.gui.extraAndroid.adaptadores.MessageAdapter;
import com.example.healthysmile.R;
import com.example.healthysmile.repository.MensajeRepository;
import com.example.healthysmile.utils.ImageUtils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Fragment_consulta_virtual_especialista extends Fragment {

    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private FirebaseFirestore db;
    private MensajeRepository mensajeRepository;


    private EditText messageInput;
    private Button sendButton;

    long idUsuarioChat, idEspecialistaChat;
    String tipoUsuario, nombreReceptorChat,fotoPerfilReceptorChat;
    ImageView fotoPerfilChat;
    LinearLayout contenedorToolBarFotoPerfil;
    TextView nombreToolBarReceptor;

    private ListenerRegistration messagesListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_virtual_especialista, container, false);

        androidx.appcompat.widget.Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        contenedorToolBarFotoPerfil = toolbar.findViewById(R.id.top_bar_contenedor_perfil_chat);
        fotoPerfilChat = toolbar.findViewById(R.id.top_bar_foto_perfil);
        nombreToolBarReceptor = toolbar.findViewById(R.id.top_bar_nombre_chat_receptor);
        contenedorToolBarFotoPerfil.setVisibility(View.VISIBLE);
        mensajeRepository = new MensajeRepository(requireContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        idUsuarioChat = sharedPreferences.getLong("idUsuario", 0);
        idEspecialistaChat = sharedPreferences.getLong("idEspecialistaChat", 0);
        nombreReceptorChat = sharedPreferences.getString("nombreReceptorChat","null");
        tipoUsuario = sharedPreferences.getString("tipoUsuario","Paciente");
        fotoPerfilReceptorChat = sharedPreferences.getString("fotoPerfilReceptorChat","No disponible");

        nombreToolBarReceptor.setText(nombreReceptorChat);
        ImageUtils imageUtils = new ImageUtils();

        if(tipoUsuario.equals("Especialista")){
            idUsuarioChat = sharedPreferences.getLong("idEspecialistaChat",0);
            idEspecialistaChat = sharedPreferences.getLong("idEspecialista",0);
            if("No disponible".equals(fotoPerfilReceptorChat)){
                fotoPerfilChat.setImageResource(R.drawable.default_photo_perfil_paciente);
            }else {
                imageUtils.cargarImagenConGlide(getContext(),fotoPerfilReceptorChat,fotoPerfilChat,"Perfil");
            }
        }else if(tipoUsuario.equals("Paciente")){
            if("No disponible".equals(fotoPerfilReceptorChat)){
                fotoPerfilChat.setImageResource(R.drawable.default_photo_perfil_especialista);
            }else {
                imageUtils.cargarImagenConGlide(getContext(),fotoPerfilReceptorChat,fotoPerfilChat,"Perfil");
            }
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
                activity.getSupportActionBar().setTitle("");
            }
        }

        return view;
    }

    private void cargarMensajes() {
        Log.d("ChatDebug", "Iniciando carga de mensajes...");

        if (!hayConexionInternet()) {
            Log.d("ChatDebug", "Sin conexión a internet. Cargando mensajes desde Room...");
            Toast.makeText(getContext(), "No hay conexión a internet. Cargando mensajes locales.", Toast.LENGTH_LONG).show();

            // Ejecutar la consulta a Room en un hilo de fondo
            Executors.newSingleThreadExecutor().execute(() -> {
                List<MensajeLocalDB> mensajesLocales = mensajeRepository.obtenerMensajesRecientes(
                        idUsuarioChat, idEspecialistaChat, 100
                );

                Log.d("ChatDebug", "Mensajes locales encontrados: " + mensajesLocales.size());

                List<Message> mensajesConvertidos = new ArrayList<>();
                for (MensajeLocalDB mensajeLocal : mensajesLocales) {
                    Log.d("ChatDebug", "Mensaje local -> " + mensajeLocal.getMensaje());
                    Message mensaje = new Message(
                            mensajeLocal.getIdEspecialista(),
                            mensajeLocal.getIdUsuario(),
                            mensajeLocal.getDestinatario(),
                            mensajeLocal.getEmisor(),
                            mensajeLocal.getFecha(),
                            mensajeLocal.getMensaje()
                    );
                    mensajesConvertidos.add(mensaje);
                }

                // Ordenar por fecha
                mensajesConvertidos.sort((m1, m2) -> m1.getFecha().compareTo(m2.getFecha()));
                Log.d("ChatDebug", "Lista ordenada por fecha.");

                // Volver al hilo principal para actualizar la UI
                new Handler(Looper.getMainLooper()).post(() -> {
                    messageList.clear();
                    messageList.addAll(mensajesConvertidos);
                    messageAdapter.notifyDataSetChanged();
                    messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                    Log.d("ChatDebug", "UI actualizada con mensajes offline.");
                });
            });

            return;
        }

        // SI HAY INTERNET: consultar Firestore normalmente
        Log.d("ChatDebug", "Conexión a internet detectada. Consultando Firestore...");
        db.collection("chats")
                .whereEqualTo("idUsuario", idUsuarioChat)
                .whereEqualTo("idEspecialista", idEspecialistaChat)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot chatDocument = querySnapshot.getDocuments().get(0);
                        String chatId = chatDocument.getId();
                        Log.d("ChatDebug", "Chat Firestore encontrado. ID: " + chatId);

                        configurarListenerMensajes(chatId);
                    } else {
                        Log.d("ChatDebug", "No se encontró el chat en Firestore.");
                    }
                })
                .addOnFailureListener(e -> Log.e("ChatDebug", "Error al consultar Firestore para mensajes", e));
    }


    private void configurarListenerMensajes(String chatId) {
        Log.d("ChatDebug", "Configurando listener para chatId: " + chatId);

        if (messagesListener != null) {
            Log.d("ChatDebug", "Eliminando listener anterior...");
            messagesListener.remove();
        }

        messagesListener = db.collection("chats")
                .document(chatId)
                .collection("mensajes")
                .orderBy("fecha", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("ChatDebug", "Error en snapshot listener", e);
                        return;
                    }

                    if (snapshots != null) {
                        Log.d("ChatDebug", "Mensajes recibidos desde Firestore: " + snapshots.size());

                        messageList.clear();

                        for (DocumentSnapshot document : snapshots.getDocuments()) {
                            Message message = document.toObject(Message.class);
                            if (message != null) {
                                Log.d("ChatDebug", "Mensaje Firestore -> " + message.getMensaje());
                                messageList.add(message);

                                // Guardar localmente
                                MensajeLocalDB mensajeLocal = new MensajeLocalDB(
                                        idEspecialistaChat,
                                        idUsuarioChat,
                                        message.getDestinatario(),
                                        message.getEmisor(),
                                        message.getFecha(),
                                        message.getMensaje()
                                );
                                mensajeRepository.insertarMensaje(mensajeLocal);
                            }
                        }

                        messageAdapter.notifyDataSetChanged();
                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }

    private void enviarMensajeSocket(String texto) {
        Log.d("ChatDebug", "Intentando enviar mensaje: " + texto);

        if (!hayConexionInternet()) {
            Toast.makeText(getContext(), "No hay conexión a internet. No se puede enviar el mensaje.", Toast.LENGTH_LONG).show();
            Log.d("ChatDebug", "Cancelado: sin conexión");
            return;
        }

        db.collection("chats")
                .whereEqualTo("idUsuario", idUsuarioChat)
                .whereEqualTo("idEspecialista", idEspecialistaChat)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String chatId = querySnapshot.getDocuments().get(0).getId();
                        Log.d("ChatDebug", "Chat ya existente. ID: " + chatId);
                        guardarMensaje(chatId, texto);
                    } else {
                        Log.d("ChatDebug", "No hay chat. Creando nuevo...");
                        Map<String, Object> nuevoChat = new HashMap<>();
                        nuevoChat.put("idUsuario", idUsuarioChat);
                        nuevoChat.put("idEspecialista", idEspecialistaChat);

                        db.collection("chats").add(nuevoChat)
                                .addOnSuccessListener(documentReference -> {
                                    String nuevoChatId = documentReference.getId();
                                    Log.d("ChatDebug", "Nuevo chat creado. ID: " + nuevoChatId);
                                    guardarMensaje(nuevoChatId, texto);
                                    configurarListenerMensajes(nuevoChatId);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Error al crear chat", Toast.LENGTH_SHORT).show();
                                    Log.e("ChatDebug", "Error al crear chat", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener chat", Toast.LENGTH_SHORT).show();
                    Log.e("ChatDebug", "Error al obtener chat", e);
                });
    }

    private void guardarMensaje(String chatId, String texto) {
        Log.d("ChatDebug", "Guardando mensaje en chatId: " + chatId);

        Date fechaActual = new Date();
        Map<String, Object> nuevoMensaje = new HashMap<>();
        nuevoMensaje.put("mensaje", texto);
        nuevoMensaje.put("fecha", fechaActual);

        Long emisorId = tipoUsuario.equals("Paciente") ? idUsuarioChat : idEspecialistaChat;
        Long destinatarioId = tipoUsuario.equals("Paciente") ? idEspecialistaChat : idUsuarioChat;

        nuevoMensaje.put("emisor", emisorId);
        nuevoMensaje.put("destinatario", destinatarioId);

        db.collection("chats").document(chatId).collection("mensajes")
                .add(nuevoMensaje)
                .addOnSuccessListener(documentReference -> {
                    Log.d("ChatDebug", "Mensaje guardado en Firestore");
                    messageInput.setText("");

                    MensajeLocalDB mensajeLocal = new MensajeLocalDB(
                            idEspecialistaChat,
                            idUsuarioChat,
                            destinatarioId,
                            emisorId,
                            fechaActual,
                            texto
                    );
                    mensajeRepository.insertarMensaje(mensajeLocal);
                    Log.d("ChatDebug", "Mensaje también guardado localmente");

                    Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                    Log.e("ChatDebug", "Error al guardar mensaje", e);
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (messagesListener != null) {
            messagesListener.remove();
        }

        if (contenedorToolBarFotoPerfil != null) {
            contenedorToolBarFotoPerfil.setVisibility(View.GONE);
        }
    }

    private boolean hayConexionInternet() {
        return true;
    }

}
