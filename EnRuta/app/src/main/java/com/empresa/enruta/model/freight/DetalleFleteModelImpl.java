package com.empresa.enruta.model.freight;

import android.util.Log;

import com.empresa.enruta.contract.freight.DetalleFleteContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetalleFleteModelImpl implements DetalleFleteContract.DetalleFleteModel {

    private DatabaseReference database;

    public DetalleFleteModelImpl() {
        database = FirebaseDatabase.getInstance().getReference("register_freight");
    }

    @Override
    public void cargarDetalleFletes(FleteDetalleCallback callback) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Freight> lista = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Freight flete = data.getValue(Freight.class);

                        if (flete != null) {
                            // Asignar el ID desde la key del nodo
                            flete.setId(data.getKey());
                            Log.i("DEBUG_FLETE_SETTER", "ID asignado: " + flete.getId());


                            lista.add(flete);
                            Log.i("DEBUG_FLETE_LISTA", "ID en lista: " + flete.getId());
                        } else {
                            Log.w("DEBUG_FLETE", "Objeto Freight nulo al obtener de Firebase");
                        }
                    }
                    callback.onFletesDetallesCargados(lista);
                } else {
                    Log.i("DEBUG_FLETE", "No hay fletes en la base de datos.");
                    callback.onFletesDetallesCargados(lista); // Devuelve lista vacía
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Error al obtener fletes: " + error.getMessage());
            }
        });
    }

    @Override
    public void cargarFletePorId(String id, FleteCallback callback) {
        database.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Freight flete = snapshot.getValue(Freight.class);
                    if (flete != null) {
                        flete.setId(snapshot.getKey());  // Asignar el ID
                        callback.onFleteCargado(flete);
                    } else {
                        callback.onError("El flete con ID " + id + " está vacío.");
                    }
                } else {
                    callback.onError("No se encontró flete con ID " + id);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Error al obtener flete: " + error.getMessage());
            }
        });
    }
}
