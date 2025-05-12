package com.empresa.enruta.view.company;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.RegisterFleteContract;
import com.empresa.enruta.presenter.company.RegisterFletePresenter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterFletesActivity extends CompanyMenuView implements RegisterFleteContract.RegisterFleteView {

    private EditText etUbicacionOrigen, etUbicacionDestino;
    private Button btnRegistrar;
    private TextView tvCiudadOrigen, tvCiudadDestino, tvBuscarDireccion, tvBuscarMapa, tvDireccionOrigen, tvDireccionDestino;
    private EditText etFechaRegistro;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RegisterFleteContract.RegisterFletePresenter presenterFletes;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<Intent> autocompleteLauncher;
    private ActivityResultLauncher<Intent> autocompleteLauncherDireccion;
    private String ciudadSeleccionadaOrigen = "";
    private LatLngBounds ciudadBoundsSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_registrar_fletes, findViewById(R.id.fragment_container_company));

        // Inicializar mapa o fusedLocationClient aquí después de solicitar permisos
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Solicitar permisos si no están concedidos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        presenterFletes = new RegisterFletePresenter(this);

        etUbicacionOrigen = findViewById(R.id.etUbicacionOrigen);
        etUbicacionDestino = findViewById(R.id.etUbicacionDestino);
        etFechaRegistro = findViewById(R.id.etFechaRegistro);

        btnRegistrar = findViewById(R.id.btnRegistrarFlete);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        autocompleteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        Log.i("PLACE", "Ciudad seleccionada: " + place.getName());
                        String ciudad = place.getName();
                        String placeId = place.getId();

                        ciudadSeleccionadaOrigen = ciudad;

                        if (tvCiudadOrigen != null) {
                            tvCiudadOrigen.setText(ciudad);
                        }

                        if (tvCiudadDestino != null) {
                            tvCiudadDestino.setText(ciudad);
                        }

                        if (placeId != null) {
                            obtenerBoundsDeCiudad(placeId);
                        }

                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && result.getData() != null) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        Log.e("AUTOCOMPLETE_ERROR", status.getStatusMessage());
                    }
                }
        );

        autocompleteLauncherDireccion = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        Log.i("BUSCADOR_DIRECCION", "Dirección seleccionada: " + place.getAddress());

                        // Colocar la dirección en el campo
                        tvBuscarDireccion.setText(place.getAddress());
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && result.getData() != null) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        Log.e("BUSCADOR_DIRECCION", "Error: " + status.getStatusMessage());
                    }
                }
        );

        etUbicacionOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarBottomSheetUbicacionOrigen();
            }
        });

        etUbicacionDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarBottomSheetUbicacionDestino();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterFletes.onRegistrarFlete();
            }
        });

        etFechaRegistro.setOnClickListener(v -> {
            final Calendar calendario = Calendar.getInstance();
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                // Cuando el usuario selecciona una fecha
                String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                etFechaRegistro.setText(fechaSeleccionada);
            }, año, mes, dia);

            datePickerDialog.show();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
    }

    @Override
    public void irARegistrarFlete() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes capturar los valores y enviarlos al servidor o guardarlos localmente
                Toast.makeText(RegisterFletesActivity.this, "Este es el boton de registrar fletes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mostrarBottomSheetUbicacionOrigen() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ubicacion_origen, null);
        bottomSheetDialog.setContentView(view);

        tvCiudadOrigen = view.findViewById(R.id.tvCiudadOrigen);
        Button btnHecho = view.findViewById(R.id.btnHechoOrigen);

        tvDireccionOrigen = view.findViewById(R.id.tvDireccionOrigen);

        tvCiudadOrigen.setOnClickListener(v -> lanzarAutocompleteCiudad());
        btnHecho.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvDireccionOrigen.setOnClickListener(v -> mostrarBottomSheetSeleccionarDireccion());
        bottomSheetDialog.show();
    }

    public void mostrarBottomSheetUbicacionDestino() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ubicacion_destino, null);
        bottomSheetDialog.setContentView(view);

        tvCiudadDestino = view.findViewById(R.id.tvCiudadDestino);
        tvDireccionDestino = view.findViewById(R.id.tvDireccionDestino);
        Button btnHecho = view.findViewById(R.id.btnHechoDestino);

        tvCiudadDestino.setOnClickListener(v -> lanzarAutocompleteCiudad());
        btnHecho.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvDireccionDestino.setOnClickListener(v -> mostrarBottomSheetSeleccionarDireccion());
        bottomSheetDialog.show();
    }

    private void lanzarAutocompleteCiudad() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAMvlMwn_Z7w6nOmO-QpGoTP9O6Hb6Ypls");
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.CITIES)  // Solo ciudades
                .setCountries(Arrays.asList("CO")) // Solo Colombia
                .build(this);

        autocompleteLauncher.launch(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegúrate de cerrar la API de Places cuando ya no la necesites.
    }

    private void lanzarBuscadorDireccion() {
        if (ciudadSeleccionadaOrigen.isEmpty() || ciudadBoundsSeleccionada == null) {
            Toast.makeText(this, "Primero selecciona una ciudad", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                //.setTypeFilter(TypeFilter.ADDRESS)
                .setCountries(Arrays.asList("CO"))
                .setLocationRestriction(RectangularBounds.newInstance(ciudadBoundsSeleccionada))
                .build(this);

        autocompleteLauncherDireccion.launch(intent);
    }


    public void mostrarBottomSheetSeleccionarDireccion() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_seleccionar_direccion, null);
        bottomSheetDialog.setContentView(view);

        tvBuscarDireccion = view.findViewById(R.id.tvBuscarDireccion);
        tvBuscarMapa = view.findViewById(R.id.tvBuscarMapa);
        Button btnHechoDireccion = view.findViewById(R.id.btnHechoDireccion);

        btnHechoDireccion.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();

        tvBuscarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarBottomSheetMapaDireccion();
            }
        });

        tvBuscarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarBuscadorDireccion();
            }
        });
        //tvBuscarDireccion.setOnClickListener(v -> lanzarBuscadorDireccion());

    }

    private void mostrarBottomSheetMapaDireccion() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_mapa_direccion, null);
        dialog.setContentView(view);
        dialog.show();

        MapView mapView = view.findViewById(R.id.mapViewDireccion);
        ImageView ivMarkerCenter = view.findViewById(R.id.iv_marker_center);
        Button btnConfirmar = view.findViewById(R.id.btnConfirmarDireccion);
        TextView tvDireccionMapa = view.findViewById(R.id.tvDireccionMapa);

        mapView.onCreate(null);
        mapView.onResume();

        // Obtener coordenadas desde Geocoder según ciudadSeleccionadaOrigen
        LatLng[] latLngInicial = {new LatLng(7.8891, -72.4967)}; // Valor por defecto en caso de error

        if (!ciudadSeleccionadaOrigen.isEmpty()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(ciudadSeleccionadaOrigen, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    latLngInicial[0] = new LatLng(address.getLatitude(), address.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mapView.getMapAsync(googleMap -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngInicial[0], 15));

            final LatLng[] currentCenter = {latLngInicial[0]};

            googleMap.setOnCameraIdleListener(() -> {
                LatLng center = googleMap.getCameraPosition().target;
                currentCenter[0] = center;

                // Animación ligera de rebote
                ivMarkerCenter.animate()
                        .translationY(-20f)
                        .setDuration(150)
                        .withEndAction(() -> ivMarkerCenter.animate()
                                .translationY(0f)
                                .setDuration(150)
                                .start())
                        .start();

                // Actualizar dirección en la card
                String direccion = obtenerDireccion(center.latitude, center.longitude);
                tvDireccionMapa.setText(direccion);

                tvDireccionMapa.animate()
                        .alpha(0f)
                        .setDuration(150)
                        .withEndAction(() -> {
                            tvDireccionMapa.setText(direccion);
                            tvDireccionMapa.animate().alpha(1f).setDuration(150).start();
                        }).start();

                Log.i("MAPA", "Centro actual: " + center.latitude + ", " + center.longitude);
            });

            btnConfirmar.setOnClickListener(v -> {
                LatLng ubicacionSeleccionada = currentCenter[0];
                String direccion = obtenerDireccion(ubicacionSeleccionada.latitude, ubicacionSeleccionada.longitude);
                Log.i("MAPA", "Dirección seleccionada: " + direccion);

                etUbicacionOrigen.setText(direccion);
                tvBuscarDireccion.setText(direccion);
                tvDireccionOrigen.setText(direccion);

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private String obtenerDireccion(double latitud, double longitud) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocation(latitud, longitud, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                Address direccion = direcciones.get(0);

                // Limpiar cualquier # en vía y número
                String via = direccion.getThoroughfare() != null ? direccion.getThoroughfare().replace("#", "").trim() : "";
                String numero = direccion.getSubThoroughfare() != null ? direccion.getSubThoroughfare().replace("#", "").trim() : "";

                if (via.isEmpty() && numero.isEmpty()) {
                    return "Dirección no disponible";
                }

                // Si ambos existen, concatenarlos con un solo #
                if (!via.isEmpty() && !numero.isEmpty()) {
                    return via + " #" + numero;
                } else if (!via.isEmpty()) {
                    return via;
                } else {
                    return numero;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Dirección no disponible";
    }


    private void obtenerBoundsDeCiudad(String placeId) {
        PlacesClient placesClient = Places.createClient(this);
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.VIEWPORT);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, fields).build();

        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    Place place = response.getPlace();
                    ciudadBoundsSeleccionada = place.getViewport();

                    if (ciudadBoundsSeleccionada == null && place.getLatLng() != null) {
                        // Si no viene viewport, creas un bound manual de 5km a la redonda, por ejemplo:
                        double lat = place.getLatLng().latitude;
                        double lng = place.getLatLng().longitude;
                        double delta = 0.045; // ~5km

                        ciudadBoundsSeleccionada = new LatLngBounds(
                                new LatLng(lat - delta, lng - delta),
                                new LatLng(lat + delta, lng + delta)
                        );
                    }

                    Log.d("PLACES", "Bounds obtenidos: " + ciudadBoundsSeleccionada);
                })
                .addOnFailureListener(e -> Log.e("PLACES", "Error al obtener bounds: " + e.getMessage()));
    }
}

