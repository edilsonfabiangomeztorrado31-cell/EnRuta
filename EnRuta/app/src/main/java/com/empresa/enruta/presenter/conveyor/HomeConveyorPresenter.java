package com.empresa.enruta.presenter.conveyor;

import com.empresa.enruta.contract.conveyor.HomeConveyorContract;

public class HomeConveyorPresenter implements HomeConveyorContract.HomeConveyorPresenter{

    private HomeConveyorContract.HomeConveyorView view;

    public HomeConveyorPresenter(HomeConveyorContract.HomeConveyorView view) {
        this.view = view;
    }


}
