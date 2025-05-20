package com.empresa.enruta.view.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.PanelEmpresaContract;
import com.empresa.enruta.presenter.company.PanelEmpresaPresenter;
import com.empresa.enruta.view.freight.RegisterFletesActivity;

public class GestionFletesActivity extends CompanyMenuView implements PanelEmpresaContract.PanelEmpresaView {

    private Button btnRegistrarFlete, btnEditarFlete, btnEliminarFlete;
    private PanelEmpresaContract.PanelEmpresaPresenter presenterPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        getLayoutInflater().inflate(R.layout.activity_gestion_fletes, findViewById(R.id.fragment_container_company));

        presenterPanel = new PanelEmpresaPresenter(this);

        btnRegistrarFlete = findViewById(R.id.btnRegistrarFlete);
        btnEditarFlete = findViewById(R.id.btnEditarFlete);
        btnEliminarFlete = findViewById(R.id.btnEliminarFlete);

        btnRegistrarFlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterPanel.onRegistrarFlete();
            }
        });

        btnEditarFlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterPanel.onEditarFlete();
            }
        });

        btnEliminarFlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterPanel.onEliminarFlete();
            }
        });
    }

    @Override
    public void irARegistrarFlete() {
        startActivity(new Intent(this, RegisterFletesActivity.class));
    }

    @Override
    public void irAEditarFlete() {
        startActivity(new Intent(this, EditarFletesActivity.class));
    }

    @Override
    public void irAEliminarFlete() {
        startActivity(new Intent(this, EliminarFletesActivity.class));
    }
}