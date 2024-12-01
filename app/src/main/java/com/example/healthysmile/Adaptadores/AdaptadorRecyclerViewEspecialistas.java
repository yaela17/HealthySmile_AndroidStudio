package com.example.healthysmile.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.healthysmile.R;

import java.util.ArrayList;

public class AdaptadorRecyclerViewEspecialistas extends RecyclerView.Adapter<AdaptadorRecyclerViewEspecialistas.EspecialistaViewHolder> {

    private Context context;
    private ArrayList<String> listaNombres;
    private ArrayList<String> listaEspecialidades;
    private ArrayList<String> listaCedulas;
    private ArrayList<String> listaDescripciones;
    private ArrayList<String> listaFotos;

    // Constructor
    public AdaptadorRecyclerViewEspecialistas(Context context, ArrayList<String> listaNombres, ArrayList<String> listaEspecialidades,
                                              ArrayList<String> listaCedulas, ArrayList<String> listaDescripciones, ArrayList<String> listaFotos) {
        this.context = context;  // Asignar el contexto
        this.listaNombres = listaNombres;
        this.listaEspecialidades = listaEspecialidades;
        this.listaCedulas = listaCedulas;
        this.listaDescripciones = listaDescripciones;
        this.listaFotos = listaFotos;
    }

    @Override
    public EspecialistaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar la plantilla XML para cada ítem
        View view = LayoutInflater.from(context).inflate(R.layout.plantilla_informacion_especialista, parent, false);
        return new EspecialistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EspecialistaViewHolder holder, int position) {
        // Asignar datos de las listas
        holder.tvNombre.setText(listaNombres.get(position));
        holder.tvEspecialidad.setText(listaEspecialidades.get(position));
        holder.tvCedula.setText("Cédula : " + listaCedulas.get(position));
        holder.tvDescripcion.setText(listaDescripciones.get(position));

        // Validar si la URL es válida
        String fotoUrl = listaFotos.get(position);
        if (fotoUrl == null || fotoUrl.isEmpty()) {
            // Mostrar imagen por defecto
            holder.imgFoto.setImageResource(R.drawable.default_photo_perfil_especialista);
        } else {
            // Cargar imagen usando Glide
            Glide.with(context)
                    .load(fotoUrl)
                    .placeholder(R.drawable.default_photo_perfil_especialista) // Imagen de carga
                    .error(R.drawable.default_photo_perfil_especialista) // Imagen de error
                    .into(holder.imgFoto);
        }
    }

    @Override
    public int getItemCount() {
        return listaNombres.size(); // Todas las listas deben tener el mismo tamaño
    }

    // ViewHolder
    public static class EspecialistaViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFoto;
        TextView tvNombre, tvEspecialidad, tvCedula, tvDescripcion;

        public EspecialistaViewHolder(View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.plantillaHomeFotoEspecialista);
            tvNombre = itemView.findViewById(R.id.plantillaHomeNombreEspecialista);
            tvEspecialidad = itemView.findViewById(R.id.plantillaHomeEspecialidadEspecialista);
            tvCedula = itemView.findViewById(R.id.plantillaHomeCedulaEspecialista);
            tvDescripcion = itemView.findViewById(R.id.plantillaHomeDescripcionEspecialista);
        }
    }
}
