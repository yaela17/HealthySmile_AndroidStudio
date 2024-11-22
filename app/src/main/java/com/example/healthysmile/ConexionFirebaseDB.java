package com.example.healthysmile;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

public class ConexionFirebaseDB {
    private final FirebaseFirestore db;

    public ConexionFirebaseDB() {
        db = FirebaseFirestore.getInstance();
    }

    // Método para verificar credenciales de inicio de sesión y obtener los datos del usuario
    public void verificarCredenciales(String correoUsuario, String contrasenaUsuario, Consumer<CredentialCallback> callback) {
        // Cambiar la búsqueda por "correoUser" en lugar de "nomUser"
        db.collection("usuarios")
                .whereEqualTo("correoUser", correoUsuario)  // Buscar por correo
                .whereEqualTo("contrasenaUser", contrasenaUsuario)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // El usuario fue encontrado
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        callback.accept(new CredentialCallback(true, document));
                    } else {
                        // No se encontró el usuario
                        callback.accept(new CredentialCallback(false, null));
                    }
                })
                .addOnFailureListener(e -> callback.accept(new CredentialCallback(false, null)));
    }

    // Clase para almacenar el resultado de la verificación
    public static class CredentialCallback {
        public final boolean isValid;
        public final DocumentSnapshot usuario;

        public CredentialCallback(boolean isValid, DocumentSnapshot usuario) {
            this.isValid = isValid;
            this.usuario = usuario;
        }
    }

    // Método para registrar un usuario, generando un nuevo ID de usuario automáticamente
    public void registrarUsuario(Map<String, Object> user, final OnSuccessListener<DocumentReference> onSuccessListener, final OnFailureListener onFailureListener) {
        // Consultamos la colección para obtener el último paciente registrado
        db.collection("usuarios")
                .orderBy("idUsuario", Query.Direction.DESCENDING) // Ordenamos de forma descendente por el ID
                .limit(1) // Obtenemos solo el último documento
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Si hay resultados, extraemos el último ID de paciente
                        String lastId = task.getResult().getDocuments().get(0).getString("idUsuario");
                        int lastNumber = Integer.parseInt(lastId.replaceAll("\\D+", "")); // Extraemos el número del ID (ej. Paciente13 -> 13)
                        int newIdNumber = lastNumber + 1; // Incrementamos el número para el nuevo ID

                        // Creamos el nuevo ID con el formato PacienteN
                        String newId = "Paciente" + newIdNumber;

                        // Asignamos el nuevo ID al mapa de datos del usuario
                        user.put("idUsuario", newId);

                        // Registramos el nuevo usuario con el nuevo ID
                        db.collection("usuarios")
                                .add(user)
                                .addOnSuccessListener(onSuccessListener)
                                .addOnFailureListener(onFailureListener);
                    } else {
                        // Si no hay usuarios, asignamos el primer ID: Paciente1
                        user.put("idUsuario", "Paciente1");
                        db.collection("usuarios")
                                .add(user)
                                .addOnSuccessListener(onSuccessListener)
                                .addOnFailureListener(onFailureListener);
                    }
                });
    }

    // Método para enviar un mensaje a un chat determinado, usando el ID del documento
    public void enviarMensaje(String texto, Usuario paciente, String chatId) {
        Message message = new Message();
        message.setRemitenteId(paciente.getIdUsuario());  // Usamos el idUsuario del paciente

        // Obtenemos el ID del documento del destinatario (ejemplo con "destinatarioId" que debe ser buscado)
        db.collection("usuarios")
                .whereEqualTo("idUsuario", "destinatarioId")  // Aquí se busca el usuario por idUsuario
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot destinatarioDoc = queryDocumentSnapshots.getDocuments().get(0);
                        String destinatarioId = destinatarioDoc.getId();  // Obtén el ID del documento

                        message.setDestinatarioId(destinatarioId);  // Usamos el ID del documento del destinatario
                        message.setText(texto);
                        message.setTimestamp(new Date());

                        db.collection("chats").document(chatId).collection("messages")
                                .add(message)
                                .addOnSuccessListener(documentReference -> {
                                    // Mensaje enviado con éxito
                                })
                                .addOnFailureListener(e -> {
                                    // Manejo de errores
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores
                });
    }
}
