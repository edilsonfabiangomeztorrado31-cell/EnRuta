package com.empresa.enruta.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.RegisterEmpresaContract;
import com.empresa.enruta.presenter.RegisterEmpresaPresenter;

public class RegisterEmpresaActivity extends AppCompatActivity implements RegisterEmpresaContract.View {

    private EditText etNombre, etNit, etCorreo, etRepresentante, etTipoEmpresa, etDireccion, etContacto, etContraseña;
    private TextView tvRecuperar;
    private Button btnRegistrar;
    private RegisterEmpresaContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empresa);

        presenter = new RegisterEmpresaPresenter(this);

        Button btnIniciarSesion = findViewById(R.id.btnIniciarSesionSwift);
        Button btnRegistrarse = findViewById(R.id.btnRegistrarseSwift);

        btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_naranja_left);
        btnRegistrarse.setBackgroundResource(R.drawable.bg_button_azul_right);

        btnIniciarSesion.setOnClickListener(v -> {
            btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_azul_left);
            btnRegistrarse.setBackgroundResource(R.drawable.bg_button_naranja_right);

            Intent intent = new Intent(RegisterEmpresaActivity.this, LoginEmpresaActivity.class);
            startActivity(intent);

        });

        btnRegistrarse.setOnClickListener(v -> {
            btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_naranja_left);
            btnRegistrarse.setBackgroundResource(R.drawable.bg_button_azul_right);

            Intent intent = new Intent(RegisterEmpresaActivity.this, RegisterEmpresaActivity.class);
        });

        etNombre = findViewById(R.id.etNombreEmpresa);
        etNit = findViewById(R.id.etCorreo);
        etCorreo = findViewById(R.id.etCorreo);
        etRepresentante = findViewById(R.id.etRepresentanteLlegal);
        etTipoEmpresa = findViewById(R.id.etTipoEmpresa);
        etDireccion = findViewById(R.id.etDirrecion);
        etContacto = findViewById(R.id.etContacto);
        etContraseña = findViewById(R.id.etPassword);
        tvRecuperar = findViewById(R.id.tvRecuperar);

        btnRegistrar = findViewById(R.id.btnRegistrarseEmpresa);
        btnRegistrar.setOnClickListener(v -> {
            presenter.registrarEmpresa(
                    etNombre.getText().toString(),
                    etNit.getText().toString(),
                    etCorreo.getText().toString(),
                    etRepresentante.getText().toString(),
                    etTipoEmpresa.getText().toString(),
                    etDireccion.getText().toString(),
                    etContacto.getText().toString(),
                    etContraseña.getText().toString()
            );
        });

        tvRecuperar.setOnClickListener(v -> presenter.onIniciarSesionClicked());

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

