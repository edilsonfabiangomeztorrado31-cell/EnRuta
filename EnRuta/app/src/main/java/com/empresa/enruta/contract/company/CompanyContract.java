package com.empresa.enruta.contract.company;

import com.empresa.enruta.model.company.Company;

import java.util.List;

public interface CompanyContract {

    interface CompanyView {
        void mostrarEmpresas(List<Company> lista);
        void mostrarMensaje(String mensaje);
    }

    interface CompanyPresenter {
        void obtenerEmpresas();
    }

    interface CompanyModel {
        void obtenerEmpresas(CompanyCallback callback);
    }

    interface CompanyCallback {
        void onEmpresasCargadas(List<Company> lista);
        void onError(String error);
    }
}
