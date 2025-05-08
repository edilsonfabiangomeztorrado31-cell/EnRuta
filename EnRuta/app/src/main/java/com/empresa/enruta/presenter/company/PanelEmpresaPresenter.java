package com.empresa.enruta.presenter.company;

import com.empresa.enruta.contract.company.PanelEmpresaContract;

public class PanelEmpresaPresenter implements PanelEmpresaContract.PanelEmpresaPresenter {

    private PanelEmpresaContract.PanelEmpresaView view;

    public PanelEmpresaPresenter(PanelEmpresaContract.PanelEmpresaView view) {
        this.view = view;
    }

    @Override
    public void onRegistrarFlete() {
        view.irARegistrarFlete();
    }

    @Override
    public void onEditarFlete() {
        view.irAEditarFlete();
    }

    @Override
    public void onEliminarFlete() {
        view.irAEliminarFlete();
    }
}
