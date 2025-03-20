package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.repository.FirebaseMessageRepository;
import com.example.healthysmile.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdaptadorBorrarEspecialista extends BaseAdapter {

    private Context contexto;
    private String[] nombres;
    private String[] especialidades;
    private String[] descripciones;
    private long[] idsEspecialista;
    private LayoutInflater inflater;

    public AdaptadorBorrarEspecialista(Context contexto,Fragment fragment, String[] nombres, String[] especialidades, String[] descripciones, long[] idsEspecialista) {
        this.contexto = contexto;
        this.nombres = nombres;
        this.especialidades = especialidades;
        this.descripciones = descripciones;
        this.idsEspecialista = idsEspecialista;
        this.inflater = LayoutInflater.from(contexto);
    }

    @Override
    public int getCount() {
        return nombres.length;
    }

    @Override
    public Object getItem(int position) {
        return nombres[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.platilla_especialista_borrar, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.plantillaBorrarEspecialistaFotoPerfil);
        TextView textNombre = convertView.findViewById(R.id.plantillaBorrarEspecialistaNombre);
        TextView textEspecialidad = convertView.findViewById(R.id.plantillaBorrarEspecialistaEspecialidad);
        TextView textDescripcion = convertView.findViewById(R.id.plantillaBorrarEspecialistaDescripcion);
        ImageView iconoBorrar = convertView.findViewById(R.id.plantillaBorrarEspecialistaIcon);

        textNombre.setText(nombres[position]);
        textEspecialidad.setText(especialidades[position]);
        textDescripcion.setText(descripciones[position]);

        if (position >= 0 && position < idsEspecialista.length) {
            iconoBorrar.setOnClickListener(v -> {
                FirebaseMessageRepository dbHelper = new FirebaseMessageRepository();

                dbHelper.eliminarDocumento(
                        "usuarios",
                        idsEspecialista[position],
                        unused -> {
                            Toast.makeText(contexto, "Especialista eliminado con éxito", Toast.LENGTH_SHORT).show();
                            actualizarLista(position); // Llama a tu método de actualización de lista aquí
                        },
                        e -> {
                            Toast.makeText(contexto, "Error al eliminar el especialista", Toast.LENGTH_SHORT).show();
                            Log.e("Eliminar Especialista", "Error: ", e);
                        }
                );

            });
        } else {
            Toast.makeText(contexto, "Índice fuera de los límites", Toast.LENGTH_SHORT).show();
        }
        return convertView;
    }




    private void actualizarLista(int position) {
        List<String> nombresList = new ArrayList<>(Arrays.asList(nombres));
        List<String> especialidadesList = new ArrayList<>(Arrays.asList(especialidades));
        List<String> descripcionesList = new ArrayList<>(Arrays.asList(descripciones));
        List<Long> idsEspecialistaList = Arrays.stream(idsEspecialista).boxed().collect(Collectors.toList());

        nombresList.remove(position);
        especialidadesList.remove(position);
        descripcionesList.remove(position);
        idsEspecialistaList.remove(position);

        nombres = nombresList.toArray(new String[0]);
        especialidades = especialidadesList.toArray(new String[0]);
        descripciones = descripcionesList.toArray(new String[0]);
        idsEspecialista = idsEspecialistaList.stream().mapToLong(Long::longValue).toArray();

        notifyDataSetChanged();
    }

}
