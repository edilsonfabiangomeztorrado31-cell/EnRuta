package com.empresa.enruta.view.freight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.freight.DetalleFleteContract;
import com.empresa.enruta.model.freight.DetalleFleteModelImpl;
import com.empresa.enruta.model.freight.Freight;
import com.empresa.enruta.presenter.freight.DetalleFletePresenter;
import com.empresa.enruta.view.adapters.DetalleFreightAdapter;
import com.empresa.enruta.view.adapters.FreightAdapter;
import com.empresa.enruta.view.conveyor.ConveyorMenuView;

import java.util.List;

public class DetalleFleteActivity extends ConveyorMenuView implements DetalleFleteContract.DetalleFleteView {

    private RecyclerView recyclerView;
    private DetalleFreightAdapter adapter;
    private DetalleFletePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_detalle_flete, findViewById(R.id.fragment_container_conveyor));

        recyclerView = findViewById(R.id.recyclerViewDetalleFletes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new DetalleFletePresenter(this, new DetalleFleteModelImpl());
        String freightId = getIntent().getStringExtra("freight_id");

        if (freightId != null) {
            presenter.obtenerFletePorId(freightId);
        } else {
            presenter.obtenerDetalleFletes();
        }
    }

    @Override
    public void mostrarDetalleFletes(List<Freight> fletes) {
        if (fletes == null || fletes.isEmpty()) {
            Toast.makeText(this, "No hay fletes disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new DetalleFreightAdapter(fletes, freight -> presenter.onTomarFleteClick(freight));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void mostrarMensaje(String mensaje) {

    }

    @Override
    public void irATomarFlete(Freight freight) {
//        Intent intent = new Intent(this, TomarFleteActivity.class);
//        intent.putExtra("freight_id", freight.getId());
//        startActivity(intent);
    }
    @Override
    public void mostrarDetalleFlete(Freight freight) {
        adapter = new DetalleFreightAdapter(
                java.util.Collections.singletonList(freight),
                f -> presenter.onTomarFleteClick(f)
        );
        recyclerView.setAdapter(adapter);
    }

}
