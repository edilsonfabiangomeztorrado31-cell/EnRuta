package com.empresa.enruta.presenter.conveyor;

import com.empresa.enruta.contract.conveyor.HomeConveyorContract;
import com.empresa.enruta.model.freight.Freight;

import java.util.List;

public class HomeConveyorPresenter implements HomeConveyorContract.HomeConveyorPresenter{

    private HomeConveyorContract.HomeConveyorView view;
    private HomeConveyorContract.HomeConveyorModel model;

    public HomeConveyorPresenter(HomeConveyorContract.HomeConveyorView view, HomeConveyorContract.HomeConveyorModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void obtenerFletes() {
        model.cargarFletes(new HomeConveyorContract.HomeConveyorModel.FleteCallback() {
            @Override
            public void onFletesCargados(List<Freight> fletes) {
                view.mostrarFletes(fletes);
            }

            @Override
            public void onError(String mensaje) {
                view.mostrarMensaje(mensaje);
            }
        });
    }

    @Override
    public void onDetalleFleteClick(Freight freight) {
        view.irADetalleFlete(freight);
    }
}
