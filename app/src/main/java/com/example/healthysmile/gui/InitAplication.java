package com.example.healthysmile.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.iniciarSesion.login.LogIn;
import com.example.healthysmile.gui.iniciarSesion.sign_up.Sing_Up;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import mx.openpay.android.Openpay;

public class InitAplication extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView cardPaciente, cardEspecialista;
    TextView textViewLogin;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_init_aplication);

        cardPaciente = findViewById(R.id.initAppCardSignUpPaciente);
        cardEspecialista = findViewById(R.id.initAppCardSignUpEspecialista);
        textViewLogin = findViewById(R.id.initAppTextViewLogin);

        cardPaciente.setOnClickListener(this);
        cardEspecialista.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Test event");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);


        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        Boolean estaLogueado = sharedPreferences.getBoolean("sesionActiva",false);
        if(estaLogueado){
            Intent intentoIrHome = new Intent(this, NavigationDrawerFragments.class);
            startActivity(intentoIrHome);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.initAppCardSignUpPaciente) {
            intent = new Intent(this, Sing_Up.class);
            intent.putExtra("tipoRegistro", "paciente");
        } else if (v.getId() == R.id.initAppCardSignUpEspecialista) {
            intent = new Intent(this, Sing_Up.class);
            intent.putExtra("tipoRegistro", "especialista");
        } else if (v.getId() == R.id.initAppTextViewLogin) {
            intent = new Intent(this, LogIn.class);
        } else {
            return;
        }
        startActivity(intent);
    }
}