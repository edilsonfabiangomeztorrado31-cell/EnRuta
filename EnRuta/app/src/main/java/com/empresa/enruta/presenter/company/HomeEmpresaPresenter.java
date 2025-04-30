package com.empresa.enruta.presenter.company;

import com.empresa.enruta.contract.company.HomeEmpresaContract;

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
