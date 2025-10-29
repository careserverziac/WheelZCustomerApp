package com.ziac.wheelzcustomer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
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
import com.android.volley.toolbox.JsonObjectRequest;
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

import Fragments.Upload_View_ImagesFragment;
import ModelClasses.CommonClass;
import ModelClasses.Global;
import ModelClasses.ModelsClass;

public class ServiceHistoryActivity extends AppCompatActivity {
    ServiceHistoryAdapter adapter;
    String vehhis_code;
    ImageView Backbtn;
    Context context;
    RecyclerView servicerv;
    ProgressBar progressBar;
    TextView emptyText;

    Toolbar toolbar;
    List<CommonClass> serviceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history);

        context = this;

        servicerv = findViewById(R.id.servicerv);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        vehhis_code = Global.sharedPreferences.getString("vehhis_code", "");

        Servicehistory();
        progressBar = findViewById(R.id.progressBardealers);
       // emptyText = findViewById(R.id.emptyText);
        toolbar = findViewById(R.id.toolbar);

        servicerv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceHistoryAdapter(this, serviceList);
        servicerv.setAdapter(adapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity
                Intent intent = new Intent(ServiceHistoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }


    private void Servicehistory() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading service history...");
        progressDialog.setCancelable(false);

        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        String userCode = Global.sharedPreferences.getString("wuser_code", "");
        String url = Global.urlBookServiceList + "wuser_code=" + userCode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    dismissProgressDialog(progressDialog);
                    progressBar.setVisibility(View.GONE);

                    try {
                        boolean isSuccess = response.getBoolean("isSuccess");
                        String message = response.getString("message");

                        if (isSuccess) {
                            JSONArray dataArray = response.getJSONArray("data");

                            Global.allleadslist = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);

                                String com_name = jsonObject.optString("com_name");
                                String Veh_modelname = jsonObject.optString("model_name");
                                String total_amt = jsonObject.optString("total_amt", "0");
                                String kms_done = jsonObject.optString("kms_done", "0");
                                String service_type = jsonObject.optString("service_type");
                                String service_date = jsonObject.optString("servicedate");
                                String service_time = jsonObject.optString("service_time");
                                String com_address = jsonObject.optString("com_address");
                                String reg_no = jsonObject.optString("reg_no");
                                String prev_kms_done = jsonObject.optString("prev_kms_done");

                                CommonClass commonClass = new CommonClass();
                                commonClass.setCom_name(com_name);
                                commonClass.setVeh_modelname(Veh_modelname);
                                commonClass.setTotal_amt(total_amt);
                                commonClass.setKms_done(kms_done);
                                commonClass.setService_type(service_type);
                                commonClass.setService_date(service_date);
                                commonClass.setService_time(service_time);
                                commonClass.setCom_address(com_address);
                                commonClass.setRegis_no(reg_no);
                                commonClass.setKms_diff(prev_kms_done);

                                Global.allleadslist.add(commonClass);
                            }

                            adapter = new ServiceHistoryAdapter(this, Global.allleadslist);
                            servicerv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Failed: " + message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        dismissProgressDialog(progressDialog);
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toast.makeText(context, "Parsing error!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dismissProgressDialog(progressDialog);
                    progressBar.setVisibility(View.GONE);
                    error.printStackTrace();

                    String errorMessage = "Network error!";
                    if (error.networkResponse != null) {
                        errorMessage += " Code: " + error.networkResponse.statusCode;
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // 15 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonObjectRequest);
    }

    // Helper method to safely dismiss progress dialog
    private void dismissProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

            holder.tvKmsDiff.setText(service.getKms_diff());
            holder.Modelname.setText(service.getVeh_modelname());
            holder.tvServiceType.setText(service.getService_type());
            holder.tvServiceDate.setText(service.getService_date());
            holder.tvVehicleModel.setText(service.getVeh_modelname());
            holder.tvRegNo.setText(service.getRegis_no());
            holder.tvRegNo1.setText(service.getRegis_no());


            double kmsDoneValue = Double.parseDouble(service.getKms_done());
            String kmsDone = (kmsDoneValue % 1 == 0) ? String.valueOf((int) kmsDoneValue) : String.valueOf(kmsDoneValue);
            holder.Kmdone.setText(kmsDone + " Km");

            String kmsDiffStr = service.getKms_diff();

            if (kmsDiffStr != null && !kmsDiffStr.equals("-") && !kmsDiffStr.isEmpty()) {
                try {
                    double kmsDoneV = Double.parseDouble(kmsDiffStr);
                    String kms = (kmsDoneV % 1 == 0)
                            ? String.valueOf((int) kmsDoneV)
                            : String.valueOf(kmsDoneV);
                    holder.tvKmsDiff.setText(kms + " Km");
                } catch (NumberFormatException e) {
                    holder.tvKmsDiff.setText("-");
                }
            } else {
                holder.tvKmsDiff.setText("-");
            }


            String dateTimeStr = service.getService_time();

            holder.tvServiceTime.setText(dateTimeStr);

            holder.BtnView.setOnClickListener(new View.OnClickListener() {
                boolean isVisible = false; // keeps track of current visibility state

                @Override
                public void onClick(View v) {
                    if (isVisible) {
                        holder.veh_details.setVisibility(View.GONE);
                        isVisible = false;
                    } else {
                        holder.veh_details.setVisibility(View.VISIBLE);
                        isVisible = true;
                    }
                }
            });



        }

        @Override
        public int getItemCount() {
            return serviceHistoryList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView Modelname,tvKmsDiff, tvVehicleModel,tvRegNo,tvRegNo1, Totalamount, Kmdone,tvServiceType,tvServiceTime,tvServiceDate;

            LinearLayout veh_details;
            AppCompatButton BtnView;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tvKmsDiff = itemView.findViewById(R.id.tvKmsDiff);
                Modelname = itemView.findViewById(R.id.tvVehicleModel);
                BtnView = itemView.findViewById(R.id.tvView);
                tvVehicleModel = itemView.findViewById(R.id.tvVehicleModel1);
                tvRegNo = itemView.findViewById(R.id.tvRegNo);
                tvRegNo1 = itemView.findViewById(R.id.tvRegNo1);
                tvServiceType = itemView.findViewById(R.id.tvServiceType);
                tvServiceDate = itemView.findViewById(R.id.tvServiceDate);
                tvServiceTime = itemView.findViewById(R.id.tvServiceTime);
                Totalamount = itemView.findViewById(R.id.totalamount);
                Kmdone = itemView.findViewById(R.id.tvKmsDone);
                veh_details = itemView.findViewById(R.id.veh_details);
            }
        }
    }
}