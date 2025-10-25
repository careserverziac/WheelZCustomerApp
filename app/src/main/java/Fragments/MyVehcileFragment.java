package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ziac.wheelzcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import AdapterClass.VehiclesAdapter;
import ModelClasses.CommonClass;
import ModelClasses.Global;


public class MyVehcileFragment extends Fragment {

    RecyclerView VehiclelistRV;
    LinearLayout Registervehicle;
    FragmentManager fragmentManager;
    VehiclesAdapter vehiclesAdapter;
    ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView tvBookingCount;
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_vehcile, container, false);
        Context context = requireContext();
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        VehiclelistRV=view.findViewById(R.id.myvehiclelist);
        Registervehicle =view.findViewById(R.id.registervehicle);
        progressBar =view.findViewById(R.id.progressBar);
        toolbar = view.findViewById(R.id.toolbar);
        swipeRefreshLayout=view.findViewById(R.id.refreshprofile);

        VehicleinDetail();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        VehiclelistRV.setLayoutManager(linearLayoutManager);
        VehiclelistRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        swipeRefreshLayout.setOnRefreshListener(this::VehicleinDetail);

        Registervehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterVehicleFragment registerVehicleFragment =new RegisterVehicleFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, registerVehicleFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, new DashboardFragment()) // replace with your fragment class
                        .addToBackStack(null)
                        .commit();
            }
        });

        return  view;
    }
    private void VehicleinDetail() {
        // ✅ Check if fragment is attached before starting anything
        if (!isAdded() || getContext() == null) return;

        // ✅ Show loader at start
        showLoading();

        Context context = getContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String baseUrl = Global.getallMyVehicles;
        String userCode = Global.sharedPreferences.getString("wuser_code", "");
        String fullUrl = baseUrl + "wuser_code=" + userCode;

        StringRequest request = new StringRequest(Request.Method.GET, fullUrl, response -> {
            // ✅ Stop if fragment was detached while waiting for response
            if (!isAdded() || getContext() == null) return;

            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean isSuccess = jsonObjectResponse.optBoolean("isSuccess", false);
                String message = jsonObjectResponse.optString("message", "");

                if (isSuccess) {
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("data");
                    Global.myvehiclelist = new ArrayList<>();

                    if (jsonArray.length() == 0) {
                        LayoutInflater inflater = LayoutInflater.from(context);
                        Global.customtoast(context, inflater, "No vehicles found.");
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String vehimage = jsonObject.optString("veh_image1", "");
                            String wuser_code1 = jsonObject.optString("wuser_code", "");
                            String com_code = jsonObject.optString("com_code", "");
                            String vehhis_code = jsonObject.optString("vehhis_code", "");
                            String cveh_code = jsonObject.optString("cveh_code", "");
                            String Veh_modelname = jsonObject.optString("model_name", "");
                            String mfg_name = jsonObject.optString("mfg_name", "");
                            String chasis_no = jsonObject.optString("chasis_no", "");
                            String engine_no = jsonObject.optString("engine_no", "");
                            String Regis_no = jsonObject.optString("reg_no", "");
                            String batt_no = jsonObject.optString("batt_no", "");
                            String vcol_name = jsonObject.optString("vcol_name", "");
                            String prv_serdt = jsonObject.optString("prv_serdt", "");
                            String nxt_serdt = jsonObject.optString("nxt_serdt", "");

                            CommonClass commonClass = new CommonClass();
                            commonClass.setImage_path(vehimage);
                            commonClass.setWuser_code(wuser_code1);
                            commonClass.setRegis_no(Regis_no);
                            commonClass.setVehiclemodelname(Veh_modelname);
                            commonClass.setMfg_name(mfg_name);
                            commonClass.setChassis_no(chasis_no);
                            commonClass.setEngine_no(engine_no);
                            commonClass.setBatt_no(batt_no);
                            commonClass.setVcol_name(vcol_name);
                            commonClass.setPrv_serdt(prv_serdt);
                            commonClass.setNxt_serdt(nxt_serdt);
                            commonClass.setCveh_code(cveh_code);
                            commonClass.setVehhis_code(vehhis_code);

                            // ✅ Use safe context
                            Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            Global.editor = Global.sharedPreferences.edit();
                            Global.editor.putString("vehhis_code", vehhis_code);
                            Global.editor.apply();

                            Global.myvehiclelist.add(commonClass);
                        }

                        // ✅ Update UI only if fragment is still attached
                        if (isAdded() && getContext() != null) {
                            vehiclesAdapter = new VehiclesAdapter(Global.myvehiclelist, context);
                            VehiclelistRV.setAdapter(vehiclesAdapter);
                            vehiclesAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                } else {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    Global.customtoast(context, inflater, message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                if (isAdded()) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    Global.customtoast(context, inflater, "Parsing error");
                }
            } finally {
                // ✅ Always hide loader safely
                if (isAdded()) hideLoading();
                swipeRefreshLayout.setRefreshing(false);

            }

        }, error -> {
            // ✅ Hide loader safely on error
            if (isAdded()) hideLoading();
            if (!isAdded() || getContext() == null) return;
            swipeRefreshLayout.setRefreshing(false);


            LayoutInflater inflater = LayoutInflater.from(context);
            String errorMsg;
            if (error instanceof TimeoutError) {
                errorMsg = "Request timed out. Please try again.";
            } else if (error instanceof NoConnectionError) {
                errorMsg = "No internet connection.";
            } else if (error instanceof ServerError) {
                errorMsg = "Server error occurred.";
            } else if (error instanceof NetworkError) {
                errorMsg = "Network error.";
            } else if (error instanceof ParseError) {
                errorMsg = "Response parsing failed.";
            } else {
                errorMsg = "Something went wrong.";
            }
            Global.customtoast(context, inflater, errorMsg);

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accessToken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }


    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}