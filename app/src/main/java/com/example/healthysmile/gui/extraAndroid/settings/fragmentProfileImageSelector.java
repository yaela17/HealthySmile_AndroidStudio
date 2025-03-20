package com.example.healthysmile.gui.extraAndroid.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import com.example.healthysmile.R;

public class fragmentProfileImageSelector extends Fragment {
    private static final int PICK_IMAGE = 1;
    private ImageView profileImageView;

    // Lanzador de la actividad para seleccionar la imagen
    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        // Establecer la imagen seleccionada en el ImageView
                        profileImageView.setImageURI(selectedImageUri);
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_profile_image_selector, container, false);

        // Referencia a la vista de la imagen de perfil
        profileImageView = view.findViewById(R.id.signInSelectImage);

        // Referencia a la vista del icono de la cámara
        ImageView cameraIcon = view.findViewById(R.id.signInSelectImageIcon);

        // Agregar un listener al icono de la cámara
        cameraIcon.setOnClickListener(v -> {
            // Abrir el selector de imágenes
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selectImageLauncher.launch(intent); // Usar el lanzador de actividad
        });

        return view;
    }
}
