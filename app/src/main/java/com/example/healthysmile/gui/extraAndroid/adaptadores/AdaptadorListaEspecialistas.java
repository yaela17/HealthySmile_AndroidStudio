package com.example.healthysmile.gui.extraAndroid.adaptadores;

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
import com.example.healthysmile.utils.ImageUtils;

public class AdaptadorListaEspecialistas extends BaseAdapter {

    private Context contexto;
    private String[] nombres;
    private String[] especialidades;
    private String[] descripciones;
    private long[] idsEspecialista;
    private String[] fotosPerfil;
    private LayoutInflater inflater;
    private Fragment fragment;
    ImageUtils imageUtils;

    public AdaptadorListaEspecialistas(Context contexto,Fragment fragment, String[] nombres, String[] especialidades, String[] descripciones, long[] idsEspecialista, String[] fotosPerfil) {
        this.contexto = contexto;
        this.nombres = nombres;
        this.especialidades = especialidades;
        this.descripciones = descripciones;
        this.idsEspecialista = idsEspecialista;
        this.inflater = LayoutInflater.from(contexto);
        this.fragment = fragment;
        this.fotosPerfil = fotosPerfil;
        this.imageUtils = new ImageUtils();
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
        if("No disponible".equals(fotosPerfil[position])){
            imageView.setImageResource(R.drawable.default_photo_perfil_especialista);
        } else {
            imageUtils.cargarImagenConGlide(contexto, fotosPerfil[position], imageView);
        }


        convertView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = contexto.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("idEspecialistaChat", idsEspecialista[position]);
            editor.putString("nombreReceptorChat", nombres[position]);
            editor.putString("fotoPerfilReceptorChat",fotosPerfil[position]);
            editor.apply();
            NavController navController = NavHostFragment.findNavController(fragment);
            navController.navigate(R.id.fragment_consulta_virtual_especialista,null);
        });

        return convertView;
    }
}
