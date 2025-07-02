package com.example.healthysmile.gui.extraAndroid.settings;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorPerfilListView;
import com.example.healthysmile.model.requests.ActualizarDescripcionRequest;
import com.example.healthysmile.model.requests.ActualizarNombreRequest;
import com.example.healthysmile.repository.FirebaseMessageRepository;
import com.example.healthysmile.R;
import com.example.healthysmile.repository.NodeApiRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings_perfil extends Fragment implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    ListView listaDefaultSettings;
    AdaptadorPerfilListView adaptador;
    Drawable[] listLeftIcon;
    String[] listTitleInputFile;
    String[] listDescriptionInputFile;
    Drawable[] lisRightIcon;
    Dialog dialog;
    FirebaseMessageRepository dbHelper;
    String nombre,correo,foto,tipoUsuario,otroTipoUsuario;
    long idUsuario;
    String cedulaProfesional = null, descripcion = null, especialidad = null;
    Long nivelPermisos = null, idEspecialista;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_perfil, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Perfil");
        dbHelper = new FirebaseMessageRepository();

        // Verificar y solicitar permisos de cámara
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getLong("idUsuario",0);
        nombre = sharedPreferences.getString("nombreUsuario", null);
        correo = sharedPreferences.getString("correoUsuario", null);
        foto = sharedPreferences.getString("fotoUsuario", null);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");
        otroTipoUsuario = sharedPreferences.getString("otroTipoUsuario", null);
        nivelPermisos = sharedPreferences.getLong("nivelPermisos",1);

        if ("Paciente".equals(tipoUsuario) && otroTipoUsuario != null) {
            listLeftIcon = new Drawable[]{
                    getResources().getDrawable(R.drawable.icon_person),
                    getResources().getDrawable(R.drawable.icon_email),
                    getResources().getDrawable(R.drawable.icon_cedulaprofesional),
            };

            lisRightIcon = new Drawable[]{
                    getResources().getDrawable(R.drawable.icon_pencil_change_input),
                    null,
                    null,
            };

            listTitleInputFile = new String[]{
                    getActivity().getApplicationContext().getString(R.string.texto_campo_nombre),
                    getActivity().getApplicationContext().getString(R.string.texto_campo_correo),
                    "Tipo de Usuario"};
            listDescriptionInputFile = new String[]{nombre, correo, tipoUsuario};

        } else {
            idEspecialista = sharedPreferences.getLong("idEspecialista", 0);
            cedulaProfesional = sharedPreferences.getString("cedulaProfesionalEsp", null);
            descripcion = sharedPreferences.getString("descripcionEsp", null);
            especialidad = sharedPreferences.getString("especialidadEsp", null);

            listLeftIcon = new Drawable[]{
                    getResources().getDrawable(R.drawable.icon_person),
                    getResources().getDrawable(R.drawable.icon_email),
                    getResources().getDrawable(R.drawable.icon_cedulaprofesional),
                    getResources().getDrawable(R.drawable.icon_cedulaprofesional),
                    getResources().getDrawable(R.drawable.icon_cedulaprofesional),
                    getResources().getDrawable(R.drawable.icon_cedulaprofesional)
            };

            lisRightIcon = new Drawable[]{
                    getResources().getDrawable(R.drawable.icon_pencil_change_input),
                    null,
                    null,
                    null,
                    null,
                    getResources().getDrawable(R.drawable.icon_pencil_change_input)
            };

            listTitleInputFile = new String[]{
                    getActivity().getApplicationContext().getString(R.string.texto_campo_nombre),
                    getActivity().getApplicationContext().getString(R.string.texto_campo_correo),
                    "Tipo de Usuario","Cedula Profesional","Especialidad","Descripcion"};
            listDescriptionInputFile = new String[]{nombre, correo, tipoUsuario, cedulaProfesional, especialidad,descripcion};
        }


        // Configurar el ListView y el adaptador
        listaDefaultSettings = view.findViewById(R.id.listViewSettingsPerfil);
        adaptador = new AdaptadorPerfilListView(getContext(), listLeftIcon, listTitleInputFile, listDescriptionInputFile, lisRightIcon);
        listaDefaultSettings.setAdapter(adaptador);
        listaDefaultSettings.setOnItemClickListener(this);

        Fragment profileImageSelectorFragment = new FragmentProfileImageSelector();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentProfileImageSelectorSettings, profileImageSelectorFragment)
                .commit();

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showDialog(listTitleInputFile[position], listDescriptionInputFile[position]);
    }

    private void showDialog(String tittleDialog, String campoDialog) {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_usuario_atribute);
        dialog.setCancelable(true);

        TextView title = dialog.findViewById(R.id.dialogChangeUsuarioAtributeTextView);
        EditText nameInput = dialog.findViewById(R.id.dialogChangeUsuarioAtributeEditText);
        Button closeButton = dialog.findViewById(R.id.dialogChangeUsuariobtnCancelar);
        Button saveButton = dialog.findViewById(R.id.dialogChangeUsuariobtnGuardar);

        title.setText(tittleDialog);
        nameInput.setHint(campoDialog);

        closeButton.setOnClickListener(v -> dialog.dismiss());
        saveButton.setOnClickListener(v -> {
            String nuevoDato = nameInput.getText().toString();

            if (title.getText().toString().equals(
                    getActivity().getApplicationContext().getString(R.string.texto_campo_nombre))) {

                ActualizarNombreRequest request = new ActualizarNombreRequest(
                        correo,
                        nuevoDato
                );

                NodeApiRetrofitClient.getApiService()
                        .actualizarNombre(request)
                        .enqueue(new Callback<ApiNodeMySqlRespuesta>() {
                            @Override
                            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    editor.putString("nombreUsuario", nuevoDato);
                                    editor.apply();
                                    listDescriptionInputFile[0] = nuevoDato;
                                    adaptador.notifyDataSetChanged();
                                    Toast.makeText(requireContext(), "Nombre actualizado con éxito", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Error: " +
                                                    (response.body() != null ? response.body().getMensaje() : "Respuesta inesperada"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                                Toast.makeText(requireContext(),
                                        "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            } else if (title.getText().toString().equals( getActivity().getApplicationContext().getString(R.string.texto_hint_descripcion))){
                ActualizarDescripcionRequest request = new ActualizarDescripcionRequest( nuevoDato,correo);
                NodeApiRetrofitClient.getApiService()
                        .actualizarDescripcion(request)
                        .enqueue(new Callback<ApiNodeMySqlRespuesta>() {
                            @Override
                            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    editor.putString("descripcionEsp", nuevoDato);
                                    editor.apply();
                                    listDescriptionInputFile[5] = nuevoDato;
                                    adaptador.notifyDataSetChanged();
                                    Toast.makeText(requireContext(), "Descripciónn actualizada con éxito", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Error: " +
                                                    (response.body() != null ? response.body().getMensaje() : "Respuesta inesperada"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                                Toast.makeText(requireContext(),
                                        "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            adaptador.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.y = 240;
            window.setAttributes(layoutParams);
        }
    }
}
