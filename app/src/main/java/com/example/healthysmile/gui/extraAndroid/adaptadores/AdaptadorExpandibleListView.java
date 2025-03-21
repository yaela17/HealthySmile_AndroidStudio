package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ayudaYSoporte.ResponderPreguntaResponse;
import com.example.healthysmile.service.ayudaYSoporte.ResponderPreguntaFrecuenteService;

import java.util.List;

public class AdaptadorExpandibleListView extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listPreguntas;
    private List<String> listRespuestas;
    private List<Long> listIdsPreguntasFrecuentes;

    public AdaptadorExpandibleListView(Context context, List<String> listPreguntas, List<String> listRespuestas,List<Long> listIdsPreguntasFrecuentes) {
        this.context = context;
        this.listPreguntas = listPreguntas;
        this.listRespuestas = listRespuestas;
        this.listIdsPreguntasFrecuentes = listIdsPreguntasFrecuentes;
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

        if (nivelPermisos >= 2) {
            EditText editTextRespuesta = convertView.findViewById(R.id.campoResponderPreguntaFrecuente);
            Button buttonEnviar = convertView.findViewById(R.id.enviarRespuestaPreguntaFrecuente);

            editTextRespuesta.setVisibility(View.VISIBLE);
            buttonEnviar.setVisibility(View.VISIBLE);

            buttonEnviar.setOnClickListener(v -> {
                String nuevaRespuesta = editTextRespuesta.getText().toString();
                if (!nuevaRespuesta.isEmpty()) {
                    long idPreguntaFrecuente = -1;
                    if (groupPosition >= 0 && groupPosition < listIdsPreguntasFrecuentes.size()) {
                        idPreguntaFrecuente = listIdsPreguntasFrecuentes.get(groupPosition);
                    } else {
                        Log.e("ResponderPregunta", "groupPosition fuera de rango");
                        return;
                    }

                    long idEspecialista = sharedPreferences.getLong("idEspecialista", -1);
                    if (idEspecialista == -1) {
                        Log.e("ResponderPregunta", "idEspecialista no encontrado");
                        return;
                    }

                    ResponderPreguntaFrecuenteService responderPreguntaFrecuenteService = new ResponderPreguntaFrecuenteService(context);
                    responderPreguntaFrecuenteService.responderPreguntaFrecuente(idPreguntaFrecuente, idEspecialista, nuevaRespuesta, new ResponderPreguntaResponse() {
                        @Override
                        public void onResponse(String mensaje) {
                            listRespuestas.set(groupPosition, nuevaRespuesta);
                            notifyDataSetChanged();
                            Log.d("ResponderPregunta", "Respuesta actualizada correctamente.");
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("ResponderPregunta", error);
                            Toast.makeText(context, "Error al responder la pregunta.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPreguntaNoEncontrada(String mensaje) {
                            Log.e("ResponderPregunta", mensaje);
                            Toast.makeText(context, "Pregunta no encontrada.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("ResponderPregunta", "La respuesta no puede estar vacía.");
                    Toast.makeText(context, "Por favor, ingresa una respuesta.", Toast.LENGTH_SHORT).show();
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
