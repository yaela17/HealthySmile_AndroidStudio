package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

public class AdaptadorListaPacientes extends BaseAdapter {
    private Context contexto;
    private String[] nombres;
    private String[] correos;
    private long[] idsUsuario;
    private LayoutInflater inflater;
    private Fragment fragment;

    public AdaptadorListaPacientes(Context contexto, String[] correos, long[] idsUsuario, Fragment fragment, String[] nombres) {
        this.contexto = contexto;
        this.correos = correos;
        this.idsUsuario = idsUsuario;
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(contexto);
        this.nombres = nombres;
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
        return idsUsuario[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.plantilla_paciente_list_chat, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.plantillaListaPacienteFotoPerfil);
        TextView textNombre = convertView.findViewById(R.id.plantillaListaPacienteNombre);
        TextView textCorreo = convertView.findViewById(R.id.plantillaListaPacienteCorreo);

        textNombre.setText(nombres[position]);
        textCorreo.setText(correos[position]);

        convertView.setOnClickListener(v -> {
            Log.d("Adaptador", "ID seleccionado: " + idsUsuario[position]);
            Log.d("Adaptador", "Nombre seleccionado: " + nombres[position]);
            SharedPreferences sharedPreferences = contexto.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("idEspecialistaChat", idsUsuario[position]);
            editor.putString("nombreReceptorChat", nombres[position]);
            editor.apply();
            NavController navController = NavHostFragment.findNavController(fragment);
            navController.navigate(R.id.fragment_consulta_virtual_especialista,null);
        });
        return convertView;
    }
}
