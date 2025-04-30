package com.empresa.enruta.contract.company;

public interface RegisterEmpresaContract {
    interface View {
        void mostrarMensaje(String mensaje);
        void registroExitoso(String mensaje);
        void irAIniciarSesion();
    }

    interface Presenter {
        void registrarEmpresa(String nombre, String nit, String correo, String representante,
                              String tipoEmpresa, String dirreccion, String contacto, String contraseña);

        void onIniciarSesionClicked();
    }
}
