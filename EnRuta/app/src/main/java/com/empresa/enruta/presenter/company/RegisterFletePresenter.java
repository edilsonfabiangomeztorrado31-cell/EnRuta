package com.empresa.enruta.presenter.company;

import com.empresa.enruta.contract.company.RegisterFleteContract;
import com.empresa.enruta.contract.conveyor.RegisterConveyorContract;

public class RegisterFletePresenter implements RegisterFleteContract.RegisterFletePresenter {

    private RegisterFleteContract.RegisterFleteView view;

    public RegisterFletePresenter(RegisterFleteContract.RegisterFleteView view) {
        this.view = view;
    }

    @Override
    public void onRegistrarFlete() {
        view.irARegistrarFlete();
    }
}
