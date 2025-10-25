package Fragments;

import static ModelClasses.Global.sharedPreferences;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.wheelzcustomer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import ModelClasses.AppStatus;
import ModelClasses.Global;
import ModelClasses.zList;


public class AddVehFragment extends Fragment {

    TextView ManufactureTV, ModelsTV, VariantTV, ColorTV, FuelTV,NumberPlateTV,TransmissionTV,InsurTV,LstngTV,OwnershipTV;
    EditText VehregistrationET, KmsdrivenET, ManufacturedyearET, SellingpriceET;
    Button Submit;
    private static zList Commonclass;
    private static Dialog zDialog;
     String numberplateValue,displayText, transmissionValue, ownershipValue, insuranceValue, listingvalue, fuelcode, variantcode, colorcode, vmodelcode;
    FragmentActivity reqact;

    ProgressDialog progressDialog;
    LinearLayout Num_plate,Transm,Insur,Lstng,Ownership,Manufacture,Models,Variant,Color,Registration,Km,Year,Fuel,Price;



    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_veh, viewGroup, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        reqact = requireActivity();
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!! please wait");
        progressDialog.setCancelable(false);

        getmanufactureveh();
        getfuel();

        ManufactureTV = view.findViewById(R.id.tv_vehman);
        ModelsTV = view.findViewById(R.id.tv_vehmdl);
        VariantTV = view.findViewById(R.id.tv_vehvrnt);
        ColorTV = view.findViewById(R.id.tv_vehclr);
        FuelTV = view.findViewById(R.id.tv_vehfuel);


        Manufacture = view.findViewById(R.id.manufacturer_layout);
        Models = view.findViewById(R.id.model_layout);
        Variant = view.findViewById(R.id.variant_layout);
        Color = view.findViewById(R.id.colour_layout);
        Registration= view.findViewById(R.id.registration_layout);
        Km = view.findViewById(R.id.km_layout);
        Year = view.findViewById(R.id.year_layout);
        Fuel = view.findViewById(R.id.fuel_layout);
        Price = view.findViewById(R.id.price_layout);
        Submit = view.findViewById(R.id.add_Vehbutton);


        Num_plate = view.findViewById(R.id.numplate_lnr);
        NumberPlateTV = view.findViewById(R.id.tv_numberplate);
        Transm = view.findViewById(R.id.trans_lnr);
        TransmissionTV = view.findViewById(R.id.tv_transmission);
        Insur = view.findViewById(R.id.insur_lnr);
        InsurTV = view.findViewById(R.id.tv_insur);
        Lstng = view.findViewById(R.id.lstg_lnr);
        LstngTV = view.findViewById(R.id.tv_lstg);
        Ownership = view.findViewById(R.id.ownership_lnr);
        OwnershipTV = view.findViewById(R.id.tv_ownership);

        VehregistrationET = view.findViewById(R.id.et_vehregistration);
        KmsdrivenET = view.findViewById(R.id.et_vehkmdriven);
        ManufacturedyearET = view.findViewById(R.id.et_vehmfgyear);
        SellingpriceET = view.findViewById(R.id.et_vehsellingprice);

        Manufacture.setOnClickListener(v -> manufacturepopup());
        Models.setOnClickListener(v -> modelsspopup());
        Variant.setOnClickListener(v -> variantspopup());
        Color.setOnClickListener(v -> vehcolourpopup());
        Fuel.setOnClickListener(v -> fuelpopup());

        Submit.setOnClickListener(v -> Addvehicledetails());

