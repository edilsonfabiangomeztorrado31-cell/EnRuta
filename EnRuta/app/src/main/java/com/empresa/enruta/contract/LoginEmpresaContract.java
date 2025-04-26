package com.empresa.enruta.contract;

public interface LoginEmpresaContract {
    interface View {
        void mostrarError(String mensaje);
        void navegaAInicio();
    }

    interface Presenter {
        void login(String ni, String contraseña);
    }
}
