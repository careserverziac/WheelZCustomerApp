package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_vehcile, container, false);
        Context context = requireContext();
        VehiclelistRV=view.findViewById(R.id.myvehiclelist);
        Registervehicle =view.findViewById(R.id.registervehicle);


        GetAllvehicles();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        VehiclelistRV.setLayoutManager(linearLayoutManager);
        VehiclelistRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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

        return  view;
    }



    private void  GetAllvehicles() {

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String wuser_code = Global.sharedPreferences.getString("wuser_code", "");


        RequestQueue queue= Volley.newRequestQueue(requireActivity());

        String url = Global.getallMyVehicles;
        String Url = url+"wuser_code="+wuser_code;

        @SuppressLint("NotifyDataSetChanged")
        StringRequest request = new StringRequest(Request.Method.GET, Url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                Global.allleadslist = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    String vehimage = jsonObject.getString("veh_image1");
                    String wuser_code1 = jsonObject.getString("wuser_code");
                    String com_code = jsonObject.getString("com_code");
                    String vehhis_code = jsonObject.getString("vehhis_code");
                    String cveh_code = jsonObject.getString("cveh_code");


                    String Veh_modelname = jsonObject.getString("model_name");
                    String mfg_name = jsonObject.getString("mfg_name");
                    String chasis_no = jsonObject.getString("chasis_no");
                    String engine_no = jsonObject.getString("engine_no");
                    String Regis_no = jsonObject.getString("reg_no");
                    String batt_no = jsonObject.getString("batt_no");
                    String vcol_name = jsonObject.getString("vcol_name");
                    String prv_serdt = jsonObject.getString("prv_serdt");
                    String nxt_serdt = jsonObject.getString("nxt_serdt");



                    CommonClass commonClass = new CommonClass();
                    commonClass.setImage_path(vehimage);
                    commonClass.setWuser_code(wuser_code1);
                    commonClass.setRegis_no(Regis_no);
                    commonClass.setVehiclemodelname(Veh_modelname);
                    commonClass.setMfg_name(mfg_name);
                    commonClass.setChassis_no(chasis_no);
                    commonClass.setEngine_no(engine_no);
                    commonClass.setRegis_no(Regis_no);
                    commonClass.setBatt_no(batt_no);
                    commonClass.setVcol_name(vcol_name);
                    commonClass.setPrv_serdt(prv_serdt);
                    commonClass.setNxt_serdt(nxt_serdt);
                    commonClass.setCveh_code(cveh_code);
                    commonClass.setVehhis_code(vehhis_code);




                    Global.allleadslist.add(commonClass);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            vehiclesAdapter = new VehiclesAdapter(Global.allleadslist,getContext());
            VehiclelistRV.setAdapter(vehiclesAdapter);
            vehiclesAdapter.notifyDataSetChanged();
           // swipeRefreshLayout.setRefreshing(false);

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
               // swipeRefreshLayout.setRefreshing(false);
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

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

}