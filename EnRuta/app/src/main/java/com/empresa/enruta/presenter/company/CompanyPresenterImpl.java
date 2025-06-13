package com.empresa.enruta.presenter.company;

import com.empresa.enruta.contract.company.CompanyContract;
import com.empresa.enruta.model.company.Company;

import java.util.List;

public class CompanyPresenterImpl implements CompanyContract.CompanyPresenter {

    private final CompanyContract.CompanyView view;
    private final CompanyContract.CompanyModel model;

    public CompanyPresenterImpl(CompanyContract.CompanyView view, CompanyContract.CompanyModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void obtenerEmpresas() {
        model.obtenerEmpresas(new CompanyContract.CompanyCallback() {
            @Override
            public void onEmpresasCargadas(List<Company> lista) {
                view.mostrarEmpresas(lista);
            }

            @Override
            public void onError(String error) {
                view.mostrarMensaje(error);
            }
        });
    }
}
