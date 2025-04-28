package com.empresa.enruta.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.MenuContract;
import com.empresa.enruta.contract.fragments.UbicacionFragment;
import com.empresa.enruta.contract.fragments.SoporteFragment;
import com.empresa.enruta.contract.fragments.AyudaFragment;
import com.empresa.enruta.contract.fragments.HistorialFragment;
import com.empresa.enruta.contract.fragments.NotificacionFragment;
import com.empresa.enruta.contract.fragments.SeguridadFragment;
import com.empresa.enruta.contract.fragments.ConfiguracionFragment;
import com.google.android.material.navigation.NavigationView;

public class MenuFragmentActivity extends AppCompatActivity implements MenuContract {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imgMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_empresa);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Agregar icono hamburguesa para abrir el Drawer
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        // Mostrar fragmento inicial
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new UbicacionFragment()) // corregido el nombre
//                    .commit();
//        }

        // Listener para abrir el Drawer al hacer click en el icono del menú
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START); // Abre el menú lateral
            }
        });

    // Listener del menú lateral sin lambda
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(android.view.MenuItem item) {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_ubicacion) {
                selectedFragment = new UbicacionFragment(); // corregido el nombre
            } else if (id == R.id.nav_historial) {
                selectedFragment = new HistorialFragment();
            } else if (id == R.id.nav_notificaciones) {
                selectedFragment = new NotificacionFragment();
            } else if (id == R.id.nav_seguridad) {
                selectedFragment = new SeguridadFragment();
            } else if (id == R.id.nav_configuracion) {
                selectedFragment = new ConfiguracionFragment();
            } else if (id == R.id.nav_ayuda) {
                selectedFragment = new AyudaFragment();
            } else if (id == R.id.nav_soporte) {
                selectedFragment = new SoporteFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                drawerLayout.closeDrawers(); // Cerrar menú
            }

            return true;
        }
    });
    }
}
