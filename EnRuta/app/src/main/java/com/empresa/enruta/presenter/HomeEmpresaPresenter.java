package com.empresa.enruta.presenter;

import com.empresa.enruta.contract.HomeEmpresaContract;

public class HomeEmpresaPresenter implements HomeEmpresaContract.HomeEmpresaPresenter {

    private HomeEmpresaContract.HomeEmpresaView view;

    public HomeEmpresaPresenter(HomeEmpresaContract.HomeEmpresaView view) {
        this.view = view;
    }

    @Override
    public void onGestionFleteClicked() {
        view.irAGestionFletes();
    }

    @Override
    public void onTransAsignadoClicked() {
        view.irATransportadoresAsignados();
    }

    @Override
    public void onVerHistorialClicked() {
        view.irAVerHistorial();
    }
}
