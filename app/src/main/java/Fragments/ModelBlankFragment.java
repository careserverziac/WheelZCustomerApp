package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import AdapterClass.ModelsAdapter;
import ModelClasses.CommonClass;
import ModelClasses.Global;

public class ModelBlankFragment extends Fragment {
    RecyclerView VehicleelistRV;
    ModelsAdapter modelsAdapter;
    ProgressBar progressBar;
    LinearLayout LinearSearch;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    CommonClass commonClass;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView tvBookingCount;
    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_model_blank, container, false);

        Context context = requireContext();

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());


        VehicleelistRV = view.findViewById(R.id.vehlisthorizontal);
        progressBar = view.findViewById(R.id.progressBarmodels);
        swipeRefreshLayout = view.findViewById(R.id.refreshprofile);

        collapsingToolbar = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle("Models");
        toolbar = view.findViewById(R.id.toolbar);

        GetAllModels();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        VehicleelistRV.setLayoutManager(linearLayoutManager);
        VehicleelistRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(this::GetAllModels);

        LinearSearch = view.findViewById(R.id.modelsearchlnr);
        searchView = view.findViewById(R.id.searchallmodels);

    /*    view.findViewById(R.id.btnBack).setOnClickListener(v ->  requireActivity()
                .getSupportFragmentManager()
                .popBackStack());*/

// Focus on EditText when search layout is clicked
        LinearSearch.setOnClickListener(v -> {
            searchView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                swipeRefreshLayout.setEnabled(false);
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
                return false;
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


        return view;
    }

    private void performSearch(String query) {


        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.searchallbrands + query;

        @SuppressLint("NotifyDataSetChanged")
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                Global.allleadslist = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String vmodelCode = jsonObject.getString("vmodel_code");
                    String vehimage = jsonObject.getString("veh_image1");
                    String vcate_name = jsonObject.getString("vcate_name");
                    String app_model_name = jsonObject.getString("app_model_name");
                    String mfg_name = jsonObject.getString("mfg_name");
                    String veh_cc = jsonObject.getString("veh_cc");
                    String veh_bhp = jsonObject.getString("veh_bhp");
                    String veh_top_speed = jsonObject.getString("veh_top_speed");
                    String body_type = jsonObject.getString("body_type");
                    String fuel_name = jsonObject.getString("fuel_name");
                    String sale_price = jsonObject.getString("sale_price");
                    String charging_time = jsonObject.getString("charging_time");

                    CommonClass commonClass = new CommonClass();
                    commonClass.setCategory(vmodelCode);
                    commonClass.setImage_path(vehimage);
                    commonClass.setCategory(vcate_name);
                    commonClass.setManufacture(mfg_name);
                    commonClass.setCc(veh_cc);
                    commonClass.setBhp(veh_bhp);
                    commonClass.setTopspeed(veh_top_speed);
                    commonClass.setBodytype(body_type);
                    commonClass.setFuelname(fuel_name);
                    commonClass.setModel_name(app_model_name);
                    commonClass.setChargingtime(charging_time);
                    commonClass.setSaleprice(sale_price);

                    Global.allleadslist.add(commonClass);
                    swipeRefreshLayout.setRefreshing(false);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            modelsAdapter = new ModelsAdapter(Global.allleadslist, getContext());
            VehicleelistRV.setAdapter(modelsAdapter);
            modelsAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

        }, error -> {

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
                swipeRefreshLayout.setRefreshing(false);
            }

        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
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

    private void GetAllModels() {

        showLoading();
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.getallbrands;

        @SuppressLint("NotifyDataSetChanged")
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() == 0) {
                    hideLoading();
                    return;
                }
                Global.allleadslist = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    String vmodelCode = jsonObject.getString("vmodel_code");
                    String vehimage = jsonObject.getString("veh_image1");
                    String vcate_name = jsonObject.getString("vcate_name");
                    String mfg_name = jsonObject.getString("mfg_name");
                    String veh_cc = jsonObject.getString("veh_cc");
                    String veh_bhp = jsonObject.getString("veh_bhp");
                    String veh_top_speed = jsonObject.getString("veh_top_speed");
                    String body_type = jsonObject.getString("body_type");
                    String fuel_name = jsonObject.getString("fuel_name");
                    String sale_price = jsonObject.getString("sale_price");
                    String charging_time = jsonObject.getString("charging_time");
                    String modelname = jsonObject.getString("model_name");

                    commonClass = new CommonClass();
                    commonClass.setModel_code(vmodelCode);
                    commonClass.setImage_path(vehimage);
                    commonClass.setCategory(vcate_name);
                    commonClass.setManufacture(mfg_name);
                    commonClass.setCc(veh_cc);
                    commonClass.setBhp(veh_bhp);
                    commonClass.setTopspeed(veh_top_speed);
                    commonClass.setBodytype(body_type);
                    commonClass.setFuelname(fuel_name);
                    commonClass.setChargingtime(charging_time);
                    commonClass.setSaleprice(sale_price);
                    commonClass.setModel_name(modelname);

                    Global.allleadslist.add(commonClass);
                    swipeRefreshLayout.setRefreshing(false);

                    hideLoading();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            modelsAdapter = new ModelsAdapter(Global.allleadslist, getContext());
            VehicleelistRV.setAdapter(modelsAdapter);
            modelsAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

        }, error -> {

            hideLoading();
            ;
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
                swipeRefreshLayout.setRefreshing(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
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
        VehicleelistRV.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        VehicleelistRV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        GetAllModels();
    }
}