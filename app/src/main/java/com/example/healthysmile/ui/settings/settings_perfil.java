package com.example.healthysmile.ui.settings;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.ConexionFirebaseDB;
import com.example.healthysmile.R;

public class settings_perfil extends Fragment implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_GALLERY_IMAGE = 101;

    ListView listaDefaultSettings;
    ImageView fotoPerfilGrande;
    AdaptadorPerfilListView adaptador;
    Drawable[] listLeftIcon;
    String[] listTitleInputFile;
    String[] listDescriptionInputFile;
    Drawable[] lisRightIcon;
    Dialog dialog;
    ConexionFirebaseDB dbHelper;
    String nombre,correo,foto,tipoUsuario;
    long idUsuario;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_settings_perfil, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Perfil");
        dbHelper = new ConexionFirebaseDB();

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

        if ("Paciente".equals(tipoUsuario)) {

        } else {
            long idEspecialista = sharedPreferences.getLong("idEspecialista", 0);
            String cedulaProfesional = sharedPreferences.getString("cedulaProfesionalEsp", null);
            String descripcion = sharedPreferences.getString("descripcionEsp", null);
            String especialidad = sharedPreferences.getString("especialidadEsp", null);
        }

        Drawable fotoPerfil =  obtenerFotoPerfil();

        listLeftIcon = new Drawable[]{
                getResources().getDrawable(R.drawable.icon_person),
                getResources().getDrawable(R.drawable.icon_email),
                getResources().getDrawable(R.drawable.icon_cedulaprofesional)
        };

        lisRightIcon = new Drawable[]{
                getResources().getDrawable(R.drawable.icon_pencil_change_input),
                getResources().getDrawable(R.drawable.icon_pencil_change_input),
                getResources().getDrawable(R.drawable.icon_pencil_change_input)
        };

        listTitleInputFile = new String[]{
                getActivity().getApplicationContext().getString(R.string.texto_campo_nombre),
                getActivity().getApplicationContext().getString(R.string.texto_campo_correo),
                getActivity().getApplicationContext().getString(R.string.texto_boton_especialista)};
        listDescriptionInputFile = new String[]{nombre, correo, tipoUsuario};

        // Configurar el ListView y el adaptador
        listaDefaultSettings = view.findViewById(R.id.listViewSettingsPerfil);
        adaptador = new AdaptadorPerfilListView(getContext(), listLeftIcon, listTitleInputFile, listDescriptionInputFile, lisRightIcon);
        listaDefaultSettings.setAdapter(adaptador);
        listaDefaultSettings.setOnItemClickListener(this);

        fotoPerfilGrande = view.findViewById(R.id.settingsFotoPerfilGrande);
        fotoPerfilGrande.setImageDrawable(fotoPerfil);

        // Listener para cambiar la foto de perfil
        fotoPerfilGrande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

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
            if (title.getText().toString().equals(getActivity().getApplicationContext().getString(R.string.texto_campo_nombre))) {
                editor.putString("nombrePaciente", nameInput.getText().toString());
                editor.apply();
                listDescriptionInputFile[0] = nameInput.getText().toString();
                dbHelper.cambiarNombrePorCorreo(
                        correo,
                        nameInput.getText().toString(),
                        unused -> {
                            // Éxito: actualizar la UI y notificar al usuario
                            adaptador.notifyDataSetChanged();
                            Toast.makeText(requireActivity().getApplicationContext(), "Nombre actualizado con éxito", Toast.LENGTH_SHORT).show();
                        },
                        e -> {
                            // Error: mostrar mensaje al usuario
                            Toast.makeText(requireActivity().getApplicationContext(), "Error al actualizar el nombre: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
            } else if (title.getText().toString().equals("Correo")) {
                editor.putString("correoPaciente", nameInput.getText().toString());
                editor.apply();
                listDescriptionInputFile[1] = nameInput.getText().toString();
            } else if (title.getText().toString().equals("Especialidad / Tipo de usuario")) {
                // Aquí podrías manejar el cambio de especialidad si es necesario
            }

            adaptador.notifyDataSetChanged(); // Notifica al ListView sobre los cambios
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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                fotoPerfilGrande.setImageURI(selectedImageUri);
                // Guarda la URI o actualiza los datos en SharedPreferences
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Drawable obtenerFotoPerfil() {
        // Si la foto de paciente o especialista es nula, se usa una foto predeterminada
        if (foto != null && !foto.equals("null")) {
            return Drawable.createFromPath(foto);
        } else {
            if ("Paciente".equals(tipoUsuario)) {
                return getResources().getDrawable(R.drawable.default_photo_paciente);
            } else {
                return getResources().getDrawable(R.drawable.default_photo_perfil_especialista);
            }
        }
    }
}
