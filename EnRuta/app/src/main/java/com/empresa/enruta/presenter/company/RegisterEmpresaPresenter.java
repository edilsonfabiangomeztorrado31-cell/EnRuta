package com.empresa.enruta.presenter.company;

import com.empresa.enruta.contract.FirebaseAuthErrorHandler;
import com.empresa.enruta.contract.company.RegisterEmpresaContract;
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
    public void registrarEmpresa(String nombre, String nit, String personaContacto, String telefono,
                                 String correo, String password, String confirmarPassword) {

        if (nombre.isEmpty() || nit.isEmpty() || personaContacto.isEmpty() || telefono.isEmpty() ||
        correo.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
            view.mostrarMensaje("Todos los campos son obligatorios");
            return;
        }

        // Validar longitud de nombre
        if (nombre.length() < 3 || nombre.length() > 50) {
            view.mostrarMensaje("El nombre debe tener entre 3 y 50 caracteres.");
            return;
        }

        // Validar NIT
        if (!nit.matches("\\d{7,10}")) {
            view.mostrarMensaje("El NIT debe contener solo números y tener entre 7 y 10 dígitos");
            return;
        }
        // Validar longitud de correo
        if (correo.length() < 5 || correo.length() > 100) {
            view.mostrarMensaje("El correo debe tener entre 5 y 100 caracteres.");
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

        auth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();

                        Map<String, Object> empresaData = new HashMap<>();
                        empresaData.put("nombre", nombre.trim());
                        empresaData.put("nit", nit.trim());
                        empresaData.put("personaContacto", personaContacto.trim());
                        empresaData.put("telefono", telefono.trim());
                        empresaData.put("correo", correo.trim());
                        empresaData.put("password", password.trim());
                        empresaData.put("confirmarPassword", confirmarPassword.trim());

                        database.child(uid).setValue(empresaData)
                                .addOnSuccessListener(aVoid -> {
                                    //view.mostrarMensaje("Empresa registrada correctamente");
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
