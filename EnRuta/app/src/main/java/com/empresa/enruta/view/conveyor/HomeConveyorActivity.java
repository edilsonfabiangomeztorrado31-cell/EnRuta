package com.empresa.enruta.view.conveyor;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.MenuContract;
import com.empresa.enruta.contract.conveyor.HomeConveyorContract;
import com.empresa.enruta.presenter.conveyor.HomeConveyorPresenter;
import com.empresa.enruta.presenter.conveyor.MenuPresenterImplConveyor;
import com.google.android.material.navigation.NavigationView;

public class HomeConveyorActivity extends ConveyorMenuView implements HomeConveyorContract.HomeConveyorView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_home_conveyor, findViewById(R.id.fragment_container_conveyor));
    }
}
