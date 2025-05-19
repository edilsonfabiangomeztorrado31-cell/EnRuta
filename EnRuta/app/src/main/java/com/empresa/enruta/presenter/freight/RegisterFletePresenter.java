package com.empresa.enruta.presenter.freight;

import com.empresa.enruta.contract.FirebaseAuthErrorHandler;
import com.empresa.enruta.contract.freight.RegisterFleteContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFletePresenter implements RegisterFleteContract.Presenter {

    private RegisterFleteContract.View view;
    private FirebaseAuth auth;
    private DatabaseReference database;

    public RegisterFletePresenter(RegisterFleteContract.View view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("register_freight");
    }

    @Override
    public void onRegistrarFlete(String ubicacionOrigen, String ubicacionDestino, String tipoCarga, String precio, String peso,
                                 String fechaRegistro) {

        // Aquí puedes obtener el UID si ya hay un usuario autenticado
        FirebaseUser user = auth.getCurrentUser();
        String uid = user != null ? user.getUid() : database.push().getKey(); // Fallback en caso de no haber auth

        Map<String, Object> fleteData = new HashMap<>();
        fleteData.put("ubicacion_origen", ubicacionOrigen.trim());
        fleteData.put("ubicacion_destino", ubicacionDestino.trim());
        fleteData.put("tipo_carga", tipoCarga.trim());
        fleteData.put("precio", precio.trim());
        fleteData.put("peso", peso.trim());
        fleteData.put("fecha_registro", fechaRegistro.trim());

        // Guardar el flete con un ID único generado por push()
        database.push().setValue(fleteData)
                .addOnSuccessListener(aVoid -> view.registroExitoso("Tu registro de flete ha sido exitoso"))
                .addOnFailureListener(e -> view.mostrarMensaje("Error al guardar flete: " + e.getMessage()));
    }
}
