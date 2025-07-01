package com.example.healthysmile.gui.consulta.citas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.controller.consulta.ObtenerCitasPorDiaResponseListener;
import com.example.healthysmile.gui.extraAndroid.adaptadores.CustomDateDecorator;
import com.example.healthysmile.gui.extraAndroid.adaptadores.CustomSpinnerAdapter;
import com.example.healthysmile.gui.extraAndroid.adaptadores.FechaColorDecorator;
import com.example.healthysmile.service.consulta.CitaService;
import com.example.healthysmile.controller.consulta.EspecialistaResponseListenerSpinnerCitas;
import com.example.healthysmile.controller.consulta.ModifyCitaResponseListener;
import com.example.healthysmile.service.consulta.ObtenerCitasPorDiaService;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.example.healthysmile.service.EspecialistaService;
import com.example.healthysmile.R;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Fragment_consulta_citas extends Fragment implements View.OnClickListener, OnDateSelectedListener {

    private EditText inputDate, inputMotivoCita;
    Button btnGuardar;
    private MaterialCalendarView calendarView;
    private int selectedYear, selectedMonth, selectedDay;
    Spinner comboEspecialidad,comboHorariosDisponibles;
    long idUsuario = -1;
    long idEspecialistaSeleccionado = -1;
    String horaSeleccionada = "";
    private List<Long> idsEspecialistas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_citas, container, false);

        calendarView = view.findViewById(R.id.consultas_calendario_citas);
        // 🔴 Lista de fechas ocupadas
        List<CalendarDay> fechasOcupadas = new ArrayList<>();
        fechasOcupadas.add(CalendarDay.from(2025, 7, 1));
        fechasOcupadas.add(CalendarDay.from(2025, 7, 2));

// 🟢 Lista de fechas disponibles
        List<CalendarDay> fechasDisponibles = new ArrayList<>();
        fechasDisponibles.add(CalendarDay.from(2025, 7, 3));
        fechasDisponibles.add(CalendarDay.from(2025, 7, 4));

