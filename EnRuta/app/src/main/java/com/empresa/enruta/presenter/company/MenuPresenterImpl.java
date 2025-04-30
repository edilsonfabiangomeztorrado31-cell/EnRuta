package com.empresa.enruta.presenter.company;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.MenuContract;
import com.empresa.enruta.contract.fragments.*;

public class MenuPresenterImpl implements MenuContract.MenuPresenter {

    private MenuContract.MenuView view;

    public MenuPresenterImpl(MenuContract.MenuView view) {
        this.view = view;
    }

    @Override
    public void onMenuItemSelected(int itemId) {

        Fragment selectedFragment = null;

        if (itemId == R.id.nav_ubicacion) {
            selectedFragment = new UbicacionFragment();
        } else if (itemId == R.id.nav_historial) {
            selectedFragment = new HistorialFragment();
        } else if (itemId == R.id.nav_notificaciones) {
            selectedFragment = new NotificacionFragment();
        } else if (itemId == R.id.nav_seguridad) {
            selectedFragment = new SeguridadFragment();
        } else if (itemId == R.id.nav_configuracion) {
            selectedFragment = new ConfiguracionFragment();
        } else if (itemId == R.id.nav_ayuda) {
            selectedFragment = new AyudaFragment();
        } else if (itemId == R.id.nav_soporte) {
            selectedFragment = new SoporteFragment();
        }

        Log.d("Menu", "Seleccionaste: " + itemId);

        if (selectedFragment != null) {
            view.cargarFragment(selectedFragment);
        } else {
            view.mostrarError("Opción no válida.");
        }
    }
}
