package com.empresa.enruta.contract.conveyor;

import com.empresa.enruta.model.conveyor.Conveyor;

import java.util.List;

public interface ConveyorContract {

    interface ConveyorModel {
        void obtenerConveyors(ConveyorCallback callback);
    }

    interface ConveyorView {
        void mostrarConveyors(List<Conveyor> lista);
        void mostrarError(String mensaje);
    }

    interface ConveyorPresenter {
        void cargarConveyors();
    }

    interface ConveyorCallback {
        void onConveyorsCargados(List<Conveyor> lista);
        void onError(String error);
    }
}
