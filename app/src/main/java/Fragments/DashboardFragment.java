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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ziac.wheelzcustomer.MainActivity;
import com.ziac.wheelzcustomer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import AdapterClass.CategoryAdapter;
import ModelClasses.CategoryModel;
import ModelClasses.Global;

public class DashboardFragment extends Fragment {

    CardView Bookservice, Servicehistory, Vehdocuments, Pre_own_veh;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String lattitude, longitude;
    FusedLocationProviderClient client;
    ImageView ProfileIcon;
    String vehtype = "";
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryList;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard2, container, false);

        Bookservice = view.findViewById(R.id.bookServiceCard);
        Servicehistory = view.findViewById(R.id.serviceHistoryCard);
        Vehdocuments = view.findViewById(R.id.vehicleDocumentsCard);
       // ServiceList = view.findViewById(R.id.bookingCD);
        Pre_own_veh = view.findViewById(R.id.preOwnedCard);
        ProfileIcon = view.findViewById(R.id.profile_icon);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        vehtype = Global.sharedPreferences.getString("vtypecode", "");

        // Initialize Category RecyclerView
       /* categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(),3, LinearLayoutManager.VERTICAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);*/


        // Prepare category data
      //  prepareCategoryData();

       /* categoryAdapter = new CategoryAdapter(requireActivity(), categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);*/

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            getcurrentlocation();

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        Bookservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DealersFragment dealersFragment = new DealersFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, dealersFragment);
                fragmentTransaction.commit();

                ((MainActivity) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_dealers);
            }
        });

        Servicehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyVehcileFragment myVehcileFragment = new MyVehcileFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
                fragmentTransaction.commit();

                ((MainActivity) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_vehicles);

            }
        });

        Vehdocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVehcileFragment myVehcileFragment = new MyVehcileFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
                fragmentTransaction.commit();

                ((MainActivity) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_vehicles);
            }
        });

        ProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, profileFragment);
                fragmentTransaction.commit();

               // ((MainActivity) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_vehicles);
            }
        });
        /*ServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServiceListFragment serviceListFragment = new ServiceListFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, serviceListFragment);
                fragmentTransaction.commit();

                ((MainActivity) requireActivity()).setBottomNavigationViewSelectedItem(R.id.dashboard);
            }
        });*/

        Pre_own_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vehtype.isEmpty()) {
                    PreferenceFragment preferenceFragment = new PreferenceFragment();
                    fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout, preferenceFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    PreOwnedVehicleFragment preOwnedVehicleFragment = new PreOwnedVehicleFragment();
                    fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout, preOwnedVehicleFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }

            }
        });
        return view;
    }

  /*  private void prepareCategoryData() {
        categoryList = new ArrayList<>();
        categoryList.add(new CategoryModel("Test Drive list", R.drawable.testdrive));
        categoryList.add(new CategoryModel("Book Test Drive", R.drawable.booktextdrive));
        categoryList.add(new CategoryModel("Watch Videos", R.drawable.youtube));

    }*/

    @SuppressLint("MissingPermission")
    private void getcurrentlocation() {

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {


                    Location location = task.getResult();
                    if (location != null) {

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();


                                lattitude = String.valueOf(location1.getLatitude());
                                longitude = String.valueOf(location1.getLongitude());


                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });

        } else {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getcurrentlocation();
        } else {
        }
    }
}