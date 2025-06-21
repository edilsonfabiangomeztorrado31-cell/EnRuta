package com.empresa.enruta.presenter.company;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import com.empresa.enruta.contract.company.UbicacionActualContract;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class UbicacionActualPresenter implements UbicacionActualContract.Presenter {
    private final Context context;
    private final UbicacionActualContract.View view;
    private final FusedLocationProviderClient fusedLocationProviderClient;


    public UbicacionActualPresenter(Context context, UbicacionActualContract.View view) {
        this.context = context;
        this.view = view;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            view.mostrarError("Permiso de ubicación no concedido.");
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        view.mostrarUbicacion(location);
                    } else {
                        view.mostrarError("No se pudo obtener ubicación.");
                    }
                })
                .addOnFailureListener(e -> view.mostrarError("Error al obtener ubicación: " + e.getMessage()));
    }

    @Override
    public void detenerActualizaciones() {

    }
}
