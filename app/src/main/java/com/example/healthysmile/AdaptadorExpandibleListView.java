package com.example.healthysmile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorExpandibleListView extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listPreguntas;
    private List<String> listRespuestas;  // Cambiar Map a List

    // Constructor para el adaptador
    public AdaptadorExpandibleListView(Context context, List<String> listPreguntas, List<String> listRespuestas) {
        this.context = context;
        this.listPreguntas = listPreguntas;
        this.listRespuestas = listRespuestas;
    }

    @Override
    public int getGroupCount() {
        return listPreguntas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1; // Un solo ítem por grupo (pregunta y su respuesta)
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listPreguntas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listRespuestas.get(groupPosition);  // Aquí ya no usamos un Map, sino que accedemos directamente a la lista
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.preguntafrecuente, null);
        }

        TextView textPregunta = convertView.findViewById(R.id.preguntaFrecuentePlantilla);
        textPregunta.setText(listPreguntas.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.desplegadopreguntafrecuente, null);
        }

        TextView textRespuesta = convertView.findViewById(R.id.respuestaPreguntaFrecuentePlantilla);
        String respuesta = listRespuestas.get(groupPosition);

        // Mostrar la respuesta o "Pregunta sin respuesta" en rojo
        if (respuesta.isEmpty()) {
            textRespuesta.setText("Pregunta sin respuesta");
            textRespuesta.setTextColor(Color.RED);
        } else {
            textRespuesta.setText(respuesta);
            textRespuesta.setTextColor(Color.BLACK);
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        long nivelPermisos = sharedPreferences.getLong("nivelPermisos", 0);
        String correoUsuario = sharedPreferences.getString("correoUsuario", null);

        // Solo si el nivel de permisos es mayor a 2, mostrar el área de texto y el botón
        if (nivelPermisos >= 2) {
            EditText editTextRespuesta = convertView.findViewById(R.id.campoResponderPreguntaFrecuente);
            Button buttonEnviar = convertView.findViewById(R.id.enviarRespuestaPreguntaFrecuente);

            editTextRespuesta.setVisibility(View.VISIBLE);
            buttonEnviar.setVisibility(View.VISIBLE);

            buttonEnviar.setOnClickListener(v -> {
                String nuevaRespuesta = editTextRespuesta.getText().toString();
                if (!nuevaRespuesta.isEmpty() && correoUsuario != null) {
                    // Actualizamos la respuesta directamente
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("preguntasFrecuentes")
                            .whereEqualTo("pregunta", listPreguntas.get(groupPosition))
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentId = documentSnapshot.getId(); // Obtenemos el ID del documento

                                    Map<String, Object> updateData = new HashMap<>();
                                    updateData.put("respuesta", nuevaRespuesta);
                                    updateData.put("correoEsp", correoUsuario);

                                    db.collection("preguntasFrecuentes")
                                            .document(documentId)
                                            .update(updateData)
                                            .addOnSuccessListener(aVoid -> {
                                                listRespuestas.set(groupPosition, nuevaRespuesta);  // Actualizamos la lista directamente
                                                notifyDataSetChanged();
                                            })
                                            .addOnFailureListener(e -> e.printStackTrace());
                                }
                            });
                }
            });
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
