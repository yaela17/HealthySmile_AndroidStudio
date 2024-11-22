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

import com.example.healthysmile.R;
import com.example.healthysmile.Usuario;

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
    Usuario paciente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_settings_perfil, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Perfil");

        // Verificar y solicitar permisos de cámara
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("idPaciente",null);
        String nombre = sharedPreferences.getString("nombrePaciente", null);
        String correo = sharedPreferences.getString("correoPaciente", null);
        String foto = sharedPreferences.getString("fotoPaciente", null);
        paciente = new Usuario(id,nombre, correo, null, "Paciente", foto);

        Drawable fotoPerfil = getResources().getDrawable(R.drawable.default_photo_paciente);
        if (paciente.getFotoPerfil() != null) {
            fotoPerfil = Drawable.createFromPath(paciente.getFotoPerfil());
        }

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

        listTitleInputFile = new String[]{"Nombre", "Correo", "Especialidad / Tipo de usuario"};
        listDescriptionInputFile = new String[]{paciente.getNombreUsuario(), paciente.getCorreoUsuario(), paciente.getTipoUsuario()};

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
            if (title.getText().toString().equals("Nombre")) {
                paciente.setNombreUsuario(nameInput.getText().toString());
                listDescriptionInputFile[0] = paciente.getNombreUsuario();
            } else if (title.getText().toString().equals("Correo")) {
                paciente.setCorreoUsuario(nameInput.getText().toString());
                listDescriptionInputFile[1] = paciente.getCorreoUsuario();
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
}
