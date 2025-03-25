package com.example.healthysmile.gui.iniciarSesion.sign_up;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.extraAndroid.settings.fragmentProfileImageSelector;

public class fragment_sign_up_foto_perfil extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_foto_perfil, container, false);

        Fragment profileImageSelectorFragment = new fragmentProfileImageSelector();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentProfileImageSelectorLogIng, profileImageSelectorFragment)
                .commit();

        return view;
    }
}