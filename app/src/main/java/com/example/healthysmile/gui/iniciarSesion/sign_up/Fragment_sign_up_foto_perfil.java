package com.example.healthysmile.gui.iniciarSesion.sign_up;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.gui.extraAndroid.settings.FragmentProfileImageSelector;
import com.example.healthysmile.gui.NavigationDrawerFragments; // Asegúrate que esta sea tu actividad principal
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_sign_up_foto_perfil extends Fragment {

    private Button btnGuardar;
    private Button btnIrSinGuardar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_foto_perfil, container, false);

        // Insertar el fragmento del selector de imagen
        Fragment profileImageSelectorFragment = new FragmentProfileImageSelector();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentProfileImageSelectorLogIng, profileImageSelectorFragment)
                .commit();

        // Inicializar botones
        btnGuardar = view.findViewById(R.id.fragmentProfileImageSelectorLogIng_btnGuardar);
        btnIrSinGuardar = view.findViewById(R.id.fragmentProfileImageSelectorLogIng_btnIrSinGuardar);

        // Listener Guardar
        btnGuardar.setOnClickListener(v -> {
            // Aquí podrías hacer validaciones o guardado si lo necesitas
            irActividadPrincipal();
        });

        // Listener Ir sin guardar
        btnIrSinGuardar.setOnClickListener(v -> irSinGuardar());

        return view;
    }

    private void irSinGuardar() {
        // 1. Asignar null a "fotoUsuario" en SharedPreferences (no remover)
        SharedPreferences prefs = getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fotoUsuario", null);  // Guardar null explícitamente
        editor.apply();

        // 2. Obtener el idUsuario desde SharedPreferences
        long idUsuario = prefs.getLong("idUsuario", -1);

        if (idUsuario == -1) {
            Log.e("FotoPerfil", "No se encontró el idUsuario en SharedPreferences");
            return;
        }

        // 3. Crear parámetros para actualizar la base de datos (foto = null)
        Map<String, Object> params = new HashMap<>();
        params.put("idUsuario", idUsuario);
        params.put("foto", null); // Esto enviará un valor null al backend

        // 4. Llamada a la API para actualizar la foto
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
        Call<ApiNodeMySqlRespuesta> call = apiService.actualizarFotoPerfil(params);

        call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful()) {
                    ApiNodeMySqlRespuesta respuesta = response.body();
                    if (respuesta != null) {
                        Log.d("FotoPerfil", "Foto de perfil actualizada correctamente: " + respuesta.toString());
                    } else {
                        Log.e("FotoPerfil", "Respuesta vacía del servidor");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
                        Log.e("FotoPerfil", "Error al actualizar la foto de perfil: " + errorBody);
                    } catch (IOException e) {
                        Log.e("FotoPerfil", "Error al leer el cuerpo de error: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.e("FotoPerfil", "Fallo al conectar con el servidor: " + t.getMessage());
            }
        });

        irActividadPrincipal();
    }

    private void irActividadPrincipal() {
        Intent intent = new Intent(getActivity(), NavigationDrawerFragments.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
