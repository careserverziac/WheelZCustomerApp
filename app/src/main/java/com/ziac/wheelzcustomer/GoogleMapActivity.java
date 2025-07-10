package com.ziac.wheelzcustomer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;
    private String currentLocationString;
    private LinearLayout shareLocationButton;
    private LatLng selectedLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_google_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        shareLocationButton = findViewById(R.id.btn_share_location);

        shareLocationButton.setOnClickListener(v -> {
            shareLocation();
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setOnMapClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                selectedLatLng = currentLocation; // Set initial location
                                currentLocationString = currentLocation.latitude + ", " + currentLocation.longitude;
                                myMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        myMap.clear();
        myMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        selectedLatLng = latLng;
    }

    private void shareLocation() {
        if (selectedLatLng != null) {
            Geocoder geocoder = new Geocoder(GoogleMapActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(selectedLatLng.latitude, selectedLatLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    String sublocality = address.getSubLocality();
                    String fullAddress = address.getAddressLine(0);

                    String uri = "http://maps.google.com/maps?saddr=" + selectedLatLng.latitude + "," + selectedLatLng.longitude;

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currentLocationString", selectedLatLng.latitude + ", " + selectedLatLng.longitude);
                    editor.putString("currentStreetName", sublocality);
                    editor.putString("currentFullAddress", fullAddress);
                    editor.putString("locationuri", uri);
                    editor.apply();

                    finish();
                } else {
                    Toast.makeText(GoogleMapActivity.this, "Unable to get address", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(GoogleMapActivity.this, "Geocoder service not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(GoogleMapActivity.this, "Location not selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}