package com.empresa.enruta.contract.company;

public interface MenuContract {

    interface MenuView {
        void mostrarOpcionesMenu();
        void mostrarError(String mensaje);
        void cargarFragment(androidx.fragment.app.Fragment fragment);
        void cerrarMenu();
    }

    interface MenuPresenter {
        void onMenuItemSelected(int itemId);
    }
}
