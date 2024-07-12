package Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ziac.wheelzcustomer.MainActivty;
import com.ziac.wheelzcustomer.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ModelClasses.Global;

public class DashboardFragment extends Fragment {

    CardView Bookservice, Servicehistory, Vehdocuments, latestnews;
    FragmentManager fragmentManager;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_LOCATION_PERMISSIONS = 2;
    private FusedLocationProviderClient fusedLocationClient;
    String statecode, citycode, currentLocationString, fullAddress, sublocality;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Bookservice = view.findViewById(R.id.servicebookCD);
        Servicehistory = view.findViewById(R.id.servicehistoryCD);
        Vehdocuments = view.findViewById(R.id.vehdocumentsCD);
        latestnews = view.findViewById(R.id.latestnewsCD);

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS);
        } else {
            enableLocation();
        }


        Bookservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DealersFragment dealersFragment = new DealersFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, dealersFragment);
                fragmentTransaction.commit();

                ((MainActivty) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_dealers);
            }
        });

        Servicehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyVehcileFragment myVehcileFragment = new MyVehcileFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
                fragmentTransaction.commit();

                ((MainActivty) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_vehicles);

            }
        });

        Vehdocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVehcileFragment myVehcileFragment = new MyVehcileFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
                fragmentTransaction.commit();

                ((MainActivty) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_vehicles);
            }
        });


        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            } else {
                Toast.makeText(requireActivity(), "Location permissions are required to use this feature", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void enableLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(500);

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());

        task.addOnSuccessListener(requireActivity(), locationSettingsResponse -> {

        });

        task.addOnFailureListener(requireActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                try {
                    resolvableApiException.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }




}