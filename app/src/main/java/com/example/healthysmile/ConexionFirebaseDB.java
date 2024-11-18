package com.example.healthysmile;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;

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

    // Método para registrar un nuevo usuario
    public void registrarUsuario(Map<String, Object> user, final OnSuccessListener<DocumentReference> onSuccessListener, final OnFailureListener onFailureListener) {
        db.collection("usuarios")
                .add(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}
