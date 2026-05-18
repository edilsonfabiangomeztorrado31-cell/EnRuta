package com.empresa.enruta.view.company;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.RegisterEmpresaContract;
import com.empresa.enruta.presenter.company.RegisterEmpresaPresenter;

public class RegisterEmpresaActivity extends AppCompatActivity implements RegisterEmpresaContract.View {

    private EditText etNombre, etNit , etPersonContacto, etTelefono, etCorreo, etPassword, etConfirmPassword;
    private TextView tvRecuperar;
    private Button btnRegistrar;
    private RegisterEmpresaContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empresa);

        presenter = new RegisterEmpresaPresenter(this);

        etNombre = findViewById(R.id.etNombreEmpresa);
        etNit = findViewById(R.id.etNit);
        etPersonContacto = findViewById(R.id.etPersonContacto);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        //tvRecuperar = findViewById(R.id.tvRecuperar);

        btnRegistrar = findViewById(R.id.btnRegistrarseEmpresa);
        btnRegistrar.setOnClickListener(v -> {
            presenter.registrarEmpresa(
                    etNombre.getText().toString(),
                    etNit.getText().toString(),
                    etPersonContacto.getText().toString(),
                    etTelefono.getText().toString(),
                    etCorreo.getText().toString(),
                    etPassword.getText().toString(),
                    etConfirmPassword.getText().toString()
            );
        });

        //tvRecuperar.setOnClickListener(v -> presenter.onIniciarSesionClicked());

    }
    @Override
    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registroExitoso(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void irAIniciarSesion() {
        Intent intent = new Intent(this, LoginEmpresaActivity.class);
        startActivity(intent);
    }
}

