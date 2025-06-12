package com.empresa.enruta.view.conveyor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.conveyor.RegisterConveyorContract;
import com.empresa.enruta.presenter.conveyor.RegisterConveyorPresenter;

import java.util.ArrayList;
import java.util.List;

public class RegisterCoveyorActivity extends AppCompatActivity implements RegisterConveyorContract.View {

    private Spinner spinnerTipoDocumento, spinnerTipoVehiculo;
    private EditText etNombre, etApellidos, etNumDocumento, etCorreo, etContacto, etPlaca, etCapcidadToneladas, etPassword;
    private Button btnRegistrar;
    private TextView tvIniciarSesion;
    private RegisterConveyorContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_conveyor);

        presenter = new RegisterConveyorPresenter(this);

//        Button btnIniciarSesion = findViewById(R.id.btnIniciarSesionSwift);
//        Button btnRegistrarse = findViewById(R.id.btnRegistrarseSwift);
//
//        btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_naranja_left);
//        btnRegistrarse.setBackgroundResource(R.drawable.bg_button_azul_right);
//
//        btnIniciarSesion.setOnClickListener(v -> {
//            btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_azul_left);
//            btnRegistrarse.setBackgroundResource(R.drawable.bg_button_naranja_right);
//
//            Intent intent = new Intent(RegisterCoveyorActivity.this, LoginConveyorActivity.class);
//            startActivity(intent);
//
//        });
//
//        btnRegistrarse.setOnClickListener(v -> {
//            btnIniciarSesion.setBackgroundResource(R.drawable.bg_button_naranja_left);
//            btnRegistrarse.setBackgroundResource(R.drawable.bg_button_azul_right);
//
//            Intent intent = new Intent(RegisterCoveyorActivity.this, RegisterCoveyorActivity.class);
//        });

        etNombre = findViewById(R.id.etNombreConveyor);
        etApellidos = findViewById(R.id.etApellido);
        spinnerTipoDocumento = findViewById(R.id.spinnerTipoDocumento);
        etNumDocumento = findViewById(R.id.etNumDocumento);
        etCorreo = findViewById(R.id.etCorreo);
        etContacto = findViewById(R.id.etContacto);
        etPlaca = findViewById(R.id.etPlaca);
        etCapcidadToneladas = findViewById(R.id.etCapacidadToneladas);
        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);
        etPassword = findViewById(R.id.etPassword);
        tvIniciarSesion = findViewById(R.id.tvIniciaSesion);

        presenter.cargarOpcionesIniciales();

        btnRegistrar = findViewById(R.id.btnRegistrarseConveyor);
        btnRegistrar.setOnClickListener(v -> {
            presenter.registrarConveyor(
                    etNombre.getText().toString(),
                    etApellidos.getText().toString(),
                    spinnerTipoDocumento.getSelectedItem().toString(),
                    etNumDocumento.getText().toString(),
                    etCorreo.getText().toString(),
                    etContacto.getText().toString(),
                    etPlaca.getText().toString(),
                    etCapcidadToneladas.getText().toString(),
                    spinnerTipoVehiculo.getSelectedItem().toString(),
                    etPassword.getText().toString()
            );
        });
        tvIniciarSesion.setOnClickListener(v -> presenter.onIniciarSesionClicked());
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
    public void showTipoDocumentoOptions(List<String> opciones) {
        List<String> opcionesModificables = new ArrayList<>(opciones); // Crear una lista modificable
        opcionesModificables.add(0, "Tipo de documento");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_custom, opcionesModificables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDocumento.setAdapter(adapter);
    }

    @Override
    public void showTipoVehiculoOptions(List<String> opciones) {
        List<String> opcionesModificables = new ArrayList<>(opciones); // Crear una lista modificable
        opcionesModificables.add(0, "Tipo de vehículo");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_custom2, opcionesModificables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoVehiculo.setAdapter(adapter);
    }

    @Override
    public void irAIniciarSesion() {
        Intent intent = new Intent(this, LoginConveyorActivity.class);
        startActivity(intent);
    }
}
