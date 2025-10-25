package com.ziac.wheelzcustomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ModelClasses.CommonClass;
import ModelClasses.Global;
import ModelClasses.zList;

public class TestDriveActivity extends AppCompatActivity {

    String Url;
    private zList statename;
    private CommonClass commonClass;
    private zList cityname;
    private Dialog zDialog;
    TextView Statetxt, Citytxt, Manufacture, Model, SelectedDate, Selectedtime, Displaydata;
    MaterialCardView Statedp, Citydp;
    String statecode, citycode, searchquery, sqldateformat, selectedTime24, vmodel_code, com_code;
    String dealerName, dealercity, dealerweb, dealeradrs;
    RecyclerView DealerlistRV;
    DealerAdapter dealerAdapter;
    ImageView Veh_image;
    MaterialCardView SelectDt, SelectTm;
    Button Submit;
    Context context;
    TextView Sub_txt;
    ImageView Backbtn;
    TextInputEditText Tdmail, Tdmobile, Tdname;

    String username, wuser_mobile1, wuser_email;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drive);

        context = this;

        DealerlistRV = findViewById(R.id.dealerlist);
        Statedp = findViewById(R.id.statedp);
        Citydp = findViewById(R.id.citydp);
        Manufacture = findViewById(R.id.manufacturer);
        Model = findViewById(R.id.modelname);
        Veh_image = findViewById(R.id.veh_image);


        SelectDt = findViewById(R.id.selectdate);
        SelectTm = findViewById(R.id.selecttime);
        SelectedDate = findViewById(R.id.selecteddate);
        Selectedtime = findViewById(R.id.selectedtime);
        Displaydata = findViewById(R.id.displaydata);
        Submit = findViewById(R.id.sub_btn);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Manufacture.setText(Global.modellist.getManufacture());
        Model.setText(Global.modellist.getModel_name());
        vmodel_code = Global.modellist.getModel_code();
        com_code = Global.sharedPreferences.getString("com_code", "");

        Global.loadWithPicasso(TestDriveActivity.this, Veh_image, Global.modelsimageurl + Global.modellist.getImage_path());

        Statetxt = findViewById(R.id.statetext);
        Citytxt = findViewById(R.id.citytext);

        searchquery = "";
        statecode = "0";
        citycode = "0";

        getstates();
        getDealerslist();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TestDriveActivity.this);
        DealerlistRV.setLayoutManager(linearLayoutManager);
        DealerlistRV.setLayoutManager(new LinearLayoutManager(TestDriveActivity.this, LinearLayoutManager.VERTICAL, false));

        Statedp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statespopup();
            }
        });

        Citydp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citiespopup();
            }
        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMMM yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat time24Format = new SimpleDateFormat("HH:mm", Locale.getDefault()); // 24-hour format

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());
        SelectedDate.setText(currentDate);
        Selectedtime.setText(currentTime);

        selectedTime24 = time24Format.format(calendar.getTime());
        sqldateformat = sqlDateFormat.format(calendar.getTime());

        SelectDt.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(TestDriveActivity.this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        String selectedDateString = dateFormat.format(selectedDate.getTime());
                        SelectedDate.setText(selectedDateString);

                        sqldateformat = String.format("%04d-%02d-%02d",
                                selectedDate.get(Calendar.YEAR),
                                selectedDate.get(Calendar.MONTH) + 1,
                                selectedDate.get(Calendar.DAY_OF_MONTH));

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());

            datePickerDialog.show();
        });


        SelectTm.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm", Locale.getDefault());

            TimePickerDialog timePickerDialog = new TimePickerDialog(TestDriveActivity.this,
                    (view12, hourOfDay, minute) -> {
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);
                        String selectedTimeString12 = timeFormat12.format(selectedTime.getTime());
                        String selectedTimeString24 = timeFormat24.format(selectedTime.getTime());

                        Selectedtime.setText(selectedTimeString12);
                        selectedTime24 = selectedTimeString24;

                    }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Global.selectedDealerCode == null || Global.selectedDealerCode.isEmpty()) {
                    Toast.makeText(context, "Please select a dealer", Toast.LENGTH_SHORT).show();
                    return; // stop further execution
                }

                // ✅ All good, proceed with your function
                BookTestDrive();
            }
        });


    }


    private void getDealerslist() {

        RequestQueue queue = Volley.newRequestQueue(TestDriveActivity.this);

        Url = Global.searchalldealers + "searchtext=" + searchquery + "&state_code=" +
                statecode + "&city_code=" + citycode;
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.POST, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Global.dealersarraylist = new ArrayList<CommonClass>();
                commonClass = new CommonClass();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject jsonObject;
                    try {

                        jsonObject = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    commonClass = new CommonClass();
                    try {
                        commonClass.setImage_path(jsonObject.getString("logo_image"));
                        //commonClass.setCom_code(jsonObject.getString("com_code"));
                        String dcom_code = jsonObject.getString("com_code");


                        commonClass.setState_name(jsonObject.getString("state_name"));
                        commonClass.setState_code(jsonObject.getString("state_code"));
                        commonClass.setCity_name(jsonObject.getString("city_name"));
                        commonClass.setCity_code(jsonObject.getString("city_code"));
                        commonClass.setCom_name(jsonObject.getString("com_name"));
                        commonClass.setCom_address(jsonObject.getString("com_address"));
                        commonClass.setCom_pin(jsonObject.getString("com_pin"));
                        commonClass.setCom_email(jsonObject.getString("com_email"));
                        commonClass.setCom_website(jsonObject.getString("com_website"));
                        commonClass.setCom_contact(jsonObject.getString("com_contact"));
                        commonClass.setCom_contact_mobno(jsonObject.getString("com_contact_mobno"));
                        commonClass.setCom_phone(jsonObject.getString("com_phone"));
                        commonClass.setCom_lng(jsonObject.getString("com_lng"));
                        commonClass.setCom_lat(jsonObject.getString("com_lat"));
                        commonClass.setCom_contact_email(jsonObject.getString("com_contact_email"));


                        commonClass.setDcom_code(dcom_code);

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.dealersarraylist.add(commonClass);
                }
                dealerAdapter = new DealerAdapter(Global.dealersarraylist, getApplicationContext(), Displaydata, DealerlistRV);
                DealerlistRV.setAdapter(dealerAdapter);
                dealerAdapter.notifyDataSetChanged();

            }
        }, error -> {

            if (error instanceof NoConnectionError) {

                if (error instanceof TimeoutError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Internet connection unavailable");
                } else if (error instanceof ServerError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Parse Error");
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

        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonArrayrequest);

    }

    private void getstates() {

        RequestQueue queue = Volley.newRequestQueue(TestDriveActivity.this);
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, Global.GetStates, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Global.statearraylist = new ArrayList<zList>();
                statename = new zList();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {
                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    statename = new zList();
                    try {
                        statename.set_name(e.getString("state_name"));
                        statename.set_code(e.getString("state_code"));
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.statearraylist.add(statename);
                }

            }
        }, error -> {

            if (error instanceof NoConnectionError) {
                if (error instanceof TimeoutError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Internet connection unavailable");
                } else if (error instanceof ServerError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Parse Error");
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
        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(jsonArrayrequest);
    }

    public void statespopup() {

        zDialog = new Dialog(TestDriveActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.statearraylist == null || Global.statearraylist.size() == 0) {
            return;
        }
        final StateAdapter laStates = new StateAdapter(Global.statearraylist);
        lvStates.setAdapter(laStates);

        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView svstate = zDialog.findViewById(R.id.svstate);
        svstate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                laStates.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class StateAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public StateAdapter(ArrayList<zList> arrayList) {
            this.mDataArrayList = arrayList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<zList> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.statearraylist;
                    } else {
                        for (zList dataList : Global.statearraylist) {
                            if (dataList.get_name().toLowerCase().contains(charString)) {
                                mFilteredList.add(dataList);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    filterResults.count = mFilteredList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mDataArrayList = (ArrayList<zList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public int getCount() {
            return mDataArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder") View v = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            final TextView tvstatenameitem = v.findViewById(R.id.tvsingleitem);
            RadioButton radioButton = v.findViewById(R.id.radio_button);
            statename = mDataArrayList.get(i);
            tvstatenameitem.setText(statename.get_name());

            radioButton.setOnClickListener(view1 -> {
                boolean isChecked = radioButton.isChecked();
                radioButton.setChecked(!isChecked);
                statename = mDataArrayList.get(i);
                statecode = statename.get_code();
                citycode = "";
                getDealerslist();
                zDialog.dismiss();
                getcity();
                Statetxt.setText(statename.get_name());
            });

            tvstatenameitem.setOnClickListener(view1 -> {
                statename = mDataArrayList.get(i);
                statecode = statename.get_code();
                citycode = "";
                getDealerslist();
                zDialog.dismiss();
                getcity();
                Statetxt.setText(statename.get_name());
            });
            return v;
        }
    }

    private void getcity() {
        RequestQueue queue = Volley.newRequestQueue(TestDriveActivity.this);

        String url = Global.GetCity;
        url = url + "state_code=" + statecode;
        StringRequest jsonArrayrequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String sresponse) {

                        JSONArray response;
                        try {
                            response = new JSONArray(sresponse);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Global.cityarraylist = new ArrayList<zList>();
                        cityname = new zList();
                        for (int i = 0; i < response.length(); i++) {
                            final JSONObject e;
                            try {
                                e = response.getJSONObject(i);
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                            cityname = new zList();
                            try {
                                cityname.set_name(e.getString("city_name"));
                                cityname.set_code(e.getString("city_code"));
                                citycode = cityname.get_code();

                                Citytxt.setText("City");
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                            Global.cityarraylist.add(cityname);
                        }
                    }
                },
                error -> {

                    if (error instanceof NoConnectionError) {
                        if (error instanceof TimeoutError) {
                            Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Request Time-Out");
                        } else if (error instanceof NoConnectionError) {
                            Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Internet connection unavailable");
                        } else if (error instanceof ServerError) {
                            Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Server Error");
                        } else if (error instanceof NetworkError) {
                            Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Network Error");
                        } else if (error instanceof ParseError) {
                            Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Parse Error");
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

        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonArrayrequest);
    }

    public void citiespopup() {

        zDialog = new Dialog(TestDriveActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);
        ListView lvStates = zDialog.findViewById(R.id.lvstates);
        if (Global.cityarraylist == null || Global.cityarraylist.size() == 0) {
            Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "No cities found for selected state");

            return;
        }
        final CityAdapter laStates = new CityAdapter(Global.cityarraylist);
        lvStates.setAdapter(laStates);

        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView svstate = zDialog.findViewById(R.id.svstate);
        svstate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                laStates.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class CityAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public CityAdapter(ArrayList<zList> arrayList) {
            this.mDataArrayList = arrayList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<zList> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.statearraylist;
                    } else {
                        for (zList dataList : Global.statearraylist) {
                            if (dataList.get_name().toLowerCase().contains(charString)) {
                                mFilteredList.add(dataList);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    filterResults.count = mFilteredList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mDataArrayList = (ArrayList<zList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public int getCount() {
            return mDataArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            final TextView tvstatenameitem = v.findViewById(R.id.tvsingleitem);
            RadioButton radioButton = v.findViewById(R.id.radio_button);
            cityname = mDataArrayList.get(i);
            tvstatenameitem.setText(cityname.get_name());


            tvstatenameitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cityname = mDataArrayList.get(i);
                    citycode = cityname.get_code();
                    Citytxt.setText(cityname.get_name());
                    getDealerslist();
                    zDialog.dismiss();


                }
            });

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cityname = mDataArrayList.get(i);
                    // Citytxt.setText(cityname.get_name());
                    citycode = cityname.get_code();
                    Citytxt.setText(cityname.get_name());
                    getDealerslist();
                    zDialog.dismiss();


                }
            });
            return v;
        }
    }

    public class DealerAdapter extends RecyclerView.Adapter<DealerAdapter.ViewHolder> {

        private final List<CommonClass> dealerList;
        Context context;
        private final TextView displayDataTextView; // Reference to the Displaydata TextView
        private final RecyclerView dealerRecyclerView;


        public DealerAdapter(List<CommonClass> dealerList, Context context, TextView displayDataTextView, RecyclerView dealerRecyclerView) {
            this.context = context;
            this.dealerList = dealerList;
            this.displayDataTextView = displayDataTextView;
            this.dealerRecyclerView = dealerRecyclerView;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealeradptr_list, parent, false);
            DealerAdapter.ViewHolder viewHolder = new DealerAdapter.ViewHolder(view);
            return viewHolder;

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            // com_code = dealerList.get(position).getCom_code();
            holder.ItemCount.setText(String.valueOf(position + 1));
            holder.ComCity.setText(dealerList.get(position).getCity_name());
            holder.Comwebsite.setText(dealerList.get(position).getCom_website());
            holder.ComAddress.setText(dealerList.get(position).getCom_address());
            holder.Comyname.setText(dealerList.get(position).getCom_name());
            holder.Com_ph.setText(dealerList.get(position).getCom_contact_mobno());

            holder.Dealerlist.setOnClickListener(v -> {
                // Hide the RecyclerView
                dealerRecyclerView.setVisibility(View.GONE);
                String dealerCode = Global.dealersarraylist.get(position).getDcom_code();

                // Get clicked dealer details
                String dealerName = dealerList.get(position).getCom_name();
                String dealerCity = dealerList.get(position).getCity_name();
                String dealerAdrs = dealerList.get(position).getCom_address();
                String dealeremail = dealerList.get(position).getCom_email();
                String dealerph = dealerList.get(position).getCom_contact_mobno();

                Global.selectedDealerCode = dealerCode;


                // Show the selected dealer data in the TextView
                TextView displayData = ((Activity) v.getContext()).findViewById(R.id.displaydata);
                displayData.setVisibility(View.VISIBLE);

                // Set formatted data
                String formattedText = dealerName
                        + dealerCity
                        + dealerAdrs
                        + dealeremail + ", "
                        + dealerph + ".";

                displayData.setText(formattedText);
            });

        }

        @Override
        public int getItemCount() {
            return dealerList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView Comyname, ComCity, Com_ph, ComAddress, Comwebsite, ItemCount;
            LinearLayout Dealerlist;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                Com_ph = itemView.findViewById(R.id.com_ph);
                Comyname = itemView.findViewById(R.id.com_name);
                ComCity = itemView.findViewById(R.id.com_city);
                ComAddress = itemView.findViewById(R.id.com_address);
                Comwebsite = itemView.findViewById(R.id.com_website);
                ItemCount = itemView.findViewById(R.id.item_count);
                Dealerlist = itemView.findViewById(R.id.dealer_list);


            }
        }

    }


    private void BookTestDrive() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(TestDriveActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urlBookTestDrive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String sresponse) {
                        JSONObject response = null;
                        try {
                            response = new JSONObject(sresponse);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            if (response.getBoolean("isSuccess")) {
                                progressDialog.dismiss();
                                Global.customtoast(TestDriveActivity.this, getLayoutInflater(), response.getString("error"));
                                finish();

                            } else {
                                progressDialog.dismiss();
                                Global.customtoast(TestDriveActivity.this, getLayoutInflater(), response.getString("error"));

                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Internet connection unavailable");
                } else if (error instanceof ServerError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(TestDriveActivity.this, getLayoutInflater(), "Parse Error");
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

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("wuser_code", Global.sharedPreferences.getString("wuser_code", ""));
// ✅ Dealer is selected — proceed
                params.put("com_code", Global.selectedDealerCode);
                params.put("vmodel_code", vmodel_code);
                params.put("testdriv_date", sqldateformat);
                params.put("testdriv_time", selectedTime24);
                // params.put("username", username);
                // params.put("wuser_mobile1", wuser_mobile1);
                // params.put("wuser_email", wuser_email);
                return params;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }


}