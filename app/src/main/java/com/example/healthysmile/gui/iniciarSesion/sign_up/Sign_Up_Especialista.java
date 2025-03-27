package com.example.healthysmile.gui.iniciarSesion.sign_up;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.TemplateParams;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.service.EmailServiceJS;
import com.example.healthysmile.utils.IconUtils;
import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.R;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.gui.extraAndroid.adaptadores.CustomSpinnerAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Sign_Up_Especialista extends Fragment implements View.OnClickListener {
    String[] opComboEspecialidad;
    CustomSpinnerAdapter adaptadorOpComboEspecialidad;

    EditText inputNombre, inputCorreo, inputContrasena, inputCedulaProfesional;
    EditText inputDescripcion;
    Spinner comboEspecialidad;
    Button btnRegistrarse;
    SharedPreferencesHelper manejadorShadPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign__up__especialista, container, false);

        manejadorShadPreferences = new SharedPreferencesHelper(getContext());

        inputNombre = view.findViewById(R.id.SignUpEspecialistaInputNombre);
        inputCorreo = view.findViewById(R.id.SignUpEspecialistaInputCorreo);
        inputContrasena = view.findViewById(R.id.SignUpEspecialistaInputContrasena);
        inputCedulaProfesional = view.findViewById(R.id.SignUpEspecialistaInputCedulaProfesional);
        comboEspecialidad = view.findViewById(R.id.SignUpEspecialistaComboEspecialidad);
        inputDescripcion = view.findViewById(R.id.SignUpEspecialistaInputDescripcion);
        btnRegistrarse = view.findViewById(R.id.SignUpEspecialistaBtnRegistrarse);
        btnRegistrarse.setOnClickListener(this);

        // Inicializa las opciones del spinner
        opComboEspecialidad = new String[]{
                getString(R.string.opcion_combo_especialidad_selecciona_tu_especialidad),
                getString(R.string.opcion_combo_especialidad_odontologia_general),
                getString(R.string.opcion_combo_especialidad_ortodoncia),
                getString(R.string.opcion_combo_especialidad_periodoncia),
                getString(R.string.opcion_combo_especialidad_endodoncia),
                getString(R.string.opcion_combo_especialidad_cirugia_bucal),
                getString(R.string.opcion_combo_especialidad_implantologia),
                getString(R.string.opcion_combo_especialidad_odontopediatria),
                getString(R.string.opcion_combo_especialidad_odontologia_estetica),
                getString(R.string.opcion_combo_especialidad_prostodoncia),
                getString(R.string.opcion_combo_especialidad_radiologia_oral_y_maxilofacial),
                getString(R.string.opcion_combo_especialidad_medicina_oral),
                getString(R.string.opcion_combo_especialidad_patologia_oral),
                getString(R.string.opcion_combo_especialidad_odontologia_forense),
                getString(R.string.opcion_combo_especialidad_odontologia_geriatrica),
                getString(R.string.opcion_combo_especialidad_odontologia_preventiva),
                getString(R.string.opcion_combo_especialidad_terapia_orofacial),
                getString(R.string.opcion_combo_especialidad_oclusion),
                getString(R.string.opcion_combo_especialidad_sistemas_dentales_digitales),
                getString(R.string.opcion_combo_especialidad_higiene_dental)
        };

        // Configura el adaptador personalizado
        adaptadorOpComboEspecialidad = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item, opComboEspecialidad);
        comboEspecialidad.setAdapter(adaptadorOpComboEspecialidad);

        // Configura el listener para el spinner
        comboEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adaptadorOpComboEspecialidad.setSelectedPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adaptadorOpComboEspecialidad.setSelectedPosition(-1);
            }
        });

        IconUtils icono = new IconUtils();
        icono.setupPasswordVisibility(inputContrasena);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.SignUpEspecialistaBtnRegistrarse) {
            registrarEspecialista();
        }
    }

    private void registrarEspecialista() {
        String nombre = inputNombre.getText().toString().trim();
        String correo = inputCorreo.getText().toString().trim();
        String contrasena = inputContrasena.getText().toString().trim();
        String cedulaProfesional = inputCedulaProfesional.getText().toString().trim();
        String especialidad = comboEspecialidad.getSelectedItem().toString().trim();
        String descripcion = inputDescripcion.getText().toString().trim();
        int nivelPermisos = 2;
        String tipoUser = "Especialista";

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || cedulaProfesional.isEmpty() ||
                especialidad.equals(getString(R.string.opcion_combo_especialidad_selecciona_tu_especialidad))) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Especialista especialista = new Especialista(contrasena, correo, null, null, nivelPermisos, nombre, tipoUser, cedulaProfesional, descripcion, especialidad, null);
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        apiService.crearEspecialista(especialista).enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Especialista registrado con éxito", Toast.LENGTH_SHORT).show();
                    long idUsuario = response.body().getIdUsuario();
                    long idEspecialista = response.body().getIdEspecialista();
                    Log.d("RegistroEspecialista", "ID Usuario: " + idUsuario);
                    Log.d("RegistroEspecialista", "ID Especialista: " + idEspecialista);
                    manejadorShadPreferences.guardarEspecialista(nombre, correo, tipoUser, nivelPermisos, cedulaProfesional, descripcion, especialidad);
                    manejadorShadPreferences.guardarIdUsuario(idUsuario);
                    manejadorShadPreferences.guardarIdEspecialista(idEspecialista);

                    EmailServiceJS emailService = new EmailServiceJS();
                    String verification_code = generadorCodigoDeVerificacion();
                    manejadorShadPreferences.guardarCodigoVerificacion(verification_code);
                    TemplateParams templateParams = new TemplateParams(contrasena,correo,nombre,verification_code);
                    emailService.enviarCorreo(templateParams);

                    limpiarCampos();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.signUpFragmentContainer, new Fragment_sign__up__verificacion_correo()) // Reemplaza con el nuevo fragmento
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Toast.makeText(getActivity(), "Error al conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        inputNombre.setText("");
        inputCorreo.setText("");
        inputContrasena.setText("");
        inputCedulaProfesional.setText("");
        inputDescripcion.setText("");
        comboEspecialidad.setSelection(0);
    }

    public String generadorCodigoDeVerificacion() {
        int code = (int) (100000 + Math.random() * 900000);
        return String.valueOf(code);
    }

}
