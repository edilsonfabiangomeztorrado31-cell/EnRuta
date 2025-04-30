package com.empresa.enruta.presenter.company;

import com.empresa.enruta.contract.FirebaseAuthErrorHandler;
import com.empresa.enruta.contract.company.LoginEmpresaContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginEmpresaPresenter implements LoginEmpresaContract.Presenter {

    private final LoginEmpresaContract.View view;
    private final FirebaseAuth auth;

    public LoginEmpresaPresenter (LoginEmpresaContract.View view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String correo, String contraseña){
        if (correo.isEmpty() || contraseña.isEmpty()) {
            view.mostrarError("Por favor completa todos los campos");
            return;
        }

        auth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            view.navegaAInicio("Inicio de sección exitoso");
                        } else {
                            view.mostrarError("Error al iniciar sesión. Usuario no encontrado.");
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            FirebaseAuthErrorHandler.handle(exception, mensaje -> view.mostrarError(mensaje));
                        } else {
                            view.mostrarError("Error desconocido al iniciar sesión");
                        }
                    }
                });
    }
}
