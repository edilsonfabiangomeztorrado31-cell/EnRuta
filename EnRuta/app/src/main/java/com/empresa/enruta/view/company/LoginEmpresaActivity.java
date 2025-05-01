package com.empresa.enruta.view.company;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.LoginEmpresaContract;
import com.empresa.enruta.presenter.company.LoginEmpresaPresenter;

public class LoginEmpresaActivity extends AppCompatActivity implements LoginEmpresaContract.View {

    private EditText etContraseña, etCorreo;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    private Button btnLoginConveyor;
    private LoginEmpresaContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_empresa);

        presenter = new LoginEmpresaPresenter(this);

        etCorreo = findViewById(R.id.etCorreo);
        etContraseña = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesionSwift);
        btnRegistrarse = findViewById(R.id.btnRegistrarseSwift);
        btnLoginConveyor = findViewById(R.id.btnLoginConveyor);

        btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_azul_left);
        btnRegistrarse.setBackgroundResource(R.drawable.bg_button_naranja_right);

        btnIniciarSesion.setOnClickListener(v -> {
            btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_azul_left);
            btnRegistrarse.setBackgroundResource(R.drawable.bg_button_naranja_right);
        });

        btnRegistrarse.setOnClickListener(v -> {
            btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_naranja_left);
            btnRegistrarse.setBackgroundResource(R.drawable.bg_button_azul_right);

            Intent intent = new Intent(LoginEmpresaActivity.this, RegisterEmpresaActivity.class);
            startActivity(intent); // <-- te faltaba este startActivity
        });

        btnLoginConveyor.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String contraseña = etContraseña.getText().toString().trim();
            presenter.login(correo, contraseña);
        });
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
