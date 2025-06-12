package com.empresa.enruta.view.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.HomeEmpresaContract;
import com.empresa.enruta.presenter.company.HomeEmpresaPresenter;
import com.empresa.enruta.presenter.company.MenuPresenterImplCompany;
import com.empresa.enruta.view.freight.RegisterFletesActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HomeCompanyActivity extends CompanyMenuView implements HomeEmpresaContract.HomeEmpresaView{

    private HomeEmpresaContract.HomeEmpresaPresenter presenterHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home_company, findViewById(R.id.fragment_container_company));

        presenterHome = new HomeEmpresaPresenter(this);

        ImageButton btnAdd = findViewById(R.id.button_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomeCompanyActivity.this);
                View bottomSheetView = LayoutInflater.from(HomeCompanyActivity.this).inflate(
                        R.layout.bottom_sheet_add,
                        findViewById(android.R.id.content),
                        false
                );

                Button btnIrARegistro = bottomSheetView.findViewById(R.id.btn_ir_a_registro);
                btnIrARegistro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeCompanyActivity.this, RegisterFletesActivity.class);
                        startActivity(intent);
                        bottomSheetDialog.dismiss(); // cerrar el diálogo
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

    }
}
