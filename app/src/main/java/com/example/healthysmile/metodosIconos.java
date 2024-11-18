package com.example.healthysmile;

import android.widget.EditText;

public class metodosIconos {
    public void borrarUltimoCaracter(EditText editText){
        String texto = editText.getText().toString();
        if(texto.length() == 0){
            if(texto.length() == 1){
                texto = "";
            }else
                texto = texto.substring(0,texto.length()-1);
        }
        editText.setText(texto);
    }
}
