package com.empresa.enruta.contract.company;

import android.location.Location;

public interface UbicacionActualContract {
    interface View {
        void mostrarUbicacion(Location location);
        void mostrarError(String mensaje);
    }

    interface Presenter {
        void obtenerUbicacion();
        void detenerActualizaciones();
    }
}
