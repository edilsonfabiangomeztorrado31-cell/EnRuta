package com.empresa.enruta.view.company;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.WelcomeAppContract;
import com.empresa.enruta.presenter.company.WelcomeAppPresenter;
import com.empresa.enruta.view.conveyor.RegisterCoveyorActivity;

public class WelcomeAppActivity extends AppCompatActivity implements WelcomeAppContract.View {

    private WelcomeAppContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_welcome_app);

        presenter = new WelcomeAppPresenter(this);

        findViewById(R.id.btnPublicarCarga).setOnClickListener(v -> presenter.onEmpresaSeleccionada());
        findViewById(R.id.btnBuscarFletes).setOnClickListener(v -> presenter.onTransportadorSeleccionado());
    }

    @Override
    public void navegarEmpresa() {
       startActivity(new Intent(this, RegisterEmpresaActivity.class));
    }

    @Override
    public void navegarTransportador() {
        startActivity(new Intent(this, RegisterCoveyorActivity.class));
    }
}
