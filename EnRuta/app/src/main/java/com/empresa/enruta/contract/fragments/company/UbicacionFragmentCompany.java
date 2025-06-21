package com.empresa.enruta.contract.fragments.company;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.UbicacionActualContract;
import com.empresa.enruta.presenter.company.UbicacionActualPresenter;

public class UbicacionFragmentCompany extends Fragment implements UbicacionActualContract.View {

    private UbicacionActualPresenter presenter;
    private TextView tvUbicacionActual;

    @Nullable
    @Override

    public View onCreateView (@Nullable LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstatnceState){

        View view = inflater.inflate(R.layout.fragment_ubicacion_company, container, false);

        tvUbicacionActual = view.findViewById(R.id.tvUbicacionActual);
        presenter = new UbicacionActualPresenter(requireContext(), this);
        presenter.obtenerUbicacion();
        return view;

    }

    @Override
    public void mostrarUbicacion(Location location) {
        String ubicacion = "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude();
        tvUbicacionActual.setText(ubicacion);
    }

    @Override
    public void mostrarError(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detenerActualizaciones();
    }
}
