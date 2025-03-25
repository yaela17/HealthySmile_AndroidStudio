package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.consulta.ModifyCitaResponseListener;
import com.example.healthysmile.service.consulta.CitaService;
import com.example.healthysmile.utils.SharedPreferencesHelper;

public class AdaptadorListaCitas extends BaseAdapter {

    private Context contexto;
    private String[] fechas;
    private String[] horas;
    private String[] motivos;
    private Long[] idsEspecialistas;
    private String[] nombresEspecialistas;
    private Long[] idsCita;
    private LayoutInflater inflater;
    private FragmentManager fragment;

    public AdaptadorListaCitas(Context contexto, String[] fechas, String[] horas,
                               String[] motivos,Long[] idsEspecialistas, String[] nombresEspecialistas, Long[] idsCita, FragmentManager  fragment) {
        this.contexto = contexto;
        this.fechas = fechas;
        this.horas = horas;
        this.motivos = motivos;
        this.idsEspecialistas = idsEspecialistas;
        this.nombresEspecialistas = nombresEspecialistas;
        this.idsCita = idsCita;
        this.inflater = LayoutInflater.from(contexto);
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return fechas.length;
    }

    @Override
    public Object getItem(int position) {
        return fechas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.plantilla_paciente_cita, parent, false);
        }

        // Referencias a los elementos de la vista
        TextView textFecha = convertView.findViewById(R.id.plantilla_paciente_cita_fecha);
        TextView textHora = convertView.findViewById(R.id.plantilla_paciente_cita_hora);
        TextView textMotivo = convertView.findViewById(R.id.plantilla_paciente_cita_motivo);
        TextView textNombreEspecialista = convertView.findViewById(R.id.plantilla_paciente_nombreEspecialista);

        Button btnModificar = convertView.findViewById(R.id.plantilla_paciente_btn_modificar);
        Button btnEliminar = convertView.findViewById(R.id.plantilla_paciente_cita_btn_eliminar);

        // Establecer valores a los TextViews
        textFecha.setText(fechas[position]);
        textHora.setText(horas[position]);
        textMotivo.setText(motivos[position]);
        textNombreEspecialista.setText("con " + nombresEspecialistas[position]);

        // Acciones de los botones
        btnModificar.setOnClickListener(v -> {
            FormularioModificarCitaPaciente formularioModificarCitaPaciente = new FormularioModificarCitaPaciente(fechas[position],idsEspecialistas[position]);
            formularioModificarCitaPaciente.show(fragment, "Mi dialog fragmen");
        });

        btnEliminar.setOnClickListener(v -> {
            CitaService citaService = new CitaService(contexto);
            citaService.eliminarCita(idsCita[position],new ModifyCitaResponseListener() {
                @Override
                public void onResponse(String mensaje) {
                    Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(contexto, error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCitaNoEncontrada(String mensaje) {
                    Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show();
                }
            });
        });
        return convertView;
    }
}
