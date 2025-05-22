package com.empresa.enruta.view.conveyor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.MenuContract;
import com.empresa.enruta.contract.conveyor.HomeConveyorContract;
import com.empresa.enruta.model.freight.Freight;
import com.empresa.enruta.model.freight.HomeConveyorModelImpl;
import com.empresa.enruta.presenter.conveyor.HomeConveyorPresenter;
import com.empresa.enruta.view.adapters.FreightAdapter;
import com.empresa.enruta.view.freight.DetalleFleteActivity;

import java.util.List;

public class HomeConveyorActivity extends ConveyorMenuView implements HomeConveyorContract.HomeConveyorView{

    private RecyclerView recyclerView;
    private FreightAdapter adapter;
    private HomeConveyorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_home_conveyor, findViewById(R.id.fragment_container_conveyor));

        recyclerView = findViewById(R.id.recyclerViewFletes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new HomeConveyorPresenter(this, new HomeConveyorModelImpl());

        presenter.obtenerFletes();

    }

    @Override
    public void mostrarFletes(List<Freight> fletes) {
        if (fletes == null || fletes.isEmpty()) {
            Toast.makeText(this, "No hay fletes disponibles", Toast.LENGTH_SHORT).show();
            return;
        }
        // Debug: revisar los IDs antes de pasar al Adapter
        for (Freight f : fletes) {
            Log.i("DEBUG_FLETE_LISTA_ENTRADA_ADAPTER", "ID en lista antes de adapter: " + f.getId());
        }
        adapter = new FreightAdapter(fletes, freight -> presenter.onDetalleFleteClick(freight));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void irADetalleFlete(Freight freight) {
        Intent intent = new Intent(this, DetalleFleteActivity.class);
        intent.putExtra("freight_id", freight.getId());
        startActivity(intent);
    }
    @Override
    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
