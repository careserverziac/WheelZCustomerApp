package com.ziac.wheelzcustomer;

import static ModelClasses.Global.lessdrivenlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ModelClasses.Global;
import ModelClasses.LessDrivenClass;
import ModelClasses.VehicleClass;

public class Less_driven_Viewall_Activity extends AppCompatActivity {

    RecyclerView LessDrvRV;
    Context context;
    private boolean allowBack = true;
    SearchView searchView;
    LessDrivenAdapter2 lessDrivenAdapter2;
    NestedScrollView nestedScrollView;
    private int lessDrivenClickedPosition = RecyclerView.NO_POSITION;
    MaterialCardView cardviewsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_less_driven_viewall);
        context = this;

        searchView = findViewById(R.id.homesearchview);
        cardviewsearch = findViewById(R.id.cardviewsearch);

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.black));
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.black));

        nestedScrollView = findViewById(R.id.scrollView);

        searchView.setQueryHint("Search for Model, Variant & Category");

        LessDrvRV = findViewById(R.id.lessDrvRV);
        LessDrvRV.setHasFixedSize(true);
        LessDrvRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getlessdrivenvevicles();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Optional: Handle search submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter as user types
                lessDrivenAdapter2.getFilter().filter(newText);
                return true;  // Return true to indicate we've handled the event
            }
        });

    }

    private void getlessdrivenvevicles() {

        RequestQueue queue = Volley.newRequestQueue(this);
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
                lessDrivenAdapter2 = new LessDrivenAdapter2(lessdrivenlist, this);
                lessDrivenAdapter2.updateList(Global.lessdrivenlist);
                LessDrvRV.setAdapter(lessDrivenAdapter2);

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


    public static class LessDrivenAdapter2 extends RecyclerView.Adapter<LessDrivenAdapter2.LessDrivenViewholder> {
        private List<LessDrivenClass> originalList;  // Original unfiltered list
        private List<LessDrivenClass> filteredList;  // List that's actually displayed
        Context context;

        public LessDrivenAdapter2(List<LessDrivenClass> lessDrivenClassList, Context context) {
            this.originalList = new ArrayList<>(lessDrivenClassList);
            this.filteredList = new ArrayList<>(lessDrivenClassList);
            this.context = context;
        }

        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<LessDrivenClass> filteredItems = new ArrayList<>();

                    if (constraint == null || constraint.length() == 0) {
                        // If no constraint, return the original list
                        filteredItems.addAll(originalList);
                    } else {
                        String filterPattern = constraint.toString().toLowerCase().trim();

                        for (LessDrivenClass item : originalList) {
                            // Check both name and registration number
                            if (item.getVart_name() != null && item.getVart_name().toLowerCase().contains(filterPattern) ||
                                    item.getLd_vname() != null && item.getLd_vname().toLowerCase().contains(filterPattern)) {
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
                    filteredList.addAll((List<LessDrivenClass>) results.values);
                    notifyDataSetChanged();
                }
            };
        }

        @NonNull
        @Override
        public LessDrivenAdapter2.LessDrivenViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.less_driven, parent, false);
            return new LessDrivenAdapter2.LessDrivenViewholder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull LessDrivenAdapter2.LessDrivenViewholder holder, @SuppressLint("RecyclerView") int position) {
            LessDrivenClass lessDrivenCarsList = filteredList.get(position);  // Use filtered list here

            String createdby=lessDrivenCarsList.getCreatedby();
            String userName=Global.sharedPreferences.getString("userName","");

            if (createdby.equals(userName)){
                holder.Edit.setVisibility(View.VISIBLE);
                holder.Delete.setVisibility(View.VISIBLE);

            } else {
                holder.Edit.setVisibility(View.GONE);
                holder.Delete.setVisibility(View.GONE);
            }

            Global.loadWithPicasso(context, holder.ld_vimage, Global.vehimageurl +
                    lessDrivenCarsList.getLd_vimg());
            holder.ld_vname.setText(lessDrivenCarsList.getLd_vname());
            holder.ld_vcolour.setText(lessDrivenCarsList.getLd_vcolor());
            holder.ld_vyear.setText(lessDrivenCarsList.getMan_year());
            holder.ld_model.setText(lessDrivenCarsList.getLd_vcategory());
            holder.ld_v_variant.setText(lessDrivenCarsList.getVart_name());
            holder.ld_Vtransmission.setText(lessDrivenCarsList.getTrans_name());
            holder.ld_vownership.setText(lessDrivenCarsList.getOwn_type() + " OWNER");
            holder.ld_vkm.setText(lessDrivenCarsList.getKm_drvn());
            holder.ld_vfuel.setText(lessDrivenCarsList.getFuel_name());
            holder.ld_selling_price.setText("\u20B9 " + lessDrivenCarsList.getSell_price() + "/-");

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



        @Override
        public int getItemCount() {
            return filteredList.size();
        }
        public void updateList(List<LessDrivenClass> newList) {
            this.originalList = new ArrayList<>(newList);
            this.filteredList = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        class LessDrivenViewholder extends RecyclerView.ViewHolder {

            ImageView ld_vimage;
            LinearLayout Edit,Delete;
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
                Edit = itemView.findViewById(R.id.edit_icon);
                Delete = itemView.findViewById(R.id.delete_icon);
            }
        }
    }

}