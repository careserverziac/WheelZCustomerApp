package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.ziac.wheelzcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ModelClasses.CommonClass;
import ModelClasses.Global;
import ModelClasses.ServiceDetails;


public class ServiceListFragment extends Fragment {

    RecyclerView ServicelistRV;
    ServiceListAdapter adapter;
    String wuser_code;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        ServicelistRV = view.findViewById(R.id.servicelist);
        ServicelistRV.setLayoutManager(new LinearLayoutManager(requireContext()));

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        wuser_code = Global.sharedPreferences.getString("wuser_code", "");
        Servicehistory();


        return view;
    }

   private void Servicehistory() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = Global.getGetBookings;
        String Url = url + "wuser_code=" + wuser_code;

        @SuppressLint("NotifyDataSetChanged")
        StringRequest request = new StringRequest(Request.Method.GET, Url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                Global.allleadslist = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String service_code = jsonObject.getString("service_code");
                    String com_code = jsonObject.getString("com_code");
                    String service_date = jsonObject.getString("service_date");
                    String service_time = jsonObject.getString("service_time");
                    String kms_done = jsonObject.getString("kms_done");
                    String service_type = jsonObject.getString("service_type");
                    String pick_flag = jsonObject.getString("pick_flag");
                    String drop_flag = jsonObject.getString("drop_flag");
                    String additional_info = jsonObject.getString("additional_info");
                    String location_map = jsonObject.getString("location_map");
                    String cveh_code = jsonObject.getString("cveh_code");
                    String vehhis_code = jsonObject.getString("vehhis_code");
                    String wuser_code = jsonObject.getString("wuser_code");
                    String service_address = jsonObject.getString("service_address");
                    String veh_image1 = jsonObject.getString("veh_image1");
                    String model_name = jsonObject.getString("model_name");
                    String com_name = jsonObject.getString("com_name");
                    String com_email = jsonObject.getString("com_email");
                    String com_phone = jsonObject.getString("com_phone");
                    String com_address = jsonObject.getString("com_address");

                    CommonClass commonClass = new CommonClass();
                    commonClass.setService_code(service_code);
                    commonClass.setCom_code(com_code);
                    commonClass.setService_date(service_date);
                    commonClass.setService_time(service_time);
                    commonClass.setKms_done(kms_done);
                    commonClass.setService_type(service_type);
                    commonClass.setCom_name(com_name);
                    commonClass.setPick_flag(pick_flag);
                    commonClass.setDrop_flag(drop_flag);
                    commonClass.setKms_done(kms_done);
                    commonClass.setAdditional_info(additional_info);
                    commonClass.setLocation_map(location_map);
                    commonClass.setCveh_code(cveh_code);
                    commonClass.setKms_done(kms_done);
                    commonClass.setVehhis_code(vehhis_code);
                    commonClass.setCom_name(com_name);
                    commonClass.setWuser_code(wuser_code);
                    commonClass.setService_address(service_address);
                    commonClass.setImage_path(veh_image1);
                    commonClass.setModel_name(model_name);
                    commonClass.setCom_name(com_name);
                    commonClass.setModel_name(com_email);
                    commonClass.setCom_phone(com_phone);
                    commonClass.setCom_address(com_address);

                    Global.allleadslist.add(commonClass);

                }
                adapter = new ServiceListAdapter(requireContext(), Global.allleadslist);
                ServicelistRV.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }


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

    public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {

        private List<CommonClass> serviceHistoryList;
        private Context context;

        public ServiceListAdapter(Context context, List<CommonClass> serviceHistoryList) {
            this.context = context;
            this.serviceHistoryList = serviceHistoryList;
        }

        @NonNull
        @Override
        public ServiceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.service_list, parent, false);
            return new ServiceListAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ServiceListAdapter.ViewHolder holder, int position) {
            CommonClass service = serviceHistoryList.get(position);

            holder.Modelname.setText(service.getModel_name());
            holder.Servicetype.setText(service.getService_type());
            double kmsDoneValue = Double.parseDouble(service.getKms_done());
            String kmsDone = (kmsDoneValue % 1 == 0) ? String.valueOf((int) kmsDoneValue) : String.valueOf(kmsDoneValue);
            holder.Kmdone.setText(kmsDone + " Km");

            String originalDateStr = service.getService_date();
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yy");

            Date date = null;
            try {
                date = originalFormat.parse(originalDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String formattedDateTimeStr = targetFormat.format(date);
            holder.Dateandtime.setText(formattedDateTimeStr);


        }

        @Override
        public int getItemCount() {
            return serviceHistoryList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView Modelname, Servicetype, Totalamount, Kmdone, Dateandtime;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                Modelname = itemView.findViewById(R.id.modelname);
                Servicetype = itemView.findViewById(R.id.servicetype);
                Totalamount = itemView.findViewById(R.id.totalamount);
                Kmdone = itemView.findViewById(R.id.kmdone);
                Dateandtime = itemView.findViewById(R.id.dateandtime);
            }
        }

    }

}
























