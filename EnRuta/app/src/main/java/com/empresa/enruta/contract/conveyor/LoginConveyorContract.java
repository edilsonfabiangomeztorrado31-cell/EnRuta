package com.empresa.enruta.contract.conveyor;

public interface LoginConveyorContract {
    interface View {
        void mostrarError(String mensaje);
        void navegaAInicio(String mensaje);
    }

    interface Presenter {
        void login(String correo, String contraseña);
    }
}
