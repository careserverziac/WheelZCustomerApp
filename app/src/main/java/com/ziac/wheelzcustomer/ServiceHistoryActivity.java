package com.ziac.wheelzcustomer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ServiceHistoryActivity extends AppCompatActivity {
    ServiceHistoryAdapter adapter;
    String vehhis_code;
    ImageView Backbtn;
    Context context;
    RecyclerView servicerv;
    ProgressBar progressBar;
    TextView emptyText;
    List<CommonClass> serviceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history);

        context = this;

        Backbtn = findViewById(R.id.btn_back);
        servicerv = findViewById(R.id.servicerv);


        // vehhis_code= Global.vehicledetails.getVehhis_code();

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        vehhis_code = Global.sharedPreferences.getString("vehhis_code", "");

        Servicehistory();
        progressBar = findViewById(R.id.progressBardealers);
       // emptyText = findViewById(R.id.emptyText);

        servicerv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceHistoryAdapter(this, serviceList);
        servicerv.setAdapter(adapter);


        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void Servicehistory() {

        //progressBar.setVisibility(View.VISIBLE);
        servicerv.setVisibility(View.GONE);
       // emptyText.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(this);

        // ✅ Make sure your API URL is correct
        String url = Global.getservicehistory + "regno=" + vehhis_code;

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            progressBar.setVisibility(View.GONE);
            Global.allleadslist = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(response);

                if (jsonArray.length() == 0) {
                    // ✅ No service history
                  //  emptyText.setVisibility(View.VISIBLE);
                    servicerv.setVisibility(View.GONE);
                    Toast.makeText(this, "No Service History Found", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                    commonClass.setVeh_modelname(Veh_modelname);
                    commonClass.setTotal_amt(total_amt);
                    commonClass.setJc_datec(jc_date);
                    commonClass.setKms_done(kms_done);

                    Global.allleadslist.add(commonClass);
                }

                adapter = new ServiceHistoryAdapter(this, Global.allleadslist);
                servicerv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                servicerv.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                progressBar.setVisibility(View.GONE);
               // emptyText.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }, error -> {
           progressBar.setVisibility(View.GONE);
           // emptyText.setVisibility(View.VISIBLE);

            if (error instanceof TimeoutError) {
                Global.customtoast(this, getLayoutInflater(), "Request Time-Out");
            } else if (error instanceof NoConnectionError) {
                Global.customtoast(this, getLayoutInflater(), "Internet connection unavailable");
            } else if (error instanceof ServerError) {
                Global.customtoast(this, getLayoutInflater(), "Server Error");
            } else if (error instanceof NetworkError) {
                Global.customtoast(this, getLayoutInflater(), "Network Error");
            } else if (error instanceof ParseError) {
                Global.customtoast(this, getLayoutInflater(), "Parse Error");
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
        public ServiceHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.service_history, parent, false);
            return new ServiceHistoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ServiceHistoryAdapter.ViewHolder holder, int position) {
            CommonClass service = serviceHistoryList.get(position);

            holder.Modelname.setText(service.getCom_name());
            holder.Servicetype.setText(service.getJobtype_name());
            String totalAmount = "₹ " + service.getTotal_amt();
            SpannableStringBuilder spannableString = new SpannableStringBuilder(totalAmount);
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.Totalamount.setText(spannableString);
            double kmsDoneValue = Double.parseDouble(service.getKms_done());
            String kmsDone = (kmsDoneValue % 1 == 0) ? String.valueOf((int) kmsDoneValue) : String.valueOf(kmsDoneValue);
            holder.Kmdone.setText(kmsDone + " Km");

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