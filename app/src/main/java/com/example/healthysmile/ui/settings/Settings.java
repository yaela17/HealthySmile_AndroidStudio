package com.example.healthysmile.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragAndDropPermissions;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;

import com.example.healthysmile.NavigationDrawerFragments;
import com.example.healthysmile.R;
import com.example.healthysmile.default_fragment_settings;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Configurar el diseño de la actividad
        setContentView(R.layout.activity_settings);

        // Configura la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Agregar un ícono a la izquierda (como un ícono de retroceso o un ícono de menú)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_arrow_back); // Cambia 'ic_menu' por tu ícono

        // Usar Edge to Edge para manejar los bordes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        item = menu.findItem(R.id.action_LogOut);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.drawable.icon_arrow_back){
            Intent intento = new Intent(this, NavigationDrawerFragments.class);
            startActivity(intento);
        }
        return super.onOptionsItemSelected(item);
    }


}