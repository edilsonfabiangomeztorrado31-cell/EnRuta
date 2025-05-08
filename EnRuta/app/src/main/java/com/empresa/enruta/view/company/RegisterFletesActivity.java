package com.empresa.enruta.view.company;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import com.empresa.enruta.R;
import com.empresa.enruta.contract.company.RegisterFleteContract;
import com.empresa.enruta.presenter.company.RegisterFletePresenter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

public class RegisterFletesActivity extends CompanyMenuView implements RegisterFleteContract.RegisterFleteView {

    private EditText etUbicacionOrigen, etUbicacionDestino;
    private Button btnRegistrar;
    private TextView tvCiudadOrigen;
    private EditText tvBuscarCiudad;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RegisterFleteContract.RegisterFletePresenter presenterFletes;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_registrar_fletes, findViewById(R.id.fragment_container_company));

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.google.android.gms", 0);
            Log.d("PlayServicesVersion", "Google Play Services version: " + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        LinearLayout contenedorOrigenUbicacion = findViewById(R.id.layoutLugarOrigenUbicacion);
        contenedorOrigenUbicacion.setOnClickListener(v -> mostrarBottomSheetUbicacionOrigen());

        LinearLayout contenedorBuscarCiudad = findViewById(R.id.layoutBuscarCiudad);
        contenedorBuscarCiudad.setOnClickListener(v -> mostrarBottomSheetBuscarCiudad());

//        LinearLayout contenedorDestinoUbicacion = findViewById(R.id.layoutLugarDestinoUbicacion);
//        contenedorDestinoUbicacion.setOnClickListener(v -> mostrarBottomSheetUbicacionDestino());


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

        btnRegistrar = findViewById(R.id.btnRegistrarFlete);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }


        etUbicacionOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarBottomSheetUbicacionOrigen();
            }
        });

        etUbicacionDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mostrarBottomSheetUbicacionDestino();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterFletes.onRegistrarFlete();
            }
        });
    }

    private void abrirGoogleMaps(String ubicacion) {
        if (!ubicacion.isEmpty()) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(ubicacion));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
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

    private void configurarMapa(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Activa el botón de ubicación y la capa de ubicación
        googleMap.setMyLocationEnabled(true);

        // Obtener ubicación actual
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15));
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

        TextView tvDireccion = view.findViewById(R.id.tvDireccionOrigen);

        tvCiudadOrigen.setOnClickListener(v -> {
            Log.d("DEBUG", "Click en Ciudad Origen");
            mostrarBottomSheetBuscarCiudad();
        });

        btnHecho.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    public void mostrarBottomSheetBuscarCiudad() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_buscar_ciudad, null);
        bottomSheetDialog.setContentView(view);

        tvBuscarCiudad = view.findViewById(R.id.tvBuscarCiudad);
        tvBuscarCiudad.setOnClickListener(v -> lanzarAutocomplete());

        Button btnHecho = view.findViewById(R.id.btnHechoDestino);
        btnHecho.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    private void lanzarAutocomplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAMvlMwn_Z7w6nOmO-QpGoTP9O6Hb6Ypls");
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.CITIES)  // Solo ciudades
                .setCountries(Arrays.asList("CO")) // Solo Colombia
                .build(this);

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE); // Define AUTOCOMPLETE_REQUEST_CODE como constante
    }

//    public void mostrarBottomSheetUbicacionDestino() {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ubicacion_destino, null);
//        bottomSheetDialog.setContentView(view);
//
//        TextView tvCiudad = view.findViewById(R.id.tvCiudadDestino);
//        TextView tvDireccion = view.findViewById(R.id.tvDireccionDestino);
//        Button btnHecho = view.findViewById(R.id.btnHechoDestino);
//
//        // Puedes setear aquí valores si los tienes
//        tvCiudad.setText("Cúcuta");
//        tvDireccion.setText("Cra. 8 # 9-92");
//
//        btnHecho.setOnClickListener(v -> bottomSheetDialog.dismiss());
//
//        bottomSheetDialog.show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("PLACE", "Ciudad seleccionada: " + place.getName());
                String ciudad = place.getName();

                // Actualizar los campos visibles en los BottomSheets
                if (tvBuscarCiudad != null) {
                    tvBuscarCiudad.setText(ciudad);
                }
                if (tvCiudadOrigen != null) {
                    tvCiudadOrigen.setText(ciudad);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("AUTOCOMPLETE_ERROR", status.getStatusMessage());
            }
        }
    }
}

