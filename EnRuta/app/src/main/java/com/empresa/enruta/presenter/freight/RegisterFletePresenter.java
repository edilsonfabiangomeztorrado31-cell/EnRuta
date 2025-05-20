package com.empresa.enruta.presenter.freight;

import com.empresa.enruta.contract.freight.RegisterFleteContract;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFletePresenter implements RegisterFleteContract.Presenter {

    private RegisterFleteContract.View view;
    private FirebaseAuth auth;
    private DatabaseReference database;

    public RegisterFletePresenter(RegisterFleteContract.View view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("register_freight");
    }

    @Override
    public void onCiudadOrigenSeleccionada(Place place) {
        String ciudad = place.getName();
        String placeId = place.getId();

        if (ciudad != null) {
            view.mostrarCiudadOrigen(ciudad);
        }

        if (placeId != null) {
            view.procesarBoundsCiudad(placeId);
        }
    }

    @Override
    public void onCiudadDestinoSeleccionada(Place place) {
        String ciudad = place.getName();
        String placeId = place.getId();

        if (ciudad != null) {
            view.mostrarCiudadDestino(ciudad);
        }

        if (placeId != null) {
            view.procesarBoundsCiudad(placeId);
        }
    }

    @Override
    public void onErrorAutocomplete(Status status) {
        String mensaje = status != null ? status.getStatusMessage() : "Error desconocido en Autocomplete";
        view.mostrarError(mensaje);
    }

    @Override
    public void onDireccionOrigenSeleccionada(String direccion, LatLng latLng) {
        if (direccion != null && !direccion.isEmpty()) {
            view.mostrarDireccionOrigen(direccion);
        } else {
            view.mostrarErrorDireccion("No se pudo obtener la dirección.");
        }
    }

    @Override
    public void onDireccionDestinoSeleccionada(Place place) {
        if (place != null && place.getAddress() != null) {
            view.mostrarDireccionDestino(place.getAddress());
        } else {
            view.mostrarErrorDireccion("No se pudo obtener la dirección.");
        }
    }

    @Override
    public void onErrorDireccionOrigen(Status status) {
        String mensaje = (status != null && status.getStatusMessage() != null)
                ? status.getStatusMessage()
                : "Error desconocido al buscar la dirección";
        view.mostrarErrorDireccion(mensaje);
    }

    @Override
    public void onErrorDireccionDestino(Status status) {

    }

    @Override
    public void onClickSeleccionarCiudadOrigen() {
        view.lanzarAutocompleteCiudadOrigen();
    }

    @Override
    public void onClickSeleccionarDireccionOrigen() {
        view.mostrarBottomSheetBuscarDireccionOrigen();
    }

    @Override
    public void onClickSeleccionarCiudadDestino() {
        view.lanzarAutocompleteCiudadDestino();
    }

    @Override
    public void onClickSeleccionarDireccionDestino() {
        view.mostrarBottomSheetBuscarDireccionDestino();
    }

    @Override
    public void onClickBuscarMapaDestino() {
        view.mostrarBottomSheetMapaDireccionDestino();
    }

    @Override
    public void onClickBuscarDireccionDestino() {
        view.lanzarBuscadorDireccionDestino();
    }

    @Override
    public void onClickBuscarMapaOrigen() {
        view.mostrarBottomSheetMapaDireccionOrigen();
    }

    @Override
    public void onClickBuscarDireccionOrigen() {
        view.lanzarBuscadorDireccionOrigen();
    }

    @Override
    public void onRegistrarFlete(String ubicacionOrigen, String ubicacionDestino, String tipoCarga, String precio, String peso,
                                 String fechaRegistro) {

        if (ubicacionOrigen.isEmpty() || ubicacionDestino.isEmpty() || tipoCarga.isEmpty() || precio.isEmpty() || peso.isEmpty() || fechaRegistro.isEmpty()){
            view.mostrarMensaje("Todos los campos son obligatorios");
            return;
        }

        if (ubicacionOrigen.length() < 3 || ubicacionOrigen.length() > 100){
            view.mostrarMensaje("La ubicacion de origen debe tener entre 3 y 100 caracteres");
        }

        if (!ubicacionOrigen.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ°.,#\\-\\s]+$")){
            view.mostrarMensaje("Solo se permiten caracteres válidos en la dirección.");
        }

        if (!ubicacionOrigen.equals(ubicacionOrigen.trim())){
            view.mostrarMensaje("En la ubicacion de origen no se permite espacios al inicio y al final. ");
        }

        if (tipoCarga.length() < 5 || tipoCarga.length() > 150){
            view.mostrarMensaje("El tipo de carga debe tener entre 5 y 150 caracteres");
        }

        if (tipoCarga.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$")){
            view.mostrarMensaje("El nombre del representante solo debe contener letras y espacios entre palabras.");
        }

        if (!tipoCarga.equals(tipoCarga.trim())){
            view.mostrarMensaje("En el tipo de carga no se permite espacios al inicio y al final. ");
        }
        // Aquí puedes obtener el UID si ya hay un usuario autenticado
        FirebaseUser user = auth.getCurrentUser();
        String uid = user != null ? user.getUid() : database.push().getKey(); // Fallback en caso de no haber auth

        Map<String, Object> fleteData = new HashMap<>();
        fleteData.put("ubicacion_origen", ubicacionOrigen.trim());
        fleteData.put("ubicacion_destino", ubicacionDestino.trim());
        fleteData.put("tipo_carga", tipoCarga.trim());
        fleteData.put("precio", precio.trim());
        fleteData.put("peso", peso.trim());
        fleteData.put("fecha_registro", fechaRegistro.trim());

        // Guardar el flete con un ID único generado por push()
        database.push().setValue(fleteData)
                .addOnSuccessListener(aVoid -> view.registroExitoso("Tu registro de flete ha sido exitoso"))
                .addOnFailureListener(e -> view.mostrarMensaje("Error al guardar flete: " + e.getMessage()));
                view.limpiarCampos();
    }
}
