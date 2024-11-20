package com.example.healthysmile;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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


public class settings_perfil extends Fragment implements AdapterView.OnItemClickListener {

    ListView listaDefaultSettings;
    ImageView fotoPerfilGrande;
    AdaptadorPerfilListView adaptador;
    Drawable[] listLeftIcon;
    String[] listTitleInputFile;
    String[] listDescriptionInputFile;
    Drawable[] lisRightIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_settings_perfil, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Perfil");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombrePaciente", null);
        String correo = sharedPreferences.getString("correoPaciente", null);
        String foto = sharedPreferences.getString("fotoPaciente", null);
        Usuario paciente = new Usuario(nombre, correo, null, "Paciente", foto);

        Drawable fotoPerfil = null;
        if(paciente.getFotoPerfil() == null){
            fotoPerfil = getResources().getDrawable(R.drawable.default_photo_paciente);
        }

        // Aquí es donde ya tienes acceso al contexto y puedes usar getResources()
        listLeftIcon = new Drawable[]{
                getResources().getDrawable(R.drawable.icon_person),
                getResources().getDrawable(R.drawable.icon_email),
                getResources().getDrawable(R.drawable.icon_cedulaprofesional)
        };

        lisRightIcon = new Drawable[]{
                getResources().getDrawable(R.drawable.icon_pencil_change_input),
                getResources().getDrawable(R.drawable.icon_pencil_change_input),
                getResources().getDrawable(R.drawable.icon_pencil_change_input)};

        listTitleInputFile = new String[]{"Nombre","Correo","Especialidad / Tipo de usuario"};
        listDescriptionInputFile = new String[] {paciente.getNombreUsuario(), paciente.getCorreoUsuario(), paciente.getTipoUsuario()};

        // Configurar el ListView y el adaptador
        listaDefaultSettings = view.findViewById(R.id.listViewSettingsPerfil);
        adaptador = new AdaptadorPerfilListView(getContext(), listLeftIcon, listTitleInputFile, listDescriptionInputFile, lisRightIcon);
        listaDefaultSettings.setAdapter(adaptador);
        listaDefaultSettings.setOnItemClickListener(this);
        fotoPerfilGrande = view.findViewById(R.id.settingsFotoPerfilGrande);
        fotoPerfilGrande.setImageDrawable(fotoPerfil);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showDialog(listTitleInputFile[position],listDescriptionInputFile[position]);
    }

    private void showDialog(String tittleDialog, String campoDialog) {
        // Crear el dialogo
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_usuario_atribute);
        dialog.setCancelable(true); // Hacerlo cancelable al tocar fuera

        // Referencias a los elementos del diálogo
        TextView title = dialog.findViewById(R.id.dialogChangeUsuarioAtributeTextView);
        EditText nameInput = dialog.findViewById(R.id.dialogChangeUsuarioAtributeEditText);
        Button saveButton = dialog.findViewById(R.id.dialogChangeUsuariobtnGuardar);

        title.setText(tittleDialog);
        nameInput.setHint(campoDialog);


        // Configurar el botón de guardar
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre introducido
                String newName = nameInput.getText().toString().trim();

                // Aquí puedes agregar lógica para guardar el nuevo nombre, por ejemplo:
                if (!newName.isEmpty()) {
                    // Guardar el nuevo nombre (puedes almacenarlo en SharedPreferences o en una base de datos)
                    // Aquí se muestra un ejemplo de cómo se podría mostrar un mensaje
                    // Toast.makeText(MainActivity.this, "Nombre guardado: " + newName, Toast.LENGTH_SHORT).show();

                    // Cerrar el diálogo
                    dialog.dismiss();
                }
            }
        });

        // Mostrar el diálogo
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM); // Posicionar en la parte inferior
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.y = 240; // Ajuste del margen inferior
            window.setAttributes(layoutParams);
        }
    }

}