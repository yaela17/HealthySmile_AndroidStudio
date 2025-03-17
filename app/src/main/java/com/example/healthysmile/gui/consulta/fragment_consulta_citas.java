package com.example.healthysmile.gui.consulta;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.gui.Adaptadores.CustomDateDecorator;
import com.example.healthysmile.gui.Adaptadores.CustomSpinnerAdapter;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.service.CitaResponseListener;
import com.example.healthysmile.service.CitaService;
import com.example.healthysmile.service.EspecialistaResponseListenerSpinnerCitas;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;


public class fragment_consulta_citas extends Fragment implements View.OnClickListener, OnDateSelectedListener {

    private EditText inputDate, inputTime,inputMotivoCita;
    Button btnGuardar;
    private MaterialCalendarView calendarView;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    Spinner comboEspecialidad;
    long idUsuario = -1;
    long idEspecialistaSeleccionado = -1;
    private List<Long> idsEspecialistas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_citas, container, false);

        calendarView = view.findViewById(R.id.consultas_calendario_citas);
        calendarView.setOnDateChangedListener(this);
        inputDate = view.findViewById(R.id.ConsultaCitaInputDate);
        inputTime = view.findViewById(R.id.ConsultaCitaInputTime);
        inputMotivoCita = view.findViewById(R.id.ConsultaCitasInputMotivoCita);
        comboEspecialidad = view.findViewById(R.id.ConsultaCitasComboEspecialistas);
        btnGuardar = view.findViewById(R.id.ConsultaCitasbtnGuardarCita);
        btnGuardar.setOnClickListener(this);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE);
        idUsuario = sharedPreferences.getLong("idUsuario", -1);
        Log.d("idUsuario", "ID Usuario: " + idUsuario);

        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.today())
                .commit();

        setupIconClickListener(inputDate, R.drawable.icon_calendary, this::showDatePickerDialog);
        setupIconClickListener(inputTime, R.drawable.icon_time_hour, this::showTimePickerDialog);

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

        Spinner comboEspecialidad = getView().findViewById(R.id.ConsultaCitasComboEspecialistas);
        CustomSpinnerAdapter adaptadorOpComboEspecialidad = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item, opComboEspecialidadArray);
        comboEspecialidad.setAdapter(adaptadorOpComboEspecialidad);
        comboEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idEspecialistaSeleccionado = idsEspecialistas.get(position);
                Log.d("Especialista seleccionado", "ID: " + idEspecialistaSeleccionado);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

            // Obtener la hora ingresada en el inputTime
            String hora12 = inputTime.getText().toString().trim();

            // Verificar que el usuario haya ingresado una hora
            if (hora12.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, ingrese una hora válida.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir la hora de formato 12 horas a 24 horas
            String hora24 = convertirHora12A24(hora12);

            // Verificar si la conversión fue exitosa
            if (hora24 == null) {
                Toast.makeText(getContext(), "Error al convertir la hora", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamar al servicio para obtener la cita
            CitaService citaService = new CitaService(getContext());
            citaService.obtenerCitaPorFecha(idUsuario, fechaSeleccionada, hora24, new CitaResponseListener() {
                @Override
                public void onResponse(long idCita, String motivoCita, long idEspecialista) {
                    // Mostrar los datos en los campos correspondientes
                    inputMotivoCita.setText(motivoCita);
                    idEspecialistaSeleccionado = idEspecialista;

                    // Seleccionar automáticamente el especialista en el Spinner
                    seleccionarEspecialistaEnSpinner(idEspecialista);

                    Toast.makeText(getContext(), "Cita encontrada: " + motivoCita, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCitaNoEncontrada(String mensaje) {
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            });

            // Mensaje de depuración
            Log.d("CalendarView", "Fecha seleccionada: " + fechaSeleccionada + " Hora: " + hora24);
        }
    }


    private void seleccionarEspecialistaEnSpinner(long idEspecialista) {
        int posicionEncontrada = -1;

        // Buscar la posición del especialista en la lista
        for (int i = 0; i < idsEspecialistas.size(); i++) {
            if (idsEspecialistas.get(i) == idEspecialista) {
                posicionEncontrada = i;
                break;
            }
        }

        // Si se encontró la posición, seleccionar en el Spinner
        if (posicionEncontrada != -1) {
            comboEspecialidad.setSelection(posicionEncontrada);
            Log.d("Especialista seleccionado", "ID: " + idEspecialista + ", Posición: " + posicionEncontrada);
        } else {
            Log.e("Especialista no encontrado", "No se encontró el ID: " + idEspecialista);
        }
    }

    public void crearCita() {
        String fecha = inputDate.getText().toString();
        String hora = convertirHora12A24(inputTime.getText().toString());
        String motivo = inputMotivoCita.getText().toString();

        Map<String, Object> citaDatos = new HashMap<>();
        citaDatos.put("fecha", fecha);
        citaDatos.put("hora", hora);
        citaDatos.put("motivo", motivo);
        citaDatos.put("idUsuario", idUsuario);
        citaDatos.put("idEspecialista", idEspecialistaSeleccionado);

        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        retrofit2.Call<ApiNodeMySqlRespuesta> call = apiService.crearCita(citaDatos);
        call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(retrofit2.Call<ApiNodeMySqlRespuesta> call, retrofit2.Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful()) {
                    ApiNodeMySqlRespuesta respuesta = response.body();
                    if (respuesta != null) {
                        // Manejar la respuesta exitosa, por ejemplo, mostrar un mensaje
                        Toast.makeText(getContext(), respuesta.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        // Si la respuesta es nula
                        Toast.makeText(getContext(), "Error: Respuesta vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar el error de la respuesta
                    Toast.makeText(getContext(), "Error al crear la cita", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                // Manejar el fallo de la llamada
                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
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
                        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
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

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    if (hourOfDay >= 8 && hourOfDay <= 16) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;

                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        String timeWithAmPm = sdf.format(calendar.getTime());

                        inputTime.setText(timeWithAmPm);
                    } else {
                        Toast.makeText(getContext(), "Por favor seleccione una hora entre las 8:00 AM y las 4:00 PM.", Toast.LENGTH_SHORT).show();
                        showTimePickerDialog();
                    }
                },
                selectedHour, selectedMinute, false);

        timePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        crearCita();
    }

    public String convertirHora12A24(String hora12) {
        try {
            SimpleDateFormat formato12Horas = new SimpleDateFormat("hh:mm a"); // 12 horas con a.m./p.m.
            SimpleDateFormat formato24Horas = new SimpleDateFormat("HH:mm:ss"); // 24 horas

            Date date = formato12Horas.parse(hora12);
            return formato24Horas.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
