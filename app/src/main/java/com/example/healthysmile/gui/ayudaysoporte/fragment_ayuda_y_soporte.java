package com.example.healthysmile.gui.ayudaysoporte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.gui.Adaptadores.AdaptadorExpandibleListView;
import com.example.healthysmile.R;
import com.example.healthysmile.repository.FirebaseMessageRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_ayuda_y_soporte extends Fragment implements View.OnClickListener {

    Button btnEnviar;
    EditText campoNombre, campoCorreo, campoPregunta;
    ExpandableListView preguntasFrecuentesLV;

    private FirebaseMessageRepository conexionFirebaseDB;

    // Listas para almacenar las preguntas y respuestas
    private List<String> preguntasList = new ArrayList<>();
    private List<String> respuestasList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda_y_soporte, container, false);

        campoNombre = view.findViewById(R.id.formPreguntaFrecuenteNombre);
        campoCorreo = view.findViewById(R.id.formPreguntaFrecuenteCorreo);
        campoPregunta = view.findViewById(R.id.formPreguntaFrecuentePregunta);
        btnEnviar = view.findViewById(R.id.formPreguntaFrecuenteBtnEnviar);
        preguntasFrecuentesLV = view.findViewById(R.id.expandableListViewPreguntasFrecuentes);

        conexionFirebaseDB = new FirebaseMessageRepository(); // Inicializamos la conexión a Firebase
        btnEnviar.setOnClickListener(this);

        // Llamada para cargar las preguntas frecuentes de Firebase
        cargarPreguntasFrecuentes();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.formPreguntaFrecuenteBtnEnviar) {

            final String correoUsuario = campoCorreo.getText().toString().trim();
            final String nombreUsuario = campoNombre.getText().toString().trim();
            final String pregunta = campoPregunta.getText().toString().trim();

            // Verificar si el correo existe en la base de datos
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("usuarios")
                    .whereEqualTo("correoUser", correoUsuario) // Verificamos el correoUser
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Si el correo existe, insertamos la nueva pregunta frecuente
                            insertarPreguntaFrecuente(correoUsuario, pregunta);
                        } else {
                            // Si el correo no existe, mostramos un mensaje de error
                            campoCorreo.setError("Correo no registrado en la aplicación");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Manejo de errores en la consulta
                        e.printStackTrace();
                    });
        }
    }

    // Método para insertar la pregunta frecuente en Firestore
    private void insertarPreguntaFrecuente(String correoUsuario, String pregunta) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Creamos un nuevo documento para la pregunta frecuente
        Map<String, Object> preguntaFrecuente = new HashMap<>();
        preguntaFrecuente.put("correoEsp", "");  // El correoEsp lo dejamos vacío inicialmente
        preguntaFrecuente.put("busquedas", 0);  // Inicializamos las búsquedas a 0
        preguntaFrecuente.put("pregunta", pregunta);
        preguntaFrecuente.put("respuesta", "");  // La respuesta se deja vacía al principio

        // Insertamos el nuevo documento en la colección 'preguntasFrecuentes'
        db.collection("preguntasFrecuentes")
                .add(preguntaFrecuente)
                .addOnSuccessListener(documentReference -> {
                    vaciarCampos();
                    // Aquí podrías mostrar un mensaje o hacer alguna otra acción
                    cargarPreguntasFrecuentes();  // Recargar las preguntas después de la inserción
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores
                    e.printStackTrace();
                });
    }

    // Método para vaciar los campos EditText
    private void vaciarCampos() {
        campoNombre.setText("");  // Limpia el campo de nombre
        campoCorreo.setText("");  // Limpia el campo de correo
        campoPregunta.setText("");  // Limpia el campo de pregunta
    }

    private void cargarPreguntasFrecuentes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("preguntasFrecuentes")
                .orderBy("busquedas", com.google.firebase.firestore.Query.Direction.DESCENDING) // Ordenar por busquedas de mayor a menor
                .limit(5) // Limitar a las 5 preguntas con más búsquedas
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        preguntasList.clear();
                        respuestasList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Agregamos las preguntas y respuestas a las listas
                            String pregunta = document.getString("pregunta");
                            String respuesta = document.getString("respuesta");

                            // Si no hay respuesta, ponemos un valor por defecto
                            if (respuesta == null) {
                                respuesta = "No disponible";
                            }

                            preguntasList.add(pregunta);
                            respuestasList.add(respuesta);
                        }

                        // Ahora enviamos los datos al adaptador
                        AdaptadorExpandibleListView adaptador = new AdaptadorExpandibleListView(
                                getContext(), preguntasList, respuestasList
                        );
                        preguntasFrecuentesLV.setAdapter(adaptador);
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores en la consulta
                    e.printStackTrace();
                });
    }

}
