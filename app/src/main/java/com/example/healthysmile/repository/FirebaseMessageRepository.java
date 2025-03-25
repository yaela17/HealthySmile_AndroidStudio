package com.example.healthysmile.repository;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;
import java.util.function.Consumer;

public class FirebaseMessageRepository {
    private final FirebaseFirestore db;

    public FirebaseMessageRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // Método para cambiar el nombre de un usuario existente basado en el correo
    public void cambiarNombrePorCorreo(String correoUser, String nuevoNombre,
                                       final OnSuccessListener<Void> onSuccessListener,
                                       final OnFailureListener onFailureListener) {
        // Referencia al documento del usuario basado en el correoUser
        db.collection("usuarios")
                .whereEqualTo("correoUser", correoUser)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Obtenemos el documento del usuario
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = document.getId(); // Obtenemos el ID del documento

                        // Actualizamos el nombre del usuario
                        db.collection("usuarios")
                                .document(documentId)
                                .update("nomUser", nuevoNombre)
                                .addOnSuccessListener(onSuccessListener) // Llamada en caso de éxito
                                .addOnFailureListener(onFailureListener); // Llamada en caso de error
                    } else {
                        // Si no se encuentra el usuario, llamamos al onFailureListener
                        onFailureListener.onFailure(new Exception("Usuario no encontrado con el correo: " + correoUser));
                    }
                })
                .addOnFailureListener(onFailureListener); // Manejo de fallos en la consulta inicial
    }

    public void insertarPreguntaFrecuente(Map<String, Object> pregunta) {
        db.collection("preguntasFrecuentes")
                .add(pregunta)
                .addOnSuccessListener(documentReference -> {
                    // Acción después de insertar exitosamente
                    Log.d("Firestore", "Pregunta insertada con ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Acción si ocurre un error
                    Log.w("Firestore", "Error al agregar la pregunta", e);
                });
    }

    public void eliminarDocumento(String coleccion, long idUsuario, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection(coleccion)
                .whereEqualTo("idUsuario", idUsuario) // Cambiado de document(idDocumento)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        Log.d("EliminarDocumento", "Intentando eliminar documento en colección: " + coleccion + " con idUsuario: " + idUsuario + " y documentId: " + documentId);
                        db.collection(coleccion)
                                .document(documentId)
                                .delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        onSuccessListener.onSuccess(null); // Llama al listener de éxito
                                    } else {
                                        onFailureListener.onFailure(task.getException()); // Llama al listener de fallo
                                    }
                                });
                    } else {
                        onFailureListener.onFailure(new Exception("Usuario no encontrado")); // Maneja el caso de que no haya coincidencias
                    }
                })
                .addOnFailureListener(onFailureListener);
    }
}
