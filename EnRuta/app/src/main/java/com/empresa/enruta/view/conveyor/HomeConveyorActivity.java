package com.empresa.enruta.view.conveyor;

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
import com.empresa.enruta.contract.conveyor.HomeConveyorContract;
import com.empresa.enruta.presenter.conveyor.HomeConveyorPresenter;
import com.empresa.enruta.presenter.conveyor.MenuPresenterImplConveyor;
import com.google.android.material.navigation.NavigationView;

public class HomeConveyorActivity extends AppCompatActivity implements HomeConveyorContract.HomeConveyorView, MenuContract.MenuView{

    private HomeConveyorContract.HomeConveyorPresenter presenterHome;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imgMenu;
    private MenuContract.MenuPresenter presenterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_conveyor);

        drawerLayout = findViewById(R.id.drawer_layout_conveyor);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenterMenu = new MenuPresenterImplConveyor(this);

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

        presenterHome = new HomeConveyorPresenter(this);
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
