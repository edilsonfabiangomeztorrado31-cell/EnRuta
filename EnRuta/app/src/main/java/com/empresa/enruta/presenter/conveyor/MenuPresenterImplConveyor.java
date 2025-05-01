package com.empresa.enruta.presenter.conveyor;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.MenuContract;
import com.empresa.enruta.contract.fragments.conveyor.AyudaFragmentConveyor;
import com.empresa.enruta.contract.fragments.conveyor.ConfiguracionFragmentCoveyor;
import com.empresa.enruta.contract.fragments.conveyor.HistorialFragmentConveyor;
import com.empresa.enruta.contract.fragments.conveyor.NotificacionFragmentConveyor;
import com.empresa.enruta.contract.fragments.conveyor.SeguridadFragmentConveyor;
import com.empresa.enruta.contract.fragments.conveyor.SoporteFragmentConveyor;
import com.empresa.enruta.contract.fragments.conveyor.UbicacionFragmentConveyor;

public class MenuPresenterImplConveyor implements MenuContract.MenuPresenter {

    private MenuContract.MenuView view;

    public MenuPresenterImplConveyor(MenuContract.MenuView view) {
        this.view = view;
    }

    @Override
    public void onMenuItemSelected(int itemId) {

        Fragment selectedFragment = null;

        if (itemId == R.id.nav_ubicacion_conveyor) {
            selectedFragment = new UbicacionFragmentConveyor();
        } else if (itemId == R.id.nav_historial_conveyor) {
            selectedFragment = new HistorialFragmentConveyor();
        } else if (itemId == R.id.nav_notificaciones_conveyor) {
            selectedFragment = new NotificacionFragmentConveyor();
        } else if (itemId == R.id.nav_seguridad_conveyor) {
            selectedFragment = new SeguridadFragmentConveyor();
        } else if (itemId == R.id.nav_configuracion_conveyor) {
            selectedFragment = new ConfiguracionFragmentCoveyor();
        } else if (itemId == R.id.nav_ayuda_conveyor) {
            selectedFragment = new AyudaFragmentConveyor();
        } else if (itemId == R.id.nav_soporte_conveyor) {
            selectedFragment = new SoporteFragmentConveyor();
        }

        Log.d("Menu", "Seleccionaste: " + itemId);

        if (selectedFragment != null) {
            view.cargarFragment(selectedFragment);
        } else {
            view.mostrarError("Opción no válida.");
        }
    }
}
