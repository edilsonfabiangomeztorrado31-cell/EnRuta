package com.empresa.enruta.view.freight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.Manifest;

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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Geocoder;
import android.location.Address;

import java.util.List;

public class DetalleFleteActivity extends ConveyorMenuView implements DetalleFleteContract.DetalleFleteView {

    private RecyclerView recyclerView;
    private DetalleFreightAdapter adapter;
    private DetalleFletePresenter presenter;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_detalle_flete, findViewById(R.id.fragment_container_conveyor));

        recyclerView = findViewById(R.id.recyclerViewDetalleFletes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        presenter = new DetalleFletePresenter(this, new DetalleFleteModelImpl());
        String freightId = getIntent().getStringExtra("freight_id");

        if (freightId != null) {
            presenter.obtenerFletePorId(freightId);
        } else {
            presenter.obtenerDetalleFletes();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void mostrarDetalleFletes(List<Freight> fletes) {
        if (fletes == null || fletes.isEmpty()) {
            Toast.makeText(this, "No hay fletes disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new DetalleFreightAdapter(fletes, new DetalleFreightAdapter.OnFreightClickListener() {
            @Override
            public void onTomarClick(Freight freight) {
                presenter.onTomarFleteClick(freight);
            }

            @Override
            public void onVerRutaClick(Freight freight) {
                presenter.onVerRutaClick(freight);
                abrirRutaEnGoogleMaps(freight.getUbicacionOrigen(), freight.getUbicacionDestino());
            }
        });

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
    public void irAMostrarMapa(Freight freight) {
       // abrirRutaEnGoogleMaps(freight.getUbicacionOrigen(), freight.getUbicacionDestino());
        iniciarNavegacionEnGoogleMaps(freight.getUbicacionDestino());

    }

    @Override
    public void mostrarDetalleFlete(Freight freight) {
        adapter = new DetalleFreightAdapter(
                java.util.Collections.singletonList(freight),
                new DetalleFreightAdapter.OnFreightClickListener() {
                    @Override
                    public void onTomarClick(Freight f) {
                        presenter.onTomarFleteClick(f);
                    }

                    @Override
                    public void onVerRutaClick(Freight f) {
                        //abrirRutaEnGoogleMaps(f.getUbicacionOrigen(), f.getUbicacionDestino());
                        if (f.getUbicacionDestino() != null && !f.getUbicacionDestino().isEmpty()) {
                            iniciarNavegacionEnGoogleMaps(f.getUbicacionDestino());
                        } else {
                            //Toast.makeText(context, "Dirección destino no disponible.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        recyclerView.setAdapter(adapter);
    }

    private void iniciarNavegacionEnGoogleMaps(String destinoDireccion) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no hay permisos, solicítalos
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return;
        }

        // Si hay permisos, continuar
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latOrigen = location.getLatitude();
                        double lngOrigen = location.getLongitude();

                        Geocoder geocoder = new Geocoder(this);
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(destinoDireccion, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address addressDestino = addresses.get(0);
                                double latDestino = addressDestino.getLatitude();
                                double lngDestino = addressDestino.getLongitude();

                                String uri = "https://www.google.com/maps/dir/?api=1" +
                                        "&origin=" + latOrigen + "," + lngOrigen +
                                        "&destination=" + latDestino + "," + lngDestino +
                                        "&travelmode=driving";

                                Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");

                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Google Maps no está instalado.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "No se encontró la dirección destino.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("Geocoding", "Error geocodificando destino", e);
                        }
                    } else {
                        Toast.makeText(this, "No se pudo obtener tu ubicación actual.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void abrirRutaEnGoogleMaps(String origen, String destino) {
        try {
            String uri = "https://www.google.com/maps/dir/?api=1&origin=" +
                    origen.replace(" ", "+") +
                    "&destination=" +
                    destino.replace(" ", "+") +
                    "&travelmode=driving";

            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Google Maps no está instalado.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MapsIntent", "Error abriendo Google Maps", e);
        }
    }

}
