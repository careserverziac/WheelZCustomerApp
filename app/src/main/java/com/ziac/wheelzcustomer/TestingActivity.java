package com.ziac.wheelzcustomer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TestingActivity extends AppCompatActivity {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_testing);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button openMapButton = findViewById(R.id.openMapButton);
        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permission
                if (ContextCompat.checkSelfPermission(TestingActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Request the permission
                    ActivityCompat.requestPermissions(TestingActivity.this,
                            new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // Permission has been granted
                    openMap();
                }
            }
        });
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                openMap();
            } else {
                // Permission denied

            }
        }
    }

    // Open the map
    private void openMap() {


        // Latitude and Longitude of India Gate
        double latitude = 28.6129;
        double longitude = 77.2295;

        // Create a Uri from latitude and longitude
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=India+Gate");

        // Create an Intent from gmmIntentUri
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        // Set the package of Google Maps
        mapIntent.setPackage("com.google.android.apps.maps");


        // Attempt to start Google Maps activity
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            Toast.makeText(this, "Latitude and Longitude of India Gate", Toast.LENGTH_SHORT).show();

            startActivity(mapIntent);
        }
    }
}