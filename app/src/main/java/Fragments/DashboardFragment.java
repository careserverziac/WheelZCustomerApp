package Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
import com.squareup.picasso.Picasso;
import com.ziac.wheelzcustomer.MainActivity;
import com.ziac.wheelzcustomer.ProfileActivity;
import com.ziac.wheelzcustomer.R;
import com.ziac.wheelzcustomer.ServiceHistoryActivity;
import com.ziac.wheelzcustomer.TestDriveListActivity;

import java.util.Arrays;
import java.util.List;

import AdapterClass.CategoryAdapter;
import ModelClasses.CategoryModel;
import ModelClasses.Global;

public class DashboardFragment extends Fragment {
    ImageSliderAdapter imageSliderAdapteradapter;
    CardView ServiceList,Bookservice, Servicehistory, Vehdocuments, Pre_own_veh,TestDriveCard;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String lattitude, longitude;
    FusedLocationProviderClient client;
    ImageView ProfileIcon;
    String vehtype = "";
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryList;
    ViewPager2 viewPager2;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard2, container, false);
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);

        Bookservice = view.findViewById(R.id.bookServiceCard);
        TestDriveCard = view.findViewById(R.id.testDriveCard);
        Servicehistory = view.findViewById(R.id.serviceHistoryCard);
        Vehdocuments = view.findViewById(R.id.vehicleDocumentsCard);
        ServiceList = view.findViewById(R.id.serviceHistoryCard);
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

        List<String> imageUrls = Arrays.asList(
                "https://images.overdrive.in/wp-content/odgallery/2022/08/63809_2022_Royal_Enfield_Hunter_350.jpg",
                "https://images.overdrive.in/wp-content/odgallery/2022/08/63815_2022_Honda_CB300F_DLX_PRO_2.jpg",
                "https://plus.unsplash.com/premium_photo-1661963005592-182d602c6a3f?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8bW90b3JiaWtlfGVufDB8fDB8fHww&fm=jpg&q=60&w=3000"
        );

        imageSliderAdapteradapter = new ImageSliderAdapter(requireActivity(), imageUrls);
        viewPager2.setAdapter(imageSliderAdapteradapter);

        Handler sliderHandler = new Handler();
        Runnable sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager2.getCurrentItem();
                int totalItems = viewPager2.getAdapter().getItemCount();
                viewPager2.setCurrentItem((currentItem + 1) % totalItems, true);
            }
        };

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000); // 3 seconds
            }
        });


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
        Vehdocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVehicleFragment myVehicleFragment = new MyVehicleFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, myVehicleFragment);
                fragmentTransaction.commit();

                ((MainActivity) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_vehicles);
            }
        });
        Servicehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ServiceHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



        ProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


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



        TestDriveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), TestDriveListActivity.class));
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

    public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder> {

        private List<String> imageUrls;
        private Context context;

        public ImageSliderAdapter(Context context, List<String> imageUrls) {
            this.context = context;
            this.imageUrls = imageUrls;
        }

        @NonNull
        @Override
        public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_slider, parent, false);
            return new SliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
          /*  Glide.with(context)
                    .load(imageUrls.get(position))
                    .centerCrop()
                    .into(holder.imageView);*/
            Picasso.get()
                    .load(imageUrls.get(position))
                    .fit()                      // fits the ImageView
                    .centerCrop()               // crops the image nicely
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return imageUrls.size();
        }

        static class SliderViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public SliderViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageSlide);
            }
        }
    }
}