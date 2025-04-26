package com.empresa.enruta.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.WelcomeAppContract;
import com.empresa.enruta.presenter.WelcomeAppPresenter;

public class WelcomeAppActivity extends AppCompatActivity implements WelcomeAppContract.View {

    private WelcomeAppContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_welcome_app);

        presenter = new WelcomeAppPresenter(this);

        findViewById(R.id.btnEmpresa).setOnClickListener(v -> presenter.onEmpresaSeleccionada());
        findViewById(R.id.btnTransportador).setOnClickListener(v -> presenter.onTransportadorSeleccionado());
    }

    @Override
    public void navegarEmpresa() {
       startActivity(new Intent(this, RegisterEmpresaActivity.class));
    }

    @Override
    public void navegarTransportador() {
        startActivity(new Intent(this, RegisterTransportadorActivity.class));
    }
}
