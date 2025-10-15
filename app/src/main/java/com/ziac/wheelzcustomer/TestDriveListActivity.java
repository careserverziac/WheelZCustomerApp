package com.ziac.wheelzcustomer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ModelClasses.Global;
import ModelClasses.TestDriveModel;
import ModelClasses.VolleyRequestHelper;

public class TestDriveListActivity extends AppCompatActivity {

    private RecyclerView rvTestDriveList;
    private TestDriveAdapter adapter;
    private List<TestDriveModel> testDriveList;
    Context context;
    String  access_token,wuser_code ;
    private ProgressDialog progressDialog;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView tvBookingCount;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_drive_list);

        context = this;

        progressDialog = new ProgressDialog(TestDriveListActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        wuser_code = Global.sharedPreferences.getString("wuser_code", "");
        access_token = Global.sharedPreferences.getString("access_token", "");


        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvBookingCount = findViewById(R.id.tvBookingCount);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle("Test Drive Bookings");

        rvTestDriveList = findViewById(R.id.rvTestDriveList);
        rvTestDriveList.setLayoutManager(new LinearLayoutManager(this));

        // Initialize list
        testDriveList = new ArrayList<>();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity
                Intent intent = new Intent(TestDriveListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Fetch data from API
        fetchTestDriveData();

    }



    private void fetchTestDriveData() {
        VolleyRequestHelper volleyHelper = new VolleyRequestHelper(TestDriveListActivity.this);
        String url = Global.urlTestDriveList + "wuser_code=" + wuser_code ;
        Map<String, String> params = new HashMap<>();

        volleyHelper.makePostRequest(url, params, access_token, new VolleyRequestHelper.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    boolean success = response.getBoolean("isSuccess");

                    if (success) {
                        JSONArray dataArray = response.getJSONArray("data");
                        testDriveList.clear();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject item = dataArray.getJSONObject(i);

                            String modelName = item.optString("model_name", "N/A");
                            String companyName = item.optString("com_name", "N/A");
                            String testDriveDate = item.optString("testdriv_date", "");
                            String testDriveTime = item.optString("testdriv_time", "");
                            String status = item.optString("testdriv_status", "Pending");
                            String customerName = item.optString("username", "N/A");
                            String contactNumber = item.optString("wuser_mobile1", "N/A");

                            TestDriveModel model = new TestDriveModel(
                                    modelName, companyName, testDriveDate,
                                    testDriveTime, status, customerName, contactNumber
                            );

                            testDriveList.add(model);
                        }

                        // Update booking count
                        tvBookingCount.setText(String.valueOf(testDriveList.size()));

                        if (adapter == null) {
                            adapter = new TestDriveAdapter(TestDriveListActivity.this, testDriveList);
                            rvTestDriveList.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                        progressDialog.dismiss();

                        if (testDriveList.isEmpty()) {
                            Global.customtoast(TestDriveListActivity.this, getLayoutInflater(),
                                    "No test drives found");
                        }

                    } else {
                        progressDialog.dismiss();
                        String error = response.optString("message", "Something went wrong");
                        Global.customtoast(TestDriveListActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Global.customtoast(TestDriveListActivity.this, getLayoutInflater(),
                            "Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Global.customtoast(TestDriveListActivity.this, getLayoutInflater(),
                        "Network Error: " + error);
            }
        });
    }


    public class TestDriveAdapter extends RecyclerView.Adapter<TestDriveAdapter.ViewHolder> {

        private Context context;
        private List<TestDriveModel> testDriveList;

        public TestDriveAdapter(Context context, List<TestDriveModel> testDriveList) {
            this.context = context;
            this.testDriveList = testDriveList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_test_drive, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TestDriveModel model = testDriveList.get(position);

            holder.tvModelName.setText(model.getModelName());
            holder.tvCompanyName.setText(model.getCompanyName());
            holder.tvCustomerName.setText(model.getCustomerName());
            holder.tvContactNumber.setText(model.getContactNumber());
          //  holder.tvStatus.setText(model.getStatus());

            // Format date and time
            String formattedDateTime = formatDateTime(model.getTestDriveDate(), model.getTestDriveTime());
            holder.tvDateTime.setText(formattedDateTime);

            // Set status background based on status
           // setStatusBackground(holder.tvStatus, model.getStatus());


        }

        @Override
        public int getItemCount() {
            return testDriveList.size();
        }

        private String formatDateTime(String date, String time) {
            try {
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                Date parsedDate = inputDateFormat.parse(date);
                String formattedDate = outputDateFormat.format(parsedDate);

                SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                Date parsedTime = inputTimeFormat.parse(time);
                String formattedTime = outputTimeFormat.format(parsedTime);

                return formattedDate + " | " + formattedTime;
            } catch (Exception e) {
                e.printStackTrace();
                return date + " | " + time;
            }
        }

        private void setStatusBackground(TextView tvStatus, String status) {
            switch (status.toLowerCase()) {
                case "pending":
                    tvStatus.setBackgroundResource(R.drawable.status_pending_bg);
                    tvStatus.setTextColor(Color.parseColor("#FF6F00"));
                    break;
                case "confirmed":
                    tvStatus.setBackgroundResource(R.drawable.status_confirmed_bg);
                    tvStatus.setTextColor(Color.parseColor("#2E7D32"));
                    break;
                case "completed":
                    tvStatus.setBackgroundResource(R.drawable.status_completed_bg);
                    tvStatus.setTextColor(Color.parseColor("#1976D2"));
                    break;
                default:
                    tvStatus.setBackgroundResource(R.drawable.status_pending_bg);
                    tvStatus.setTextColor(Color.parseColor("#757575"));
                    break;
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvModelName, tvCompanyName, tvDateTime, tvCustomerName, tvContactNumber, tvStatus;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvModelName = itemView.findViewById(R.id.tvModelName);
                tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
                tvDateTime = itemView.findViewById(R.id.tvDateTime);
                tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
                tvContactNumber = itemView.findViewById(R.id.tvContactNumber);
               // tvStatus = itemView.findViewById(R.id.tvStatus);
            }
        }
    }

}