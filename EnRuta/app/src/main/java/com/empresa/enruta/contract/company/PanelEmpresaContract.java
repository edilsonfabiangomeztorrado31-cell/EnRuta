package com.empresa.enruta.contract.company;

public interface PanelEmpresaContract {

    interface PanelEmpresaView {
        void irARegistrarFlete();
        void irAEditarFlete();
        void irAEliminarFlete();
    }

    interface PanelEmpresaPresenter {
        void onRegistrarFlete();
        void onEditarFlete();
        void onEliminarFlete();
    }
}
