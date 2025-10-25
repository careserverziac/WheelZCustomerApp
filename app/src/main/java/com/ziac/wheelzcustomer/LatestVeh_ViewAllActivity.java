package com.ziac.wheelzcustomer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Fragments.PreOwnedVehicleFragment;
import Fragments.UpdateVehFragment;
import ModelClasses.CommonVehClass;
import ModelClasses.Global;
import ModelClasses.LatestVehiclesClass;
import ModelClasses.LessDrivenClass;
import ModelClasses.VehicleClass;

public class LatestVeh_ViewAllActivity extends AppCompatActivity {

    RecyclerView LatestDrvRV;
    Context context;
    SearchView searchView;
    LatestDrivenAdapter2 latestDrivenAdapter2;
    ProgressBar progressBardealers;
    SwipeRefreshLayout refreshprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_veh_view_all);

        context = this;

        searchView = findViewById(R.id.searchlat);

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.black));
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.black));



        progressBardealers = findViewById(R.id.progress);
        refreshprofile = findViewById(R.id.refreshprofile);
        LatestDrvRV = findViewById(R.id.latestDrvRV);
        LatestDrvRV.setHasFixedSize(true);
        LatestDrvRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getlatestdrivenvevicles();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Optional: Handle search submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter as user types
                latestDrivenAdapter2.getFilter().filter(newText);
                return true;  // Return true to indicate we've handled the event
            }
        });
    }

    private void getlatestdrivenvevicles() {

        RequestQueue queue = Volley.newRequestQueue(context);
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

                    latestDrivenAdapter2 = new LatestDrivenAdapter2(Global.latestVehicleslist, this);
                    latestDrivenAdapter2.updateList(Global.latestVehicleslist);
                    refreshprofile.setRefreshing(false);

                    LatestDrvRV.setAdapter(latestDrivenAdapter2);

                } else {
                    refreshprofile.setRefreshing(false);
                    String errorMsg = response.optString("message", "Failed to load vehicles.");
                    Global.customtoast(this, getLayoutInflater(), errorMsg);
                }

            } catch (JSONException e) {
                refreshprofile.setRefreshing(false);

                e.printStackTrace();
                Global.customtoast(this, getLayoutInflater(), "Data parsing error.");
            }

        }, error -> {
            if (error instanceof TimeoutError) {
                Global.customtoast(this, getLayoutInflater(), "Request Time-Out");
            } else if (error instanceof NoConnectionError) {
                Global.customtoast(this, getLayoutInflater(), "No Internet Connection");
            } else if (error instanceof ServerError) {
                Global.customtoast(this, getLayoutInflater(), "Server Error");
            } else if (error instanceof NetworkError) {
                Global.customtoast(this, getLayoutInflater(), "Network Error");
            } else if (error instanceof ParseError) {
                Global.customtoast(this, getLayoutInflater(), "Response Parsing Failed");
            } else {
                Global.customtoast(this, getLayoutInflater(), "Unknown error occurred");
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

    public class LatestDrivenAdapter2 extends RecyclerView.Adapter<LatestDrivenAdapter2.RecentcarsViewHolder> {
        private List<LatestVehiclesClass> originalList;  // Original unfiltered list
        private List<LatestVehiclesClass> filteredList;  // List that's actually displayed
        Context context;


        public LatestDrivenAdapter2(List<LatestVehiclesClass> latestDrivenClassList, Context context) {
            this.originalList = new ArrayList<>(latestDrivenClassList);
            this.filteredList = new ArrayList<>(latestDrivenClassList);
            this.context = context;
        }

        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<LatestVehiclesClass> filteredItems = new ArrayList<>();

                    if (constraint == null || constraint.length() == 0) {
                        // If no constraint, return the original list
                        filteredItems.addAll(originalList);
                    } else {
                        String filterPattern = constraint.toString().toLowerCase().trim();

                        for (LatestVehiclesClass item : originalList) {
                            // Check both name and registration number
                            if (item.getVart_name() != null && item.getVart_name().toLowerCase().contains(filterPattern) ||
                                    item.getLatestvname() != null && item.getLatestvname().toLowerCase().contains(filterPattern)) {
                                filteredItems.add(item);
                            }
                        }
                    }

                    results.values = filteredItems;
                    results.count = filteredItems.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredList.clear();
                    filteredList.addAll((List<LatestVehiclesClass>) results.values);
                    notifyDataSetChanged();
                }
            };
        }

        @NonNull
        @Override
        public LatestDrivenAdapter2.RecentcarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_veh3, parent, false);
            return new RecentcarsViewHolder(view);
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull LatestDrivenAdapter2.RecentcarsViewHolder holder, @SuppressLint("RecyclerView") int position) {
            LatestVehiclesClass latestVehiclesList = filteredList.get(position);  // Use filtered list here

            String createdby = latestVehiclesList.getCreatedby();
            String userName = Global.sharedPreferences.getString("userName", "");

            if (createdby.equals(userName)) {
                holder.Edit.setVisibility(View.VISIBLE);
                holder.Delete.setVisibility(View.VISIBLE);
            } else {
                holder.Edit.setVisibility(View.GONE);
                holder.Delete.setVisibility(View.GONE);
            }

            String img = Global.vehimageurl + latestVehiclesList.getLatestvimage();
            Global.loadWithPicasso(context, holder.lv_image, img);

            holder.lv_name.setText(latestVehiclesList.getLatestvname() + " (" +
                    latestVehiclesList.getMan_year() + ")");
            holder.lvcolor.setText(latestVehiclesList.getLatestvcolour());
            holder.lv_category.setText(latestVehiclesList.getLatestvcategory());
            holder.lv_variant.setText(latestVehiclesList.getVart_name());
            holder.lv_transmission.setText(latestVehiclesList.getTrans_name());
            holder.lv_ownership.setText(latestVehiclesList.getOwn_type() + " OWNER");
            holder.lv_fuel.setText(latestVehiclesList.getFuel_name());
            holder.lv_date.setText("KMS run " + latestVehiclesList.getKm_drvn() + " Added on : " +
                    latestVehiclesList.getLatestvdate());
            holder.lv_sellingprice.setText("\u20B9 " + latestVehiclesList.getSell_price() + "/- Negotiable");

        }


        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        public void updateList(List<LatestVehiclesClass> newList) {
            this.originalList = new ArrayList<>(newList);
            this.filteredList = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        public static class RecentcarsViewHolder extends RecyclerView.ViewHolder {

            private ImageView lv_image;
            FloatingActionButton Edit, Delete;
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

}