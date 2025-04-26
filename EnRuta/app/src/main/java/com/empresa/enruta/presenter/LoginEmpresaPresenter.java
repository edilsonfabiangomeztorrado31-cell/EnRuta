package com.empresa.enruta.presenter;

import com.empresa.enruta.contract.LoginEmpresaContract;

public class LoginEmpresaPresenter implements LoginEmpresaContract.Presenter {

    private final LoginEmpresaContract.View view;

    public LoginEmpresaPresenter (LoginEmpresaContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String ni, String contraseña){
        if (ni.isEmpty() || contraseña.isEmpty()) {
            view.mostrarError("Por favor completa todos los campos");
            return;
        }

        // Simulación de autenticación
        if (ni.equals("1092176000") && contraseña.equals("empresa123")) {
            view.navegaAInicio();
        } else {
            view.mostrarError("Credenciales incorrectas");
        }
    }
}