// ✨ Decoradores de color
        calendarView.addDecorator(new FechaColorDecorator(fechasOcupadas, getResources().getColor(R.color.calendario_ocupado)));
        calendarView.addDecorator(new FechaColorDecorator(fechasDisponibles, getResources().getColor(R.color.calendario_disponible)));

        calendarView.setOnDateChangedListener(this);
        inputDate = view.findViewById(R.id.ConsultaCitaInputDate);
        inputMotivoCita = view.findViewById(R.id.ConsultaCitasInputMotivoCita);
        comboEspecialidad = view.findViewById(R.id.ConsultaCitasComboEspecialistas);
        comboHorariosDisponibles = view.findViewById(R.id.ConsultaCitasComboHorariosDisponibles);

        btnGuardar = view.findViewById(R.id.ConsultaCitasbtnGuardarCita);
        btnGuardar.setOnClickListener(this);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE);
        idUsuario = sharedPreferences.getLong("idUsuario", -1);


        Log.d("idUsuario", "ID Usuario: " + idUsuario);

        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.today())
                .commit();

        setupIconClickListener(inputDate, R.drawable.icon_calendary, this::showDatePickerDialog);

        calendarView.setOnMonthChangedListener((widget, date) -> {
            decorateSundays(date);
        });

        decorateSundays(CalendarDay.today());
        obtenerEspecialistasSpinner();

        return view;
    }

    public void obtenerEspecialistasSpinner() {
        EspecialistaService especialistaService = new EspecialistaService(getContext());
        especialistaService.obtenerEspecialistasSpinnerCitas(new EspecialistaResponseListenerSpinnerCitas() {
            @Override
            public void onResponseSpinnerCitas(List<String> nombres, List<String> especialidades, List<Long> idsEspecialistas) {
                llenarSpinnerConEspecialistas(nombres, especialidades, idsEspecialistas);
            }

            @Override
            public void onErrorSpinnerCitas(String error) {
                Log.e("Error", error);
            }
        });
    }

    private void llenarSpinnerConEspecialistas(List<String> nombres, List<String> especialidades, List<Long> idsEspecialistas) {
        this.idsEspecialistas = idsEspecialistas;
        List<String> opComboEspecialidad = new ArrayList<>();
        for (int i = 0; i < nombres.size(); i++) {
            String item = nombres.get(i) + " - " + especialidades.get(i);
            opComboEspecialidad.add(item);
        }

        String[] opComboEspecialidadArray = opComboEspecialidad.toArray(new String[0]);

        CustomSpinnerAdapter adaptadorOpComboEspecialidad = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item, opComboEspecialidadArray);
        comboEspecialidad.setAdapter(adaptadorOpComboEspecialidad);
        comboEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idEspecialistaSeleccionado = idsEspecialistas.get(position);
                String fechaSeleccionadaActual = inputDate.getText().toString().trim();
                llenarSpinnerConHorarios(fechaSeleccionadaActual,getContext());
                Log.d("Especialista seleccionado", "ID: " + idEspecialistaSeleccionado);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void llenarSpinnerConHorarios(String dia, Context context) {
        Log.d("Debug", "Llamando a llenarSpinnerConHorarios con día: " + dia);
        ObtenerCitasPorDiaService servicio = new ObtenerCitasPorDiaService(context);

        servicio.obtenerCitasPorDia(dia,idEspecialistaSeleccionado,new ObtenerCitasPorDiaResponseListener() {
            @Override
            public void onResponse(List<Long> idsCita, List<String> motivosCita, List<Long> idsEspecialista, List<String> fechasCita) {
                List<String> horariosTotales = generarListaHorarios();
                List<String> horasOcupadas = new ArrayList<>();

                // Mostrar las fechas recibidas
                Log.d("Debug", "Fechas de citas recibidas: " + fechasCita);

                // Recorrer las fechas de las citas y extraer solo la hora en formato 24h
                SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                for (String fechaCita : fechasCita) {
                    try {
                        // Parsear la fecha en el formato adecuado (eliminamos el UTC)
                        Date date = dateFormatLocal.parse(fechaCita); // Usamos el formato que el procedimiento devuelve
                        if (date != null) {
                            // Extraer solo la hora en formato 24h
                            SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            String horaLocal = horaFormat.format(date); // Formateamos la hora en formato 24h
                            horasOcupadas.add(horaLocal.trim());
                            Log.d("Debug", "Fecha parseada: " + fechaCita + " -> Hora local: " + horaLocal.trim());
                        }
                    } catch (Exception e) {
                        Log.e("Horarios", "Error al parsear la fecha: " + fechaCita, e);
                    }
                }

                // Mostrar las horas ocupadas
                Log.d("Debug", "Horas ocupadas: " + horasOcupadas);

                // Filtrar horarios disponibles
                List<String> horariosDisponibles = new ArrayList<>();
                for (String hora : horariosTotales) {
                    if (!horasOcupadas.contains(hora)) {
                        horariosDisponibles.add(hora);
                        Log.d("Debug", "Hora disponible agregada: " + hora);
                    } else {
                        Log.d("Debug", "Hora ocupada: " + hora);
                    }
                }

                // Mostrar las horas disponibles antes de actualizar el Spinner
                Log.d("Debug", "Horarios disponibles para el Spinner: " + horariosDisponibles);

                // Actualizar el Spinner con las horas disponibles
                actualizarSpinnerHorarios(context, horariosDisponibles);
            }

            @Override
            public void onError(String error) {
                Log.e("Horarios", "Error al obtener citas: " + error);
            }
        });
    }


    // Nuevo método para actualizar el Spinner
    private void actualizarSpinnerHorarios(Context context, List<String> horariosDisponibles) {
        String[] opComboHorariosArray = horariosDisponibles.toArray(new String[0]);
        CustomSpinnerAdapter adaptadorOpComboHorariosDisponibles =
                new CustomSpinnerAdapter(context, R.layout.custom_selected_spinner_item, opComboHorariosArray);

        comboHorariosDisponibles.setAdapter(adaptadorOpComboHorariosDisponibles);
        comboHorariosDisponibles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horaSeleccionada = horariosDisponibles.get(position);
                Log.d("Horario seleccionado", "Hora: " + horaSeleccionada);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private List<String> generarListaHorarios() {
        List<String> horarios = new ArrayList<>();
        for (int hora = 8; hora <= 16; hora++) {
            horarios.add(String.format("%02d:00", hora));
        }
        return horarios;
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if (selected) {
            selectedYear = date.getYear();
            selectedMonth = date.getMonth();
            selectedDay = date.getDay();

            // Formatear correctamente la fecha seleccionada (YYYY-MM-DD)
            String fechaSeleccionada = String.format(Locale.getDefault(),"%04d-%02d-%02d",
                    selectedYear, selectedMonth, selectedDay);

            // Establecer la fecha en el EditText
            inputDate.setText(fechaSeleccionada);

            llenarSpinnerConHorarios(fechaSeleccionada,getContext());
        }
    }


    public void crearCita() {
        String fecha = inputDate.getText().toString();
        String hora = comboHorariosDisponibles.getSelectedItem().toString();
        String motivo = inputMotivoCita.getText().toString();

        CitaService citaService = new CitaService(getContext());
        citaService.crearCita(fecha,hora,motivo,idUsuario,idEspecialistaSeleccionado);
    }

    private void setupIconClickListener(EditText editText, int iconResId, Runnable action) {
        editText.setOnTouchListener((v, event) -> {
            int drawableRight = 2;  // Índice para el icono derecho
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Verificar si el toque fue en el área del icono derecho
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[drawableRight].getBounds().width())) {
                    action.run();  // Ejecutar la acción asociada al icono
                    return true;
                }
            }
            return false;
        });
    }

    private void decorateSundays(CalendarDay date) {
        // Obtener el mes y año del calendario actual
        int currentYear = date.getYear();
        int currentMonth = date.getMonth(); // Mes en formato 1-12 (no 0-11)

        // Usar Calendar para obtener el número máximo de días del mes
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth - 1, 1); // El mes en Calendar es 0-11, por eso restamos 1
        int maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Limpiar los decoradores previos (si hay alguno)
        calendarView.removeDecorators();

        // Recorremos los días del mes
        for (int day = 1; day <= maxDaysInMonth; day++) {
            try {
                // Crear un LocalDateTime con la fecha específica
                LocalDateTime localDateTime = LocalDateTime.of(currentYear, currentMonth, day, 0, 0);

                // Crear un CalendarDay a partir de LocalDateTime
                CalendarDay calendarDay = CalendarDay.from(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());

                // Verificar si el día es un domingo (Calendar.SUNDAY = 1)
                if (localDateTime.getDayOfWeek().getValue() == 7) { // 7 es domingo en `DayOfWeek`
                    // Si es domingo, aplicar el decorador
                    calendarView.addDecorator(new CustomDateDecorator(calendarDay, getResources().getColor(R.color.calendario_inhabil)));
                }
            } catch (Exception e) {
                // Manejo de excepción en caso de error de fecha
                continue;
            }
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Crear un objeto Calendar con la fecha seleccionada
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, monthOfYear, dayOfMonth);

                    // Verificar si la fecha seleccionada es un domingo (Calendar.SUNDAY = 1)
                    if (selectedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        // Mostrar un mensaje de error o advertencia
                        Toast.makeText(getContext(), "No puedes seleccionar un domingo.", Toast.LENGTH_SHORT).show();

                        // Mostrar nuevamente el DatePickerDialog sin cerrar el anterior
                        showDatePickerDialog();
                    } else {
                        // Actualizar la fecha seleccionada
                        selectedYear = year;
                        selectedMonth = monthOfYear;
                        selectedDay = dayOfMonth;

                        // Formatear y mostrar la fecha en el EditText de fecha
                        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
                        inputDate.setText(sdf.format(selectedDate.getTime()));

                        // Actualizar el calendario con la fecha seleccionada
                        CalendarDay selectedCalendarDay = CalendarDay.from(selectedYear, selectedMonth + 1, selectedDay);
                        calendarView.setSelectedDate(selectedCalendarDay);
                    }
                },
                selectedYear, selectedMonth, selectedDay);

        // Configurar el DatePicker para no permitir fechas pasadas
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        // Evitar que el usuario seleccione domingos manualmente
        datePickerDialog.setOnDismissListener(dialog -> {
            // Se ejecuta cuando el DatePickerDialog se cierra, reiniciamos la lógica
            if (datePickerDialog.isShowing()) {
                datePickerDialog.show(); // Esto ayuda a reiniciar si el usuario se quedó en un domingo
            }
        });

        datePickerDialog.show();
    }



    @Override
    public void onClick(View v) {
        crearCita();
    }


    public void modificarCita() {
        // Obtener los valores de la UI
        String fechaSeleccionada = inputDate.getText().toString(); // Fecha seleccionada en el CalendarView
        String horaSeleccionada = comboHorariosDisponibles.getSelectedItem().toString();
        String motivoCita = inputMotivoCita.getText().toString(); // Motivo de la cita
        long idEspecialista = idsEspecialistas.get(comboEspecialidad.getSelectedItemPosition()); // Especialista seleccionado

        if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty() || motivoCita.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        CitaService citaService = new CitaService(getContext());
        citaService.modificarCita(idUsuario,fechaSeleccionada,horaSeleccionada,motivoCita,idEspecialista, new ModifyCitaResponseListener() {
            @Override
            public void onResponse(String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCitaNoEncontrada(String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void eliminarCita() {
        String fechaSeleccionada = inputDate.getText().toString(); // Fecha seleccionada en el CalendarView
        String horaSeleccionada = comboHorariosDisponibles.getSelectedItem().toString();

        if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        CitaService citaService = new CitaService(getContext());
        citaService.eliminarCita(idUsuario, new ModifyCitaResponseListener() {
            @Override
            public void onResponse(String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCitaNoEncontrada(String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }
}
