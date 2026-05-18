package com.empresa.enruta.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.LoginEmpresaContract;
import com.empresa.enruta.presenter.LoginEmpresaPresenter;

public class LoginEmpresaActivity extends AppCompatActivity implements LoginEmpresaContract.View {

    private EditText etNI, etContraseña, etCorreo;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    private Button btnLoginEmpresa;
    private LoginEmpresaContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_empresa);

        presenter = new LoginEmpresaPresenter(this);

        etCorreo = findViewById(R.id.etCorreo);
        etContraseña = findViewById(R.id.etPassword);
        btnLoginEmpresa = findViewById(R.id.btnLoginEmpresa);


     
    }

    @Override
    public void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navegaAInicio(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeEmpresaActivity.class);
        startActivity(intent);
        finish();
    }
}