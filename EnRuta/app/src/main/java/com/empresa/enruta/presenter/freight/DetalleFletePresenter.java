package com.empresa.enruta.presenter.freight;

import android.util.Log;

import com.empresa.enruta.contract.freight.DetalleFleteContract;
import com.empresa.enruta.model.freight.Freight;

import java.util.List;

public class DetalleFletePresenter implements DetalleFleteContract.DetalleFletePresenter {

    private DetalleFleteContract.DetalleFleteView view;
    private DetalleFleteContract.DetalleFleteModel model;

    public DetalleFletePresenter(DetalleFleteContract.DetalleFleteView view, DetalleFleteContract.DetalleFleteModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void obtenerDetalleFletes() {
        model.cargarDetalleFletes(new DetalleFleteContract.DetalleFleteModel.FleteDetalleCallback() {
            @Override
            public void onFletesDetallesCargados(List<Freight> fletes) {
                view.mostrarDetalleFletes(fletes);
            }

            @Override
            public void onError(String mensaje) {
                view.mostrarMensaje(mensaje);
            }
        });
    }

    @Override
    public void onTomarFleteClick(Freight freight) {
        view.irATomarFlete(freight);
    }

    @Override
    public void obtenerFletePorId(String id) {
        model.cargarFletePorId(id, new DetalleFleteContract.DetalleFleteModel.FleteCallback() {
            @Override
            public void onFleteCargado(Freight freight) {
                view.mostrarDetalleFlete(freight);
            }

            @Override
            public void onError(String mensaje) {
                view.mostrarMensaje(mensaje);
            }
        });
    }

}
