package com.example.healthysmile.gui.educacionDental;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.healthysmile.R;


public class Fragment_visualizacion_modelos_3d_init extends Fragment implements View.OnClickListener {

    ImageView imgGingivitis,imgCariesDentales,imgCancerBucal,imgTraumatismosBucodentales;
    ImageView imgHalitosis, imgSensibilidadDental, imgImplantes, imgPeriodontits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visualizacion_modelos_3d_init, container, false);
        imgGingivitis = view.findViewById(R.id.modelos_init_imagen_gingivitis);
        imgCariesDentales = view.findViewById(R.id.modelos_init_imagen_caries_dentales);
        imgCancerBucal = view.findViewById(R.id.modelos_init_imagen_cancer_bucal);
        imgTraumatismosBucodentales = view.findViewById(R.id.modelos_init_imagen_traumatismos_bucodentales);
        imgHalitosis = view.findViewById(R.id.modelos_init_imagen_halitosis);
        imgSensibilidadDental = view.findViewById(R.id.modelos_init_imagen_sensibilidad_dental);
        imgImplantes = view.findViewById(R.id.modelos_init_imagen_implantes);
        imgPeriodontits = view.findViewById(R.id.modelos_init_imagen_periodontitis);

        imgGingivitis.setOnClickListener(this);
        imgCariesDentales.setOnClickListener(this);
        imgCancerBucal.setOnClickListener(this);
        imgTraumatismosBucodentales.setOnClickListener(this);
        imgHalitosis.setOnClickListener(this);
        imgSensibilidadDental.setOnClickListener(this);
        imgImplantes.setOnClickListener(this);
        imgPeriodontits.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        NavController navController = NavHostFragment.findNavController(Fragment_visualizacion_modelos_3d_init.this);
        if(v.getId() == R.id.modelos_init_imagen_gingivitis){
            navController.navigate(R.id.action_nav_EducacionDental_to_fragment_visualizacion_modelos_3d_gingivitis);
        }else
            if(v.getId() == R.id.modelos_init_imagen_caries_dentales){
                navController.navigate(R.id.action_nav_EducacionDental_to_fragment_visualizacion_modelos_3d_caries_dentales);
            }else
                if(v.getId() == R.id.modelos_init_imagen_cancer_bucal){
                    navController.navigate(R.id.action_nav_EducacionDental_to_fragment_visualizacion_modelos_3d_cancer_bucal);
                }else
                    if(v.getId() == R.id.modelos_init_imagen_traumatismos_bucodentales){
                        navController.navigate(R.id.action_nav_EducacionDental_to_fragment_visualizacion_modelos_3d_traumatismos_bucodentales);
                    }else
                        if(v.getId() == R.id.modelos_init_imagen_sensibilidad_dental){
                            navController.navigate(R.id.action_nav_EducacionDental_to_fragment_visualizacion_modelos_3d_sensibilidad_dental);
                        }else
                            if(v.getId() == R.id.modelos_init_imagen_implantes){
                                navController.navigate(R.id.action_nav_EducacionDental_to_fragment_visualizacion_modelos_3d_implantes);
                            }else
                                if(v.getId() == R.id.modelos_init_imagen_periodontitis){
                                    navController.navigate(R.id.action_nav_EducacionDental_to_fragment_visualizacion_modelos_3d_periodontitis);

                                }else
                                    if(v.getId() == R.id.modelos_init_imagen_halitosis){
                                        navController.navigate(R.id.action_nav_EducacionDental_to_fragment_visualizacion_modelos_3d_halitosis);
                                    }
    }
}