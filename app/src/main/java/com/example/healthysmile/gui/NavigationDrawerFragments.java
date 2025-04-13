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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;  // Importa Toast

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.healthysmile.utils.ReutilizableMethods;
import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.example.healthysmile.R;
import com.example.healthysmile.gui.extraAndroid.settings.Settings;
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
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(NavigationDrawerFragments.this);
        sharedPreferencesHelper.imprimirDatosSharedPreferences();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_ayudaYSoporte,
                R.id.nav_ConsultaVirtual,
                R.id.nav_EducacionDental,
                R.id.nav_TiendaVirutal,
                R.id.nav_gestionAdmin)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer_fragments);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // No necesitas hacer nada aquí si solo quieres cargar la foto cuando se abre
            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Cargar la foto de perfil cuando el drawer se despliegue
                ImageView fotoPerfil = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_foto_perfil);
                ReutilizableMethods reutilizableMethods = new ReutilizableMethods();
                reutilizableMethods.cargarFotoPerfil(getApplicationContext(), fotoPerfil);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // No necesitas hacer nada aquí si no es necesario hacer algo cuando se cierre el drawer
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // No necesitas hacer nada aquí si no es necesario hacer algo cuando cambie el estado del drawer
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavController navController = Navigation.findNavController(NavigationDrawerFragments.this, R.id.nav_host_fragment_content_navigation_drawer_fragments);

                EditText inputSearch = findViewById(R.id.top_bar_input_search);
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

                inputSearch.setVisibility(View.GONE);

                // Manejar la opción nav_ConsultaVirtual
                if (item.getItemId() == R.id.nav_ConsultaVirtual) {
                    return navegarConPopBackStack(navController,drawer, R.id.nav_ConsultaVirtual);
                }

                // Manejar la opción nav_home
                if (item.getItemId() == R.id.nav_home) {
                    return navegarConPopBackStack(navController,drawer, R.id.nav_home);
                }

                // Manejar la opción nav_ayudaYSoporte
                if (item.getItemId() == R.id.nav_ayudaYSoporte) {
                    return navegarConPopBackStack(navController,drawer, R.id.nav_ayudaYSoporte);
                }

                if (item.getItemId() == R.id.nav_EducacionDental) {
                    return navegarConPopBackStack(navController,drawer, R.id.nav_EducacionDental);
                }

                if (item.getItemId() == R.id.nav_TiendaVirutal) {
                    toolbar.setTitle("");
                    inputSearch.setVisibility(View.VISIBLE);
                    return navegarConPopBackStack(navController,drawer, R.id.nav_TiendaVirutal);
                }

                if (item.getItemId() == R.id.nav_gestionAdmin) {
                    return navegarConPopBackStack(navController,drawer, R.id.nav_gestionAdmin);
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

        ReutilizableMethods reutilizableMethods = new ReutilizableMethods();
        reutilizableMethods.cargarFotoPerfil(getApplicationContext(),fotoPerfil);


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

    private boolean navegarConPopBackStack(NavController navController, DrawerLayout drawer, int destinoId) {
        navController.popBackStack(destinoId, false);
        navController.navigate(destinoId);
        drawer.closeDrawers();
        return true;
    }
}
