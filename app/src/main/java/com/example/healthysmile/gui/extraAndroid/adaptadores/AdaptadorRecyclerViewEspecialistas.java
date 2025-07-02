package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.healthysmile.R;
import com.example.healthysmile.model.entities.Especialista;

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

        // Validar si la URL es válida y cargar imagen
        String fotoUrl = listaFotos.get(position);
        if (fotoUrl == null || fotoUrl.isEmpty()) {
            holder.imgFoto.setImageResource(R.drawable.default_photo_perfil_especialista);
        } else {
            Glide.with(context)
                    .load(fotoUrl)
                    .placeholder(R.drawable.default_photo_perfil_especialista) // Imagen de carga
                    .error(R.drawable.default_photo_perfil_especialista) // Imagen de error
                    .into(holder.imgFoto);
        }

        holder.itemView.setAlpha(0f);
        holder.itemView.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(position * 100)
                .start();

        // Fondo dinámico al hacer clic
        holder.itemView.setOnClickListener(v -> {
            boolean isExpanded = holder.tvDescripcion.getVisibility() == View.VISIBLE;

            // Cambiar fondo dinámico
            holder.itemView.setBackgroundResource(isExpanded ? R.drawable.plantilla_informacion_especialistas_default_background : R.drawable.plantilla_informacion_especialistas_clicked_background);

            // Alternar expansión
            holder.tvDescripcion.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

            // Agregar animación de expansión/contracción
            holder.tvDescripcion.animate()
                    .alpha(isExpanded ? 0f : 1f)
                    .setDuration(300)
                    .start();
        });
    }


    @Override
    public int getItemCount() {
        return listaNombres.size(); // Todas las listas deben tener el mismo tamaño
    }

    public Especialista getItem(int position) {
        return new Especialista(listaNombres.get(position), listaEspecialidades.get(position).intValue(), listaCedulas.get(position), listaFotos.get(position), listaDescripciones.get(position));
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
