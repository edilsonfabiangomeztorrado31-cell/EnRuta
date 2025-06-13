package com.empresa.enruta.model.company;

import android.util.Log;

import com.empresa.enruta.contract.company.CompanyContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyModelImpl implements CompanyContract.CompanyModel {

    private final DatabaseReference database;

    public CompanyModelImpl() {
        database = FirebaseDatabase.getInstance().getReference("register_company");
    }

    @Override
    public void obtenerEmpresas(CompanyContract.CompanyCallback callback) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Company> lista = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Company company = data.getValue(Company.class);
                    if (company != null) {
                        lista.add(company);
                        Log.d("DEBUG_COMPANY", "Empresa cargada: " + company.getNombre());
                    }
                }
                callback.onEmpresasCargadas(lista);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Error al obtener empresas: " + error.getMessage());
            }
        });
    }
}
