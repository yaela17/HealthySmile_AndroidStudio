package com.example.healthysmile.Adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthysmile.R;

public class AdaptadorOpcionesPerfilListView extends BaseAdapter {

    Context contexto;
    Drawable[] listLeftIcon;
    String[] listTitleInputFile;
    String[] listDescriptionInputFile;
    Drawable[] lisRightIcon;
    LayoutInflater inflater;

    public AdaptadorOpcionesPerfilListView(Context contexto, Drawable[] listLeftIcon, String[] listTitleInputFile, String[] listDescriptionInputFile, Drawable[] lisRightIcon) {
        this.contexto = contexto;
        this.listLeftIcon = listLeftIcon;
        this.listTitleInputFile = listTitleInputFile;
        this.listDescriptionInputFile = listDescriptionInputFile;
        this.lisRightIcon = lisRightIcon;
        this.inflater = LayoutInflater.from(contexto);
    }

    public AdaptadorOpcionesPerfilListView(Context contexto, Drawable[] listLeftIcon, String[] listTitleInputFile, String[] listDescriptionInputFile, LayoutInflater inflater) {
        this.contexto = contexto;
        this.listLeftIcon = listLeftIcon;
        this.listTitleInputFile = listTitleInputFile;
        this.listDescriptionInputFile = listDescriptionInputFile;
        this.lisRightIcon = null;
        this.inflater = LayoutInflater.from(contexto);
    }

    @Override
    public int getCount() {
        return listTitleInputFile.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.plantilla_listview_opciones_perfil,null);
        ImageView leftIcon = view.findViewById(R.id.plantillaOpcionesPerfilLeftIcon);
        TextView inputTitle = view.findViewById(R.id.plantillaOpcionesPerfilTitleInputFile);
        TextView inputDesc = view.findViewById(R.id.plantillaOpcionesPerfilDescriptionInputFile);
        ImageView rightIcon = view.findViewById(R.id.plantillaOpcionesPerfilRightIcon);
        leftIcon.setImageDrawable(listLeftIcon[position]);
        inputTitle.setText(listTitleInputFile[position]);
        inputDesc.setText(listDescriptionInputFile[position]);
        if (position == 0) {
            // Configurar el tamaño de la imagen
            leftIcon.setLayoutParams(new LinearLayout.LayoutParams(250, 250));

            // Establecer padding
            leftIcon.setPadding(0, 8, 0, 0);


            // Establecer el fondo circular (asegurate de tener un drawable de fondo circular)
            leftIcon.setBackgroundResource(R.drawable.custom_circle_image);

            // Establecer el tipo de escala de la imagen
            leftIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Establecer el clip a los bordes (solo es necesario si el fondo es circular)
            leftIcon.setClipToOutline(true);

        }
        if(lisRightIcon[position] != null){
            rightIcon.setImageDrawable(lisRightIcon[position]);
        }else {
            rightIcon.setVisibility(View.GONE);
        }
        return view;
    }
}
