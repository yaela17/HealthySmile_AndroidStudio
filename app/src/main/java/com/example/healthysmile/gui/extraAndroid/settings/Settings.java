package com.example.healthysmile.gui.extraAndroid.settings;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.healthysmile.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Configura la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Agregar un ícono a la izquierda (como un ícono de retroceso o un ícono de menú)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_arrow_back); // Cambia 'ic_menu' por tu ícono

        // Si no hay un fragmento previamente cargado (es decir, si la actividad se está iniciando),
        // se carga el fragmento por defecto
        loadDefaultFragment();


    }

    // Método para cargar el fragmento por defecto
    private void loadDefaultFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingsFrameListViewContainer, new default_fragment_settings())
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
