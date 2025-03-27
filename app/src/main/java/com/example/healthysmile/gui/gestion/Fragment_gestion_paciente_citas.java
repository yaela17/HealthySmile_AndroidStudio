package com.example.healthysmile.gui.gestion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.gestion.ObtenerCitasPacienteResponseListener;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorListaCitas;
import com.example.healthysmile.gui.extraAndroid.adaptadores.CustomDateDecorator;
import com.example.healthysmile.service.gestion.ObtenerCitasPacienteService;
import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Fragment_gestion_paciente_citas extends Fragment {

    private MaterialCalendarView calendarView;
    private ListView listaCitas;
    SharedPreferencesHelper sharedPreferencesHelper;
    long idUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_paciente_citas, container, false);

        calendarView = view.findViewById(R.id.gestion_paciente_citas_calendario);
        listaCitas = view.findViewById(R.id.gestion_paciente_citas_listView_citas);

        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.today())
                .commit();

        // Obtener las citas del paciente (suponiendo que el id del usuario ya está disponible)
        idUsuario = sharedPreferencesHelper.obtenerIdUsuario();
        obtenerCitasPaciente(idUsuario);

        return view;
    }

    private void obtenerCitasPaciente(long idUsuario) {
        ObtenerCitasPacienteService service = new ObtenerCitasPacienteService(getContext());
        service.obtenerCitasPaciente(new ObtenerCitasPacienteResponseListener() {
            @Override
            public void onResponse(List<Long> idsCitas, List<String> motivosCita, List<String> fechasCita, List<Long> idsEspecialistas, List<String> nombresEspecialistas) {

                List<String> fechas = new ArrayList<>();
                List<String> horas = new ArrayList<>();

                // Separar las fechas y las horas
                for (String fechaHora : fechasCita) {
                    String[] fechaHoraSeparada = fechaHora.split(" ");

                    // Verificar que haya al menos dos partes (fecha y hora)
                    if (fechaHoraSeparada.length >= 2) {
                        fechas.add(fechaHoraSeparada[0]); // Fecha
                        horas.add(fechaHoraSeparada[1]); // Hora (solo parte de la hora, sin intentar acceder al índice [2])
                    } else {
                        // Si el formato es incorrecto o solo hay una parte, manejar el caso
                        fechas.add(fechaHora); // Agregar la fecha sin hora
                        horas.add(""); // Agregar hora vacía
                    }
                }

                // Convertir las listas a arreglos
                Long[] idsCitasArray = idsCitas.toArray(new Long[0]);
                String[] motivosCitaArray = motivosCita.toArray(new String[0]);
                String[] fechasArray = fechas.toArray(new String[0]);
                String[] horasArray = horas.toArray(new String[0]);
                Long[] idsEspecialistasArray = idsEspecialistas.toArray(new Long[0]);
                String[] nombresEspecialistasArray = nombresEspecialistas.toArray(new String[0]);

                // Cargar las citas en la lista con los datos procesados
                cargarCitasEnLista(idsCitasArray, motivosCitaArray, fechasArray, horasArray, idsEspecialistasArray, nombresEspecialistasArray);
                // Marcar las fechas ocupadas en el calendario
                marcarFechasEnCalendarView(fechas);
                // Establecer la fecha mínima
                insertarFechaMinima(fechas);
            }
            @Override
            public void onError(String error) {
                Log.e("ObtenerCitasPaciente", error);
            }
        }, idUsuario);
    }

    // Método para cargar las citas en la lista
    private void cargarCitasEnLista(Long[] idsCitas, String[] motivosCita, String[] fechas, String[] horas, Long[] idsEspecialistas, String[] nombresEspecialistas) {
        AdaptadorListaCitas adaptadorListaCitas = new AdaptadorListaCitas(getContext(),fechas,horas,motivosCita,idsEspecialistas,nombresEspecialistas,idsCitas,getChildFragmentManager());
        listaCitas.setAdapter(adaptadorListaCitas);
    }

    // Método para marcar las fechas ocupadas en el CalendarView
    private void marcarFechasEnCalendarView(List<String> fechasCita) {
        for (String fechaCita : fechasCita) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(fechaCita.split(" ")[0], formatter);
                CalendarDay calendarDay = CalendarDay.from(localDate.getYear(), localDate.getMonthValue() , localDate.getDayOfMonth()); // Mes en CalendarDay es 0-based

                // Agregar el decorador (color) para las fechas ocupadas
                CustomDateDecorator customDateDecorator = new CustomDateDecorator(calendarDay,getResources().getColor(R.color.calendario_pasadas));
                calendarView.addDecorator(customDateDecorator);
            } catch (Exception e) {
                Log.e("Fecha", "Error al parsear la fecha: " + fechaCita, e);
            }
        }
    }

    // Método para establecer la fecha mínima en el CalendarView
    private void insertarFechaMinima(List<String> fechasCita) {
        LocalDate fechaMinima = LocalDate.now(); // Inicializar con la fecha actual

        for (String fechaCita : fechasCita) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(fechaCita.split(" ")[0], formatter);

                if (localDate.isBefore(fechaMinima)) {
                    fechaMinima = localDate; // Actualizar si encontramos una fecha más temprana
                }
            } catch (Exception e) {
                Log.e("Fecha", "Error al parsear la fecha: " + fechaCita, e);
            }
        }

        // Establecer la fecha mínima en el CalendarView
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(fechaMinima.getYear(), fechaMinima.getMonthValue() , fechaMinima.getDayOfMonth()))
                .commit();
    }

}
