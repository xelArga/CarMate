package com.example.carmate;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap){
        LatLng kelowna = new LatLng(49.8801, -119.4436);
        LatLng ubco = new LatLng(49.9394, -119.3948);
        LatLng lakeCountry = new LatLng(50.0537, -119.4106);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(kelowna).title("Kelowna"));
        googleMap.addMarker(new MarkerOptions().position(ubco).title("UBCO"));
        googleMap.addMarker(new MarkerOptions().position(lakeCountry).title("Lake Country"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kelowna, 10));
        googleMap.getUiSettings().setZoomControlsEnabled(true);

    }
}