package com.example.healthysmile.gui.iniciarSesion.sign_up;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.InitAplication;
import com.example.healthysmile.gui.NavigationDrawerFragments;
import com.example.healthysmile.utils.SharedPreferencesHelper; // Asegúrate de importar esta clase

import java.util.Locale;

public class fragment_sign__up__verificacion_correo extends Fragment implements View.OnClickListener {

    private EditText inputCodigoVerificacion;
    private Button btnVerificar;
    private TextView cronometro;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private String codigoGuardado;
    private CountDownTimer countDownTimer;

    private static final long TIEMPO_LIMITE = 5 * 60 * 1000; // 5 minutos en milisegundos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign__up__verificacion_correo, container, false);

        inputCodigoVerificacion = view.findViewById(R.id.sign_up_verificacion_input_codigo);
        btnVerificar = view.findViewById(R.id.sign_up_verificacion_btn_verificar);
        cronometro = view.findViewById(R.id.sign_up_verificacion_text_view_temporizador);

        sharedPreferencesHelper = new SharedPreferencesHelper(requireContext());

        btnVerificar.setOnClickListener(this);

        iniciarCronometro();

        return view;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE);
        String codigoGuardado = sharedPreferences.getString("codigoVerificacion"," ");
        String codigoIngresado = inputCodigoVerificacion.getText().toString().trim();

        if (codigoIngresado.equals(codigoGuardado)) {
            sharedPreferencesHelper.eliminarCodigoVerificacion();
            cronometro.setText("Código correcto. Iniciando sesión...");
            Toast.makeText(getContext(), "Código correcto", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), NavigationDrawerFragments.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Código incorrecto, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarCronometro() {
        countDownTimer = new CountDownTimer(TIEMPO_LIMITE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutos = (int) (millisUntilFinished / 1000) / 60;
                int segundos = (int) (millisUntilFinished / 1000) % 60;
                String tiempoRestante = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
                cronometro.setText("Tiempo restante: " + tiempoRestante);
            }

            @Override
            public void onFinish() {
                sharedPreferencesHelper.eliminarCodigoVerificacion();
                sharedPreferencesHelper.terminarSesion();
                Toast.makeText(getContext(), "Tiempo agotado. Volviendo a inicio", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), InitAplication.class);
                startActivity(intent);
                getActivity().finish();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
