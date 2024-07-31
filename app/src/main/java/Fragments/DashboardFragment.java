package Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ziac.wheelzcustomer.MainActivty;
import com.ziac.wheelzcustomer.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ModelClasses.Global;

public class DashboardFragment extends Fragment {

    CardView Bookservice, Servicehistory, Vehdocuments, ServiceList;
    FragmentManager fragmentManager;
    private static final int REQUEST_CHECK_SETTINGS = 11;
    private static final int REQUEST_LOCATION_PERMISSIONS = 22;
    String lattitude, longitude, currentLocationString, fullAddress, sublocality;

    FusedLocationProviderClient client;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Bookservice = view.findViewById(R.id.servicebookCD);
        Servicehistory = view.findViewById(R.id.servicehistoryCD);
        Vehdocuments = view.findViewById(R.id.vehdocumentsCD);
        ServiceList = view.findViewById(R.id.bookingCD);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        client=LocationServices.getFusedLocationProviderClient(getActivity());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            getcurrentlocation();

        } else {
           requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }


        /*checkPermissionsAndEnableLocation();*/


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
        ServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServiceListFragment serviceListFragment = new ServiceListFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, serviceListFragment);
                fragmentTransaction.commit();

                ((MainActivty) requireActivity()).setBottomNavigationViewSelectedItem(R.id.dashboard);
            }
        });


        return view;

    }

    @SuppressLint("MissingPermission")
    private void getcurrentlocation() {

        LocationManager locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {


                    Location location=task.getResult();
                    if (location !=null){

                     /*  lattitude=String.valueOf(location.getLatitude());
                       longitude=String.valueOf(location.getLongitude());
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("lattitude", lattitude);
                        Global.editor.putString("longitude", longitude);
                        Global.editor.commit();*/

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                       // saveLocationData(latitude, longitude);


                    }else {
                        LocationRequest locationRequest=new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback=new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1= locationResult.getLastLocation();


                                lattitude=String.valueOf(location1.getLatitude());
                                longitude=String.valueOf(location1.getLongitude());


                            }
                        };
                        client.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });

        }else {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void saveLocationData(double latitude, double longitude) {
        String latitudeString = String.valueOf(latitude);
        String longitudeString = String.valueOf(longitude);

        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String sublocality = address.getSubLocality();
                String fullAddress = address.getAddressLine(0);

                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("latitude", latitudeString);
                Global.editor.putString("longitude", longitudeString);
                Global.editor.putString("sublocality", sublocality);
                Global.editor.putString("fullAddress", fullAddress);
                Global.editor.commit();
            } else {
                Log.e("DashboardFragment", "Unable to get address");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DashboardFragment", "Geocoder service not available");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && (grantResults.length >0 ) && (grantResults[0] + grantResults[1]  == PackageManager.PERMISSION_GRANTED)) {
            getcurrentlocation();
        }else {
           // Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }



    }

  /*  private void enableLocation() {
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
    }*/




}