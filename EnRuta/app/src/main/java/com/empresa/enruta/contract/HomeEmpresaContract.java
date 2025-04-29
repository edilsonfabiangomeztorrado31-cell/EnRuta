package com.empresa.enruta.contract;

public interface HomeEmpresaContract {

    interface HomeEmpresaView {
        void irAGestionFletes();
        void irATransportadoresAsignados();
        void irAVerHistorial();
    }

    interface HomeEmpresaPresenter {
        void onGestionFleteClicked();
        void onTransAsignadoClicked();
        void onVerHistorialClicked();
    }
}
