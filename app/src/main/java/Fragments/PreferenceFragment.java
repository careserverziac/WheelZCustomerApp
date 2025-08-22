package Fragments;

import static ModelClasses.Global.editor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.wheelzcustomer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import ModelClasses.Global;
import ModelClasses.zList;


public class PreferenceFragment extends Fragment {

    TextView StatePref,CityPref,VtypePref;
    private zList modelclass,cityname,vehicletype;
    private String vtypecode,statecode,citycode;
    private String vtypeimageurl;
    private Dialog zDialog;
    Context context;
    ImageView Backbtn;
    //ProgressDialog progressDialog;
    LinearLayout Selectstate,Selectcity,Pref_Veh_type;
    Button Submit;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_preference, container, false);
        context = getActivity();

        getstates();
        getcity();
        getvehicletype();

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Selectstate=view.findViewById(R.id.selectstate);
        Selectcity=view.findViewById(R.id.selectcity);
        Pref_Veh_type =view.findViewById(R.id.select_veh_type);

        StatePref =view.findViewById(R.id.pref_state);
        CityPref =view.findViewById(R.id.pref_city);
        VtypePref =view.findViewById(R.id.pref_Vtype);
        Backbtn = view.findViewById(R.id.backbtn);
        Submit =view.findViewById(R.id.submit);

        Selectstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statespopup();
            }
        });

        Selectcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citiespopup();
            }
        });

        Pref_Veh_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicletypepopup();
            }
        });
        Backbtn.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        String statename = Global.sharedPreferences.getString("modelclass", "");
        String cityname = Global.sharedPreferences.getString("cityname", "");
        String vehicletype = Global.sharedPreferences.getString("vehicletype", "");

        vtypecode= Global.sharedPreferences.getString("vtypecode", "0");
        statecode= Global.sharedPreferences.getString("statecode", "0");
        citycode= Global.sharedPreferences.getString("citycode", "0");


        VtypePref.setText(vehicletype);
        CityPref.setText(cityname);
        StatePref.setText(statename);


        Submit.setOnClickListener(v -> {

            String state = StatePref.getText().toString().trim();
            String city = CityPref.getText().toString().trim();
            String vehtype = VtypePref.getText().toString().trim();

            boolean isValid = true;

            if (state.isEmpty()) {
                Selectstate.setBackgroundResource(R.drawable.bg_edittext_error);
                isValid = false;
            } else {
                Selectstate.setBackgroundResource(R.drawable.bg_edittext_normal);
            }

            if (city.isEmpty()) {
                Selectcity.setBackgroundResource(R.drawable.bg_edittext_error);
                isValid = false;
            } else {
                Selectcity.setBackgroundResource(R.drawable.bg_edittext_normal);
            }

            if (vehtype.isEmpty()) {
                Pref_Veh_type.setBackgroundResource(R.drawable.bg_edittext_error);
                isValid = false;
            } else {
                Pref_Veh_type.setBackgroundResource(R.drawable.bg_edittext_normal);
            }

            if (isValid) {

                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("modelclass", state);
                Global.editor.putString("cityname", city);
                Global.editor.putString("vehicletype", vehtype);
                Global.editor.putString("statecode", String.valueOf(statecode));
                Global.editor.putString("citycode", String.valueOf(citycode));
                Global.editor.putString("vtypecode", String.valueOf(vtypecode));
                Global.editor.apply();

                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });



        return  view;
    }



    private void getstates() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, Global.GetStates, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Global.statearraylist = new ArrayList<zList>();
                modelclass = new zList();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {
                        // converting to json object
                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    modelclass = new zList();
                    try {
                        // getting the state name from the object
                        modelclass.set_name(e.getString("state_name"));
                        modelclass.set_code(e.getString("state_code"));
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.statearraylist.add(modelclass);
                }

            }
        },  error -> {

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

        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
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
            modelclass = mDataArrayList.get(i);
            tvstatenameitem.setText(modelclass.get_name());

            radioButton.setOnClickListener(view1 -> {
                boolean isChecked = radioButton.isChecked();
                radioButton.setChecked(!isChecked);
                modelclass = mDataArrayList.get(i);
                statecode = modelclass.get_code();

                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("statecode",String.valueOf(statecode));
                Global.editor.apply();

                citycode = "";
                CityPref.setText("");
                zDialog.dismiss();
                getcity();
                StatePref.setText(modelclass.get_name());
            });

            tvstatenameitem.setOnClickListener(view1 -> {
                modelclass = mDataArrayList.get(i);
                statecode = modelclass.get_code();
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("statecode",String.valueOf(statecode));
                Global.editor.apply();
                citycode = "";
                CityPref.setText("");
                zDialog.dismiss();
                getcity();
                StatePref.setText(modelclass.get_name());
            });

            return v;
        }


    }


    private void getcity() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url=Global.GetCity;
        url= url+"state_code="+Global.sharedPreferences.getString("statecode","");
        StringRequest jsonArrayrequest = new StringRequest(Request.Method.GET,url,
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
                                citycode = cityname.get_code();

                                if (cityname.get_name().isEmpty()) {
                                    CityPref.setText("");
                                }else {
                                    CityPref.setText(cityname.get_name());
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

        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jsonArrayrequest);

    }

    public void citiespopup() {

        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        //zDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.cityarraylist == null || Global.cityarraylist.size() == 0) {
            CityPref.setText("");
            Global.customtoast(requireActivity(), getLayoutInflater(), "No cities found for selected state");

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
            RadioButton radioButton=v.findViewById(R.id.radio_button);
            cityname = mDataArrayList.get(i);
            tvstatenameitem.setText(cityname.get_name());


            tvstatenameitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cityname = mDataArrayList.get(i);
                    citycode = cityname.get_code();
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("citycode",String.valueOf(citycode));
                    Global.editor.apply();
                    CityPref.setText(cityname.get_name());
                    Selectcity.setBackgroundResource(R.drawable.bg_edittext_normal);
                    zDialog.dismiss();


                }
            });

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cityname = mDataArrayList.get(i);
                    citycode = cityname.get_code();
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("citycode",String.valueOf(citycode));
                    Global.editor.apply();
                    CityPref.setText(cityname.get_name());
                    Selectcity.setBackgroundResource(R.drawable.bg_edittext_normal);
                    zDialog.dismiss();

                }
            });

            return v;
        }
    }

    private void getvehicletype() {
        String comcode =Global.sharedPreferences.getString("com_code","");
        String URL = Global.urlGetVehicletype + "comcode=" + comcode;
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, response -> {
            try {
                if (response.getBoolean("isSuccess")) {
                    JSONArray dataArray = response.getJSONArray("data");
                    Global.vehicletypearraylist = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject obj = dataArray.getJSONObject(i);
                        zList type = new zList();
                        type.set_name(obj.optString("vcate_name", ""));
                        type.set_code(obj.optString("vcate_code", ""));
                        Global.vehicletypearraylist.add(type);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void vehicletypepopup() {

        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvVtype = zDialog.findViewById(R.id.lvstates);

        if (Global.vehicletypearraylist == null || Global.vehicletypearraylist.size() == 0) {
            Toast.makeText(requireActivity(), "Vehicle type list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final VehicletypeAdapter lavtype = new VehicletypeAdapter(Global.vehicletypearraylist);
        lvVtype.setAdapter(lavtype);

        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView svvtype = zDialog.findViewById(R.id.svstate);
        svvtype.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                lavtype.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class VehicletypeAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public VehicletypeAdapter(ArrayList<zList> arrayList) {
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
                        mFilteredList = Global.vehicletypearraylist;
                    } else {
                        for (zList dataList : Global.vehicletypearraylist) {
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
            @SuppressLint({"ViewHolder", "InflateParams"}) View v = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            final TextView tvstatenameitem = v.findViewById(R.id.tvsingleitem);
            vehicletype = mDataArrayList.get(i);
            tvstatenameitem.setText(vehicletype.get_name());

            tvstatenameitem.setOnClickListener(view1 -> {
                zList selectedItem = mDataArrayList.get(i);
                VtypePref.setText(selectedItem.get_name());
                Pref_Veh_type.setBackgroundResource(R.drawable.bg_edittext_normal);
                vtypecode = selectedItem.get_code();

                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("vtypecode", vtypecode);
                Global.editor.apply(); // Use apply() instead of commit() for async saving
                zDialog.dismiss();
            });
            /*tvstatenameitem.setOnClickListener(view1 -> {
                vehicletype = mDataArrayList.get(i);
                VtypePref.setText(vehicletype.get_name());
                Pref_Veh_type.setBackgroundResource(R.drawable.bg_edittext_normal);
                vtypecode = vehicletype.get_code();
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("vtypecode", vtypecode);
                Global.editor.commit();
                zDialog.dismiss();

            });*/
            return v;
        }
    }



}