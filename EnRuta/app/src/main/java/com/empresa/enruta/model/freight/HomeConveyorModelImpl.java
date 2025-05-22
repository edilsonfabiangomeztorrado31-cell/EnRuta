package com.empresa.enruta.model.freight;

import android.util.Log;

import com.empresa.enruta.contract.conveyor.HomeConveyorContract;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class HomeConveyorModelImpl implements HomeConveyorContract.HomeConveyorModel {

    private DatabaseReference database;

    public HomeConveyorModelImpl() {
        database = FirebaseDatabase.getInstance().getReference("register_freight");
    }

    @Override
    public void cargarFletes(FleteCallback callback) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Freight> lista = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Freight flete = data.getValue(Freight.class);

                        if (flete != null) {
                            flete.setId(data.getKey());
                            lista.add(flete);
                        } else {
                            Log.w("DEBUG_FLETE", "Objeto Freight nulo al obtener de Firebase");
                        }
                    }
                    callback.onFletesCargados(lista);
                } else {
                    Log.i("DEBUG_FLETE", "No hay fletes en la base de datos.");
                    callback.onFletesCargados(lista); // Devuelve lista vacía
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Error al obtener fletes: " + error.getMessage());
            }
        });
    }

}

