package com.empresa.enruta.presenter.company;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.MenuContract;
import com.empresa.enruta.contract.fragments.company.AyudaFragmentCompany;
import com.empresa.enruta.contract.fragments.company.ConfiguracionFragmentCompany;
import com.empresa.enruta.contract.fragments.company.HistorialFragmentCompany;
import com.empresa.enruta.contract.fragments.company.NotificacionFragmentCompany;
import com.empresa.enruta.contract.fragments.company.SeguridadFragmentCompany;
import com.empresa.enruta.contract.fragments.company.SoporteFragmentCompany;
import com.empresa.enruta.contract.fragments.company.UbicacionFragmentCompany;

public class MenuPresenterImplCompany implements MenuContract.MenuPresenter {

    private MenuContract.MenuView view;

    public MenuPresenterImplCompany(MenuContract.MenuView view) {
        this.view = view;
    }

    @Override
    public void onMenuItemSelected(int itemId) {

        Fragment selectedFragment = null;

        if (itemId == R.id.nav_ubicacion_company) {
            selectedFragment = new UbicacionFragmentCompany();
        } else if (itemId == R.id.nav_historial_company) {
            selectedFragment = new HistorialFragmentCompany();
        } else if (itemId == R.id.nav_notificaciones_company) {
            selectedFragment = new NotificacionFragmentCompany();
        } else if (itemId == R.id.nav_seguridad_company) {
            selectedFragment = new SeguridadFragmentCompany();
        } else if (itemId == R.id.nav_configuracion_company) {
            selectedFragment = new ConfiguracionFragmentCompany();
        } else if (itemId == R.id.nav_ayuda_company) {
            selectedFragment = new AyudaFragmentCompany();
        }
//        else if (itemId == R.id.nav_soporte_company) {
//            selectedFragment = new SoporteFragmentCompany();
//        }

        Log.d("Menu", "Seleccionaste: " + itemId);

        if (selectedFragment != null) {
            view.cargarFragment(selectedFragment);
        } else {
            view.mostrarError("Opción no válida.");
        }
    }
}
