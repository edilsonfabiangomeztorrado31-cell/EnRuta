package com.empresa.enruta.presenter.company;

import com.empresa.enruta.contract.company.WelcomeAppContract;

public class WelcomeAppPresenter implements WelcomeAppContract.Presenter {

    private final WelcomeAppContract.View view;

    public WelcomeAppPresenter(WelcomeAppContract.View view){
        this.view = view;
    }

    @Override
    public void onEmpresaSeleccionada() {
        view.navegarEmpresa();
    }

    @Override
    public void onTransportadorSeleccionado() {
        view.navegarTransportador();
    }
}
