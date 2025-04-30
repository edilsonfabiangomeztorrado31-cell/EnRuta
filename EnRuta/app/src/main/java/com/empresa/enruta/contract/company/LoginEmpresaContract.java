package com.empresa.enruta.contract.company;

public interface LoginEmpresaContract {
    interface View {
        void mostrarError(String mensaje);
        void navegaAInicio(String mensaje);
    }

    interface Presenter {
        void login(String correo, String contraseña);
    }
}
