package com.example.healthysmile.gui.extraAndroid.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;
import com.example.healthysmile.service.SupabaseFileStorageService;
import com.example.healthysmile.service.extraAndroid.ActualizarFotoService;
import com.example.healthysmile.utils.ImageUtils;
import com.example.healthysmile.utils.ReutilizableMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FragmentProfileImageSelector extends Fragment {

    private static final int REQUEST_GALLERY_IMAGE = 101;
    private ImageView profileImage;
    private ImageView profileImageIcon;
    private String foto;
    private String tipoUsuario;

    private SupabaseFileStorageService supabaseFileStorageService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_image_selector, container, false);

        profileImage = view.findViewById(R.id.profileImageSelectImage);
        profileImageIcon = view.findViewById(R.id.profileImageSelectImageIcon);

        obtenerFotoPerfil();

        profileImageIcon.setOnClickListener(v -> openGallery());

        supabaseFileStorageService = new SupabaseFileStorageService();

        return view;
    }

    private void cargarDatosUsuario() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        foto = sharedPreferences.getString("fotoUsuario", null);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");
    }

    private void obtenerFotoPerfil() {
        cargarDatosUsuario();
        ReutilizableMethods reutilizableMethods = new ReutilizableMethods();
        reutilizableMethods.cargarFotoPerfil(getContext(),profileImage);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Convertir la URI a un archivo temporal
                ImageUtils imageUtils = new ImageUtils();
                File file = imageUtils.convertUriToFile(getContext(),selectedImageUri);
                if (file != null && file.exists()) {
                    Log.d("FotoPerfil", "El archivo existe");
                    Log.d("FotoPerfil", "Nombre del archivo: " + file.getName());
                    ActualizarFotoService actualizarFotoService = new ActualizarFotoService(getContext());
                    actualizarFotoService.actualizarFoto(file);
                    profileImage.setImageURI(selectedImageUri);
                } else {
                    Log.e("FotoPerfil", "El archivo no existe o es nulo");
                    Toast.makeText(requireContext(), "No se encontró el archivo de imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
