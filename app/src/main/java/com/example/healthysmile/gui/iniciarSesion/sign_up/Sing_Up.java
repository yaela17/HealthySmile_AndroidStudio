package com.example.healthysmile.gui.iniciarSesion.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.gui.InitAplication;
import com.example.healthysmile.R;

public class Sing_Up extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_up);
        Intent intentRecibirTipoUsuario = getIntent();
        String tipoUsuario = intentRecibirTipoUsuario.getStringExtra("tipoRegistro");
        if (tipoUsuario != null) {
            if (tipoUsuario.equals("paciente")) {
                cambiarFragmento(new Sign_Up_Paciente());
            } else
                    if (tipoUsuario.equals("especialista")) {
                    cambiarFragmento(new Sign_Up_Especialista());
            }
        }else {
            Intent intentitoInitAplication = new Intent(this, InitAplication.class);
            startActivity(intentitoInitAplication);
        }
    }

    public void cambiarFragmento(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.signUpFragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, InitAplication.class);
        startActivity(intent);
    }
}