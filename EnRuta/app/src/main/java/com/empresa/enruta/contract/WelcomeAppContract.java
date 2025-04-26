package com.empresa.enruta.contract;

public interface WelcomeAppContract {

    interface View {
        void navegarEmpresa();
        void navegarTransportador();
    }

    interface Presenter {
        void onEmpresaSeleccionada();
        void onTransportadorSeleccionado();
    }
}
