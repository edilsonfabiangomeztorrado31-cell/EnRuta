package com.empresa.enruta.view.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.HomeEmpresaContract;

public class HomeCompanyActivity extends CompanyMenuView implements HomeEmpresaContract.HomeEmpresaView{

    private Button btnGestionFlete, btnTransAsignado, btnVerHistorial;
    private HomeEmpresaContract.HomeEmpresaPresenter presenterHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home_company, findViewById(R.id.fragment_container_company));

        btnGestionFlete = findViewById(R.id.btnGestionFletes);
        btnTransAsignado = findViewById(R.id.btnTransAsignado);
        btnVerHistorial = findViewById(R.id.btnVerHistorial);


        btnGestionFlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterHome.onGestionFleteClicked();
            }
        });

        btnTransAsignado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterHome.onTransAsignadoClicked();
            }
        });

        btnVerHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterHome.onVerHistorialClicked();
            }
        });
    }

    @Override
    public void irAGestionFletes() {
        startActivity(new Intent(this, GestionFletesActivity.class));
    }

    @Override
    public void irATransportadoresAsignados() {
        startActivity(new Intent(this, TransportadoresAsignadosActivity.class));
    }

    @Override
    public void irAVerHistorial() {
        startActivity(new Intent(this, VerHistorialActivity.class));
    }
}
