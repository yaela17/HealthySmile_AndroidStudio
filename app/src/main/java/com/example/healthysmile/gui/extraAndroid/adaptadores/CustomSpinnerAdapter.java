package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.healthysmile.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private int selectedPosition = -1;

    public CustomSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = (TextView) convertView;
        textView.setText(getItem(position));

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

        if (position == selectedPosition) {
            textView.setTextColor(getContext().getColor(R.color.white));
            textView.setBackgroundColor(getContext().getColor(R.color.color_principal_glow));
        } else {
            textView.setTextColor(getContext().getColor(R.color.black));
            textView.setBackgroundResource(R.drawable.border_background);
        }

        return convertView;
    }
}
