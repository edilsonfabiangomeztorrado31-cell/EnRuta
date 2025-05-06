package com.empresa.enruta.view.company;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.MenuContract;
import com.empresa.enruta.presenter.company.MenuPresenterImplCompany;
import com.google.android.material.navigation.NavigationView;

public class CompanyMenuView extends AppCompatActivity implements MenuContract.MenuView{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imgMenu;
    private MenuContract.MenuPresenter presenterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_background_view_menu);

        drawerLayout = findViewById(R.id.drawer_layout_company);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenterMenu = new MenuPresenterImplCompany(this);

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
                .replace(R.id.fragment_container_company, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cerrarMenu() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
