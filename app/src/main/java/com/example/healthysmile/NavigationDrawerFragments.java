package com.example.healthysmile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;  // Importa Toast

import com.bumptech.glide.Glide;
import com.example.healthysmile.ui.settings.Settings;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    Usuario pacienteLocal;
    Especialista especialistaLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationDrawerFragmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawerFragments.toolbar);
        binding.appBarNavigationDrawerFragments.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_ayudaYSoporte,R.id.nav_ConsultaVirtual,R.id.fragment_consulta_virtual_especialista)
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

                // Agrega más condiciones aquí para otras opciones del menú

                return false;
            }
        });




        // Accediendo al Intent y obteniendo los datos
        Intent intent = getIntent();
        String idUsuario = intent.getStringExtra("idUsuario");
        String nombre = intent.getStringExtra("nomUser");
        String correo = intent.getStringExtra("correoUser");
        String tipoUsuario = intent.getStringExtra("tipoUsuario");  // Asegúrate de usar la clave correcta aquí
        if(tipoUsuario.equals("Paciente")){
            pacienteLocal = new Usuario(idUsuario,nombre,correo,null,tipoUsuario,null);
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("idPaciente",pacienteLocal.getIdUsuario());
            editor.putString("nombrePaciente", pacienteLocal.getNombreUsuario());
            editor.putString("correoPaciente", pacienteLocal.getCorreoUsuario());
            editor.putString("tipoUsuario",pacienteLocal.getTipoUsuario());
            editor.putString("fotoPaciente", pacienteLocal.getFotoPerfil());
            editor.apply();
        }else
            if(tipoUsuario.equals("Especialista")){

            }

        Toast.makeText(this, "Datos recibidos: Nombre - " + nombre + ", Correo - " + correo + ", TipoUsuario - " + tipoUsuario, Toast.LENGTH_LONG).show();

        // Referenciando los TextViews del header
        TextView nombreUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_nombreUser);
        TextView tipoUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_tipo_user);
        TextView correoUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_correoUser);
        ImageView fotoPerfil = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_foto_perfil);

        // Verificando si los datos no son null y actualizando las vistas
        if (nombre != null) {
            if(pacienteLocal != null){
                nombreUser.setText(pacienteLocal.getNombreUsuario());
            }
            Toast.makeText(this, "Nombre actualizado: " + nombre, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nombre es null", Toast.LENGTH_SHORT).show();
        }

        if (correo != null) {
            if(pacienteLocal != null){
                correoUser.setText(pacienteLocal.getCorreoUsuario());
            }
            Toast.makeText(this, "Correo actualizado: " + correo, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Correo es null", Toast.LENGTH_SHORT).show();
        }

        if (tipoUsuario != null) {
            if(pacienteLocal != null){
                tipoUser.setText(pacienteLocal.getTipoUsuario());
            }
            Toast.makeText(this, "Tipo de usuario actualizado: " + tipoUsuario, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tipo de usuario es null", Toast.LENGTH_SHORT).show();
        }

        if (pacienteLocal != null) {
            String rutaImagen = pacienteLocal.getFotoPerfil();  // Obtener la ruta de la imagen desde el objeto Usuario
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
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
