package com.example.healthysmile.gui.modelos3d;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;
import com.example.healthysmile.repository.SupabaseFileStorageRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class fragment_visualizacion_modelos_3d_cancer_bucal extends Fragment {

    private static final int PICK_FILE_REQUEST = 1;
    private SupabaseFileStorageRepository supabaseClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizacion_modelos_3d_cancer_bucal, container, false);

        Button btnUpload = view.findViewById(R.id.btnUpload);
        supabaseClient = new SupabaseFileStorageRepository();

        btnUpload.setOnClickListener(v -> openFileChooser());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");  // Permite seleccionar cualquier tipo de archivo
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                File file = convertUriToFile(fileUri);
                if (file != null) {
                    new Thread(() -> {
                        try {
                            String response = supabaseClient.uploadFile(file);
                            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Subida exitosa: " + response, Toast.LENGTH_LONG).show());
                        } catch (Exception e) {
                            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error al subir: " + e.getMessage(), Toast.LENGTH_LONG).show());
                        }
                    }).start();
                }
            }
        }
    }

    private File convertUriToFile(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            File tempFile = new File(getActivity().getCacheDir(), getFileName(uri));
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
