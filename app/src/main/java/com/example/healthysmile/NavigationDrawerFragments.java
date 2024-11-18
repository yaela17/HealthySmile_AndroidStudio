package com.example.healthysmile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;  // Importa Toast

import com.google.android.material.snackbar.Snackbar;
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
                R.id.nav_home, R.id.nav_ayudaYSoporte)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer_fragments);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Accediendo al Intent y obteniendo los datos
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nomUser");
        String correo = intent.getStringExtra("correoUser");
        String tipoUsuario = intent.getStringExtra("tipoUsuario");  // Asegúrate de usar la clave correcta aquí


        // Mostrando los datos recibidos a través de Toast
        Toast.makeText(this, "Datos recibidos: Nombre - " + nombre + ", Correo - " + correo + ", TipoUsuario - " + tipoUsuario, Toast.LENGTH_LONG).show();

        // Referenciando los TextViews del header
        TextView nombreUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_nombreUser);
        TextView tipoUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_tipo_user);
        TextView correoUser = navigationView.getHeaderView(0).findViewById(R.id.nav_header_navigation_drawer_lateral_correoUser);

        // Verificando si los datos no son null y actualizando las vistas
        if (nombre != null) {
            nombreUser.setText(nombre);
            Toast.makeText(this, "Nombre actualizado: " + nombre, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nombre es null", Toast.LENGTH_SHORT).show();
        }

        if (correo != null) {
            correoUser.setText(correo);
            Toast.makeText(this, "Correo actualizado: " + correo, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Correo es null", Toast.LENGTH_SHORT).show();
        }

        if (tipoUsuario != null) {
            tipoUser.setText(tipoUsuario);
            Toast.makeText(this, "Tipo de usuario actualizado: " + tipoUsuario, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tipo de usuario es null", Toast.LENGTH_SHORT).show();
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
        if(item.getItemId() == R.id.action_LogOut){

        }else
            if(item.getItemId() == R.id.action_settings){

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
