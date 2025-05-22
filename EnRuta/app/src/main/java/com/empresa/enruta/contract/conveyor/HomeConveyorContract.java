package com.empresa.enruta.contract.conveyor;

import com.empresa.enruta.model.freight.Freight;

import java.util.List;

public interface HomeConveyorContract {

    interface HomeConveyorView {
        void mostrarFletes(List<Freight> fletes);
        void mostrarMensaje(String mensaje);
        void irADetalleFlete(Freight freight);
    }

    interface HomeConveyorPresenter {
        void obtenerFletes();
        void onDetalleFleteClick(Freight freight);
    }

    interface HomeConveyorModel {
        void cargarFletes(FleteCallback callback);

        interface FleteCallback {
            void onFletesCargados(List<Freight> fletes);
            void onError(String mensaje);
        }
    }
}
