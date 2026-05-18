package com.empresa.enruta.contract.conveyor;

import java.util.List;

public interface RegisterConveyorContract {
    interface View {
        void mostrarMensaje(String mensaje);
        void registroExitoso(String mensaje);
        void showTipoDocumentoOptions(List<String> opciones);
        void showTipoVehiculoOptions(List<String> opciones);

        void irAIniciarSesion();
    }

    interface Presenter {
        void registrarConveyor(String name, String apellido, String tipoDocumento, String numeroDocumento, String correo, String contacto,
                               String placa, String capacidadToneladas, String tipoVehiculo, String password, String confirmarPassword);

        void onIniciarSesionClicked();
        void cargarOpcionesIniciales();
    }
}
