package com.ziac.wheelzcustomer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ModelClasses.AppStatus;
import ModelClasses.Global;
import ModelClasses.zList;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 10;
    private static final int REQUEST_CODE_DOCUMENT = 1;
    FloatingActionButton Camera;
    zList statename, cityname, vehicletype;
    String  country_code, state_code, city_code,country_name,state_name,city_name;
    Dialog zDialog;
    FrameLayout DL;
    EditText Name, Mobilenumber, Email;
    TextView Country, State, City;
    CircleImageView circularImageView;
    AppCompatButton UpdateProfilebtn;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    String image, name, mobile, user_mail, file_name, file_type, actual_filename, fileUrl, file;
    ImageButton uploadRc, viewRc;
    boolean isExpanded = false;
    Context context;
    ImageView Backbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        context = ProfileActivity.this;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (AppStatus.getInstance(this).isOnline()) {
        } else {
            Global.customtoast(ProfileActivity.this, getLayoutInflater(), "Connected WIFI or Mobile data has no internet access!!");
        }

        image = Global.userimageurl + Global.sharedPreferences.getString("Image", "");
        circularImageView = findViewById(R.id.profile_images);
        Picasso.Builder builder = new Picasso.Builder(getApplication());
        Picasso picasso = builder.build();
        Global.loadWithPicasso(this, circularImageView, image);

        progressBar = findViewById(R.id.progressbarfiles);
        Name = findViewById(R.id.name);
        Mobilenumber = findViewById(R.id.mobile);
        Email = findViewById(R.id.email);
        Country = findViewById(R.id.country);
        State = findViewById(R.id.state);
        City = findViewById(R.id.city);
        UpdateProfilebtn = findViewById(R.id.updateprofile);
        Camera = findViewById(R.id.pro_pic);
        DL = findViewById(R.id.custom_fab);
        Backbtn = findViewById(R.id.backbtn);

        name = Global.sharedPreferences.getString("key_person", "");
        mobile = Global.sharedPreferences.getString("Mobile1", "");
        user_mail = Global.sharedPreferences.getString("Email", "");

        country_name = Global.sharedPreferences.getString("country_name", "");
        state_name = Global.sharedPreferences.getString("state_name", "");
        city_name = Global.sharedPreferences.getString("city_name", "");

        country_code = Global.sharedPreferences.getString("country_code", "");
        state_code = Global.sharedPreferences.getString("state_code", "");
        city_code = Global.sharedPreferences.getString("city_code", "");

        Name.setText(name);
        Mobilenumber.setText(mobile);
        Email.setText(user_mail);
        Country.setText(country_name);
        State.setText(state_name);
        City.setText(city_name);

        getcountries();
        getstates();

        UpdateProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(ProfileActivity.this).isOnline()) {
                    Updateprofiledetails();
                } else {
                    Global.customtoast(ProfileActivity.this, getLayoutInflater(), "Connected WIFI or Mobile data has no internet access!!");
                }
            }
        });

        //Camera = findViewById(R.id.fab_right);
        Camera.setOnClickListener(v -> openCamera());
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(ProfileActivity.this).isOnline()) {
                    openCamera();
                } else {
                    Global.customtoast(ProfileActivity.this, getLayoutInflater(), "Connected WIFI or Mobile data has no internet access!!");
                }
            }
        });


        circularImageView.setOnClickListener(v -> {
            image = Global.userimageurl + Global.sharedPreferences.getString("Image", "");
            showImage(picasso, image);

        });

        uploadRc = findViewById(R.id.upload_rc);
        viewRc = findViewById(R.id.view_rc);


        uploadRc.setAlpha(0f);
        uploadRc.setVisibility(View.GONE);
        viewRc.setAlpha(0f);
        viewRc.setVisibility(View.GONE);

        DL.setOnClickListener(v -> {
            if (!isExpanded) {

                uploadRc.setVisibility(View.VISIBLE);
                uploadRc.animate().alpha(1f).setDuration(500).start();

                viewRc.setVisibility(View.VISIBLE);
                viewRc.animate().alpha(1f).setDuration(800).start();
            } else {

                uploadRc.animate().alpha(0f).setDuration(800).withEndAction(() -> uploadRc.setVisibility(View.GONE)).start();
                viewRc.animate().alpha(0f).setDuration(500).withEndAction(() -> viewRc.setVisibility(View.GONE)).start();
            }
            isExpanded = !isExpanded;
        });

        uploadRc.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            startActivityForResult(intent, 1);
        });

        viewRc.setOnClickListener(v -> {

            fileUrl = Global.dlpath + Global.sharedPreferences.getString("imgdoc_path", ""); // Your file URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
            startActivity(intent);
        });

        Backbtn.setOnClickListener(v -> {
            finish();
        });

        Country.setOnClickListener(v -> {
            countrypopup();
        });

        State.setOnClickListener(v -> {
            statespopup();
        });

        City.setOnClickListener(v -> {
            citiespopup();
        });


    }

    private void getcountries() {

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, Global.Getcountries, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Global.Countryarraylist = new ArrayList<zList>();
                statename = new zList();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {
                        // converting to json object
                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    statename = new zList();
                    try {
                        // getting the state name from the object
                        statename.set_name(e.getString("country_name"));
                        statename.set_code(e.getString("country_code"));
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Countryarraylist.add(statename);
                }

            }
        }, error -> {

            if (error instanceof NoConnectionError) {
                if (error instanceof TimeoutError) {
                    Global.customtoast(context, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(context, getLayoutInflater(), "Internet connection unavailable");
                } else if (error instanceof ServerError) {
                    Global.customtoast(context, getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(context, getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(context, getLayoutInflater(), "Parse Error");
                }
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

        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonArrayrequest);

    }

    public void countrypopup() {

        zDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.Countryarraylist == null || Global.Countryarraylist.size() == 0) {
            // Toast.makeText(getBaseContext(), "States list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final CountryAdapter laStates = new CountryAdapter(Global.Countryarraylist);
        lvStates.setAdapter(laStates);

        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView svstate = zDialog.findViewById(R.id.svstate);
        svstate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                laStates.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class CountryAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public CountryAdapter(ArrayList<zList> arrayList) {
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
                        mFilteredList = Global.Countryarraylist;
                    } else {
                        for (zList dataList : Global.Countryarraylist) {
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
                country_code = statename.get_code();
                city_code = "";
                zDialog.dismiss();
                //getcity();
                Country.setText(statename.get_name());
            });

            tvstatenameitem.setOnClickListener(view1 -> {
                statename = mDataArrayList.get(i);
                country_code = statename.get_code();
                city_code = "";
                zDialog.dismiss();
               // getcity();
                Country.setText(statename.get_name());
            });

            return v;
        }


    }


    private void getstates() {

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, Global.GetStates, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Global.statearraylist = new ArrayList<zList>();
                statename = new zList();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {
                        // converting to json object
                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    statename = new zList();
                    try {
                        // getting the state name from the object
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
                    Global.customtoast(context, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(context, getLayoutInflater(), "Internet connection unavailable");
                } else if (error instanceof ServerError) {
                    Global.customtoast(context, getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(context, getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(context, getLayoutInflater(), "Parse Error");
                }
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

        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonArrayrequest);

    }

    public void statespopup() {

        zDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.statearraylist == null || Global.statearraylist.size() == 0) {
            // Toast.makeText(getBaseContext(), "States list not found !! Please try again !!", Toast.LENGTH_LONG).show();
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
                //  Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
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
                state_code = statename.get_code();
                city_code = "";
                City.setText("");
                zDialog.dismiss();
                getcity();
                State.setText(statename.get_name());
            });

            tvstatenameitem.setOnClickListener(view1 -> {
                statename = mDataArrayList.get(i);
                state_code = statename.get_code();
                city_code = "";
                City.setText("");
                zDialog.dismiss();
                getcity();
                State.setText(statename.get_name());
            });

            return v;
        }


    }

    private void getcity() {
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Global.GetCity;
        url = url + "state_code=" + state_code;
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
                        // tvCity.setText("");
                        for (int i = 0; i < response.length(); i++) {
                            final JSONObject e;
                            try {
                                // converting to json object
                                e = response.getJSONObject(i);
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                            cityname = new zList();
                            try {
                                // getting the city name from the object
                                cityname.set_name(e.getString("city_name"));
                                cityname.set_code(e.getString("city_code"));
                                city_code = cityname.get_code();

                                if (cityname.get_name().isEmpty()) {
                                    Toast.makeText(context, "No city found for the selected state", Toast.LENGTH_SHORT).show();
                                    City.setText("");
                                } else {
                                    City.setText(cityname.get_name());
                                }


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
                            Global.customtoast(context, getLayoutInflater(), "Request Time-Out");
                        } else if (error instanceof NoConnectionError) {
                            Global.customtoast(context, getLayoutInflater(), "Internet connection unavailable");
                        } else if (error instanceof ServerError) {
                            Global.customtoast(context, getLayoutInflater(), "Server Error");
                        } else if (error instanceof NetworkError) {
                            Global.customtoast(context, getLayoutInflater(), "Network Error");
                        } else if (error instanceof ParseError) {
                            Global.customtoast(context, getLayoutInflater(), "Parse Error");
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

        zDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);        ;
        //zDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.cityarraylist == null || Global.cityarraylist.size() == 0) {
            City.setText("");
            Global.customtoast(context, getLayoutInflater(), "No cities found for selected state");

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
                //  Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
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
                    // Citytxt.setText(cityname.get_name());
                    city_code = cityname.get_code();
                    City.setText(cityname.get_name());
                    zDialog.dismiss();


                }
            });

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cityname = mDataArrayList.get(i);
                    // Citytxt.setText(cityname.get_name());
                    city_code = cityname.get_code();
                    City.setText(cityname.get_name());
                    zDialog.dismiss();

                }
            });
            return v;
        }
    }


    private void Updateprofiledetails() {

        String personname, mobile, email;

        personname = Name.getText().toString();
        mobile = Mobilenumber.getText().toString();
        email = Email.getText().toString();
        country_name = Country.getText().toString();
        state_name = State.getText().toString();
        city_name = City.getText().toString();

        if (personname.isEmpty()) {

            Toast.makeText(ProfileActivity.this, "Person name should not be empty!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Mobile number should not be empty !!", Toast.LENGTH_SHORT).show();

            return;
        }
        if (mobile.length() < 10) {
            Toast.makeText(ProfileActivity.this, "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();

            return;
        }
        if (email.length() < 10) {
            Toast.makeText(ProfileActivity.this, "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();

            return;
        }


        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urlupdateprofile,
                sresponse -> {

                    // progressBar.setVisibility(View.GONE);
                    JSONObject response = null;
                    try {
                        response = new JSONObject(sresponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("key_person", personname);
                    Global.editor.putString("Mobile1", mobile);
                    Global.editor.putString("Email", email);
                    Global.editor.putString("country_code", country_code);
                    Global.editor.putString("state_code", state_code);
                    Global.editor.putString("city_code", city_code);
                    Global.editor.putString("country_name", country_name);
                    Global.editor.putString("state_name", state_name);
                    Global.editor.putString("city_name", city_name);
                    Global.editor.commit();


                    try {
                        if (response.getBoolean("isSuccess")) {
//                                Toast.makeText(ProfileActivity.this, "Updated successfully !!", Toast.LENGTH_SHORT).show();
                            Global.customtoast(ProfileActivity.this, getLayoutInflater(), "Updated successfully !!");
                            finish();
                            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(intent);

                        } else {
                            //textViewError.setText(response.getString("error"));
                            Toast.makeText(ProfileActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                            //textViewError.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, error -> {

            //  progressBar.setVisibility(View.GONE);
            //  Toast.makeText(ProfileActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    /*textViewError.setText(error.getLocalizedMessage());
                    textViewError.setVisibility(View.VISIBLE);*/

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
                params.put("key_person", personname);
                params.put("username", Global.sharedPreferences.getString("userName", ""));
                params.put("wuser_code", Global.sharedPreferences.getString("wuser_code", ""));
                params.put("wuser_mobile1", mobile);
                params.put("wuser_mobile2", Mobilenumber.getText().toString());
                params.put("wuser_email", email);
                params.put("country_code", country_code);
                params.put("state_code", state_code);
                params.put("city_code", city_code);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);


    }

    private void openCamera() {
        // Check if the CAMERA permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the CAMERA permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            // Permission is already granted, start the camera intent
            startCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // CAMERA permission granted, start the camera intent
                startCameraIntent();
            } else {
                // CAMERA permission denied, handle accordingly (e.g., show a message to the user)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCameraIntent() {
        ImagePicker.with(ProfileActivity.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(10);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            String mimeType = context.getContentResolver().getType(fileUri);

            if (requestCode == REQUEST_CODE_IMAGE) {
                // Handle image
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), fileUri);
                    Uploadselectedimage(); // Your method
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == REQUEST_CODE_DOCUMENT) {
                // Handle document
                file_name = convertFileToBase64(fileUri); // Your method to convert to Base64

                if (mimeType != null) {
                    if (mimeType.equals("application/pdf")) {
                        file_type = "PDF";
                    } else if (mimeType.equals("application/msword")) {
                        file_type = "DOC";
                    } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                        file_type = "DOCX";
                    } else if (mimeType.equals("application/vnd.ms-excel")) {
                        file_type = "XLS";
                    } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                        file_type = "XLSX";
                    } else if (mimeType.startsWith("image/")) {
                        file_type = "IMG"; // Optional — to handle image selection from file picker
                    } else {
                        Toast.makeText(context, "Unknown file type", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                actual_filename = getFileName(fileUri); // Your method to get file name
                uploadfiletoserver(); // Your upload method
            }
        }
    }

    private void Uploadselectedimage() {

        if (imageBitmap == null) {
            return;
        }

        String url = Global.urlUpdateprofileImage;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            JSONObject resp;
            try {
                resp = new JSONObject(response);

                System.out.println(resp);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            try {
                if (resp.getBoolean("success")) {
                    String Message = resp.getString("message");
                    String uploadimage = resp.getString("data");

                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("Image", uploadimage);
                    Global.editor.commit();

                    String image = Global.userimageurl + uploadimage;
                    /*Picasso.get().load(image).into(circularImageView);*/
                    Global.loadWithPicasso(this, circularImageView, image);
                    Global.customtoast(ProfileActivity.this, getLayoutInflater(), Message);

                } else {
                    if (resp.has("error")) {

                        String errorMessage = resp.getString("error");
                        Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();


                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }


        }, error -> {
            //  progressBar.setVisibility(View.GONE);

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }


            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String image = imageToString(imageBitmap);

                params.put("fileName", image);
                params.put("cveh_code", Global.sharedPreferences.getString("cveh_code",""));
                Log.d("YourTag", "File Name: " + params.get("fileName"));

                // params.put("vehmas_code",Global.selectedvstock.getStockItemId());


                return params;
            }
        };

        // Add the stringRequest to the requestQueue to send the data to the server
        requestQueue.add(stringRequest);
    }

    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public void showImage(Picasso picasso, String userimage) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(dialogInterface -> {
            // Nothing
        });

        // Calculate display dimensions
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Load the image using Picasso
        picasso.load(Uri.parse(userimage)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageView imageView = new ImageView(getApplicationContext());

                // Calculate dimensions to fit the image within the screen
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                float aspectRatio = (float) imageWidth / imageHeight;

                int newWidth = screenWidth;
                int newHeight = (int) (screenWidth / aspectRatio);
                if (newHeight > screenHeight) {
                    newHeight = screenHeight;
                    newWidth = (int) (screenHeight * aspectRatio);
                }

                // Add padding values
                int paddingInDp = 16; // You can adjust the padding as per your requirement
                int paddingInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingInDp, ProfileActivity.this.getResources().getDisplayMetrics());

                // Adjust the newWidth and newHeight with padding
                newWidth -= 2 * paddingInPx; // Subtract padding from both sides
                newHeight -= 2 * paddingInPx; // Subtract padding from both top and bottom

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
                layoutParams.setMargins(paddingInPx, paddingInPx, paddingInPx, paddingInPx); // Set padding
                imageView.setLayoutParams(layoutParams);

                imageView.setImageBitmap(bitmap);

                builder.addContentView(imageView, layoutParams);
                builder.show();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle bitmap loading failure
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Prepare bitmap loading
            }
        });
    }


    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String convertFileToBase64(Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(inputStream);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void uploadfiletoserver() {
        showLoading();
        RequestQueue queue = Volley.newRequestQueue(context);
        String wuser_code= Global.sharedPreferences.getString("wuser_code", "");
        String url = Global.urluploadfiles;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resp = new JSONObject(response);
                    if (resp.getBoolean("isSuccess")) {
                        Global.customtoast(context, getLayoutInflater(), resp.getString("message"));
                        hideLoading();
                        getuserprofile();

                    } else {

                        if (resp.has("message")) {
                            Global.customtoast(context, getLayoutInflater(), resp.getString("message"));
                            hideLoading();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideLoading();
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    String errorResponse = new String(error.networkResponse.data);
                    try {
                        JSONObject errorJson = new JSONObject(errorResponse);
                        String errorDescription = errorJson.optString("error_description", "");
                        Global.customtoast(context, getLayoutInflater(), errorDescription);
                    } catch (JSONException e) {
                        Global.customtoast(context, getLayoutInflater(), "An error occurred. Please try again later.");
                    }
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error", Toast.LENGTH_LONG).show();
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("wuser_code", Global.sharedPreferences.getString("wuser_code", ""));
                params.put("imgdoc_path", file_name);
                params.put("file_type", file_type);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }

    private void showLoading() {
        // progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {

        //progressBar.setVisibility(View.GONE);
    }

    private void getuserprofile() {

        String url = Global.getuserprofiledetails;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

            try {
                JSONObject respObj1 = new JSONObject(response);
                JSONObject respObj = new JSONObject(respObj1.getString("data"));

                String userName = respObj.getString("userName");
                String key_person = respObj.getString("key_person");
                String Email = respObj.getString("Email");
                String Image = respObj.getString("Image");
                String Mobile1 = respObj.getString("Mobile1");
                String Mobile2 = respObj.getString("Mobile2");
                String Approved = respObj.getString("Approved");
                String Ref_Code = respObj.getString("Ref_Code");
                String Active = respObj.getString("Active");
                String Type = respObj.getString("Type");
                String wuser_code = respObj.getString("wuser_code");
                String cveh_code = respObj.getString("cveh_code");
                String imgdoc_path = respObj.getString("imgdoc_path");



                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("userName", userName);
                Global.editor.putString("key_person", key_person);
                Global.editor.putString("Email", Email);
                Global.editor.putString("Image", Image);
                Global.editor.putString("Mobile1", Mobile1);
                Global.editor.putString("Mobile2", Mobile2);
                Global.editor.putString("Approved", Approved);
                Global.editor.putString("Ref_Code", Ref_Code);
                Global.editor.putString("Active", Active);
                Global.editor.putString("Type", Type);
                Global.editor.putString("wuser_code", wuser_code);
                Global.editor.putString("cveh_code", cveh_code);
                Global.editor.putString("imgdoc_path", imgdoc_path);
                Global.editor.putString("country_code", country_code);
                Global.editor.putString("state_code", state_code);
                Global.editor.putString("city_code", city_code);
                Global.editor.putString("country_name", country_name);
                Global.editor.putString("state_name", state_name);
                Global.editor.putString("city_name", city_name);
                Global.editor.commit();
                Global.editor.commit();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "Internet connection unavailable", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error", Toast.LENGTH_LONG).show();
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        queue.add(request);
    }

    @Override
    public void onBackPressed() {

        finish();

        super.onBackPressed();
    }


}