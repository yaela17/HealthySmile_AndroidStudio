package com.example.healthysmile.gui.extraAndroid.adaptadores;

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

public class AdaptadorPerfilListView extends BaseAdapter {

    Context contexto;
    Drawable[] listLeftIcon;
    String[] listTitleInputFile;
    String[] listDescriptionInputFile;
    Drawable[] lisRightIcon;
    LayoutInflater inflater;

    public AdaptadorPerfilListView(Context contexto, Drawable[] listLeftIcon, String[] listTitleInputFile, String[] listDescriptionInputFile, Drawable[] lisRightIcon) {
        this.contexto = contexto;
        this.listLeftIcon = listLeftIcon;
        this.listTitleInputFile = listTitleInputFile;
        this.listDescriptionInputFile = listDescriptionInputFile;
        this.lisRightIcon = lisRightIcon;
        this.inflater = LayoutInflater.from(contexto);
    }

    public AdaptadorPerfilListView(Context contexto, Drawable[] listLeftIcon, String[] listTitleInputFile, String[] listDescriptionInputFile, LayoutInflater inflater) {
        this.contexto = contexto;
        this.listLeftIcon = listLeftIcon;
        this.listTitleInputFile = listTitleInputFile;
        this.listDescriptionInputFile = listDescriptionInputFile;
        this.lisRightIcon = null;
        this.inflater = LayoutInflater.from(contexto);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return lisRightIcon[position] != null ;
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
        LinearLayout plantilla = view.findViewById(R.id.idPlantillaOpcionesPerfil);
        leftIcon.setImageDrawable(listLeftIcon[position]);
        inputTitle.setText(listTitleInputFile[position]);
        inputDesc.setText(listDescriptionInputFile[position]);
        LinearLayout.LayoutParams params;

        if(lisRightIcon[position] != null){
            rightIcon.setImageDrawable(lisRightIcon[position]);
        }else {
            rightIcon.setVisibility(View.GONE);
        }
        if(listTitleInputFile[position].isEmpty()){
            inputTitle.setVisibility(View.GONE);
        }
        if(listDescriptionInputFile[position].isEmpty()){
            inputDesc.setVisibility(View.GONE);
        }
        if(position > 0){
            plantilla.setClickable(false);
            plantilla.setEnabled(true);
        }
        return view;
    }
}
