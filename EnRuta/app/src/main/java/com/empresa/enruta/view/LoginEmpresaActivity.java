package com.empresa.enruta.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.LoginEmpresaContract;
import com.empresa.enruta.presenter.LoginEmpresaPresenter;

public class LoginEmpresaActivity extends AppCompatActivity implements LoginEmpresaContract.View {

    private EditText etNI, etContraseña;
    private Button btnIniciarSesion;
    private LoginEmpresaContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_empresa);

        etNI = findViewById(R.id.etNI);
        //etContraseña = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        presenter = new LoginEmpresaPresenter(this);

        btnIniciarSesion.setOnClickListener(view ->
                presenter.login(etNI.getText().toString(), etContraseña.getText().toString())
        );

        Button btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);

// Color por defecto (puedes usar getColor si estás en API 23+)
        int azul = ContextCompat.getColor(this, R.color.blue_logo);
        int naranja = Color.parseColor("#F28C28");

        btnIniciarSesion.setOnClickListener(v -> {
            btnIniciarSesion.setBackgroundColor(azul);
            btnRegistrarse.setBackgroundColor(naranja);
        });

        btnRegistrarse.setOnClickListener(v -> {
            btnRegistrarse.setBackgroundColor(azul);
            btnIniciarSesion.setBackgroundColor(naranja);
        });
    }
    @Override
    public void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navegaAInicio() {
        // Ir al home del empresario
        //Intent intent = new Intent(this, HomeEmpresaActivity.class);
        //startActivity(intent);
        finish();
    }
}
