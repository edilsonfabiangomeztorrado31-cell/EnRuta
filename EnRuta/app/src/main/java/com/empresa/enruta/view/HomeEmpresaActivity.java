package com.empresa.enruta.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.HomeEmpresaContract;
import com.empresa.enruta.contract.MenuContract;
import com.empresa.enruta.presenter.HomeEmpresaPresenter;
import com.empresa.enruta.presenter.MenuPresenterImpl;
import com.google.android.material.navigation.NavigationView;

public class HomeEmpresaActivity extends AppCompatActivity implements HomeEmpresaContract.HomeEmpresaView, MenuContract.MenuView{

    private Button btnGestionFlete, btnTransAsignado, btnVerHistorial;
    private HomeEmpresaContract.HomeEmpresaPresenter presenterHome;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imgMenu;
    private MenuContract.MenuPresenter presenterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_empresa);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenterMenu = new MenuPresenterImpl(this);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(android.view.MenuItem item) {
                presenterMenu.onMenuItemSelected(item.getItemId());
                cerrarMenu();
                return true;
            }
        });

        btnGestionFlete = findViewById(R.id.btnGestionFletes);
        btnTransAsignado = findViewById(R.id.btnTransAsignado);
        btnVerHistorial = findViewById(R.id.btnVerHistorial);

        presenterHome = new HomeEmpresaPresenter(this);

        btnGestionFlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterHome.onGestionFleteClicked();
            }
        });

        btnTransAsignado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterHome.onTransAsignadoClicked();
            }
        });

        btnVerHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterHome.onVerHistorialClicked();
            }
        });
    }

    @Override
    public void irAGestionFletes() {
        startActivity(new Intent(this, GestionFletesActivity.class));
    }

    @Override
    public void irATransportadoresAsignados() {
        startActivity(new Intent(this, TransportadoresAsignadosActivity.class));
    }

    @Override
    public void irAVerHistorial() {
        startActivity(new Intent(this, VerHistorialActivity.class));
    }

    @Override
    public void mostrarOpcionesMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void mostrarError(String mensaje) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cargarFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cerrarMenu() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
