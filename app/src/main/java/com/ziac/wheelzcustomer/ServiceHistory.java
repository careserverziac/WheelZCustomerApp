package com.ziac.wheelzcustomer;

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


 public class ServiceHistory extends Fragment {

     RecyclerView ServiceRV;
     ServiceHistoryAdapter adapter;
     String vehhis_code;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_service_history, container, false);

        ServiceRV=view.findViewById(R.id.servicerv);
        ServiceRV.setLayoutManager(new LinearLayoutManager(requireContext()));


        vehhis_code=Global.vehicledetails.getVehhis_code();

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Servicehistory();
        return view;
    }

    private void Servicehistory() {

        RequestQueue queue= Volley.newRequestQueue(requireActivity());

        String url = Global.getservicehistory;
        String Url = url+"vehhis_code="+vehhis_code;

        @SuppressLint("NotifyDataSetChanged")
        StringRequest request = new StringRequest(Request.Method.POST, Url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                Global.allleadslist = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String com_name = jsonObject.getString("com_name");
                    String jobtype_name = jsonObject.getString("jobtype_name");
                    String Veh_modelname = jsonObject.getString("model_name");
                    String total_amt = jsonObject.getString("total_amt");
                    String jc_date = jsonObject.getString("jc_date");
                    String kms_done = jsonObject.getString("kms_done");


                    CommonClass commonClass = new CommonClass();
                    commonClass.setJobtype_name(jobtype_name);
                    commonClass.setCom_name(com_name);
                    commonClass.setTotal_amt(total_amt);
                    commonClass.setJc_datec(jc_date);
                    commonClass.setKms_done(kms_done);

                    Global.allleadslist.add(commonClass);

                }
                adapter = new ServiceHistoryAdapter(requireContext(), Global.allleadslist);
                ServiceRV.setAdapter(adapter);
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

     public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.ViewHolder> {

         private List<CommonClass> serviceHistoryList;
         private Context context;

         public ServiceHistoryAdapter(Context context, List<CommonClass> serviceHistoryList) {
             this.context = context;
             this.serviceHistoryList = serviceHistoryList;
         }

         @NonNull
         @Override
         public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View view = LayoutInflater.from(context).inflate(R.layout.service_history, parent, false);
             return new ViewHolder(view);
         }

         @Override
         public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
             CommonClass service = serviceHistoryList.get(position);

             holder.Modelname.setText(service.getCom_name());
             holder.Servicetype.setText(service.getJobtype_name());
             String totalAmount = "₹ " + service.getTotal_amt();
             SpannableStringBuilder spannableString = new SpannableStringBuilder(totalAmount);
             spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
             holder.Totalamount.setText(spannableString);
             double kmsDoneValue = Double.parseDouble(service.getKms_done());
             String kmsDone = (kmsDoneValue % 1 == 0) ? String.valueOf((int) kmsDoneValue) : String.valueOf(kmsDoneValue);
             holder.Kmdone.setText(kmsDone+" Km");

             String originalDateStr = service.getJc_datec();
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

             TextView Modelname,Servicetype,Totalamount,Kmdone,Dateandtime;


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
