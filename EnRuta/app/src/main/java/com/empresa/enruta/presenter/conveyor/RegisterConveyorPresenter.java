package com.empresa.enruta.presenter.conveyor;

import com.empresa.enruta.contract.FirebaseAuthErrorHandler;
import com.empresa.enruta.contract.conveyor.RegisterConveyorContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterConveyorPresenter implements RegisterConveyorContract.Presenter {

    private RegisterConveyorContract.View view;
    private FirebaseAuth auth;
    private DatabaseReference database;

    public RegisterConveyorPresenter(RegisterConveyorContract.View view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("register_conveyor");
    }

    public void cargarOpcionesIniciales() {
        List<String> tiposDocumento = Arrays.asList("C.C.", "T.I.", "NIT", "C.E.", "P.P.", "R.C.");
        List<String> tiposVehiculo = Arrays.asList("Turbo 4 Ton", "Turbo 6 Ton", "Camión 8 Ton", "Camión 10 Ton", "Camión sencillo", "Camión doble troque", "Tractomula", "Camión articulado");

        view.showTipoDocumentoOptions(tiposDocumento);
        view.showTipoVehiculoOptions(tiposVehiculo);
    }

    @Override
    public void registrarConveyor(String nombre, String apellido, String tipoDocumento, String numeroDocumento, String correo, String contacto,
                                  String placa, String capacidadToneladas, String tipoVehiculo, String contraseña) {

        if (nombre.isEmpty() || apellido.isEmpty() || tipoDocumento.isEmpty() || numeroDocumento.isEmpty() || correo.isEmpty() ||contacto.isEmpty() ||
        placa.isEmpty() || capacidadToneladas.isEmpty() || tipoVehiculo.isEmpty() || contraseña.isEmpty()) {
            view.mostrarMensaje("Todos los campos son obligatorios");
        }

        if (nombre.isEmpty() || nombre.length() < 2) {
            view.mostrarMensaje("Ingresa un nombre válido (mínimo 2 caracteres)");
            return;
        }

        if (apellido.isEmpty() || apellido.length() < 2) {
            view.mostrarMensaje("Ingresa un apellido válido (mínimo 2 caracteres)");
            return;
        }

        if (tipoDocumento.isEmpty()) {
            view.mostrarMensaje("Selecciona un tipo de documento");
            return;
        }

        if (tipoDocumento.equals("Tipo de documento")) {
            view.mostrarMensaje("Selecciona un tipo de documento válido");
            return;
        }

        if (tipoVehiculo.equals("Tipo de vehículo")) {
            view.mostrarMensaje("Selecciona un tipo de vehículo válido");
            return;
        }


        if (!numeroDocumento.matches("\\d{6,10}")) {
            view.mostrarMensaje("El número de documento debe tener entre 6 y 10 dígitos numéricos");
            return;
        }

        if (!contacto.matches("^\\d{7,10}$")) {
            view.mostrarMensaje("Número de contacto no válido. Debe contener entre 7 y 10 dígitos");
            return;
        }

        if (!placa.matches("^[A-Z]{3}\\d{3}$")) { // Ejemplo de placa colombiana estándar
            view.mostrarMensaje("Placa no válida. Formato esperado: ABC123");
            return;
        }

        if (!capacidadToneladas.matches("^\\d{1,2}(\\.\\d{1,2})?$")) {
            view.mostrarMensaje("Capacidad no válida. Usa solo números (ej. 5 o 7.5)");
            return;
        }

        auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();

                        Map<String, Object> empresaData = new HashMap<>();
                        empresaData.put("nombre", nombre.trim());
                        empresaData.put("apellido", apellido.trim());
                        empresaData.put("tipoDocumento", tipoDocumento.trim());
                        empresaData.put("numeroDocumento", numeroDocumento.trim());
                        empresaData.put("correo", correo.trim());
                        empresaData.put("contacto", contacto.trim());
                        empresaData.put("placa", placa.trim());
                        empresaData.put("capacidadToneladas", capacidadToneladas.trim());
                        empresaData.put("tipoVehiculo", tipoVehiculo.trim());
                        empresaData.put("password", contraseña.trim());

                        database.child(uid).setValue(empresaData)
                                .addOnSuccessListener(aVoid -> {
                                   // view.mostrarMensaje("Empresa registrada correctamente");
                                    view.registroExitoso("Tu registro ha sido exitoso");
                                })
                                .addOnFailureListener(e -> view.mostrarMensaje("Error al guardar: " + e.getMessage()));
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            FirebaseAuthErrorHandler.handle(exception, mensaje -> view.mostrarMensaje(mensaje));
                        } else {
                            view.mostrarMensaje("Error desconocido al registrar");
                        }
                    }
                });
    }

    @Override
    public void onIniciarSesionClicked() {
        view.irAIniciarSesion();
    }
}
