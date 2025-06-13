package com.empresa.enruta.model.conveyor;

import android.util.Log;

import com.empresa.enruta.contract.conveyor.ConveyorContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConveyorModelImpl implements ConveyorContract.ConveyorModel {

    private DatabaseReference database;

    public ConveyorModelImpl() {
        database = FirebaseDatabase.getInstance().getReference("register_conveyor");
    }

    @Override
    public void obtenerConveyors(ConveyorContract.ConveyorCallback callback) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Conveyor> lista = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Conveyor conveyor = data.getValue(Conveyor.class);
                        if (conveyor != null) {
                            conveyor.setId(data.getKey()); // Asignar el ID desde Firebase
                            lista.add(conveyor);
                            Log.d("DEBUG_CONVEYOR", "Transportador ID: " + conveyor.getId());
                        }
                    }
                    callback.onConveyorsCargados(lista);
                } else {
                    callback.onConveyorsCargados(lista); // lista vacía
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Error al obtener transportadores: " + error.getMessage());
            }
        });
    }
}
