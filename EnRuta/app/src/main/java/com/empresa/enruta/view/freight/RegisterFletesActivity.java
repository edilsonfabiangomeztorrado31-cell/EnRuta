package com.empresa.enruta.view.freight;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.empresa.enruta.contract.freight.RegisterFleteContract;
import com.empresa.enruta.presenter.freight.RegisterFletePresenter;
import com.empresa.enruta.view.company.CompanyMenuView;
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

public class RegisterFletesActivity extends CompanyMenuView implements RegisterFleteContract.View {

    private EditText etUbicacionOrigen, etUbicacionDestino, etTipoCarga, etPrecio, etPeso;
    public String direccionFinal = "";
    private Button btnRegistrar;
    private TextView tvCiudadOrigen, tvCiudadDestino, tvBuscarDireccionOrigen, tvBuscarMapaOrigen, tvDireccionOrigen, tvDireccionDestino, tvBuscarDireccionDestino, tvBuscarMapaDestino;
    private EditText etFechaRegistro;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RegisterFleteContract.Presenter presenterFletes;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<Intent> autocompleteLauncherCiudadOrigen, autocompleteLauncherCiudadDestino;
    private ActivityResultLauncher<Intent> autocompleteLauncherDireccionOrigen, autocompleteLauncherDireccionDestino;
    private String ciudadSeleccionadaOrigen = "";
    private String ciudadSeleccionadaDestino = "";
    private LatLngBounds ciudadBoundsSeleccionada;
    private static PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_registrar_fletes, findViewById(R.id.fragment_container_company));

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }

        placesClient = Places.createClient(this);

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
        etTipoCarga = findViewById(R.id.etTipoCarga);
        etPrecio = findViewById(R.id.etPrecio);
        etPeso = findViewById(R.id.etPeso);
        etFechaRegistro = findViewById(R.id.etFechaRegistro);

        btnRegistrar = findViewById(R.id.btnRegistrarFlete);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        autocompleteLauncherCiudadOrigen = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        presenterFletes.onCiudadOrigenSeleccionada(place);
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && result.getData() != null) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        presenterFletes.onErrorAutocomplete(status);
                    }
                }
        );

        autocompleteLauncherCiudadDestino = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        Log.i("PLACE", "Ciudad seleccionada: " + place.getName());

                        presenterFletes.onCiudadDestinoSeleccionada(place);
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && result.getData() != null) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        presenterFletes.onErrorAutocomplete(status);
                    }
                }
        );

        autocompleteLauncherDireccionOrigen = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        String direccionCompleta = place.getAddress();

                        String direccionCorta = obtenerDireccionSinCiudadOrigen(direccionCompleta, ciudadSeleccionadaOrigen);
                        direccionFinal = direccionCorta + ", " + ciudadSeleccionadaOrigen;

                        Log.i("BUSCARDOR_DIRECCION_ORIGEN", "Dirección seleccionada: " + direccionFinal);
                        presenterFletes.onDireccionOrigenSeleccionada(direccionFinal, place.getLatLng());

                        if (etUbicacionOrigen != null) {
                            etUbicacionOrigen.setText(ciudadSeleccionadaOrigen + ", " + direccionCorta);
                        }
                        // Aquí actualizamos el TextView si ya está creado
                        if (tvDireccionOrigen != null) {
                            tvDireccionOrigen.setText(direccionCorta);
                        }

                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && result.getData() != null) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        presenterFletes.onErrorDireccionOrigen(status);
                    }
                }
        );

        autocompleteLauncherDireccionDestino = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        String direccionCompleta = place.getAddress();

                        String direccionCorta = obtenerDireccionSinCiudadDestino(direccionCompleta, ciudadSeleccionadaOrigen);
                        direccionFinal = direccionCorta + ", " + ciudadSeleccionadaOrigen;

                        Log.i("BUSCARDOR_DIRECCION_ORIGEN", "Dirección seleccionada: " + direccionFinal);
                        presenterFletes.onDireccionOrigenSeleccionada(direccionFinal, place.getLatLng());

                        if (etUbicacionDestino != null) {
                            etUbicacionDestino.setText(ciudadSeleccionadaDestino + ", " + direccionCorta);
                        }
                        // Aquí actualizamos el TextView si ya está creado
                        if (tvDireccionDestino != null) {
                            tvDireccionDestino.setText(direccionCorta);
                        }

                        if (tvBuscarDireccionDestino != null) {
                            tvBuscarDireccionDestino.setText(direccionCorta);
                        }

                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && result.getData() != null) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        Log.e("BUSCADOR_DIRECCION", "Error: " + status.getStatusMessage());
                        presenterFletes.onErrorDireccionDestino(status);
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

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterFletes.onRegistrarFlete(
                        etUbicacionOrigen.getText().toString(),
                        etUbicacionDestino.getText().toString(),
                        etTipoCarga.getText().toString(),
                        etPrecio.getText().toString(),
                        etPeso.getText().toString(),
                        etFechaRegistro.getText().toString());
            }
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
    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registroExitoso(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
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

    @Override
    public void mostrarCiudadOrigen(String ciudad) {
        ciudadSeleccionadaOrigen = ciudad;
        tvCiudadOrigen.setText(ciudad);
    }

    @Override
    public void procesarBoundsCiudad(String placeId) {
        obtenerBoundsDeCiudad(placeId); // método local que ya tienes
    }

    @Override
    public void mostrarError(String mensaje) {
        Log.e("AUTOCOMPLETE_ERROR", mensaje);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarCiudadDestino(String ciudad) {
        ciudadSeleccionadaDestino = ciudad;
        tvCiudadDestino.setText(ciudad);
    }

    @Override
    public void mostrarDireccionOrigen(String direccion) {
        tvBuscarDireccionOrigen.setText(direccion);
    }

    @Override
    public void mostrarDireccionDestino(String direccion) {
        tvBuscarDireccionDestino.setText(direccion);
    }

    @Override
    public void mostrarErrorDireccion(String mensaje) {
        Log.e("BUSCADOR_DIRECCION", mensaje);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarBottomSheetUbicacionOrigen() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ubicacion_origen, null);
        bottomSheetDialog.setContentView(view);

        tvCiudadOrigen = view.findViewById(R.id.tvCiudadOrigen);
        Button btnHecho = view.findViewById(R.id.btnHechoOrigen);
        tvDireccionOrigen = view.findViewById(R.id.tvDireccionOrigen);

        // Delegar acciones al Presenter
        tvCiudadOrigen.setOnClickListener(v -> presenterFletes.onClickSeleccionarCiudadOrigen());
        tvDireccionOrigen.setOnClickListener(v -> presenterFletes.onClickSeleccionarDireccionOrigen());

            btnHecho.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    public void mostrarBottomSheetUbicacionDestino() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ubicacion_destino, null);
        bottomSheetDialog.setContentView(view);

        tvCiudadDestino = view.findViewById(R.id.tvCiudadDestino);
        tvDireccionDestino = view.findViewById(R.id.tvDireccionDestino);
        Button btnHecho = view.findViewById(R.id.btnHechoDestino);

        // Delegar acciones al Presenter
        tvCiudadDestino.setOnClickListener(v -> presenterFletes.onClickSeleccionarCiudadDestino());
        tvDireccionDestino.setOnClickListener(v -> presenterFletes.onClickSeleccionarDireccionDestino());

        btnHecho.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    public void lanzarAutocompleteCiudadDestino() {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.CITIES)  // Solo ciudades
                .setCountries(Arrays.asList("CO")) // Solo Colombia
                .build(this);

        autocompleteLauncherCiudadDestino.launch(intent);
    }

    public void lanzarAutocompleteCiudadOrigen() {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.CITIES)  // Solo ciudades
                .setCountries(Arrays.asList("CO")) // Solo Colombia
                .build(this);

        autocompleteLauncherCiudadOrigen.launch(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegúrate de cerrar la API de Places cuando ya no la necesites.
    }

    @Override
    public void mostrarBottomSheetBuscarDireccionOrigen() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_seleccionar_direccion_origen, null);
        bottomSheetDialog.setContentView(view);

        tvBuscarDireccionOrigen = view.findViewById(R.id.tvBuscarDireccionOrigen);
        tvBuscarMapaOrigen = view.findViewById(R.id.tvBuscarMapaOrigen);
        Button btnHechoDireccion = view.findViewById(R.id.btnHechoDireccionOrigen);

        // Delegar al Presenter
        tvBuscarMapaOrigen.setOnClickListener(v -> presenterFletes.onClickBuscarMapaOrigen());
        tvBuscarDireccionOrigen.setOnClickListener(v -> presenterFletes.onClickBuscarDireccionOrigen());

        btnHechoDireccion.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    @Override
    public void mostrarBottomSheetBuscarDireccionDestino() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_seleccionar_direccion_destino, null);
        bottomSheetDialog.setContentView(view);

        tvBuscarDireccionDestino = view.findViewById(R.id.tvBuscarDireccionDestino);
        tvBuscarMapaDestino = view.findViewById(R.id.tvBuscarMapaDestino);
        Button btnHechoDireccionDestino = view.findViewById(R.id.btnHechoDireccionDestino);

        tvBuscarMapaDestino.setOnClickListener(v -> presenterFletes.onClickBuscarMapaDestino());
        tvBuscarDireccionDestino.setOnClickListener(v -> presenterFletes.onClickBuscarDireccionDestino());

        btnHechoDireccionDestino.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    public void lanzarBuscadorDireccionOrigen() {
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

        autocompleteLauncherDireccionOrigen.launch(intent);
    }

    public void mostrarBottomSheetMapaDireccionOrigen() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_mapa_direccion_origen, null);
        dialog.setContentView(view);
        dialog.show();

        MapView mapView = view.findViewById(R.id.mapViewDireccionOrigen);
        ImageView ivMarkerCenter = view.findViewById(R.id.iv_marker_center_origen);
        Button btnConfirmar = view.findViewById(R.id.btnConfirmarDireccionOrigen);
        TextView tvDireccionMapa = view.findViewById(R.id.tvDireccionMapaOrigen);

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
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(ubicacionSeleccionada.latitude, ubicacionSeleccionada.longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        String direccionSolo = obtenerDireccion(ubicacionSeleccionada.latitude, ubicacionSeleccionada.longitude);

                        // Mostrar solo dirección en tvBuscarDireccionOrigen
                        tvBuscarDireccionOrigen.setText(direccionSolo);

                        // Concatenar ciudad ya seleccionada + dirección, y mostrar en etUbicacionOrigen y tvDireccionOrigen
                        String direccionCompleta = ciudadSeleccionadaOrigen + " - " + direccionSolo;

                        etUbicacionOrigen.setText(direccionCompleta);
                        tvDireccionOrigen.setText(direccionCompleta);

                        Log.i("MAPA", "Dirección seleccionada: " + direccionCompleta);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    public void lanzarBuscadorDireccionDestino() {
        if (ciudadSeleccionadaDestino.isEmpty() || ciudadBoundsSeleccionada == null) {
            Toast.makeText(this, "Primero selecciona una ciudad", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                //.setTypeFilter(TypeFilter.ADDRESS)
                .setCountries(Arrays.asList("CO"))
                .setLocationRestriction(RectangularBounds.newInstance(ciudadBoundsSeleccionada))
                .build(this);

        autocompleteLauncherDireccionDestino.launch(intent);
    }

    public void mostrarBottomSheetMapaDireccionDestino() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_mapa_direccion_destino, null);
        dialog.setContentView(view);
        dialog.show();

        MapView mapView = view.findViewById(R.id.mapViewDireccionDestino);
        ImageView ivMarkerCenter = view.findViewById(R.id.iv_marker_center_destino);
        Button btnConfirmar = view.findViewById(R.id.btnConfirmarDireccionDestino);
        TextView tvDireccionMapa = view.findViewById(R.id.tvDireccionMapaDestino);

        mapView.onCreate(null);
        mapView.onResume();

        // Obtener coordenadas desde Geocoder según ciudadSeleccionadaOrigen
        LatLng[] latLngInicial = {new LatLng(7.8891, -72.4967)}; // Valor por defecto en caso de error

        if (!ciudadSeleccionadaDestino.isEmpty()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(ciudadSeleccionadaDestino, 1);
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
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(ubicacionSeleccionada.latitude, ubicacionSeleccionada.longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        String direccionSolo = obtenerDireccion(ubicacionSeleccionada.latitude, ubicacionSeleccionada.longitude);

                        // Mostrar solo dirección en tvBuscarDireccionOrigen
                        tvBuscarDireccionDestino.setText(direccionSolo);

                        // Concatenar ciudad ya seleccionada + dirección, y mostrar en etUbicacionOrigen y tvDireccionOrigen
                        String direccionCompleta = ciudadSeleccionadaDestino + " - " + direccionSolo;

                        etUbicacionDestino.setText(direccionCompleta);
                        tvDireccionDestino.setText(direccionCompleta);

                        Log.i("MAPA", "Dirección seleccionada: " + direccionCompleta);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.VIEWPORT
        );

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, fields).build();

        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    Place place = response.getPlace();
                    ciudadBoundsSeleccionada = place.getViewport();

                    if (ciudadBoundsSeleccionada == null && place.getLatLng() != null) {
                        // Si no viene viewport, creas un bound manual de 5km a la redonda
                        double lat = place.getLatLng().latitude;
                        double lng = place.getLatLng().longitude;
                        double delta = 0.045; // Aproximadamente 5km

                        ciudadBoundsSeleccionada = new LatLngBounds(
                                new LatLng(lat - delta, lng - delta),
                                new LatLng(lat + delta, lng + delta)
                        );
                    }

                    Log.d("PLACES", "Bounds obtenidos: " + ciudadBoundsSeleccionada);
                })
                .addOnFailureListener(e -> Log.e("PLACES", "Error al obtener bounds: " + e.getMessage()));
    }

    private String obtenerDireccionSinCiudadOrigen(String direccionCompleta, String ciudadSeleccionada) {
        if (direccionCompleta == null) return "";

        if (direccionCompleta.contains(ciudadSeleccionada)) {
            return direccionCompleta.split(ciudadSeleccionada)[0].replaceAll(",\\s*$", "").trim();
        }

        String[] partes = direccionCompleta.split(",");
        return partes.length > 0 ? partes[0].trim() : direccionCompleta.trim();
    }


    private String obtenerDireccionSinCiudadDestino(String direccionCompleta, String ciudadSeleccionada) {
        if (direccionCompleta == null) return "";

        // Ejemplo: "Cra. 10 #20-30, Medellín, Antioquia, Colombia"
        // Vamos a tomar solo lo que aparece antes de la ciudad
        if (direccionCompleta.contains(ciudadSeleccionada)) {
            return direccionCompleta.split(ciudadSeleccionada)[0].replaceAll(",\\s*$", "").trim();
        }

        // Alternativa: tomar solo la primera parte antes de la primera coma
        String[] partes = direccionCompleta.split(",");
        return partes.length > 0 ? partes[0].trim() : direccionCompleta.trim();
    }

    @Override
    public void limpiarCampos() {
        etUbicacionOrigen.setText("");
        etUbicacionDestino.setText("");
        etTipoCarga.setText("");
        etPrecio.setText("");
        etPeso.setText("");
        etFechaRegistro.setText(""); // si estás mostrando la fecha en un TextView
    }
}

