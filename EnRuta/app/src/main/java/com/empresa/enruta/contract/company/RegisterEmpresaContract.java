package com.empresa.enruta.contract.company;

public interface RegisterEmpresaContract {
    interface View {
        void mostrarMensaje(String mensaje);
        void registroExitoso(String mensaje);
        void irAIniciarSesion();

    }

    interface Presenter {
        void registrarEmpresa(String nombre, String nit, String personaContacto, String telefono,
                              String correo, String password, String confirmarPassword);

        void onIniciarSesionClicked();
    }
}
