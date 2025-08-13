package Fragments;

import static ModelClasses.Global.lessdrivenlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import android.widget.LinearLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.ziac.wheelzcustomer.LatestVeh_ViewAllActivity;
import com.ziac.wheelzcustomer.Less_driven_Viewall_Activity;
import com.ziac.wheelzcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ModelClasses.CommonVehClass;
import ModelClasses.Global;
import ModelClasses.LatestVehiclesClass;
import ModelClasses.LessDrivenClass;
import ModelClasses.ModelsClass;
import ModelClasses.VehicleClass;

public class PreOwnedVehicleFragment extends Fragment {

    private boolean allowBack = true;
    SearchView searchView;
    RecyclerView recyclermodels, Latestvehicles, Lessvehicles;
    TextView latestcarsviewallTV, lessdrivenviewallTV, Veh_typeTV, StateTV, CityTV;
    ImageView Edit, Img_veh_type;
    ModelsClass singleModel;
    LatestVehiclesClass latestVehiclesClass;
    LessDrivenClass lessDrivenClass;
    CardView cardView;
    ModelAdapter modelAdapter;
    LatestVehiclesAdapter latestvehadapter;
    LessDrivenAdapter lessDrivenAdapter;
    FloatingActionButton fab;
    NestedScrollView nestedScrollView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String vehtype;
    private int lessDrivenClickedPosition = RecyclerView.NO_POSITION;

    @SuppressLint("MissingInflatedId")

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setInitialSavedState(@Nullable SavedState state) {
        super.setInitialSavedState(state);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_owned_vehicle, viewGroup, false);


        searchView = view.findViewById(R.id.homesearchview);
        searchView = view.findViewById(R.id.homesearchview);
        StateTV = view.findViewById(R.id.statetv);
        CityTV = view.findViewById(R.id.citytv);
        Veh_typeTV = view.findViewById(R.id.veh_type);
        Edit = view.findViewById(R.id.edit);
        nestedScrollView = view.findViewById(R.id.scrollView);
        Img_veh_type = view.findViewById(R.id.vicon);
        fab = view.findViewById(R.id.fab_add_vehicle);
        fab.show();


        searchView.setQueryHint("Search for Model, Variant & Category");
        StateTV.setText(Global.sharedPreferences.getString("statename", "Select Vehicle Type"));
        CityTV.setText(Global.sharedPreferences.getString("cityname", "Select City"));
        vehtype = Global.sharedPreferences.getString("vehicletype", "Select City");
        Veh_typeTV.setText(vehtype);

