package com.example.healthysmile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private int selectedPosition = -1;

    public CustomSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    // Método para actualizar la posición seleccionada
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    // Este método controla cómo se muestra el `Spinner` cuando está cerrado
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = (TextView) convertView;
        textView.setText(getItem(position));

        // Aplicamos el estilo estándar para el elemento seleccionado en el `Spinner` cerrado
        textView.setTextColor(getContext().getColor(R.color.black));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item, parent, false);
        }

        TextView textView = (TextView) convertView;
        textView.setText(getItem(position));

        // Aplicamos un estilo especial solo si el elemento es el seleccionado y en el menú desplegable
        if (position == selectedPosition) {
            textView.setTextColor(getContext().getColor(R.color.white));  // Color de texto especial para el seleccionado
            textView.setBackgroundColor(getContext().getColor(R.color.color_principal_glow));  // Fondo especial
        } else {
            textView.setTextColor(getContext().getColor(R.color.black));  // Color de texto normal
            textView.setBackgroundResource(R.drawable.border_background);
        }

        return convertView;
    }
}