        Num_plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numplate_dp();
            }
        });

        Transm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transmissiondp();
            }
        });

        Insur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insurancedp();
            }
        });

        Lstng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listingdp();
            }
        });

        Ownership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ownershipdp();
            }
        });


    }

    private void numplate_dp() {
        String[] numberplate = {"White Board", "Yellow Board", "Green Board"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        TextView titleView = new TextView(requireContext());
        titleView.setText("Select");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Set text size
        titleView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.arsenal));
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        Handler handler = new Handler();

        for (String plate : numberplate) {
            View itemView = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            TextView tvItem = itemView.findViewById(R.id.tvsingleitem);
            RadioButton radioButton = itemView.findViewById(R.id.radio_button);

            tvItem.setText(plate);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    switch (plate) {
                        case "White Board":
                            numberplateValue = "W";
                            displayText = "White Board";
                            break;
                        case "Yellow Board":
                            numberplateValue = "Y";
                            displayText = "Yellow Board";
                            break;
                        case "Green Board":
                            numberplateValue = "G";
                            displayText = "Green Board";
                            break;
                        default:
                            numberplateValue = "";
                            displayText = "";
                            break;
                    }
                    if (NumberPlateTV != null) {
                        NumberPlateTV.setText(displayText);
                    }
                    handler.postDelayed(() -> {
                        if (buttonView.getTag() instanceof AlertDialog) {
                            ((AlertDialog) buttonView.getTag()).dismiss();
                        }
                    }, 500);
                }
            });
            radioButton.setTag(builder.create());
            itemView.setOnClickListener(v -> {
                radioButton.setChecked(!radioButton.isChecked());
            });

            layout.addView(itemView);
        }

        builder.setView(layout);
        AlertDialog dialog = builder.create();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View item = layout.getChildAt(i);
            RadioButton rb = item.findViewById(R.id.radio_button);
            rb.setTag(dialog);
        }

        dialog.show();
    }
    private void transmissiondp() {
        String[] transmission = {"Auto", "Manual", "Not Applicable"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        TextView titleView = new TextView(requireContext());
        titleView.setText("Select");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Set text size
        titleView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.arsenal));
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        Handler handler = new Handler();

        for (String plate : transmission) {
            View itemView = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            TextView tvItem = itemView.findViewById(R.id.tvsingleitem);
            RadioButton radioButton = itemView.findViewById(R.id.radio_button);

            tvItem.setText(plate);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    switch (plate) {
                        case "Auto":
                            transmissionValue = "A";
                            displayText = "Auto";
                            break;
                        case "Manual":
                            transmissionValue = "M";
                            displayText = "Manual";
                            break;
                        case "Not Applicable":
                            transmissionValue = "N";
                            displayText = "Not Applicable";
                            break;
                        default:
                            transmissionValue = "";
                            break;
                    }
                    if (TransmissionTV != null) {
                        TransmissionTV.setText(displayText);
                    }
                    handler.postDelayed(() -> {
                        if (buttonView.getTag() instanceof AlertDialog) {
                            ((AlertDialog) buttonView.getTag()).dismiss();
                        }
                    }, 500);
                }
            });
            radioButton.setTag(builder.create());
            itemView.setOnClickListener(v -> {
                radioButton.setChecked(!radioButton.isChecked());
            });

            layout.addView(itemView);
        }

        builder.setView(layout);
        AlertDialog dialog = builder.create();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View item = layout.getChildAt(i);
            RadioButton rb = item.findViewById(R.id.radio_button);
            rb.setTag(dialog);
        }

        dialog.show();
    }
    private void insurancedp() {
        String[] insurance = {"Comprehensive", "Third Party", "No Insurance"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        TextView titleView = new TextView(requireContext());
        titleView.setText("Select");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Set text size
        titleView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.arsenal));
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        Handler handler = new Handler();

        for (String plate : insurance) {
            View itemView = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            TextView tvItem = itemView.findViewById(R.id.tvsingleitem);
            RadioButton radioButton = itemView.findViewById(R.id.radio_button);

            tvItem.setText(plate);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    switch (plate) {
                        case "Comprehensive":
                            insuranceValue = "C";
                            displayText = "Comprehensive";
                            break;
                        case "Third Party":
                            insuranceValue = "T";
                            displayText = "Third Party";
                            break;
                        case "No Insurance":
                            insuranceValue = "N";
                            displayText = "No Insurance";
                            break;
                        default:
                            insuranceValue = ""; // Handle other cases if needed

                            break;
                    }
                    if (InsurTV != null) {
                        InsurTV.setText(displayText);
                    }
                    handler.postDelayed(() -> {
                        if (buttonView.getTag() instanceof AlertDialog) {
                            ((AlertDialog) buttonView.getTag()).dismiss();
                        }
                    }, 500);
                }
            });
            radioButton.setTag(builder.create());
            itemView.setOnClickListener(v -> {
                radioButton.setChecked(!radioButton.isChecked());
            });

            layout.addView(itemView);
        }

        builder.setView(layout);
        AlertDialog dialog = builder.create();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View item = layout.getChildAt(i);
            RadioButton rb = item.findViewById(R.id.radio_button);
            rb.setTag(dialog);
        }

        dialog.show();
    }
    private void listingdp() {
        String[] listingtype = {"Normal", "Silver", "Gold", "Platinum"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        TextView titleView = new TextView(requireContext());
        titleView.setText("Select");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Set text size
        titleView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.arsenal));
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        Handler handler = new Handler();

        for (String plate : listingtype) {
            View itemView = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            TextView tvItem = itemView.findViewById(R.id.tvsingleitem);
            RadioButton radioButton = itemView.findViewById(R.id.radio_button);

            tvItem.setText(plate);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    switch (plate) {
                        case "Normal":
                            listingvalue = "N";
                            displayText = "Normal";
                            break;
                        case "Silver":
                            listingvalue = "S";
                            displayText = "Silver";
                            break;
                        case "Gold":
                            listingvalue = "G";
                            displayText = "Gold";
                            break;
                        case "Platinum":
                            listingvalue = "P";
                            displayText = "Platinum";
                            break;
                        default:
                            listingvalue = ""; // Handle other cases if needed

                            break;
                    }
                    if (LstngTV != null) {
                        LstngTV.setText(displayText);
                    }
                    handler.postDelayed(() -> {
                        if (buttonView.getTag() instanceof AlertDialog) {
                            ((AlertDialog) buttonView.getTag()).dismiss();
                        }
                    }, 500);
                }
            });
            radioButton.setTag(builder.create());
            itemView.setOnClickListener(v -> {
                radioButton.setChecked(!radioButton.isChecked());
            });

            layout.addView(itemView);
        }

        builder.setView(layout);
        AlertDialog dialog = builder.create();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View item = layout.getChildAt(i);
            RadioButton rb = item.findViewById(R.id.radio_button);
            rb.setTag(dialog);
        }

        dialog.show();
    }
    private void ownershipdp() {

        String[] ownershiptype = {"Single", "Second", "Third", "Fourth", "Multiple"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        TextView titleView = new TextView(requireContext());
        titleView.setText("Select");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Set text size
        titleView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.arsenal));
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        Handler handler = new Handler();

        for (String plate : ownershiptype) {
            View itemView = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            TextView tvItem = itemView.findViewById(R.id.tvsingleitem);
            RadioButton radioButton = itemView.findViewById(R.id.radio_button);

            tvItem.setText(plate);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    switch (plate) {
                        case "Single":
                            ownershipValue = "S";
                            break;
                        case "Second":
                            ownershipValue = "D";
                            break;
                        case "Third":
                            ownershipValue = "T";
                            break;
                        case "Fourth":
                            ownershipValue = "F";
                            break;
                        case "Multiple":
                            ownershipValue = "M";
                            break;
                        default:
                            ownershipValue = "";

                            break;
                    }
                    if (OwnershipTV != null) {
                        OwnershipTV.setText(displayText);
                    }
                    handler.postDelayed(() -> {
                        if (buttonView.getTag() instanceof AlertDialog) {
                            ((AlertDialog) buttonView.getTag()).dismiss();
                        }
                    }, 500);
                }
            });
            radioButton.setTag(builder.create());
            itemView.setOnClickListener(v -> {
                radioButton.setChecked(!radioButton.isChecked());
            });

            layout.addView(itemView);
        }

        builder.setView(layout);
        AlertDialog dialog = builder.create();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View item = layout.getChildAt(i);
            RadioButton rb = item.findViewById(R.id.radio_button);
            rb.setTag(dialog);
        }

        dialog.show();
    }

    private void getmanufactureveh() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.urlgetmanufacture + "?vcate_code=" + sharedPreferences.getString("vtypecode", "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try {
                JSONArray dataArray = response.getJSONArray("data");

                Global.vehiclemanufacturelist = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject e = dataArray.getJSONObject(i);
                    zList item = new zList();

                    item.set_name(e.getString("mfg_name"));
                    item.set_code(e.getString("mfg_code"));
                    item.setVeh_type_code(e.getString("vcate_code"));

                    Global.vehiclemanufacturelist.add(item);
                }


                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                Global.editor = sharedPreferences.edit();

                if (!Global.vehiclemanufacturelist.isEmpty()) {
                    Global.editor.putString("mfg_code", Global.vehiclemanufacturelist.get(0).get_code());
                    Global.editor.putString("mfg_name", Global.vehiclemanufacturelist.get(0).get_name());
                    ManufactureTV.setText(Global.vehiclemanufacturelist.get(0).get_name());
                    getmodels();
                } else {
                    Global.editor.putString("mfg_code", "0");
                    Global.editor.putString("mfg_name", "");
                }

                Global.editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            // Handle error response
            // Global.customtoast(getContext(), getLayoutInflater(), "Failed to get vehicle manufacture list.." + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void manufacturepopup() {


        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.vehiclemanufacturelist == null || Global.vehiclemanufacturelist.size() == 0) {
            return;
        }

        final ManufactureAdapter laStates = new ManufactureAdapter(Global.vehiclemanufacturelist);
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

    private class ManufactureAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public ManufactureAdapter(ArrayList<zList> arrayList) {
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
                        mFilteredList = Global.vehiclemanufacturelist;
                    } else {
                        for (zList dataList : Global.vehiclemanufacturelist) {
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
            final RadioButton radioButton = v.findViewById(R.id.radio_button);

            Commonclass = mDataArrayList.get(i);
            tvstatenameitem.setText(Commonclass.get_name());
            radioButton.setText(""); // optional, remove label

            View.OnClickListener clickListener = view1 -> {
                Commonclass = mDataArrayList.get(i);
                ManufactureTV.setText(Commonclass.get_name());

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                Global.editor = sharedPreferences.edit();
                Global.editor.putString("mfg_code", Commonclass.get_code());
                Global.editor.putString("mfg_name", Commonclass.get_name());
                Global.editor.commit();

                // Delay dismiss and method call
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (zDialog != null && zDialog.isShowing()) {
                        zDialog.dismiss();
                    }
                    getmodels(); // call after dismissing
                }, 500);
            };

            // Apply to both TextView and RadioButton
            tvstatenameitem.setOnClickListener(clickListener);
            radioButton.setOnClickListener(clickListener);

            return v;
        }

    }


    private void getmodels() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.urlGetVehiclesModel;
        url = url + "&vcate_code=" + sharedPreferences.getString("vtypecode", "0");
        url = url + "&mfg_code=" + sharedPreferences.getString("mfg_code", "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try {
                JSONArray dataArray = response.getJSONArray("data");

                Global.vehiclemodellist = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject e = dataArray.getJSONObject(i);
                    zList item = new zList();

                    item.set_name(e.getString("model_name"));
                    item.set_code(e.getString("vmodel_code"));

                    Global.vehiclemodellist.add(item);
                }

               // ModelsTV.setText("Not found");

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                Global.editor = sharedPreferences.edit();

                if (!Global.vehiclemodellist.isEmpty()) {
                    Global.editor.putString("vmodel_code", Global.vehiclemodellist.get(0).get_code());
                    Global.editor.putString("model_name", Global.vehiclemodellist.get(0).get_name());
                    vmodelcode = Global.vehiclemodellist.get(0).get_code();
                    ModelsTV.setText(Global.vehiclemodellist.get(0).get_name());
                    getvariant();
                } else {
                    Global.editor.putString("vmodel_code", "0");
                    Global.editor.putString("model_name", "0");
                }

                Global.editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            // Global.customtoast(requireActivity(), getLayoutInflater(), "Failed to get vehicle models.." + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void modelsspopup() {

        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        //zDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zDialog.setContentView(R.layout.popup_list);
        ListView lvStates;
        lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.vehiclemodellist == null || Global.vehiclemodellist.size() == 0) {

            Global.customtoast(requireActivity(), getLayoutInflater(), "Vehicle model list not found !! Please try again !!");
            return;
        }
        ModelsAdapter laStates;
        laStates = new ModelsAdapter(Global.vehiclemodellist);
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

    private class ModelsAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public ModelsAdapter(ArrayList<zList> arrayList) {
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
                        mFilteredList = Global.vehiclemodellist;
                    } else {
                        for (zList dataList : Global.vehiclemodellist) {
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
            final TextView tvmodelname = v.findViewById(R.id.tvsingleitem);
            Commonclass = mDataArrayList.get(i);
            tvmodelname.setText(Commonclass.get_name());
            tvmodelname.setOnClickListener(view1 -> {
                Commonclass = mDataArrayList.get(i);
                ModelsTV.setText(Commonclass.get_name());
                vmodelcode = Commonclass.get_code();
                zDialog.dismiss();
                getvariant();
                getvehiclecolour();
            });
            return v;
        }
    }


    private void getvariant() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = Global.urlgetvariant + "vmodel_code=" + vmodelcode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try {
                JSONArray dataArray = response.getJSONArray("data");

                Global.vehiclevariantlist = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject e = dataArray.getJSONObject(i);
                    zList item = new zList();

                    item.set_name(e.getString("vart_name").toUpperCase());
                    item.set_code(e.getString("vart_code"));
                    item.setVeh_type_code(e.getString("vmodel_code"));

                    Global.vehiclevariantlist.add(item);
                }

                //VariantTV.setText("Not found");

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                Global.editor = sharedPreferences.edit();

                if (!Global.vehiclevariantlist.isEmpty()) {
                    Global.editor.putString("vart_code", Global.vehiclevariantlist.get(0).get_code());
                    Global.editor.putString("vart_name", Global.vehiclevariantlist.get(0).get_name());
                    variantcode = Global.vehiclevariantlist.get(0).get_code();
                    VariantTV.setText(Global.vehiclevariantlist.get(0).get_name());
                    getvehiclecolour();
                } else {
                    Global.editor.putString("vart_code", "0");
                    Global.editor.putString("vart_name", "0");
                }

                Global.editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            // Global.customtoast(getContext(), getLayoutInflater(), "Failed to get vehicle variant list.." + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }


    public void variantspopup() {


        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        //zDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.vehiclevariantlist == null || Global.vehiclevariantlist.size() == 0) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "No data found for variants !!");

            // Toast.makeText(getContext(), "Manufacture list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final VariantAdapter laStates = new VariantAdapter(Global.vehiclevariantlist);
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

    class VariantAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public VariantAdapter(ArrayList<zList> arrayList) {
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
                        mFilteredList = Global.vehiclevariantlist;
                    } else {
                        for (zList dataList : Global.vehiclevariantlist) {
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
            final TextView tvvariantname = v.findViewById(R.id.tvsingleitem);
            Commonclass = mDataArrayList.get(i);
            tvvariantname.setText(Commonclass.get_name());
            tvvariantname.setOnClickListener(view1 -> {
                Commonclass = mDataArrayList.get(i);
                VariantTV.setText(Commonclass.get_name());
                variantcode = Commonclass.get_code();

                zDialog.dismiss();


            });
            return v;
        }
    }


    private void getvehiclecolour() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = Global.urlgetvehcolour + "vmodel_code=" + vmodelcode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try {
                JSONArray dataArray = response.getJSONArray("data");

                Global.vehiclecolourlist = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject e = dataArray.getJSONObject(i);
                    zList item = new zList();

                    item.set_name(e.getString("vcol_name").toUpperCase());
                    item.set_code(e.getString("vcol_code"));
                    item.setVeh_type_code(e.getString("vmodel_code"));

                    Global.vehiclecolourlist.add(item);
                }

                //ColorTV.setText("Not found");

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                Global.editor = sharedPreferences.edit();

                if (!Global.vehiclecolourlist.isEmpty()) {
                    Global.editor.putString("vcol_code", Global.vehiclecolourlist.get(0).get_code());
                    Global.editor.putString("vcol_name", Global.vehiclecolourlist.get(0).get_name());
                    colorcode = Global.vehiclecolourlist.get(0).get_code();
                    ColorTV.setText(Global.vehiclecolourlist.get(0).get_name());
                } else {
                    Global.editor.putString("vcol_code", "0");
                    Global.editor.putString("vcol_name", "0");
                }

                Global.editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Global.customtoast(getContext(), getLayoutInflater(), "Failed to get colour list.." + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }


    public void vehcolourpopup() {


        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.vehiclecolourlist == null || Global.vehiclecolourlist.size() == 0) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "No data found for colour !!");
            // Toast.makeText(getContext(), "Manufacture list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final VehcolourAdapter laStates = new VehcolourAdapter(Global.vehiclecolourlist);
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

    class VehcolourAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public VehcolourAdapter(ArrayList<zList> arrayList) {
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
                        mFilteredList = Global.vehiclecolourlist;
                    } else {
                        for (zList dataList : Global.vehiclecolourlist) {
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
            final TextView tvvariantname = v.findViewById(R.id.tvsingleitem);
            Commonclass = mDataArrayList.get(i);
            tvvariantname.setText(Commonclass.get_name());
            //colorcode=Commonclass.get_code();
            // Toast.makeText(reqact, colorcode, Toast.LENGTH_SHORT).show();
            tvvariantname.setOnClickListener(view1 -> {
                Commonclass = mDataArrayList.get(i);
                ColorTV.setText(Commonclass.get_name());
                colorcode = Commonclass.get_code();
                // Toast.makeText(reqact, colorcode, Toast.LENGTH_SHORT).show();

                zDialog.dismiss();


            });
            return v;
        }
    }


    private void getfuel() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = Global.urlgetfuel;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try {
                JSONArray dataArray = response.getJSONArray("data");

                Global.vehiclefuellist = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject e = dataArray.getJSONObject(i);
                    zList item = new zList();

                    item.set_name(e.getString("fuel_name").toUpperCase());
                    item.set_code(e.getString("fuel_code"));

                    Global.vehiclefuellist.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            // Uncomment if you want to show specific error messages:
        /*
        if (error instanceof TimeoutError) {
            Global.customtoast(getContext(), getLayoutInflater(), "Request Time-Out fuel");
        } else if (error instanceof NoConnectionError) {
            Global.customtoast(getContext(), getLayoutInflater(), "No Connection Found fuel");
        } else if (error instanceof ServerError) {
            Global.customtoast(getContext(), getLayoutInflater(), "Server Error fuel");
        } else if (error instanceof NetworkError) {
            Global.customtoast(getContext(), getLayoutInflater(), "Network Error fuel");
        } else if (error instanceof ParseError) {
            Global.customtoast(getContext(), getLayoutInflater(), "Parse Error fuel");
        }
        */
            // Global.customtoast(getContext(), getLayoutInflater(), "Failed to get fuel list.." + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }


    public void fuelpopup() {


        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        //zDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.vehiclefuellist == null || Global.vehiclefuellist.size() == 0) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Colour list not found !! Please try again !!");
            // Toast.makeText(getContext(), "Manufacture list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final NumberplateAdapter laStates = new NumberplateAdapter(Global.vehiclefuellist);
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

    class NumberplateAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public NumberplateAdapter(ArrayList<zList> arrayList) {
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
                        mFilteredList = Global.vehiclefuellist;
                    } else {
                        for (zList dataList : Global.vehiclefuellist) {
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
            final TextView tvvariantname = v.findViewById(R.id.tvsingleitem);
            Commonclass = mDataArrayList.get(i);
            tvvariantname.setText(Commonclass.get_name());
            tvvariantname.setOnClickListener(view1 -> {
                Commonclass = mDataArrayList.get(i);
                FuelTV.setText(Commonclass.get_name());
                fuelcode = Commonclass.get_code();

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                Global.editor = sharedPreferences.edit();
                Global.editor.putString("fuel_code", Commonclass.get_code());
                Global.editor.commit();

                zDialog.dismiss();
            });
            return v;
        }
    }


    private void Addvehicledetails() {

        if (AppStatus.getInstance(requireActivity()).isOnline()) {

        } else {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Connected WIFI or Mobile data has no internet access!!");
        }

        String vehmanufacture, vehmodels, vehvariant, vehcolor, registration_no, kmsdriven, manufacturedyear, fuel, sellingprice;

        registration_no = VehregistrationET.getText().toString();
        vehmodels = ModelsTV.getText().toString();
        vehvariant = VariantTV.getText().toString();
        vehcolor = ColorTV.getText().toString();
        kmsdriven = KmsdrivenET.getText().toString();
        manufacturedyear = ManufacturedyearET.getText().toString();
        sellingprice = SellingpriceET.getText().toString();
        vehmanufacture = ManufactureTV.getText().toString();
        fuel = FuelTV.getText().toString();


        if (vehmanufacture.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Vehicle manufacture field must not be empty !!");
            return;
        }
        if (vehmodels.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Vehicle model field must not be empty !!");
            return;
        }
        if (vehvariant.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Vehicle variant field must not be empty !!");
            return;
        }
        if (vehcolor.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Vehicle colour field must not be empty !!");
            return;
        }
        if (registration_no.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Registration field must not be empty !!");
            return;
        }
        if (kmsdriven.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Kilometer field must not be empty!!");
            return;
        } else {
            int kmsDrivenValue = Integer.parseInt(kmsdriven);
            if (kmsDrivenValue == 0) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "0 is not accepted!!");
                return;
            }

        }

        if (manufacturedyear.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Manufacture year field must not be empty !!");
            return;
        } else {
            if (!manufacturedyear.matches("\\d{4}")) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Please enter a valid year (4-digit number) !!");
                return;
            }

            int year = Integer.parseInt(manufacturedyear);

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1900 || year > currentYear) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Please select a valid year before " + currentYear + " !!");
                return;
            }
        }
        if (fuel.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "vehicle fuel field must not be empty !!");
            return;
        }
        if (sellingprice.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Selling price field must not be empty !!");
            return;
        }

        try {
            double price = Double.parseDouble(sellingprice);
            int priceIntegerPart = (int) price;

            if (priceIntegerPart <= 9999 || priceIntegerPart == 0) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Invalid selling price format !!");
                return;
            }

        } catch (NumberFormatException e) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Invalid selling price format !!");
            return;
        }


        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urlpostaddvehicle,
                sresponse -> {
                    JSONObject response;

                    try {
                        response = new JSONObject(sresponse);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    try {
                        if (response.getBoolean("isSuccess")) {
                            Global.customtoast(requireActivity(), getLayoutInflater(), response.getString("message"));

                        } else {
                            if (response.has("message")) {
                                progressDialog.dismiss();
                                Global.customtoast(requireActivity(), getLayoutInflater(), response.getString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "No Connection Found");
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
                String accesstoken = sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("city_code", sharedPreferences.getString("citycode", ""));
                params.put("com_code", sharedPreferences.getString("com_code", ""));
                params.put("vmodel_code", vmodelcode);
                params.put("vcol_code", colorcode);
                params.put("year_of_mfg", manufacturedyear);
                params.put("kms_driven", kmsdriven);
                params.put("expected_price_range", "0");
               // params.put("pricing_type", kmsdriven);
                params.put("price_range_flag", "1");
                params.put("veh_sale_price", sellingprice);
                params.put("reg_no", registration_no);
                params.put("vart_code", variantcode);
                params.put("fuel_code", fuelcode);
                params.put("ownership_type", ownershipValue);
                params.put("insurance_type", insuranceValue);
                params.put("listing_type", listingvalue);
                params.put("transmission_type", transmissionValue);
                params.put("no_plate_type", numberplateValue);

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
