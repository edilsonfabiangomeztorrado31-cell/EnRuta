package com.empresa.enruta.presenter.conveyor;

import com.empresa.enruta.contract.conveyor.ConveyorContract;
import com.empresa.enruta.model.conveyor.Conveyor;

import java.util.List;

public class ConveyorPresenter implements ConveyorContract.ConveyorPresenter {

    private ConveyorContract.ConveyorView view;
    private ConveyorContract.ConveyorModel model;

    public ConveyorPresenter(ConveyorContract.ConveyorView view, ConveyorContract.ConveyorModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void cargarConveyors() {

        model.obtenerConveyors(new ConveyorContract.ConveyorCallback() {
            @Override
            public void onConveyorsCargados(List<Conveyor> lista) {
                view.mostrarConveyors(lista);
            }

            @Override
            public void onError(String error) {
                view.mostrarError(error);
            }
        });
    }
}
