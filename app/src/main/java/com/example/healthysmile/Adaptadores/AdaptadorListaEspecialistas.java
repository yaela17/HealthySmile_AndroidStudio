package com.example.healthysmile.Adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.healthysmile.R;

public class AdaptadorListaEspecialistas extends BaseAdapter {

    private Context contexto;
    private String[] nombres;
    private String[] especialidades;
    private String[] descripciones;
    private long[] idsEspecialista;
    private LayoutInflater inflater;
    private Fragment fragment;

    public AdaptadorListaEspecialistas(Context contexto,Fragment fragment, String[] nombres, String[] especialidades, String[] descripciones, long[] idsEspecialista) {
        this.contexto = contexto;
        this.nombres = nombres;
        this.especialidades = especialidades;
        this.descripciones = descripciones;
        this.idsEspecialista = idsEspecialista;
        this.inflater = LayoutInflater.from(contexto);
        this.fragment = fragment;
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
            convertView = inflater.inflate(R.layout.platilla_especialista_list_chat, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.plantillaListaEspecialistaFotoPerfil);
        TextView textNombre = convertView.findViewById(R.id.plantillaListaEspecialistaNombre);
        TextView textEspecialidad = convertView.findViewById(R.id.plantillaListaEspecialistaEspecialidad);
        TextView textDescripcion = convertView.findViewById(R.id.plantillaListaEspecialistaDescripcion);

        textNombre.setText(nombres[position]);
        textEspecialidad.setText(especialidades[position]);
        textDescripcion.setText(descripciones[position]);

        convertView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = contexto.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("idEspecialistaChat", idsEspecialista[position]);
            editor.putString("nombreReceptorChat", nombres[position]);
            editor.apply();
            NavController navController = NavHostFragment.findNavController(fragment);
            navController.navigate(R.id.fragment_consulta_virtual_especialista,null);
        });

        return convertView;
    }
}
