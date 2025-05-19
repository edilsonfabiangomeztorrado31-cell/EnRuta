package com.empresa.enruta.contract.freight;

public interface RegisterFleteContract {

    interface View {
        void mostrarMensaje(String mensaje);
        void registroExitoso(String mensaje);
        void irARegistrarFlete();
    }

    interface Presenter {
        void onRegistrarFlete(String ubicacionOrigen, String ubicacionDestino, String tipoCarga, String precio, String peso,
                              String fechaRegistro);
    }
}
