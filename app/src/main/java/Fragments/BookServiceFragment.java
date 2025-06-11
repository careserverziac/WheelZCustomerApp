package Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
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
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ziac.wheelzcustomer.GoogleMapActivity;
import com.ziac.wheelzcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ModelClasses.Global;
import ModelClasses.zList;
import de.hdodenhof.circleimageview.CircleImageView;

public class BookServiceFragment extends Fragment {

    TextView Dealername, DealerMob, Companyname, SerApptDate, SelectedTm, Currentadress, Streetname, Dealeraddress, Selectedveh;
    LinearLayout SelectDt, SelectTm, Maplocation, Servicedp, Pickupdp, Vehicledropdp, CnfrmbkgbBTN, Complaintdp, SelectedvehDp;
    String wuser_code, vehhis_code, lattitude, longitude;
    EditText Odometer, Complaints;
    CircleImageView DealerImage;
    BottomSheetDialog bottomSheetDialog;
    private zList modelclass;
    Context context;
    private Dialog zDialog;
    String locationUri, sqldateformat, odoreading, complaint, selectedTime24, serviceValue, pickupvalue, vehdropvalue, vehcode, streetname, fulladdress;
    FusedLocationProviderClient client;
    RadioGroup ServiceRB, PickupRB, VehdropRB;
    RadioButton Freeservice, Paidservice, PickupTrue, PickupFalse, VehdropTrue, VehdropFalse;
    FragmentTransaction fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_service, container, false);
        context = getContext();

        Companyname = view.findViewById(R.id.bkcomname);
        Dealername = view.findViewById(R.id.bkdealername);
        DealerMob = view.findViewById(R.id.bkdealermob);
        SerApptDate = view.findViewById(R.id.ser_app_date);
        SelectedTm = view.findViewById(R.id.seld_time);
        SelectDt = view.findViewById(R.id.selectdate);
        SelectTm = view.findViewById(R.id.selecttime);
        DealerImage = view.findViewById(R.id.bkdealer_image);
        Maplocation = view.findViewById(R.id.maplocation);
        Currentadress = view.findViewById(R.id.currentaddress);
        Streetname = view.findViewById(R.id.streetname);
        Dealeraddress = view.findViewById(R.id.bkdlraddress);
        Servicedp = view.findViewById(R.id.sevicedp);
        Pickupdp = view.findViewById(R.id.pickupdp);
        Vehicledropdp = view.findViewById(R.id.vehicledropdp);
        Complaintdp = view.findViewById(R.id.complaintdp);
        CnfrmbkgbBTN = view.findViewById(R.id.confirmbooking);
        Odometer = view.findViewById(R.id.odometerreading);
        Complaints = view.findViewById(R.id.complaints);

        SelectedvehDp = view.findViewById(R.id.selectvehicledp);
        Selectedveh = view.findViewById(R.id.selected_veh);
        ServiceRB = view.findViewById(R.id.serviceRG);
        PickupRB = view.findViewById(R.id.pickupRG);
        VehdropRB = view.findViewById(R.id.vehdropRG);

        Freeservice = view.findViewById(R.id.freeservice);
        Paidservice = view.findViewById(R.id.paidservice);

        PickupTrue = view.findViewById(R.id.pickuptrue);
        PickupFalse = view.findViewById(R.id.pickupfalse);

        VehdropTrue = view.findViewById(R.id.vehdroptrue);
        VehdropFalse = view.findViewById(R.id.vehdropfalse);

        ServiceRB.check(R.id.paidservice);
        PickupRB.check(R.id.pickupfalse);
        VehdropRB.check(R.id.vehdropfalse);

        Streetname.setText("Select");
        Currentadress.setText("from google");

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        locationUri = Global.sharedPreferences.getString("locationuri", "");
        streetname = Global.sharedPreferences.getString("currentStreetName", "");
        fulladdress = Global.sharedPreferences.getString("currentFullAddress", "");

        getvehicles();


        client = LocationServices.getFusedLocationProviderClient(getActivity());
        getcurrentlocation();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            getcurrentlocation();

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        Global.loadWithPicasso(context, DealerImage, Global.companyimageurl + Global.dealersdetails.getImage_path());
        Companyname.setText(Global.dealersdetails.getCom_name());
        Dealername.setText(Global.dealersdetails.getCom_contact());
        DealerMob.setText(Global.dealersdetails.getCom_contact_mobno());
        Dealeraddress.setText(Global.dealersdetails.getCom_address());


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMMM yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat time24Format = new SimpleDateFormat("HH:mm", Locale.getDefault()); // 24-hour format

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());
        SerApptDate.setText(currentDate);
        SelectedTm.setText(currentTime);


        selectedTime24 = time24Format.format(calendar.getTime());
        sqldateformat = sqlDateFormat.format(calendar.getTime());


        SelectedvehDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehiclepopup();
                ;
            }
        });


        SerApptDate.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        String selectedDateString = dateFormat.format(selectedDate.getTime());
                        SerApptDate.setText(selectedDateString);


                        sqldateformat = String.format("%04d %02d %02d",
                                selectedDate.get(Calendar.YEAR),
                                selectedDate.get(Calendar.MONTH) + 1,
                                selectedDate.get(Calendar.DAY_OF_MONTH));

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        SelectTm.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm", Locale.getDefault());

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (view12, hourOfDay, minute) -> {
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);
                        String selectedTimeString12 = timeFormat12.format(selectedTime.getTime());
                        String selectedTimeString24 = timeFormat24.format(selectedTime.getTime());

                        SelectedTm.setText(selectedTimeString12);
                        selectedTime24 = selectedTimeString24;

                    }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });


        Maplocation.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), GoogleMapActivity.class))));

        CnfrmbkgbBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationprocess();
            }
        });


        serviceValue = "0";
        ServiceRB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.freeservice) {
                    serviceValue = "1";
                    // Free service selected, paid service unselected
                } else if (checkedId == R.id.paidservice) {
                    serviceValue = "0";
                    // Paid service selected, free service unselected
                }
            }
        });

        pickupvalue = "0";
        PickupRB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pickuptrue) {
                    pickupvalue = "1";
                    // Free service selected, paid service unselected
                } else if (checkedId == R.id.pickupfalse) {
                    pickupvalue = "0";
                    // Paid service selected, free service unselected
                }
            }
        });

        vehdropvalue = "0";
        VehdropRB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.vehdroptrue) {
                    vehdropvalue = "1";
                    // Free service selected, paid service unselected
                } else if (checkedId == R.id.vehdropfalse) {
                    vehdropvalue = "0";
                    // Paid service selected, free service unselected
                }
            }
        });


        return view;
    }


    private void validationprocess() {

        odoreading = Odometer.getText().toString();
        complaint = Complaints.getText().toString();


        if (odoreading.isEmpty()) {
            Odometer.setError("Odometer field must not be null !!");
            Odometer.requestFocus();
            return;
        }
        if (complaint.isEmpty()) {
            Complaints.setError("Complaints field must not be null !!");
            Complaints.requestFocus();
            return;
        }

        bookingservice();
    }

    private void bookingservice() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urlBookServices,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String sresponse) {


                        JSONObject response = null;
                        try {
                            response = new JSONObject(sresponse);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                        try {
                            if (response.getBoolean("isSuccess")) {
                                Global.customtoast(getActivity(), getLayoutInflater(), response.getString("error"));

                                DealersFragment dealersFragment = new DealersFragment();
                                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.framelayout, dealersFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();


                            } else {
                                Global.customtoast(getActivity(), getLayoutInflater(), response.getString("error"));

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        /*progressDialog.dismiss();*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*progressDialog.dismiss();*/

                // Global.customtoast(context, getLayoutInflater(), error.getLocalizedMessage());

                if (error instanceof TimeoutError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Internet connection unavailable");
                } else if (error instanceof ServerError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Parse Error");
                }


            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("com_code", Global.dealersdetails.getCom_code());
                params.put("service_date", sqldateformat);
                params.put("cveh_code", vehcode);
                params.put("vehhis_code", vehhis_code);
                params.put("wuser_code", wuser_code);
                params.put("service_time", selectedTime24);
                params.put("kms_done", odoreading);
                params.put("service_type", serviceValue);
                params.put("pick_flag", pickupvalue);
                params.put("drop_flag", vehdropvalue);
                params.put("additional_info", complaint);
                params.put("location_map", locationUri);
                params.put("address", streetname + " " + fulladdress);                    //Log.d("params",params.toString());
                return params;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);


    }


    @SuppressLint("MissingPermission")
  /*  private void getcurrentlocation() {

        LocationManager locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {


                    Location location=task.getResult();
                    if (location !=null){

                     *//*  lattitude=String.valueOf(location.getLatitude());
                       longitude=String.valueOf(location.getLongitude());
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("lattitude", lattitude);
                        Global.editor.putString("longitude", longitude);
                        Global.editor.commit();*//*

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                              *//*  String sublocality = address.getSubLocality();
                                String fullAddress = address.getAddressLine(0);*//*

     *//*  Streetname.setText(address.getSubLocality());
                                Currentadress.setText(address.getAddressLine(0));*//*



                            } else {
                                Log.e("DashboardFragment", "Unable to get address");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("DashboardFragment", "Geocoder service not available");
                        }


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
    }*/


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private void getcurrentlocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            fetchLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                fetchLocation();
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Location permission is required to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                // Use the address as needed
                            } else {
                                Log.e("DashboardFragment", "Unable to get address");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("DashboardFragment", "Geocoder service not available");
                        }
                    } else {
                        LocationRequest locationRequest = LocationRequest.create()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                String latitude = String.valueOf(location1.getLatitude());
                                String longitude = String.valueOf(location1.getLongitude());

                            }
                        };
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                            return;
                        }
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    private void saveLocationData(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String sublocality = address.getSubLocality();
                String fullAddress = address.getAddressLine(0);


             /*   Currentadress.setText(fullAddress);
                Streetname.setText(sublocality);*/

               /* Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("latitude", latitudeString);
                Global.editor.putString("longitude", longitudeString);
                Global.editor.putString("sublocality", sublocality);
                Global.editor.putString("fullAddress", fullAddress);
                Global.editor.commit();*/
            } else {
                Log.e("DashboardFragment", "Unable to get address");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DashboardFragment", "Geocoder service not available");
        }
    }


 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && (grantResults.length >0 ) && (grantResults[0] + grantResults[1]  == PackageManager.PERMISSION_GRANTED)) {
            getcurrentlocation();
        }else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void getvehicles() {

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String wusercode = Global.sharedPreferences.getString("wuser_code", "");
        String Url = Global.getallMyVehicles+"wuser_code="+wusercode;


        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Global.statearraylist = new ArrayList<zList>();
                modelclass = new zList();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject jsonObject;
                    try {
                        // converting to json object
                        jsonObject = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    modelclass = new zList();
                    try {
                        // getting the data  from the object
                        modelclass.set_name(jsonObject.getString("model_name"));
                        modelclass.setReg_no(jsonObject.getString("reg_no"));
                        modelclass.set_code(jsonObject.getString("cveh_code"));
                        modelclass.setWuser_code(jsonObject.getString("wuser_code"));
                        modelclass.setCom_code(jsonObject.getString("com_code"));
                        modelclass.setVehhis_code(jsonObject.getString("vehhis_code"));

                        vehcode = modelclass.get_code();
                        vehhis_code = modelclass.getVehhis_code();
                        wuser_code = modelclass.getWuser_code();


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.statearraylist.add(modelclass);
                }

            }
        },  error -> {

            if (error instanceof NoConnectionError) {
                if (error instanceof TimeoutError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Internet connection unavailable");
                } else if (error instanceof ServerError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Parse Error");
                }
            }
            // Global.customtoast(getApplicationContext(),getLayoutInflater(),"Technical error : Unable to get dashboard data !!" + error);

        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }


        };

        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonArrayrequest);

    }

    public void vehiclepopup() {

        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.statearraylist == null || Global.statearraylist.size() == 0) {
            // Toast.makeText(getBaseContext(), "States list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final VehicleAdapter laStates = new VehicleAdapter(Global.statearraylist);
        lvStates.setAdapter(laStates);

        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView svstate = zDialog.findViewById(R.id.svstate);
        svstate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                laStates.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class VehicleAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public VehicleAdapter(ArrayList<zList> arrayList) {
            this.mDataArrayList = arrayList;
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<zList> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.statearraylist;
                    } else {
                        for (zList dataList : Global.statearraylist) {
                            if (dataList.get_name().toLowerCase().contains(charString)) {
                                mFilteredList.add(dataList);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    filterResults.count = mFilteredList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mDataArrayList = (ArrayList<zList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public int getCount() {
            return mDataArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder") View v = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            final TextView tvstatenameitem = v.findViewById(R.id.tvsingleitem);
            RadioButton radioButton = v.findViewById(R.id.radio_button);
            modelclass = mDataArrayList.get(i);
            tvstatenameitem.setText(modelclass.get_name()+"( "+modelclass.getReg_no()+" )");

            radioButton.setOnClickListener(view1 -> {
                boolean isChecked = radioButton.isChecked();
                radioButton.setChecked(!isChecked);
                modelclass = mDataArrayList.get(i);
                vehcode = modelclass.get_code();
                vehhis_code = modelclass.getVehhis_code();
                wuser_code = modelclass.getWuser_code();
                zDialog.dismiss();
                Selectedveh.setText(modelclass.get_name());
            });

            tvstatenameitem.setOnClickListener(view1 -> {
                modelclass = mDataArrayList.get(i);
                vehcode = modelclass.get_code();
                vehhis_code = modelclass.getVehhis_code();
                wuser_code = modelclass.getWuser_code();
                zDialog.dismiss();
                Selectedveh.setText(modelclass.get_name());
            });

            return v;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
    private void refreshData() {
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        streetname = Global.sharedPreferences.getString("currentStreetName", "");
        fulladdress = Global.sharedPreferences.getString("currentFullAddress", "");


        if (streetname == null || streetname.isEmpty()) {
            Streetname.setText("Select the preferred location");
            Currentadress.setText("from google map");
        } else {
            Streetname.setText(streetname);
            Currentadress.setText(fulladdress);
        }

    }



}