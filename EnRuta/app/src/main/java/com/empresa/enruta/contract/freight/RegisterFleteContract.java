package com.empresa.enruta.contract.freight;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

public interface RegisterFleteContract {

    interface View {
        void mostrarMensaje(String mensaje);
        void registroExitoso(String mensaje);
        void irARegistrarFlete();
        void mostrarCiudadOrigen(String ciudad);
        void mostrarCiudadDestino (String ciudad);
        void mostrarError(String mensaje);
        void procesarBoundsCiudad(String placeId);
        void mostrarDireccionOrigen(String direccion);
        void mostrarDireccionDestino(String direccion);
        void mostrarErrorDireccion(String mensaje);
        void lanzarAutocompleteCiudadOrigen();
        void lanzarAutocompleteCiudadDestino();
        void mostrarBottomSheetUbicacionOrigen();
        void mostrarBottomSheetBuscarDireccionDestino();
        void lanzarBuscadorDireccionDestino();
        void mostrarBottomSheetMapaDireccionDestino();
        void mostrarBottomSheetBuscarDireccionOrigen();
        void mostrarBottomSheetMapaDireccionOrigen();
        void lanzarBuscadorDireccionOrigen();
        void limpiarCampos();

    }

    interface Presenter {
        void onCiudadOrigenSeleccionada(Place place);
        void onCiudadDestinoSeleccionada(Place place);
        void onErrorAutocomplete(Status status);
        void onDireccionOrigenSeleccionada(String direccion, LatLng latLng);
        void onDireccionDestinoSeleccionada(Place place);
        void onErrorDireccionOrigen(Status status);
        void onErrorDireccionDestino(Status status);
        void onClickSeleccionarCiudadOrigen();
        void onClickSeleccionarDireccionOrigen();
        void onClickSeleccionarCiudadDestino();
        void onClickSeleccionarDireccionDestino();
        void onClickBuscarMapaDestino();
        void onClickBuscarDireccionDestino();
        void onClickBuscarMapaOrigen();
        void onClickBuscarDireccionOrigen();
        void onRegistrarFlete(String ubicacionOrigen, String ubicacionDestino, String tipoCarga, String precio, String peso,
                              String fechaRegistro);
    }
}
