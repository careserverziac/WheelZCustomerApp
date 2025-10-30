package com.ziac.wheelzcustomer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import ModelClasses.ModelsClass;

public class GetAllVehViewAllActivity extends AppCompatActivity {

    RecyclerView recyclermodels;
    Context context;
    SearchView searchView;
    LatestVeh_ViewAllActivity.LatestDrivenAdapter2 latestDrivenAdapter2;
    NestedScrollView nestedScrollView;
    ModelAdapter modelAdapter;
    ModelsClass singleModel;
    Toolbar toolbar;

    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_vehicles);


        context = this;

        searchView = findViewById(R.id.searchless);
        toolbar = findViewById(R.id.toolbar);

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.black));
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.black));

        nestedScrollView = findViewById(R.id.scrollView);
        swipeRefreshLayout = findViewById(R.id.refreshprofile);

        searchView.setQueryHint("Search for Model, Variant & Category");

        recyclermodels = findViewById(R.id.latestDrvRV);
        recyclermodels.setHasFixedSize(true);
        recyclermodels.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getallmodels();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getallmodels();
            }
        });
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // or finish() if you want to close the activity completely
            }
        });



    }

    private void getallmodels() {
        // If using SwipeRefreshLayout, set refreshing to true at start
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Getmodels;

        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                // Dismiss refresh indicator when response is received
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Global.modelsList = new ArrayList<ModelsClass>();
                singleModel = new ModelsClass();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {
                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        // Dismiss refresh on error too
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        throw new RuntimeException(ex);
                    }
                    singleModel = new ModelsClass();
                    try {
                        singleModel.setVmodelcode(e.getString("vmodel_code"));
                        singleModel.setVmodelname(e.getString("shortname").toUpperCase());
                        singleModel.setVmodelimage(e.getString("veh_image1"));
                    } catch (JSONException ex) {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        throw new RuntimeException(ex);
                    }
                    Global.modelsList.add(singleModel);
                }

                Collections.sort(Global.modelsList, new Comparator<ModelsClass>() {
                    @Override
                    public int compare(ModelsClass model1, ModelsClass model2) {
                        return model1.getVmodelname().compareToIgnoreCase(model2.getVmodelname());
                    }
                });

                modelAdapter = new ModelAdapter(Global.modelsList, context);
                recyclermodels.setAdapter(modelAdapter);
                modelAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Dismiss refresh on error
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                // You might want to show an error message here
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                15000, // Increased timeout to 15 seconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonArrayrequest);
    }
    public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.VallCarsViewHOlder> {


        private final List<ModelsClass> viewallmodelsList;

        private final Context context;


        public ModelAdapter(List<ModelsClass> viewallmodelslist, Context context) {
            this.viewallmodelsList = viewallmodelslist;
            this.context = context;

        }

        @NonNull
        @Override
        public ModelAdapter.VallCarsViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewallmodels2, parent, false);
            return new ModelAdapter.VallCarsViewHOlder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ModelAdapter.VallCarsViewHOlder holder, @SuppressLint("RecyclerView") int position) {

//        Picasso.Builder builder=new Picasso.Builder(context);
//        Picasso picasso=builder.build();
//        picasso.load(Uri.parse(Global.websitedataurl + viewallmodelsList.get(position).getVmodelimage().substring(2))).into(holder.modelimageiv);

            Global.loadWithPicasso(context, holder.modelimageiv, Global.websitedataurl + viewallmodelsList.get(position).getVmodelimage().substring(2));


            holder.modelnametv.setText(viewallmodelsList.get(position).getVmodelname());
            holder.vmodel_code = viewallmodelsList.get(position).getVmodelcode();

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("vmodel_code", Global.modelsList.get(position).getVmodelcode());
                    Global.editor.commit();


                  /*  BrandlWiseVehicleModels_Fragment modelWiseVehiclesFragment = new BrandlWiseVehicleModels_Fragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, modelWiseVehiclesFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/


                }
            });

        }

        @Override
        public int getItemCount() {
            return viewallmodelsList.size();
        }

        public static class VallCarsViewHOlder extends RecyclerView.ViewHolder {

            private TextView modelnametv;
            private ImageView modelimageiv;
            private String vmodel_code;
            LinearLayout cardView;

            public VallCarsViewHOlder(@NonNull View itemView) {
                super(itemView);

                cardView = itemView.findViewById(R.id.viewallcardview);
                modelnametv = itemView.findViewById(R.id.clothing_offer_tv);
                modelimageiv = itemView.findViewById(R.id.clothing_image);
            }
        }
    }

}