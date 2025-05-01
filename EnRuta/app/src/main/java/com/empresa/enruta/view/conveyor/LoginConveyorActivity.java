package com.empresa.enruta.view.conveyor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.conveyor.LoginConveyorContract;
import com.empresa.enruta.presenter.conveyor.LoginConveyorPresenter;
import com.empresa.enruta.view.company.RegisterEmpresaActivity;

public class LoginConveyorActivity extends AppCompatActivity implements LoginConveyorContract.View {

    private EditText etContraseña, etCorreo;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    private Button btnLoginConveyor;
    private LoginConveyorContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_conveyor);

        presenter = new LoginConveyorPresenter(this);

        etContraseña = findViewById(R.id.etContrasena);
        etCorreo = findViewById(R.id.etCorreo);
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

            Intent intent = new Intent(LoginConveyorActivity.this, RegisterEmpresaActivity.class);
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
        Intent intent = new Intent(this, HomeConveyorActivity.class);
        startActivity(intent);
        finish();
    }
}
