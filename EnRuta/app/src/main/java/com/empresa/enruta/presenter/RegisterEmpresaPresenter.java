package com.empresa.enruta.presenter;

import com.empresa.enruta.contract.FirebaseAuthErrorHandler;
import com.empresa.enruta.contract.RegisterEmpresaContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegisterEmpresaPresenter implements RegisterEmpresaContract.Presenter {

    private RegisterEmpresaContract.View view;
    private FirebaseAuth auth;
    private DatabaseReference database;

    public RegisterEmpresaPresenter(RegisterEmpresaContract.View view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("register_company");
    }

    @Override
    public void registrarEmpresa(String nombre, String nit, String correo, String representante,
                                 String tipoEmpresa, String direccion, String contacto, String contraseña) {

        if (nombre.isEmpty() || nit.isEmpty() || correo.isEmpty() || representante.isEmpty() ||
        tipoEmpresa.isEmpty() || direccion.isEmpty() || contacto.isEmpty() || contraseña.isEmpty()) {
            view.mostrarMensaje("Todos los campos son obligatorios");
            return;
        }

        // Validar longitud de nombre
        if (nombre.length() < 3 || nombre.length() > 50) {
            view.mostrarMensaje("El nombre debe tener entre 3 y 50 caracteres.");
            return;
        }

        // Validar longitud de correo
        if (correo.length() < 5 || correo.length() > 100) {
            view.mostrarMensaje("El correo debe tener entre 5 y 100 caracteres.");
            return;
        }

        // Validar longitud de representante
        if (representante.length() < 3 || representante.length() > 50) {
            view.mostrarMensaje("El representante debe tener entre 3 y 50 caracteres.");
            return;
        }

        // Validar longitud de tipo de empresa
        if (tipoEmpresa.length() < 3 || tipoEmpresa.length() > 100) {
            view.mostrarMensaje("El tipo de empresa debe tener entre 3 y 100 caracteres.");
            return;
        }

        // Validar longitud de dirección
        if (direccion.length() < 5 || direccion.length() > 100) {
            view.mostrarMensaje("La dirección debe tener entre 5 y 100 caracteres.");
            return;
        }

        // Validar nombre
        if (!nombre.equals(nombre.trim())) {
            view.mostrarMensaje("El nombre no debe tener espacios al inicio o al final.");
            return;
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$")) {
            view.mostrarMensaje("El nombre solo debe contener letras y espacios entre palabras.");
            return;
        }

        // Validar NIT
        if (!nit.matches("\\d{7,10}")) {
            view.mostrarMensaje("El NIT debe contener solo números y tener entre 7 y 10 dígitos");
            return;
        }

        // Validar representante
        if (!representante.equals(representante.trim())) {
            view.mostrarMensaje("El representante no debe tener espacios al inicio o al final.");
            return;
        }
        if (!representante.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$")) {
            view.mostrarMensaje("El nombre del representante solo debe contener letras y espacios entre palabras.");
            return;
        }

        // Validar tipoEmpresa
        if (!tipoEmpresa.equals(tipoEmpresa.trim())) {
            view.mostrarMensaje("El tipo de empresa no debe tener espacios al inicio o al final.");
            return;
        }
        if (tipoEmpresa.length() < 2) {
            view.mostrarMensaje("Debes especificar un tipo de empresa válido");
            return;
        }

        // Validar dirección
        if (!direccion.equals(direccion.trim())) {
            view.mostrarMensaje("La dirección no debe tener espacios al inicio o al final.");
            return;
        }
        if (!direccion.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ#\\.\\-\\/,\\s]+") || direccion.length() < 5) {
            view.mostrarMensaje("La dirección contiene caracteres no permitidos o es demasiado corta");
            return;
        }

        // Validar contacto
        if (!contacto.matches("\\d{7,10}")) {
            view.mostrarMensaje("El número de contacto debe ser numérico y tener entre 7 y 10 dígitos");
            return;
        }

        auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();

                        Map<String, Object> empresaData = new HashMap<>();
                        empresaData.put("nombre", nombre.trim());
                        empresaData.put("nit", nit.trim());
                        empresaData.put("correo", correo.trim());
                        empresaData.put("representante", representante.trim());
                        empresaData.put("tipoEmpresa", tipoEmpresa.trim());
                        empresaData.put("direccion", direccion.trim());
                        empresaData.put("contacto", contacto.trim());

                        database.child(uid).setValue(empresaData)
                                .addOnSuccessListener(aVoid -> {
                                    view.mostrarMensaje("Empresa registrada correctamente");
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

    public void onIniciarSesionClicked() {
        // Lógica de navegación, pedirle a la View que abra la pantalla de login
        view.irAIniciarSesion();
    }

}
