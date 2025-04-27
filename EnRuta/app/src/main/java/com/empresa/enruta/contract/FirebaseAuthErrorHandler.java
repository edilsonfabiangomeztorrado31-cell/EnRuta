package com.empresa.enruta.contract;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class FirebaseAuthErrorHandler {

    public interface ErrorCallback {
        void onError(String message);
    }

    public static void handle(Exception e, ErrorCallback callback) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            callback.onError("Este correo ya está registrado. ¿Quieres iniciar sesión?");
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            callback.onError("La contraseña es muy débil (mínimo 6 caracteres).");
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            callback.onError("Correo o contraseña incorrectos");
        } else {
            callback.onError("Error al registrar: " + e.getMessage());
        }
    }
}
