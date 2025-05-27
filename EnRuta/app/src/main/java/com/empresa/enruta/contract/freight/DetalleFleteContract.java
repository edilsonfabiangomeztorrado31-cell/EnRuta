package com.empresa.enruta.contract.freight;

import com.empresa.enruta.contract.conveyor.HomeConveyorContract;
import com.empresa.enruta.model.freight.Freight;

import java.util.List;

public interface DetalleFleteContract {

    interface DetalleFleteView {
        void mostrarDetalleFletes(List<Freight> fletes);
        void mostrarDetalleFlete(Freight freight);
        void mostrarMensaje(String mensaje);
        void irATomarFlete(Freight freight);
        void irAMostrarMapa(Freight freight);
    }

    interface DetalleFletePresenter {
        void obtenerDetalleFletes();
        void obtenerFletePorId(String id);
        void onTomarFleteClick(Freight freight);
        void onVerRutaClick(Freight freight);
    }
    interface DetalleFleteModel {
        void cargarDetalleFletes(FleteDetalleCallback callback);
        void cargarFletePorId(String id, FleteCallback callback);

        interface FleteDetalleCallback{
            void onFletesDetallesCargados(List<Freight> fletes);
            void onError(String mensaje);
        }
        interface FleteCallback {
            void onFleteCargado(Freight freight);
            void onError(String mensaje);
        }
    }
}
