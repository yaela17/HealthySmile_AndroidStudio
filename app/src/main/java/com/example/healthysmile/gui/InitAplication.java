package com.example.healthysmile.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.login.LogIn;
import com.example.healthysmile.gui.sign_up.Sing_Up;
import com.google.firebase.analytics.FirebaseAnalytics;

public class InitAplication extends AppCompatActivity implements View.OnClickListener {

    Button btnPaciente,btnEspecialista,btnLogin;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_init_aplication);

        btnPaciente = findViewById(R.id.initAppBtnSignUpPaciente);
        btnEspecialista = findViewById(R.id.initAppBtnSignUpEspecialista);
        btnLogin = findViewById(R.id.initAppBtnLogin);
        btnPaciente.setOnClickListener(this);
        btnEspecialista.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Test event");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        Boolean estaLogueado = sharedPreferences.getBoolean("sesionActiva",false);
        if(estaLogueado){
            Intent intentoIrHome = new Intent(this, NavigationDrawerFragments.class);
            startActivity(intentoIrHome);
        }
    }

    @Override
    public void onClick(View v) {
        String textoBoton = ((Button)v).getText().toString();
        if(textoBoton.equals(getString(R.string.texto_boton_paciente))){
            Intent intentitoMandarPaciente = new Intent(this, Sing_Up.class);
            intentitoMandarPaciente.putExtra("tipoRegistro","paciente");
            startActivity(intentitoMandarPaciente);
        }else
            if(textoBoton.equals(getString(R.string.texto_boton_especialista))){
                Intent intentitoMandarEspecialista = new Intent(this,Sing_Up.class);
                intentitoMandarEspecialista.putExtra("tipoRegistro","especialista");
                startActivity(intentitoMandarEspecialista);
            }else
                if(textoBoton.equals(getString(R.string.texto_boton_iniciarsesion))){
                    Intent intentitoMandarLogin = new Intent(this, LogIn.class);
                    startActivity(intentitoMandarLogin);
                }
    }
}