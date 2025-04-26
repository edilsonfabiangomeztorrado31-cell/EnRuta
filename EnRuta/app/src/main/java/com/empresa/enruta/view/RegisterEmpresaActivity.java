package com.empresa.enruta.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.RegisterEmpresaContract;
import com.empresa.enruta.presenter.RegisterEmpresaPresenter;

public class RegisterEmpresaActivity extends AppCompatActivity implements RegisterEmpresaContract.View {

    private EditText etNombre, etNit, etCorreo, etRepresentante, etTipoEmpresa, etDireccion, etContacto, etContraseña;
    private Button btnRegistrar;
    private RegisterEmpresaContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empresa);

        presenter = new RegisterEmpresaPresenter(this);

        etNombre = findViewById(R.id.etNombreEmpresa);
        etNit = findViewById(R.id.etNI);
        etCorreo = findViewById(R.id.etCorreo);
        etRepresentante = findViewById(R.id.etRepresentanteLlegal);
        etTipoEmpresa = findViewById(R.id.etTipoEmpresa);
        etDireccion = findViewById(R.id.etDirrecion);
        etContacto = findViewById(R.id.etContacto);
        etContraseña = findViewById(R.id.etPassword);

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
    }
    @Override
    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registroExitoso(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}

