package com.example.healthysmile.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;  // Importa Toast

import com.bumptech.glide.Glide;
import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.example.healthysmile.R;
import com.example.healthysmile.gui.settings.Settings;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthysmile.databinding.ActivityNavigationDrawerFragmentsBinding;

public class NavigationDrawerFragments extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationDrawerFragmentsBinding binding;

    String foto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationDrawerFragmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawerFragments.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("nombreUsuario",null);
        String correoUsuario = sharedPreferences.getString("correoUsuario",null);
        String tipoUsuario = sharedPreferences.getString("tipoUsuario",null);
        foto = sharedPreferences.getString("fotoUsuario", null);

        Menu menu = navigationView.getMenu();
        if ("Administrador".equals(tipoUsuario)) {
            MenuItem adminItem = menu.findItem(R.id.nav_gestionAdmin);
            if (adminItem != null) {
                adminItem.setVisible(true);
            }
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_ayudaYSoporte,
                R.id.nav_ConsultaVirtual,
                R.id.nav_EducacionDental,
                R.id.nav_TiendaVirutal,
                R.id.nav_gestionAdmin,
                R.id.fragment_visualizacion_modelos_3d_gingivitis,
                R.id.fragment_visualizacion_modelos_3d_caries_dentales,
                R.id.fragment_visualizacion_modelos_3d_cancer_bucal,
                R.id.fragment_visualizacion_modelos_3d_traumatismos_bucodentales,
                R.id.fragment_visualizacion_modelos_3d_halitosis,
                R.id.fragment_visualizacion_modelos_3d_sensibilidad_dental,
                R.id.fragment_visualizacion_modelos_3d_implantes,
                R.id.fragment_visualizacion_modelos_3d_periodontitis,
                R.id.fragment_consulta_list_chat,
                R.id.fragment_consulta_list_chat_paciente)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer_fragments);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavController navController = Navigation.findNavController(NavigationDrawerFragments.this, R.id.nav_host_fragment_content_navigation_drawer_fragments);

                // Manejar la opción nav_ConsultaVirtual
                if (item.getItemId() == R.id.nav_ConsultaVirtual) {
                    navController.popBackStack(R.id.nav_ConsultaVirtual, false);
                    navController.navigate(R.id.nav_ConsultaVirtual);
                    return true;
                }

                // Manejar la opción nav_home
                if (item.getItemId() == R.id.nav_home) {
                    navController.popBackStack(R.id.nav_home, false);
                    navController.navigate(R.id.nav_home);
                    return true;
                }

                // Manejar la opción nav_ayudaYSoporte
                if (item.getItemId() == R.id.nav_ayudaYSoporte) {
                    navController.popBackStack(R.id.nav_ayudaYSoporte, false);
                    navController.navigate(R.id.nav_ayudaYSoporte);
                    return true;
                }

                if (item.getItemId() == R.id.nav_EducacionDental) {
                    navController.popBackStack(R.id.nav_EducacionDental, false);
                    navController.navigate(R.id.nav_EducacionDental);
                    return true;
                }

                if (item.getItemId() == R.id.nav_TiendaVirutal) {
                    navController.popBackStack(R.id.nav_TiendaVirutal, false);
                    navController.navigate(R.id.nav_TiendaVirutal);
                    return true;
                }

                if (item.getItemId() == R.id.nav_gestionAdmin) {
                    navController.popBackStack(R.id.nav_gestionAdmin, false);
                    navController.navigate(R.id.nav_gestionAdmin);
                    return true;
                }

                if(item.getItemId() == R.id.nav_logout){
                    SharedPreferencesHelper manejadorShadPreferences = new SharedPreferencesHelper(getApplicationContext());
                    manejadorShadPreferences.terminarSesion();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intentoIrInit = new Intent(getApplicationContext(), InitAplication.class);
                        startActivity(intentoIrInit);
                    }, 200);
                }



                return false;
            }
        });



 // Asegúrate de usar la clave correcta aquí
        if(tipoUsuario.equals("Paciente")){

        }else
            if(tipoUsuario.equals("Especialista")){

            }

        Toast.makeText(this, "Datos recibidos: Nombre - " + nombreUsuario + ", Correo - " + correoUsuario + ", TipoUsuario - " + tipoUsuario, Toast.LENGTH_LONG).show();

        // Referenciando los TextViews del header
        TextView nombreUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_nombreUser);
        TextView tipoUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_tipo_user);
        TextView correoUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_correoUser);
        ImageView fotoPerfil = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_foto_perfil);

        // Verificando si los datos no son null y actualizando las vistas
        if (nombreUsuario != null) {
                nombreUser.setText(nombreUsuario);
            }
        else {
            Toast.makeText(this, "Nombre es null", Toast.LENGTH_SHORT).show();
        }

        if (correoUsuario != null) {
                correoUser.setText(correoUsuario);
        } else {
            Toast.makeText(this, "Correo es null", Toast.LENGTH_SHORT).show();
        }

        if (tipoUsuario != null) {
                tipoUser.setText(tipoUsuario);
        } else {
            Toast.makeText(this, "Tipo de usuario es null", Toast.LENGTH_SHORT).show();
        }

        if (tipoUsuario.equals("Paciente")) {
            String rutaImagen = foto;  // Obtener la ruta de la imagen desde el objeto Usuario
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                // Si el usuario tiene una foto de perfil, carga la imagen desde la URI
                Glide.with(this)
                        .load(Uri.parse(rutaImagen))  // Ruta de la imagen (URI)
                        .into(fotoPerfil); // ImageView donde se cargará la imagen
            } else {
                // Si no tiene foto de perfil, carga la imagen predeterminada desde los recursos
                Glide.with(this)
                        .load(R.drawable.default_photo_paciente)  // Ruta del recurso predeterminado
                        .into(fotoPerfil); // ImageView donde se cargará la imagen
            }
        }else {
            String rutaImagen = foto;  // Obtener la ruta de la imagen desde el objeto Usuario
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                // Si el usuario tiene una foto de perfil, carga la imagen desde la URI
                Glide.with(this)
                        .load(Uri.parse(rutaImagen))  // Ruta de la imagen (URI)
                        .into(fotoPerfil); // ImageView donde se cargará la imagen
            } else {
                // Si no tiene foto de perfil, carga la imagen predeterminada desde los recursos
                Glide.with(this)
                        .load(R.drawable.default_photo_perfil_especialista)  // Ruta del recurso predeterminado
                        .into(fotoPerfil); // ImageView donde se cargará la imagen
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.action_settings){
                Intent intentIrSettings = new Intent(this, Settings.class);
                startActivity(intentIrSettings);
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer_fragments);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
