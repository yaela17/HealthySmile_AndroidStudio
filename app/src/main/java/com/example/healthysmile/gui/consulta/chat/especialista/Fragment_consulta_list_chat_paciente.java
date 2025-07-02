package com.example.healthysmile.gui.consulta.chat.especialista;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthysmile.controller.consulta.ObtenerPacientesResponseListener;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorListaPacientes;
import com.example.healthysmile.R;
import com.example.healthysmile.model.DAO.ChatContactoDao;
import com.example.healthysmile.model.entities.ChatContactoLocalDB;
import com.example.healthysmile.service.consulta.ObtenerPacientesService;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Fragment_consulta_list_chat_paciente extends Fragment {

    private static final String TAG = "FragmentChatPaciente";

    private ListView listaChatUsuarios;
    private String tipoUsuario;

    private FirebaseFirestore db;

    private List<String> nombresLV = new ArrayList<>();
    private List<String> correosLV = new ArrayList<>();
    private List<String> fotosPerfilLV = new ArrayList<>();
    private List<Long> idsPacientesLV = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_list_chat_paciente, container, false);

        listaChatUsuarios = view.findViewById(R.id.consultaListViewChatsPaciente);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");

        db = FirebaseFirestore.getInstance();

        Log.d(TAG, "Tipo de usuario: " + tipoUsuario);

        if (tipoUsuario.equals("Especialista")) {
            cargarPacientes();
        }

        return view;
    }

    private void cargarPacientes() {
        Log.d(TAG, "Iniciando carga de pacientes...");

        nombresLV.clear();
        correosLV.clear();
        idsPacientesLV.clear();
        fotosPerfilLV.clear();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        long idEspecialista = sharedPreferences.getLong("idEspecialista", -1);

        Log.d(TAG, "ID especialista: " + idEspecialista);

        if (idEspecialista == -1) {
            Toast.makeText(getContext(), "Error: No se encontró el ID del especialista.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hayConexion()) {
            Log.d(TAG, "Hay conexión, consultando Firestore...");
            db.collection("chats")
                    .whereEqualTo("idEspecialista", idEspecialista)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Log.d(TAG, "Firestore - documentos encontrados: " + queryDocumentSnapshots.size());
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<Long> idsPacientesOnline = new ArrayList<>();

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Long idPaciente = doc.getLong("idUsuario");
                                String nombrePaciente = doc.getString("nombre");
                                String correoPaciente = doc.getString("correo");

                                if (idPaciente != null) {
                                    idsPacientesOnline.add(idPaciente);
                                }
                            }

                            Log.d(TAG, "IDs pacientes obtenidos: " + idsPacientesOnline);

                            if (idsPacientesOnline.isEmpty()) {
                                Toast.makeText(getContext(), "No se encontraron pacientes asociados.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            obtenerDetallesPacientes(idsPacientesOnline);
                        } else {
                            Toast.makeText(getContext(), "No se encontraron chats asociados al especialista.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al consultar chats", e);
                        Toast.makeText(getContext(), "Error al consultar chats", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.d(TAG, "No hay conexión. Cargando desde base local...");
            cargarContactosDesdeLocalOffline();
        }
    }

    private void obtenerDetallesPacientes(List<Long> idsPacientes) {
        Log.d(TAG, "Obteniendo detalles de pacientes para IDs: " + idsPacientes);
        ObtenerPacientesService pacientesService = new ObtenerPacientesService(getContext());

        pacientesService.obtenerPacientesChat(new ObtenerPacientesResponseListener() {
            @Override
            public void onResponse(List<String> nombres, List<String> correos, List<Long> idsPacientes, List<String> fotosPerfil) {
                Log.d(TAG, "Detalles obtenidos correctamente. Cantidad: " + nombres.size());

                nombresLV.clear();
                correosLV.clear();
                idsPacientesLV.clear();
                fotosPerfilLV.clear();

                for (int i = 0; i < nombres.size(); i++) {
                    nombresLV.add(nombres.get(i));
                    correosLV.add(correos.get(i));
                    idsPacientesLV.add(idsPacientes.get(i));
                    fotosPerfilLV.add(fotosPerfil.get(i));

                    Log.d(TAG, "Detalle paciente #" + i + ": Nombre='" + nombres.get(i) + "', Correo='" + correos.get(i) + "'");
                }

                Log.d(TAG, "Actualizando ListView con detalles obtenidos...");
                guardarContactosEnLocal(idsPacientesLV, nombresLV, correosLV);
                cargarListView();
                Log.d(TAG, "ListView actualizado con detalles.");
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error al obtener detalles de pacientes: " + error);
                Toast.makeText(getContext(), "Error al obtener datos de pacientes", Toast.LENGTH_SHORT).show();
            }
        }, idsPacientes);
    }

    private void guardarContactosEnLocal(List<Long> idsPacientesOnline, List<String> nombres, List<String> correos) {
        Log.d(TAG, "Guardando contactos localmente, total: " + idsPacientesOnline.size());
        new Thread(() -> {
            ChatContactoDao dao = com.example.healthysmile.model.LocalDB.getInstance(getContext()).chatContactoDao();

            Log.d(TAG, "Eliminando contactos antiguos...");
            dao.eliminarTodos();
            Log.d(TAG, "Contactos antiguos eliminados.");

            for (int i = 0; i < idsPacientesOnline.size(); i++) {
                ChatContactoLocalDB contacto = new ChatContactoLocalDB();
                contacto.idUsuarioContacto = idsPacientesOnline.get(i);
                contacto.tipoUsuarioContacto = "Paciente";
                contacto.nombre = nombres.get(i);
                contacto.correo = correos.get(i);

                dao.insertarContacto(contacto);

                Log.d(TAG, "Guardado contacto #" + i + ": Nombre='" + contacto.nombre + "', Correo='" + contacto.correo + "'");
            }

            Log.d(TAG, "Todos los contactos guardados localmente.");
        }).start();
    }

    private void cargarContactosDesdeLocalOffline() {
        Log.d(TAG, "Cargando contactos desde base local (offline)...");
        new Thread(() -> {
            ChatContactoDao dao = com.example.healthysmile.model.LocalDB.getInstance(getContext()).chatContactoDao();

            List<ChatContactoLocalDB> contactos = dao.obtenerTodos();
            Log.d(TAG, "Contactos encontrados localmente: " + contactos.size());

            if (contactos.isEmpty()) {
                Log.d(TAG, "No hay contactos guardados localmente.");
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "No hay datos offline disponibles.", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            nombresLV.clear();
            correosLV.clear();
            idsPacientesLV.clear();
            fotosPerfilLV.clear();

            for (int i = 0; i < contactos.size(); i++) {
                ChatContactoLocalDB contacto = contactos.get(i);
                idsPacientesLV.add(contacto.idUsuarioContacto);
                nombresLV.add(contacto.nombre != null ? contacto.nombre : "");
                correosLV.add(contacto.correo != null ? contacto.correo : "");
                fotosPerfilLV.add(contacto.fotoPerfil != null ? contacto.fotoPerfil : "No disponible");

                Log.d(TAG, "Contacto cargado localmente #" + i + ": Nombre='" + contacto.nombre + "', Correo='" + contacto.correo + "'");
            }

            Log.d(TAG, "Actualizando ListView en UI thread...");
            getActivity().runOnUiThread(() -> {
                cargarListView();
                Log.d(TAG, "ListView actualizado con datos offline.");
            });
        }).start();
    }


    private boolean hayConexion() {
        // Aquí tu lógica para detectar conexión real
        return true;
    }

    private void cargarListView() {
        Log.d(TAG, "Preparando ListView con " + nombresLV.size() + " elementos.");
        String[] nombresArray = nombresLV.toArray(new String[0]);
        String[] correosArray = correosLV.toArray(new String[0]);
        long[] idsArray = idsPacientesLV.stream().mapToLong(Long::longValue).toArray();
        String[] fotosPerfil = fotosPerfilLV.toArray(new String[0]);

        AdaptadorListaPacientes adaptador = new AdaptadorListaPacientes(
                getContext(),
                correosArray,
                idsArray,
                this,
                nombresArray,
                fotosPerfil
        );

        listaChatUsuarios.setAdapter(adaptador);
        Log.d(TAG, "ListView seteado con el adaptador.");
    }
}
