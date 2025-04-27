package com.empresa.enruta.contract;

public interface LoginEmpresaContract {
    interface View {
        void mostrarError(String mensaje);
        void navegaAInicio(String mensaje);
    }

    interface Presenter {
        void login(String correo, String contraseña);
    }
}
