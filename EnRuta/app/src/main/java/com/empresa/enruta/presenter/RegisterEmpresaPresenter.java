package com.empresa.enruta.presenter;

import com.empresa.enruta.contract.RegisterEmpresaContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegisterEmpresaPresenter implements RegisterEmpresaContract.Presenter {

    private RegisterEmpresaContract.View view;
    private FirebaseAuth auth;
    private DatabaseReference database;

    public RegisterEmpresaPresenter(RegisterEmpresaContract.View view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("register_company");
    }

    @Override
    public void registrarEmpresa(String nombre, String nit, String correo, String representante,
                                 String tipoEmpresa, String direccion, String contacto, String contraseña) {

        if (nombre.isEmpty() || nit.isEmpty() || correo.isEmpty() || representante.isEmpty() ||
        tipoEmpresa.isEmpty() || direccion.isEmpty() || contacto.isEmpty() || contraseña.isEmpty()) {
            view.mostrarMensaje("Todos los campos son obligatorios");
            return;
        }

        auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();

                        Map<String, Object> empresaData = new HashMap<>();
                        empresaData.put("nombre", nombre);
                        empresaData.put("nit", nit);
                        empresaData.put("correo", correo);
                        empresaData.put("representante", representante);
                        empresaData.put("tipoEmpresa", tipoEmpresa);
                        empresaData.put("direccion", direccion);
                        empresaData.put("contacto", contacto);

                        database.child(uid).setValue(empresaData)
                                .addOnSuccessListener(aVoid -> {
                                    view.mostrarMensaje("Empresa registrada correctamente");
                                    view.registroExitoso("Tu registro ha sido exitoso");
                                })
                                .addOnFailureListener(e -> view.mostrarMensaje("Error al guardar: " + e.getMessage()));
                    } else {
                        view.mostrarMensaje("Error al registrar: " + task.getException().getMessage());
                    }
                });
    }
}
