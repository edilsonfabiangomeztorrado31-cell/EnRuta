package com.empresa.enruta.view.conveyor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.MenuContract;
import com.empresa.enruta.contract.conveyor.ConveyorContract;
import com.empresa.enruta.contract.conveyor.HomeConveyorContract;
import com.empresa.enruta.model.conveyor.Conveyor;
import com.empresa.enruta.model.conveyor.ConveyorModelImpl;
import com.empresa.enruta.model.freight.Freight;
import com.empresa.enruta.model.freight.HomeConveyorModelImpl;
import com.empresa.enruta.presenter.conveyor.ConveyorPresenterImpl;
import com.empresa.enruta.presenter.conveyor.HomeConveyorPresenter;
import com.empresa.enruta.view.adapters.FreightAdapter;
import com.empresa.enruta.view.freight.DetalleFleteActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeConveyorActivity extends ConveyorMenuView implements HomeConveyorContract.HomeConveyorView , ConveyorContract.ConveyorView {

    private RecyclerView recyclerView;
    private FreightAdapter adapter;
    private HomeConveyorPresenter presenter;
    private ConveyorPresenterImpl present;
    private ConveyorContract.ConveyorPresenter presenterConveyor;
    private TextView tvMensajeFletes;
    private Handler handler = new Handler();
    private int mensajeIndex = 0;
    private String[] mensajes;

    private boolean hiloActivo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_home_conveyor, findViewById(R.id.fragment_container_conveyor));

        recyclerView = findViewById(R.id.recyclerViewFletes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new HomeConveyorPresenter(this, new HomeConveyorModelImpl());

        presenter.obtenerFletes();

        present = new ConveyorPresenterImpl(this, new ConveyorModelImpl());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Puedes usar el userId si lo necesitas aquí
            Log.d("DEBUG_UID", "Usuario autenticado: " + userId);
        } else {
            mostrarMensaje("No hay usuario autenticado. Redirigiendo al login.");
            startActivity(new Intent(this, LoginConveyorActivity.class));
            finish();
            return;
        }

        mensajes = new String[]{
                getString(R.string.mensaje_flete_1),
                getString(R.string.mensaje_flete_2),
                getString(R.string.mensaje_flete_3),
                getString(R.string.mensaje_flete_4),
                getString(R.string.mensaje_flete_5)
        };


        tvMensajeFletes = findViewById(R.id.tvMensajeFletes);
        iniciarHiloMensajes();

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

    @Override
    public void mostrarConveyors(List<Conveyor> lista) {
        for (Conveyor c : lista) {
            Log.d("DEBUG_CONVEYOR", "Transportador cargado: " + c.getNombre() + " (ID: " + c.getId() + ")");
        }
    }

    private void iniciarHiloMensajes() {
        Thread hilo = new Thread(() -> {
            while (hiloActivo) {
                try {
                    Thread.sleep(10000); // Espera 5 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    mensajeIndex = (mensajeIndex + 1) % mensajes.length;

                    // Animación de fade out
                    tvMensajeFletes.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .withEndAction(() -> {
                                // Cambiar mensaje y aplicar fade in
                                tvMensajeFletes.setText(mensajes[mensajeIndex]);
                                tvMensajeFletes.animate()
                                        .alpha(1f)
                                        .setDuration(500)
                                        .start();
                            }).start();
                });
            }
        });

        hilo.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hiloActivo = false; // para detener el hilo cuando se cierra la actividad
    }
}