        if (vehtype.equals("4 WHEELER")) {
            Img_veh_type.setImageResource(R.drawable.car);
        } else if (vehtype.equals("2 WHEELER")) {
            Img_veh_type.setImageResource(R.drawable.bike);
        }

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private Handler handler = new Handler();
            private Runnable showFabRunnable = new Runnable() {
                @Override
                public void run() {
                    fab.show();
                }
            };

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (Math.abs(scrollY - oldScrollY) < 10) return;
                fab.hide();
                handler.removeCallbacks(showFabRunnable);
                handler.postDelayed(showFabRunnable, 200);
            }
        });
        if (savedInstanceState != null) {
            lessDrivenClickedPosition = savedInstanceState.getInt("lessDrivenClickedPosition", RecyclerView.NO_POSITION);
        }

        String vehicleTypeImageUrl = Global.sharedPreferences.getString("vehicletypeimageurl", "");


       /* if (vehicleTypeImageUrl != null && !vehicleTypeImageUrl.isEmpty()) {
            String vimage = Global.dealersdetails.com_website + vehicleTypeImageUrl.substring(2);

            Global.loadWithPicasso(requireActivity(), Img_veh_type, vimage);
        } else {
            if (Img_veh_type != null) {
                Img_veh_type.setImageResource(R.drawable.no_image_available_icon);
            }
        }*/


        /**/


        // VIEW ALL VEHICLES
        recyclermodels = view.findViewById(R.id.viewmodelsrv);
        recyclermodels.setHasFixedSize(true);
        recyclermodels.setAdapter(modelAdapter);
        recyclermodels.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getallmodels();

        // LATEST VEHICLES
        Latestvehicles = view.findViewById(R.id.latestvehicles_Rv);
        Latestvehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
        Latestvehicles.setHasFixedSize(true);
        Latestvehicles.setAdapter(latestvehadapter);
        Latestvehicles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getlatestvehicles();


        // LESS DRIVEN VEHICLES
        Lessvehicles = view.findViewById(R.id.lessDrvRV);
        Lessvehicles.setHasFixedSize(true);
        Lessvehicles.setAdapter(lessDrivenAdapter);
        Lessvehicles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getlessdrivenvevicles();


        cardView = view.findViewById(R.id.cardviewsearch);
        cardView.setOnClickListener(v -> {
            // searchView.setIconified(false);
            // searchView.requestFocus();
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                performSearch(query);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });


        latestcarsviewallTV = view.findViewById(R.id.viewalllatestvehicles);
        lessdrivenviewallTV = view.findViewById(R.id.viewalllessdriven);
        //Viewallbrandsicon = view.findViewById(R.id.viewall_vehicles);

        /* Viewallbrandsicon.setOnClickListener(v -> {
         *//*  GetBrands_Fragment getBrandsFragment = new GetBrands_Fragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, getBrandsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*//*
        });*/
        latestcarsviewallTV.setOnClickListener(v -> {
           /* Latest_veh_Fragment_viewall latest_veh_fragmentViewall = new Latest_veh_Fragment_viewall();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, latest_veh_fragmentViewall);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
            startActivity(new Intent(getContext(), LatestVeh_ViewAllActivity.class));

        });

        lessdrivenviewallTV.setOnClickListener(v -> {
            /*Less_driven_ViewallFragment less_driven_Viewall_fragment = new Less_driven_ViewallFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, less_driven_Viewall_fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
            startActivity(new Intent(getContext(), Less_driven_Viewall_Activity.class));
        });

        fab.setOnClickListener(v -> {
            AddVehFragment addVehFragment = new AddVehFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.framelayout, addVehFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        Edit.setOnClickListener(v -> {
            PreferenceFragment preferenceFragment = new PreferenceFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.framelayout, preferenceFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }


    private void getallmodels() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.Getmodels;
        // progressDialog.show();
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {

                Global.modelsList = new ArrayList<ModelsClass>();
                singleModel = new ModelsClass();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {
                        // converting to json object
                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    singleModel = new ModelsClass();
                    try {
                        // getting the values from the object
                        singleModel.setVmodelcode(e.getString("vmodel_code"));
                        singleModel.setVmodelname(e.getString("shortname").toUpperCase());
                        singleModel.setVmodelimage(e.getString("veh_image1"));

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.modelsList.add(singleModel);
                }
                Collections.sort(Global.modelsList, new Comparator<ModelsClass>() {
                    @Override
                    public int compare(ModelsClass model1, ModelsClass model2) {
                        return model1.getVmodelname().compareToIgnoreCase(model2.getVmodelname());
                    }
                });

                modelAdapter = new ModelAdapter(Global.modelsList, getContext());
                recyclermodels.setAdapter(modelAdapter);
                modelAdapter.notifyDataSetChanged();
                // progressDialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
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

    private void getlatestvehicles() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String citycode = Global.sharedPreferences.getString("citycode", "0");
        String vehtypecode = Global.sharedPreferences.getString("vtypecode", "0");

        String url = Global.GetRecentVehicles + "city_code=" + citycode + "&vcate_code=" + vehtypecode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try {
                boolean isSuccess = response.getBoolean("isSuccess");

                if (isSuccess) {
                    JSONArray dataArray = response.getJSONArray("data");

                    Global.latestVehicleslist = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject e = dataArray.getJSONObject(i);
                        LatestVehiclesClass vehicle = new LatestVehiclesClass();

                        vehicle.setVehmas_code(e.optString("vehmas_code", ""));
                        vehicle.setLatestvname(e.optString("model_name", "").toUpperCase());
                        vehicle.setLatestmfgname(e.optString("mfg_name", ""));
                        vehicle.setLatestvimage(e.optString("veh_image1", ""));
                        vehicle.setMan_year(e.optString("year_of_mfg", "").toUpperCase());
                        vehicle.setLatestvcolour(e.optString("vcol_name", "").toUpperCase());
                        vehicle.setLatestvcategory(e.optString("vcate_name", "").toUpperCase());
                        vehicle.setVart_name(e.optString("vart_name", "").toUpperCase());
                        vehicle.setVart_code(e.optString("vart_code", "").toUpperCase());
                        vehicle.setTrans_name(e.optString("transmission_type_name", "").toUpperCase());
                        vehicle.setOwn_type(e.optString("ownership_type_name", "").toUpperCase());
                        vehicle.setLatestvcc(e.optString("veh_cc", "").toUpperCase());
                        vehicle.setFuel_name(e.optString("fuel_name", "").toUpperCase());
                        vehicle.setLatestvdate(e.optString("created_on", "").toUpperCase());
                        vehicle.setKm_drvn(e.optString("kms_driven", "").toUpperCase());
                        vehicle.setSell_price(e.optString("veh_sale_price", "").toUpperCase());
                        vehicle.setLatestvcomname(e.optString("com_name", ""));
                        vehicle.setLatestvcommob(e.optString("com_contact_mobno", ""));
                        vehicle.setLatestvcomaltermob(e.optString("user_mobile1", ""));
                        vehicle.setLatestvcomemail(e.optString("com_email", ""));
                        vehicle.setLatestvcomcity(e.optString("city_name", ""));
                        vehicle.setCreatedby(e.optString("createdby", ""));
                        vehicle.setNum_plate(e.optString("no_plate_type", ""));
                        vehicle.setFuel_code(e.optString("fuel_code", ""));
                        vehicle.setVmodel_code(e.optString("vmodel_code", ""));
                        vehicle.setReg_no(e.optString("reg_no", ""));
                        vehicle.setInsc_type(e.optString("insurance_type_name", ""));
                        vehicle.setLis_type(e.optString("listing_type", ""));
                        vehicle.setCity_code(e.optString("city_code", ""));
                        vehicle.setState_code(e.optString("state_code", ""));

                        Global.latestVehicleslist.add(vehicle);
                    }

                    // Sort list by vehicle name
                    Collections.sort(Global.latestVehicleslist, (v1, v2) ->
                            v1.getLatestvname().compareToIgnoreCase(v2.getLatestvname()));

                    latestvehadapter = new LatestVehiclesAdapter(Global.latestVehicleslist, getContext());
                    Latestvehicles.setAdapter(latestvehadapter);

                } else {
                    String errorMsg = response.optString("message", "Failed to load vehicles.");
                    Global.customtoast(requireActivity(), getLayoutInflater(), errorMsg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Global.customtoast(requireActivity(), getLayoutInflater(), "Data parsing error.");
            }

        }, error -> {
            if (error instanceof TimeoutError) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Request Time-Out");
            } else if (error instanceof NoConnectionError) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "No Internet Connection");
            } else if (error instanceof ServerError) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Server Error");
            } else if (error instanceof NetworkError) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Network Error");
            } else if (error instanceof ParseError) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Response Parsing Failed");
            } else {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Unknown error occurred");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonObjectRequest);
    }

    private void getlessdrivenvevicles() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.GetLessDrivenVehicles;

        String citycode = Global.sharedPreferences.getString("citycode", "0");
        String vehtypecode = Global.sharedPreferences.getString("vtypecode", "0");

        url = url + "city_code=" + citycode + "&vcate_code=" + vehtypecode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {

            lessdrivenlist = new ArrayList<>();

            try {
                JSONArray dataArray = response.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject e = dataArray.getJSONObject(i);

                    LessDrivenClass vehicle = new LessDrivenClass();

                    vehicle.setVehmas_code(e.optString("vehmas_code", ""));
                    vehicle.setLd_mfgname(e.optString("mfg_name"));
                    vehicle.setLd_vimg(e.optString("veh_image1"));  // changed from Image_name
                    vehicle.setLd_vname(e.optString("model_name").toUpperCase());
                    vehicle.setMan_year(String.valueOf(e.optInt("year_of_mfg")));
                    vehicle.setLd_vcategory(e.optString("vcate_name").toUpperCase());
                    vehicle.setVart_name(e.optString("vart_name").toUpperCase());
                    vehicle.setLd_vcolor(e.optString("vcol_name").toUpperCase());
                    vehicle.setTrans_name(e.optString("transmission_type_name").toUpperCase());
                    vehicle.setOwn_type(e.optString("ownership_type_name").toUpperCase());
                    vehicle.setLd_vcc(String.valueOf(e.optDouble("veh_cc")));
                    vehicle.setKm_drvn(String.valueOf(e.optDouble("kms_driven")));
                    vehicle.setFuel_name(e.optString("fuel_name").toUpperCase());
                    vehicle.setSell_price(String.valueOf(e.optDouble("veh_sale_price")));
                    vehicle.setLd_vmodelcode(String.valueOf(e.optDouble("vehmas_code")));
                    vehicle.setLd_dname(e.optString("com_name"));
                    vehicle.setLd_dmob(e.optString("com_phno"));
                    vehicle.setLd_altermob(String.valueOf(e.optDouble("user_mobile1")));
                    vehicle.setLd_dmail(e.optString("com_email"));
                    vehicle.setLd_dcity(e.optString("city_name"));
                    vehicle.setCreatedby(e.optString("createdby"));
                    vehicle.setNum_plate(e.optString("no_plate_type", ""));
                    vehicle.setFuel_code(e.optString("fuel_code", ""));
                    vehicle.setVmodel_code(e.optString("vmodel_code", ""));
                    vehicle.setReg_no(e.optString("reg_no", ""));
                    vehicle.setInsc_type(e.optString("insurance_type_name", ""));
                    vehicle.setLis_type(e.optString("listing_type", ""));
                    vehicle.setCity_code(e.optString("city_code", ""));
                    vehicle.setState_code(e.optString("state_code", ""));

                    lessdrivenlist.add(vehicle);
                }

                // Sort by model name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(lessdrivenlist, Comparator.comparing(LessDrivenClass::getLd_vname));
                }

                // Set adapter and scroll
                lessDrivenAdapter = new LessDrivenAdapter(lessdrivenlist, getContext());
                Lessvehicles.setAdapter(lessDrivenAdapter);
                Lessvehicles.scrollToPosition(lessDrivenClickedPosition);

            } catch (JSONException e) {
                e.printStackTrace();
                // Optional: handle JSON parsing error
            }

        }, error -> {
            // Handle errors here if needed
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accessToken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("city_code", citycode);
                params.put("veh_type_code", vehtypecode);
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonObjectRequest);
    }


    private void performSearch(String query) {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.Getsearchdetails;

        StringRequest jsonObjectrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // progressDialog.dismiss();

                JSONObject jobj;
                try {
                    jobj = new JSONObject(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JSONArray jarrrecent;
                try {
                    jarrrecent = jobj.getJSONArray("api_mvh_vehmas_recent_vehicles_vu");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Global.latestVehicleslist = new ArrayList<LatestVehiclesClass>();
                for (int i = 0; i < jarrrecent.length(); i++) {
                    final JSONObject e;
                    try {
                        e = jarrrecent.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    latestVehiclesClass = new LatestVehiclesClass();
                    try {

                        latestVehiclesClass.setLatestmfgname(e.getString("mfg_name"));
                        latestVehiclesClass.setLatestvimage(e.getString("veh_image1"));
                        latestVehiclesClass.setLatestvname(e.getString("model_name").toUpperCase());
                        latestVehiclesClass.setMan_year(e.getString("year_of_mfg").toUpperCase());
                        latestVehiclesClass.setLatestvcolour(e.getString("vcol_name").toUpperCase());
                        latestVehiclesClass.setLatestvcategory(e.getString("vcate_name").toUpperCase());
                        latestVehiclesClass.setVart_name(e.getString("vart_name").toUpperCase());
                        latestVehiclesClass.setTrans_name(e.getString("transmission_type_name").toUpperCase());
                        latestVehiclesClass.setOwn_type(e.getString("ownership_type_name").toUpperCase());
                        latestVehiclesClass.setLatestvcc(e.getString("veh_cc").toUpperCase());
                        latestVehiclesClass.setFuel_name(e.getString("fuel_name").toUpperCase());
                        latestVehiclesClass.setLatestvdate(e.getString("created_on").toUpperCase());
                        latestVehiclesClass.setKm_drvn(e.getString("kms_driven").toUpperCase());
                        latestVehiclesClass.setSell_price(e.getString("veh_sale_price").toUpperCase());
                        latestVehiclesClass.setLatestvcomname(e.getString("com_name"));
                        latestVehiclesClass.setLatestvcommob(e.getString("com_contact_mobno"));
                        latestVehiclesClass.setLatestvcomaltermob(e.getString("user_mobile1"));
                        latestVehiclesClass.setLatestvcomemail(e.getString("com_email"));
                        latestVehiclesClass.setLatestvcomcity(e.getString("city_name"));
                        latestVehiclesClass.setVehmas_code(e.getString("vehmas_code"));

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.latestVehicleslist.add(latestVehiclesClass);
                }

                LatestVehiclesAdapter latestvehadapter = new LatestVehiclesAdapter(Global.latestVehicleslist, getContext());
                Latestvehicles.setAdapter(latestvehadapter);

                JSONArray jarrless;
                try {
                    jarrless = jobj.getJSONArray("api_mvh_vehmas_lessdriven_vehicles_vu");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                lessdrivenlist = new ArrayList<LessDrivenClass>();
                for (int i = 0; i < jarrless.length(); i++) {
                    final JSONObject e;
                    try {

                        e = jarrless.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    lessDrivenClass = new LessDrivenClass();
                    try {
                        lessDrivenClass.setLd_mfgname(e.getString("mfg_name"));
                        lessDrivenClass.setLd_vimg(e.getString("veh_image1"));
                        lessDrivenClass.setLd_vname(e.getString("model_name").toUpperCase());
                        lessDrivenClass.setMan_year(e.getString("year_of_mfg").toUpperCase());
                        lessDrivenClass.setLd_vcategory(e.getString("vcate_name").toUpperCase());
                        lessDrivenClass.setVart_name(e.getString("vart_name").toUpperCase());
                        lessDrivenClass.setLd_vcolor(e.getString("vcol_name").toUpperCase());
                        lessDrivenClass.setTrans_name(e.getString("transmission_type_name").toUpperCase());
                        lessDrivenClass.setOwn_type(e.getString("ownership_type_name").toUpperCase());
                        lessDrivenClass.setLd_vcc(e.getString("veh_cc").toUpperCase());
                        lessDrivenClass.setKm_drvn(e.getString("kms_driven").toUpperCase());
                        lessDrivenClass.setFuel_name(e.getString("fuel_name").toUpperCase());
                        lessDrivenClass.setSell_price(e.getString("veh_sale_price").toUpperCase());
                        lessDrivenClass.setLd_dname(e.getString("com_name"));
                        lessDrivenClass.setLd_dmob(e.getString("com_contact_mobno"));
                        lessDrivenClass.setLd_altermob(e.getString("user_mobile1"));
                        lessDrivenClass.setLd_dmail(e.getString("com_email"));
                        lessDrivenClass.setLd_dcity(e.getString("city_name"));
                        lessDrivenClass.setLd_vmodelcode(e.getString("vehmas_code"));

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    lessdrivenlist.add(lessDrivenClass);
                }

                LessDrivenAdapter lessDrivenAdapter = new LessDrivenAdapter(lessdrivenlist, getContext());
                Lessvehicles.setAdapter(lessDrivenAdapter);

                JSONArray jarrmodel;
                try {
                    jarrmodel = jobj.getJSONArray("api_mvh_models_vu");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Global.modelsList = new ArrayList<ModelsClass>();
                for (int i = 0; i < jarrmodel.length(); i++) {
                    final JSONObject e;
                    try {
                        // converting to json object
                        e = jarrmodel.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    singleModel = new ModelsClass();
                    try {
                        // getting the values from the object
                        singleModel.setVmodelcode(e.getString("vmodel_code"));
                        singleModel.setVmodelname(e.getString("shortname").toUpperCase());
                        singleModel.setVmodelimage(e.getString("veh_image1"));

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.modelsList.add(singleModel);
                }

                ModelAdapter modelAdapter = new ModelAdapter(Global.modelsList, getContext());
                recyclermodels.setAdapter(modelAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // progressDialog.dismiss();

                // Global.customtoast(getContext(), getLayoutInflater(),"Failed to get latest vehicle .." + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("search_text", query);
                params.put("dealer_city_code", Global.sharedPreferences.getString("citycode", "0"));
                params.put("veh_type_code", Global.sharedPreferences.getString("vehicletypecode", "0"));
                //Log.d("params", params.toString());
                return params;
            }
        };

        jsonObjectrequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonObjectrequest);

    }

    public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.VallCarsViewHOlder> {


        private final List<ModelsClass> viewallmodelsList;

        private final Context context;


        public ModelAdapter(List<ModelsClass> viewallmodelslist, Context context) {
            this.viewallmodelsList = viewallmodelslist;
            this.context = context;

        }

        @NonNull
        @Override
        public ModelAdapter.VallCarsViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_models, parent, false);
            return new VallCarsViewHOlder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VallCarsViewHOlder holder, @SuppressLint("RecyclerView") int position) {

//        Picasso.Builder builder=new Picasso.Builder(context);
//        Picasso picasso=builder.build();
//        picasso.load(Uri.parse(Global.websitedataurl + viewallmodelsList.get(position).getVmodelimage().substring(2))).into(holder.modelimageiv);

            Global.loadWithPicasso(context, holder.modelimageiv, Global.websitedataurl + viewallmodelsList.get(position).getVmodelimage().substring(2));


            holder.modelnametv.setText(viewallmodelsList.get(position).getVmodelname());
            holder.vmodel_code = viewallmodelsList.get(position).getVmodelcode();

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("vmodel_code", Global.modelsList.get(position).getVmodelcode());
                    Global.editor.commit();


                  /*  BrandlWiseVehicleModels_Fragment modelWiseVehiclesFragment = new BrandlWiseVehicleModels_Fragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, modelWiseVehiclesFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/


                }
            });

        }

        @Override
        public int getItemCount() {
            return viewallmodelsList.size();
        }

        public static class VallCarsViewHOlder extends RecyclerView.ViewHolder {

            private TextView modelnametv;
            private ImageView modelimageiv;
            private String vmodel_code;
            LinearLayout cardView;

            public VallCarsViewHOlder(@NonNull View itemView) {
                super(itemView);

                cardView = itemView.findViewById(R.id.viewallcardview);
                modelnametv = itemView.findViewById(R.id.clothing_offer_tv);
                modelimageiv = itemView.findViewById(R.id.clothing_image);
            }
        }
    }


    public class LatestVehiclesAdapter extends RecyclerView.Adapter<LatestVehiclesAdapter.RecentcarsViewHolder> {

        private final List<LatestVehiclesClass> latestVehiclesList;
        Context context;

        public LatestVehiclesAdapter(List<LatestVehiclesClass> latestVehiclesList, Context context) {
            this.latestVehiclesList = latestVehiclesList;
            this.context = context;

        }

        @NonNull
        @Override
        public RecentcarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_vech2, parent, false);
            return new RecentcarsViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecentcarsViewHolder holder, @SuppressLint("RecyclerView") int position) {

            String createdby = latestVehiclesList.get(position).getCreatedby();
            String userName = Global.sharedPreferences.getString("userName", "");

            if (createdby.equals(userName)) {
                holder.Edit.setVisibility(View.VISIBLE);
                holder.Delete.setVisibility(View.VISIBLE);
            } else {
                holder.Edit.setVisibility(View.GONE);
                holder.Delete.setVisibility(View.GONE);
            }

            String img = Global.vehimageurl + latestVehiclesList.get(position).getLatestvimage();
            Global.loadWithPicasso(context, holder.lv_image, img);

            holder.lv_name.setText(latestVehiclesList.get(position).getLatestvname() + " (" + latestVehiclesList.get(position).getMan_year() + ")");
            holder.lvcolor.setText(latestVehiclesList.get(position).getLatestvcolour());
            holder.lv_category.setText(latestVehiclesList.get(position).getLatestvcategory());
            holder.lv_variant.setText(latestVehiclesList.get(position).getVart_name());
            holder.lv_transmission.setText(latestVehiclesList.get(position).getTrans_name());
            holder.lv_ownership.setText(latestVehiclesList.get(position).getOwn_type() + " OWNER");
            holder.lv_fuel.setText(latestVehiclesList.get(position).getFuel_name());
            holder.lv_date.setText("KMS run " + latestVehiclesList.get(position).getKm_drvn() + " Added on : " + latestVehiclesList.get(position).getLatestvdate());
            holder.lv_sellingprice.setText("\u20B9 " + latestVehiclesList.get(position).getSell_price() + "/- Negotiable");

            holder.lv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    VehicleClass svel = new VehicleClass();

                    svel.setVeh_mfgname(Global.latestVehicleslist.get(position).getLatestmfgname());
                    svel.setVeh_image(Global.latestVehicleslist.get(position).getLatestvimage());
                    svel.setVeh_fullname(Global.latestVehicleslist.get(position).getLatestvname());
                    svel.setVeh_year(Global.latestVehicleslist.get(position).getMan_year());
                    svel.setVeh_colour(Global.latestVehicleslist.get(position).getLatestvcolour());
                    svel.setVeh_model(Global.latestVehicleslist.get(position).getLatestvcategory());
                    svel.setVeh_variant(Global.latestVehicleslist.get(position).getVart_name());
                    svel.setVeh_transmission(Global.latestVehicleslist.get(position).getTrans_name());
                    svel.setVeh_ownership(Global.latestVehicleslist.get(position).getOwn_type());
                    svel.setVeh_km(Global.latestVehicleslist.get(position).getKm_drvn());
                    svel.setVeh_fuel(Global.latestVehicleslist.get(position).getFuel_name());
                    svel.setVeh_price(Global.latestVehicleslist.get(position).getSell_price());
                    svel.setVeh_cc(Global.latestVehicleslist.get(position).getLatestvcc());
                    svel.setDealercom_name(Global.latestVehicleslist.get(position).getLatestvcomname());
                    svel.setDealer_mob(Global.latestVehicleslist.get(position).getLatestvcommob());
                    svel.setDealer_altermob(Global.latestVehicleslist.get(position).getLatestvcomaltermob());
                    svel.setDealermailid(Global.latestVehicleslist.get(position).getLatestvcomemail());
                    svel.setDealer_city(Global.latestVehicleslist.get(position).getLatestvcomcity());

                    Global.selectedvehicle = svel;
                    showDetailsAlertDialog();

                }
            });

            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonVehClass commonVehClass = new CommonVehClass();
                    commonVehClass.setVart_code(Global.latestVehicleslist.get(position).getVart_code());
                    commonVehClass.setVart_name(Global.latestVehicleslist.get(position).getVart_name());
                    commonVehClass.setTrans_name(Global.latestVehicleslist.get(position).getTrans_name());
                    commonVehClass.setKm_drvn(Global.latestVehicleslist.get(position).getKm_drvn());
                    commonVehClass.setNum_plate(Global.latestVehicleslist.get(position).getNum_plate());
                    commonVehClass.setMan_year(Global.latestVehicleslist.get(position).getMan_year());
                    commonVehClass.setOwn_type(Global.latestVehicleslist.get(position).getOwn_type());
                    commonVehClass.setFuel_code(Global.latestVehicleslist.get(position).getFuel_code());
                    commonVehClass.setFuel_name(Global.latestVehicleslist.get(position).getFuel_name());
                    commonVehClass.setSell_price(Global.latestVehicleslist.get(position).getSell_price());
                    commonVehClass.setVmodel_code(Global.latestVehicleslist.get(position).getVmodel_code());
                    commonVehClass.setVehmas_code(Global.latestVehicleslist.get(position).getVehmas_code());
                    commonVehClass.setReg_no(Global.latestVehicleslist.get(position).getReg_no());
                    commonVehClass.setInsc_type(Global.latestVehicleslist.get(position).getInsc_type());
                    commonVehClass.setLis_type(Global.latestVehicleslist.get(position).getLis_type());
                    commonVehClass.setCity_code(Global.latestVehicleslist.get(position).getCity_code());
                    commonVehClass.setState_code(Global.latestVehicleslist.get(position).getState_code());
                    commonVehClass.setVeh_img(Global.latestVehicleslist.get(position).getLatestvimage());
                    Global.commonVehClass = commonVehClass;

                    UpdateVehFragment updateVehFragment = new UpdateVehFragment();
                    fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout, updateVehFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });

            holder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vehmas_code = Global.latestVehicleslist.get(position).getVehmas_code();
                    deleteimage(vehmas_code);
                }
            });

            holder.lv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String imageUrl = Global.vehicleimageurl + latestVehiclesList.get(position).getLatestvimage();
                    ImageView imageView = new ImageView(context);
                    Picasso.get().load(imageUrl).into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                            Bitmap bitmap = bitmapDrawable.getBitmap();

                            String vehicleDetails = "I'have found this on #SWASTI CARS for your review." + "\n" +
                                    latestVehiclesList.get(position).getLatestvname() + "\n" +
                                    latestVehiclesList.get(position).getMan_year() + "\n" +
                                    latestVehiclesList.get(position).getLatestvdate() + "\n" +
                                    latestVehiclesList.get(position).getFuel_name() + "\n" +
                                    "\u20B9 " + latestVehiclesList.get(position).getSell_price() + "/-" + "\n" +
                                    latestVehiclesList.get(position).getLatestvcolour() + "\n" +
                                    latestVehiclesList.get(position).getVart_name() + "\n" +
                                    latestVehiclesList.get(position).getLatestvcategory() + "\n" +
                                    latestVehiclesList.get(position).getOwn_type() + " OWNER" + "\n" +
                                    latestVehiclesList.get(position).getTrans_name() + "\n" +
                                    latestVehiclesList.get(position).getKm_drvn();

                            shareImageAndText(bitmap, vehicleDetails);
                        }

                        @Override
                        public void onError(Exception e) {
                            // Handle errors if the image couldn't be loaded
                            e.printStackTrace();
                        }
                    });
                }
            });

/*
        holder.lv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Construct the deep link URL for your app
                String deepLinkUrl = "yourapp://open/vehicleDetails?id=" + latestVehiclesList.get(position).getLatestvimage();

                // Create an Intent with the deep link URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl));

                // Create a chooser to allow the user to select an app to handle the intent
                Intent chooser = Intent.createChooser(intent, "Open with");

                // Check if there's an app to handle the intent
                if (intent.resolveActivity(context.getPackageManager()) != null) {

                    // Start the activity with the chooser
                    context.startActivity(chooser);
                }

                // Rest of your code for sharing the image
                // ...

                String imageUrl = Global.vehicleimageurl + latestVehiclesList.get(position).getLatestvimage();
                ImageView imageView = new ImageView(context);
                Picasso.get().load(imageUrl).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();

                        String vehicleDetails = "I have found this on #SWASTI CARS for your review." + "\n" +
                                latestVehiclesList.get(position).getLatestvname() + "\n" +
                                latestVehiclesList.get(position).getMan_year() + "\n" +
                                latestVehiclesList.get(position).getLatestvdate() + "\n" +
                                latestVehiclesList.get(position).getFuel_name() + "\n" +
                                "\u20B9 " + latestVehiclesList.get(position).getSell_price() + "/-" + "\n" +
                                latestVehiclesList.get(position).getLatestvcolour() + "\n" +
                                latestVehiclesList.get(position).getVart_name() + "\n" +
                                latestVehiclesList.get(position).getLatestvcategory() + "\n" +
                                latestVehiclesList.get(position).getOwn_type() + " OWNER" + "\n" +
                                latestVehiclesList.get(position).getTrans_name() + "\n" +
                                latestVehiclesList.get(position).getKm_drvn();

                        shareImageAndText(bitmap, vehicleDetails);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle errors if the image couldn't be loaded
                        e.printStackTrace();
                    }
                });
            }
        });
*/

        /*holder.lv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Construct the deep link URL for your app
                */

            /*String deepLinkUrl = "yourapp://open/vehicleDetails?id=" + latestVehiclesList.get(position).getLatestvimage();*//*
                String deepLinkUrl = "www.google.com" + latestVehiclesList.get(position).getLatestvimage();

                // Create an Intent with the deep link URL
                Intent deepLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl));

                // Check if there's an app to handle the deep link
                if (deepLinkIntent.resolveActivity(context.getPackageManager()) != null) {
                    // Create a custom Intent for sharing the URL
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this vehicle on #SWASTI CARS:\n" + deepLinkUrl);

                    // Create a chooser to allow the user to select an app to handle the intent
                    Intent chooser = Intent.createChooser(shareIntent, "Share with");

                    // Start the activity with the chooser
                    context.startActivity(chooser);
                } else {
                    // If no app is available to handle the deep link, show an error message or handle it as needed
                    Toast.makeText(context, "No app found to handle the deep link", Toast.LENGTH_SHORT).show();
                }

                // Rest of your code for sharing the image
                String imageUrl = Global.vehicleimageurl + latestVehiclesList.get(position).getLatestvimage();
                ImageView imageView = new ImageView(context);
                Picasso.get().load(imageUrl).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();

                        String vehicleDetails = "I have found this on #SWASTI CARS for your review." + "\n" +
                                latestVehiclesList.get(position).getLatestvname() + "\n" +
                                latestVehiclesList.get(position).getMan_year() + "\n" +
                                latestVehiclesList.get(position).getLatestvdate() + "\n" +
                                latestVehiclesList.get(position).getFuel_name() + "\n" +
                                "\u20B9 " + latestVehiclesList.get(position).getSell_price() + "/-" + "\n" +
                                latestVehiclesList.get(position).getLatestvcolour() + "\n" +
                                latestVehiclesList.get(position).getVart_name() + "\n" +
                                latestVehiclesList.get(position).getLatestvcategory() + "\n" +
                                latestVehiclesList.get(position).getOwn_type() + " OWNER" + "\n" +
                                latestVehiclesList.get(position).getTrans_name() + "\n" +
                                latestVehiclesList.get(position).getKm_drvn() + "\n" +
                                "More details: " + deepLinkUrl;

                        shareImageAndText(bitmap, vehicleDetails);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle errors if the image couldn't be loaded
                        e.printStackTrace();
                    }
                });
            }
        });*/

        }

        private void showDetailsAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            Object Activity;
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.alertdailoglayout, null);


            ImageView Veh_image;
            TextView Veh_name, Veh_variant, Veh_year, Veh_trnmsn, Veh_category, Veh_price, Veh_fuel, Veh_colour, Veh_cc, Veh_km, Veh_ownership, Veh_rc,
                    Dealercom_name, Dealercity, Dealermobno, Dealeraltermobno, Dealermailid, Manufacture;


            Manufacture = view.findViewById(R.id.manufacture);
            Veh_image = view.findViewById(R.id.vehindtl_image);
            Veh_name = view.findViewById(R.id.vehindtl_name);
            Veh_year = view.findViewById(R.id.vehindtl_year);
            Veh_trnmsn = view.findViewById(R.id.vehindtl_transmission);
            Veh_category = view.findViewById(R.id.vehindtl_category);
            Veh_variant = view.findViewById(R.id.vehindtl_variant);
            Veh_fuel = view.findViewById(R.id.vehindtl_fuel);
            Veh_colour = view.findViewById(R.id.vehindtl_colour);
            Veh_cc = view.findViewById(R.id.vehindtl_cc);
            Veh_km = view.findViewById(R.id.vehindtl_km);
            Veh_ownership = view.findViewById(R.id.vehindtl_ownership);
            Veh_price = view.findViewById(R.id.vehindtl_price);
            Dealercom_name = view.findViewById(R.id.common_dlrname);
            Dealercity = view.findViewById(R.id.common_dlrcity);
            Dealermobno = view.findViewById(R.id.common_dlrmob);
            Dealeraltermobno = view.findViewById(R.id.common_dlraltmob);
            Dealermailid = view.findViewById(R.id.common_dlrmail);


            Global.loadWithPicasso(context, Veh_image, Global.vehicleimageurl + Global.selectedvehicle.getVeh_image());

            Manufacture.setText(Global.selectedvehicle.getVeh_mfgname());
            Veh_name.setText(Global.selectedvehicle.getVeh_fullname());
            Veh_variant.setText(Global.selectedvehicle.getVeh_variant());
            Veh_year.setText(Global.selectedvehicle.getVeh_year());
            Veh_trnmsn.setText(Global.selectedvehicle.getVeh_transmission());
            Veh_category.setText(Global.selectedvehicle.getVeh_model());
            Veh_colour.setText(Global.selectedvehicle.getVeh_colour());
            Veh_cc.setText(Global.selectedvehicle.getVeh_cc() + " cc");
            Veh_km.setText(Global.selectedvehicle.getVeh_km() + " km");
            Veh_ownership.setText(Global.selectedvehicle.getVeh_ownership() + " OWNER");
            Veh_fuel.setText(Global.selectedvehicle.getVeh_fuel());
            Veh_price.setText(Global.selectedvehicle.getVeh_price());


            Dealercom_name.setText(Global.selectedvehicle.getDealercom_name());
            Dealercity.setText(Global.selectedvehicle.getDealer_city());
            Dealermobno.setText(Global.selectedvehicle.getDealer_mob());
            Dealeraltermobno.setText(Global.selectedvehicle.getDealer_altermob());
            Dealermailid.setText(Global.selectedvehicle.getDealermailid());



              /*  @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(context, Global.selectedvehicle);
        viewPager.setAdapter(imagePagerAdapter);
*/


            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


        private void sendMessage(String vehicleDetails, String imageUrl) {

            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");

            intent.putExtra(Intent.EXTRA_TEXT, vehicleDetails);

            PackageManager packageManager = context.getPackageManager();
            if (intent.resolveActivity(packageManager) == null) {
                Toast.makeText(context, "Make sure whatsapp is installed.", Toast.LENGTH_SHORT).show();
                return;
            }

            context.startActivity(intent);

        }


        private void shareImageAndText(Bitmap bitmap, String vehicleDetails) {
            Uri uri = getImageToShare(bitmap);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_TEXT, vehicleDetails);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            intent.setType("image/png");
            intent.setPackage("com.whatsapp");
            // startActivity(Intent.createChooser(intent, "Share Via"));
            context.startActivity(intent);
        }

        private Uri getImageToShare(Bitmap bitmap) {
            File imagefolder = new File(context.getCacheDir(), "images");
            Uri uri = null;
            try {
                imagefolder.mkdirs();
                File file = new File(imagefolder, "shared_image.png");
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.flush();
                outputStream.close();
                uri = FileProvider.getUriForFile(context, "com.ziac.carz.fileprovider", file);
            } catch (Exception e) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return uri;
        }

        @Override
        public int getItemCount() {
            return latestVehiclesList.size();
        }

        public static class RecentcarsViewHolder extends RecyclerView.ViewHolder {

            private ImageView lv_image;
            MaterialButton Edit, Delete;
            TextView lv_name, lv_ownership;
            private TextView lv_category, lv_variant, lv_transmission, lvcolor;
            private TextView lv_fuel, lv_date, lv_sellingprice;

            @SuppressLint("CutPasteId")
            public RecentcarsViewHolder(@NonNull View itemView) {
                super(itemView);

                lv_image = itemView.findViewById(R.id.lv_veh_img);
                lv_name = itemView.findViewById(R.id.lv_veh_name);
                lv_ownership = itemView.findViewById(R.id.lv_veh_ownership);
                lv_category = itemView.findViewById(R.id.lv_veh_category);
                lv_variant = itemView.findViewById(R.id.lv_veh_varient);
                lv_transmission = itemView.findViewById(R.id.lv_veh_transmission);
                lvcolor = itemView.findViewById(R.id.lv_veh_colour);
                Edit = itemView.findViewById(R.id.edit_icon);
                lv_fuel = itemView.findViewById(R.id.lv_veh_fuel);
                lv_date = itemView.findViewById(R.id.lv_veh_dateTime);
                lv_sellingprice = itemView.findViewById(R.id.lv_veh_sellingprice);
                Delete = itemView.findViewById(R.id.delete_icon);
            }
        }
    }

    public class LessDrivenAdapter extends RecyclerView.Adapter<LessDrivenAdapter.LessDrivenViewholder> {

        private final List<LessDrivenClass> lessDrivenCarsList;
        private int clickedPosition = RecyclerView.NO_POSITION;
        Context context;


        public LessDrivenAdapter(List<LessDrivenClass> lessDrivenCarsList, Context context) {
            this.lessDrivenCarsList = lessDrivenCarsList;
            this.context = context;

        }

        @NonNull
        @Override
        public LessDrivenViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.less_driven, parent, false);
            return new LessDrivenViewholder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull LessDrivenViewholder holder, @SuppressLint("RecyclerView") int position) {

            String createdby = lessDrivenCarsList.get(position).getCreatedby();
            String userName = Global.sharedPreferences.getString("userName", "");

            if (createdby.equals(userName)) {
                holder.PEdit.setVisibility(View.VISIBLE);
                holder.pDelete.setVisibility(View.VISIBLE);

            } else {
                holder.PEdit.setVisibility(View.GONE);
                holder.pDelete.setVisibility(View.GONE);
            }

            Global.loadWithPicasso(context, holder.ld_vimage, Global.vehimageurl + lessDrivenCarsList.get(position).getLd_vimg());
            holder.ld_vname.setText(lessDrivenCarsList.get(position).getLd_vname());
            holder.ld_vcolour.setText(lessDrivenCarsList.get(position).getLd_vcolor());
            holder.ld_vyear.setText(lessDrivenCarsList.get(position).getMan_year());
            holder.ld_model.setText(lessDrivenCarsList.get(position).getLd_vcategory());
            holder.ld_v_variant.setText(lessDrivenCarsList.get(position).getVart_name());
            holder.ld_Vtransmission.setText(lessDrivenCarsList.get(position).getTrans_name());
            holder.ld_vownership.setText(lessDrivenCarsList.get(position).getOwn_type() + " OWNER");
            holder.ld_vkm.setText(lessDrivenCarsList.get(position).getKm_drvn());
            holder.ld_vfuel.setText(lessDrivenCarsList.get(position).getFuel_name());
            holder.ld_selling_price.setText("\u20B9 " + lessDrivenCarsList.get(position).getSell_price() + "/-");

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    VehicleClass vehicleClass = new VehicleClass();
                    vehicleClass.setVeh_mfgname(lessdrivenlist.get(position).getLd_mfgname());
                    vehicleClass.setVeh_image(lessdrivenlist.get(position).getLd_vimg());
                    vehicleClass.setVeh_fullname(lessdrivenlist.get(position).getLd_vname());
                    vehicleClass.setVeh_year(lessdrivenlist.get(position).getMan_year());
                    vehicleClass.setVeh_colour(lessdrivenlist.get(position).getLd_vcolor());
                    vehicleClass.setVeh_model(lessdrivenlist.get(position).getLd_vcategory());
                    vehicleClass.setVeh_variant(lessdrivenlist.get(position).getVart_name());
                    vehicleClass.setVeh_transmission(lessdrivenlist.get(position).getTrans_name());
                    vehicleClass.setVeh_ownership(lessdrivenlist.get(position).getOwn_type());
                    vehicleClass.setVeh_km(lessdrivenlist.get(position).getKm_drvn());
                    vehicleClass.setVeh_fuel(lessdrivenlist.get(position).getFuel_name());
                    vehicleClass.setVeh_price(lessdrivenlist.get(position).getSell_price());
                    vehicleClass.setVeh_cc(lessdrivenlist.get(position).getLd_vcc());
                    vehicleClass.setVeh_price(lessdrivenlist.get(position).getSell_price());
                    vehicleClass.setDealercom_name(lessdrivenlist.get(position).getLd_dname());
                    vehicleClass.setDealer_mob(lessdrivenlist.get(position).getLd_dmob());
                    vehicleClass.setDealer_altermob(lessdrivenlist.get(position).getLd_altermob());
                    vehicleClass.setDealermailid(lessdrivenlist.get(position).getLd_dmail());
                    vehicleClass.setDealer_city(lessdrivenlist.get(position).getLd_dcity());
                    Global.selectedvehicle = vehicleClass;
                    showDetailsAlertDialog();

                }
            });

            holder.PEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonVehClass commonVehClass = new CommonVehClass();
                    commonVehClass.setVart_code(Global.lessdrivenlist.get(position).getVart_code());
                    commonVehClass.setVart_name(Global.lessdrivenlist.get(position).getVart_name());
                    commonVehClass.setTrans_name(Global.lessdrivenlist.get(position).getTrans_name());
                    commonVehClass.setKm_drvn(Global.lessdrivenlist.get(position).getKm_drvn());
                    commonVehClass.setNum_plate(Global.lessdrivenlist.get(position).getNum_plate());
                    commonVehClass.setMan_year(Global.lessdrivenlist.get(position).getMan_year());
                    commonVehClass.setOwn_type(Global.lessdrivenlist.get(position).getOwn_type());
                    commonVehClass.setFuel_code(Global.lessdrivenlist.get(position).getFuel_code());
                    commonVehClass.setFuel_name(Global.lessdrivenlist.get(position).getFuel_name());
                    commonVehClass.setSell_price(Global.lessdrivenlist.get(position).getSell_price());
                    commonVehClass.setVehmas_code(Global.lessdrivenlist.get(position).getVehmas_code());
                    commonVehClass.setReg_no(Global.lessdrivenlist.get(position).getReg_no());
                    commonVehClass.setInsc_type(Global.lessdrivenlist.get(position).getInsc_type());
                    commonVehClass.setLis_type(Global.lessdrivenlist.get(position).getLis_type());
                    commonVehClass.setCity_code(Global.lessdrivenlist.get(position).getCity_code());
                    commonVehClass.setState_code(Global.lessdrivenlist.get(position).getState_code());
                    commonVehClass.setVeh_img(Global.lessdrivenlist.get(position).getLd_vimg());
                    Global.commonVehClass = commonVehClass;

                    UpdateVehFragment updateVehFragment = new UpdateVehFragment();
                    fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout, updateVehFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });

            holder.pDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vehmas_code = Global.lessdrivenlist.get(position).getVehmas_code();
                    deleteimage(vehmas_code);
                }
            });

        }

        public int getClickedPosition() {
            return clickedPosition;
        }

        @SuppressLint("MissingInflatedId")
        private void showDetailsAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.alertdailoglayout, null);


            ImageView Veh_image;
            TextView Veh_name, Veh_variant, Veh_year, Veh_trnmsn, Veh_category, Veh_price, Veh_fuel, Veh_colour, Veh_cc, Veh_km, Veh_ownership, Veh_rc,
                    Dealercom_name, Dealercity, Dealermobno, Dealeraltermobno, Dealermailid, Manufacture;


            Manufacture = view.findViewById(R.id.manufacture);
            Veh_image = view.findViewById(R.id.vehindtl_image);
            Veh_name = view.findViewById(R.id.vehindtl_name);
            Veh_year = view.findViewById(R.id.vehindtl_year);
            Veh_trnmsn = view.findViewById(R.id.vehindtl_transmission);
            Veh_category = view.findViewById(R.id.vehindtl_category);
            Veh_variant = view.findViewById(R.id.vehindtl_variant);
            Veh_fuel = view.findViewById(R.id.vehindtl_fuel);
            Veh_colour = view.findViewById(R.id.vehindtl_colour);
            Veh_cc = view.findViewById(R.id.vehindtl_cc);
            Veh_km = view.findViewById(R.id.vehindtl_km);
            Veh_ownership = view.findViewById(R.id.vehindtl_ownership);
            Veh_price = view.findViewById(R.id.vehindtl_price);
            Dealercom_name = view.findViewById(R.id.common_dlrname);
            Dealercity = view.findViewById(R.id.common_dlrcity);
            Dealermobno = view.findViewById(R.id.common_dlrmob);
            Dealeraltermobno = view.findViewById(R.id.common_dlraltmob);
            Dealermailid = view.findViewById(R.id.common_dlrmail);


            Global.loadWithPicasso(context, Veh_image, Global.vehicleimageurl + Global.selectedvehicle.getVeh_image());

            Manufacture.setText(Global.selectedvehicle.getVeh_mfgname());
            Veh_name.setText(Global.selectedvehicle.getVeh_fullname());
            Veh_variant.setText(Global.selectedvehicle.getVeh_variant());
            Veh_year.setText(Global.selectedvehicle.getVeh_year());
            Veh_trnmsn.setText(Global.selectedvehicle.getVeh_transmission());
            Veh_category.setText(Global.selectedvehicle.getVeh_model());
            Veh_colour.setText(Global.selectedvehicle.getVeh_colour());
            Veh_cc.setText(Global.selectedvehicle.getVeh_cc() + " cc");
            Veh_km.setText(Global.selectedvehicle.getVeh_km() + " km");
            Veh_ownership.setText(Global.selectedvehicle.getVeh_ownership() + " OWNER");
            Veh_fuel.setText(Global.selectedvehicle.getVeh_fuel());
            Veh_price.setText(Global.selectedvehicle.getVeh_price());


            Dealercom_name.setText(Global.selectedvehicle.getDealercom_name());
            Dealercity.setText(Global.selectedvehicle.getDealer_city());
            Dealermobno.setText(Global.selectedvehicle.getDealer_mob());
            Dealeraltermobno.setText(Global.selectedvehicle.getDealer_altermob());
            Dealermailid.setText(Global.selectedvehicle.getDealermailid());

            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        private void sendMessage(String vehicleDetails, String imageUrl) {

            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");

            intent.putExtra(Intent.EXTRA_TEXT, vehicleDetails);

            PackageManager packageManager = context.getPackageManager();
            if (intent.resolveActivity(packageManager) == null) {
                Toast.makeText(context, "Make sure whatsapp is installed.", Toast.LENGTH_SHORT).show();
                return;
            }

            context.startActivity(intent);

        }


        @Override
        public int getItemCount() {
            return lessDrivenCarsList.size();
        }


        class LessDrivenViewholder extends RecyclerView.ViewHolder {

            private String vmodel_code;
            ImageView ld_vimage;
            LinearLayout PEdit, pDelete;
            TextView ld_vname, ld_vcolour, ld_vyear, ld_model, ld_v_variant, ld_Vtransmission, ld_vownership, ld_vkm, ld_vfuel, ld_selling_price;
            LinearLayout cardView;

            public LessDrivenViewholder(@NonNull View itemView) {
                super(itemView);

                cardView = itemView.findViewById(R.id.lessdrvcardview);
                ld_vimage = itemView.findViewById(R.id.lessdrv_vehicle_image);
                ld_vname = itemView.findViewById(R.id.lessdrv_vehicle_name);
                ld_vcolour = itemView.findViewById(R.id.lessdrv_vehicle_colour);
                ld_vyear = itemView.findViewById(R.id.lessdrv_vehicle_year);
                ld_model = itemView.findViewById(R.id.lessdrv_vehicle_model);
                ld_v_variant = itemView.findViewById(R.id.lessdrv_vehicle_type);
                ld_Vtransmission = itemView.findViewById(R.id.lessdrv_vehicle_trensmission);
                ld_vownership = itemView.findViewById(R.id.lessdrv_vehicle_ownership);
                ld_vkm = itemView.findViewById(R.id.lessdrv_vehicle_km);
                ld_vfuel = itemView.findViewById(R.id.lessdrv_vehicle_fuel);
                ld_selling_price = itemView.findViewById(R.id.lessdrv_veh_sellingprice);
                PEdit = itemView.findViewById(R.id.edit_icon);
                pDelete = itemView.findViewById(R.id.delete_icon);
            }
        }
    }

    private void deleteimage(String vehmas_code) {
        String url = Global.deletevehicle + "vehmas_code=" + vehmas_code;

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject respObj = new JSONObject(response.trim());

                        boolean isSuccess = respObj.optBoolean("isSuccess", false);
                        String message = respObj.has("error") ? respObj.getString("error") : "No message provided.";

                        // Show toast whether success or failure
                        Global.customtoast(requireActivity(), getLayoutInflater(), message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Global.customtoast(requireActivity(), getLayoutInflater(), "Error parsing server response");
                    }
                },
                error -> {
                    String errorMsg;

                    if (error instanceof TimeoutError) {
                        errorMsg = "Request Timed Out";
                    } else if (error instanceof NoConnectionError) {
                        errorMsg = "No Internet Connection";
                    } else if (error instanceof ServerError) {
                        errorMsg = "Server Error";
                    } else if (error instanceof NetworkError) {
                        errorMsg = "Network Error";
                    } else if (error instanceof ParseError) {
                        errorMsg = "Response Parse Error";
                    } else {
                        errorMsg = "Unexpected Error Occurred";
                    }

                    Global.customtoast(requireActivity(), getLayoutInflater(), errorMsg);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accessToken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>(); // Add POST parameters here if needed
            }
        };

        // Optional retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }


}




