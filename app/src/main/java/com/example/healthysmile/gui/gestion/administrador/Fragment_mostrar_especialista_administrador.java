package com.example.healthysmile.gui.gestion.administrador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.extraAndroid.EspecialistaResponseListenerInicio;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorLVProductos;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorRecyclerViewEspecialistas;
import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.service.EspecialistaService;
import com.example.healthysmile.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class Fragment_mostrar_especialista_administrador extends Fragment {

    ListView especialistasLV;
    EspecialistaService especialistaService;
    SharedPreferencesHelper sharedPreferencesHelper;
    boolean eliminar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mostrar_especialista_administrador, container, false);

        especialistasLV = view.findViewById(R.id.mostrar_especialistas_disponibles_administrador_list_view);
        especialistaService = new EspecialistaService(requireContext());
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

        eliminar = sharedPreferencesHelper.obtenerAccionSeleccionada();

        cargarEspecislistas();

        return view;
    }

    private void cargarEspecislistas() {
        especialistaService.obtenerEspecialistasInicio(new EspecialistaResponseListenerInicio() {
            @Override
            public void onResponseInicio(List<String> nombres, List<String> especialidades,
                                         List<String> descripciones, List<Long> idsEspecialista,
                                         List<String> cedulasProfesional, List<String> fotos) {
                ArrayList<String> nombresDisponibles = new ArrayList<>();
                ArrayList<String> especialidadesDisponibles = new ArrayList<>();
                ArrayList<String> descripcionesDisponibles = new ArrayList<>();
                ArrayList<Long> idsEspecialistaDisponibles = new ArrayList<>();
                ArrayList<String> cedulasProfesionalDisponibles = new ArrayList<>();
                ArrayList<String> fotosDisponibles = new ArrayList<>();

                for (int i = 0; i < idsEspecialista.size(); i++) {
                    nombresDisponibles.add(nombres.get(i));
                    especialidadesDisponibles.add(especialidades.get(i));
                    descripcionesDisponibles.add(descripciones.get(i));
                    idsEspecialistaDisponibles.add(idsEspecialista.get(i));
                    cedulasProfesionalDisponibles.add(cedulasProfesional.get(i));
                    fotosDisponibles.add(fotos.get(i));
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        AdaptadorRecyclerViewEspecialistas adaptador = new AdaptadorRecyclerViewEspecialistas(
                                requireContext(), nombresDisponibles, especialidadesDisponibles,
                                cedulasProfesionalDisponibles, descripcionesDisponibles, fotosDisponibles
                        );
                        especialistasLV.setAdapter((ListAdapter) adaptador);
                    });
                }

            }

            @Override
            public void onErrorInicio(String mensaje) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });

    }
}