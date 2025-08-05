package Fragments;

import static ModelClasses.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.wheelzcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ModelClasses.Global;
import ModelClasses.zList;

public class UpdateVehFragment extends Fragment {


    private Bitmap imageBitmap;
    TextView ManufactureTV, ModelsTV, VariantTV, ColorTV, FuelTV, NumberPlateTV, TransmissionTV, InsurTV, LstngTV, OwnershipTV;
    LinearLayout Num_plate, Transm, Insur, Lstng, Ownership, Manufacture, Models, Variant, Color, Registration, Km, Year, Fuel, Price;

    EditText VehregistrationET, KmsdrivenET, ManufacturedyearET, SellingpriceET, Man_yearET, Sell_priceET;
    LinearLayout Update_veh;
    private zList Commonclass;
    private Dialog zDialog;

    FloatingActionButton Camera;
    ImageView Veh_img;
    String vart_code,vart_name,transm_name,km,num_plate,man_year,owntype,fuel_type,fuel_code,
            sell_price,vmodel_code,variantcode,fuelcode,vehmas_code,numberplateValue,displayText,
            transmissionValue, ownershipValue, insuranceValue, listingvalue,colorcode, vmodelcode,
            reg_no,insc_type,list_type,city_code,state_code,img;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);


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
        Update_veh = view.findViewById(R.id.add_Vehbutton);
        Camera = view.findViewById(R.id.img_upload);
        Veh_img = view.findViewById(R.id.veh_image);

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


        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewVehImageFragment viewVehImageFragment = new ViewVehImageFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, viewVehImageFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


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

        vart_code = Global.commonVehClass.getVart_code();
        vart_name = Global.commonVehClass.getVart_name();
        transm_name = Global.commonVehClass.getTrans_name();
        km = Global.commonVehClass.getKm_drvn();
        num_plate = Global.commonVehClass.getNum_plate();
        man_year = Global.commonVehClass.getMan_year();
        owntype = Global.commonVehClass.getOwn_type();
        fuel_type = Global.commonVehClass.getFuel_name();
        fuel_code = Global.commonVehClass.getFuel_code();
        sell_price = Global.commonVehClass.getSell_price();
        vmodel_code = Global.commonVehClass.getVmodel_code();
        vehmas_code = Global.commonVehClass.getVehmas_code();
        reg_no = Global.commonVehClass.getReg_no();
        insc_type = Global.commonVehClass.getInsc_type();
        list_type = Global.commonVehClass.getLis_type();
        city_code = Global.commonVehClass.getCity_code();
        state_code = Global.commonVehClass.getState_code();
        img = Global.commonVehClass.getVeh_img();

        Global.loadWithPicasso(requireActivity(), Veh_img, Global.vehimageurl + img);

        getmanufactureveh();
        getvariant();
        getfuel();

        String num_plate_code = Global.commonVehClass.getNum_plate();
        String[] numberplate = {"White Board", "Yellow Board", "Green Board"};
        switch (num_plate_code.toUpperCase()) {
            case "G":
                NumberPlateTV.setText(numberplate[2]);
                break;
            case "W":
                NumberPlateTV.setText(numberplate[0]);
                break;
            case "Y":
                NumberPlateTV.setText(numberplate[1]);
                break;
            default:
                NumberPlateTV.setText("Unknown");
        }

        String list_type = Global.commonVehClass.getLis_type();
        String[] listing_type = {"Normal", "Silver", "Gold","Platinum"};
        switch (list_type.toUpperCase()) {
            case "N":
                LstngTV.setText(listing_type[0]);
                break;
            case "S":
                LstngTV.setText(listing_type[1]);
                break;
            case "G":
                LstngTV.setText(listing_type[2]);
                break;
            case "P":
                LstngTV.setText(listing_type[3]);
                break;
            default:
                LstngTV.setText("Unknown");
        }


        VariantTV.setText(vart_name);
        TransmissionTV.setText(transm_name);
        KmsdrivenET.setText(km);
        ManufacturedyearET.setText(man_year);
        OwnershipTV.setText(owntype);
        FuelTV.setText(fuel_type);
        SellingpriceET.setText(sell_price);
        VehregistrationET.setText(reg_no);
        InsurTV.setText(insc_type);



        Variant.setOnClickListener(v -> variantspopup());
        Update_veh.setOnClickListener(v -> Updatevehicledetails());
        Fuel.setOnClickListener(v -> fuelpopup());

        return  view;

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
                            displayText = "Single";
                            break;
                        case "Second":
                            ownershipValue = "D";
                            displayText = "Second";
                            break;
                        case "Third":
                            ownershipValue = "T";
                            displayText = "Third";
                            break;
                        case "Fourth":
                            ownershipValue = "F";
                            displayText = "Fourth";
                            break;
                        case "Multiple":
                            ownershipValue = "M";
                            displayText = "Multiple";
                            break;
                        default:
                            ownershipValue = "";
                            displayText = "";

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
                    getvehiclecolour();
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
                getvehiclecolour();
                getvariant();

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

                    item.set_name(e.getString("vcol_name"));
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

    private void Updatevehicledetails() {

       /* String vehvariant, kmsdriven, rto_code, manufacturedyear, vehfuel, vehmileage, sellingprice;


        vehvariant = VehvariantTV.getText().toString();
        kmsdriven = KmsdrivenET.getText().toString();
        manufacturedyear = Man_yearET.getText().toString();
        vehfuel = Vehiclefuel.getText().toString();
        sellingprice = Sell_priceET.getText().toString();


        if (vehvariant.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Variant field must not be empty!!");
            return;
        }

        if (vehfuel.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Fuel field must not be empty!!");
            return;
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

        if (rto_code.isEmpty()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "RTO field must not be empty!!");
            return;
        }

        if (rto_code.equals(" ")) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "RTO field must not be null !!!");
            return;
        }
        if (vehmileage.isEmpty() || vehmileage.equals(" ")) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Mileage field must not be empty!!");
            return;
        } else {
            int vehmileageValue = Integer.parseInt(vehmileage);
            if (vehmileageValue == 0) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "0 is not accepted!!");
                return;
            }
        }


        try {
            double price = Double.parseDouble(sellingprice);
            int priceIntegerPart = (int) price;

            if (priceIntegerPart <= 9999 || priceIntegerPart == 0) {
                Global.customtoast(requireActivity(), getLayoutInflater(), "Invalid selling price format !!");
                return;
            }

        } catch (NumberFormatException e) {
            // Handle the case where selling price is not a valid number
            Global.customtoast(requireActivity(), getLayoutInflater(), "Invalid selling price format !!");
            return;
        }
*/

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urlUpdateVehicleDetails,
                sresponse -> {
                    JSONObject response;
                    try {
                        response = new JSONObject(sresponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    try {
                        if (response.getBoolean("isSuccess")) {

                            Global.customtoast(requireActivity(), getLayoutInflater(),response.getString("message"));

                        } else {
                            if (response.has("error")) {
                                Toast.makeText(requireActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                            }
                        }
                    } catch (JSONException e) {
                        // Handle JSON parsing exception here
                        e.printStackTrace();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("vmodel_code",vmodel_code);
                params.put("vart_code", vart_code);
                params.put("transmission_type", transm_name);
                params.put("kms_driven", km);
                params.put("no_plate_type", num_plate);
                params.put("year_of_mfg", man_year);
                params.put("ownership_type", owntype);
                params.put("fuel_code", fuel_code);
                params.put("veh_sale_price", sell_price);
                params.put("vehmas_code", vehmas_code);
                params.put("city_code", city_code);
                params.put("state_code", state_code);
                params.put("vcol_code", colorcode);
              //  params.put("pricing_type", kmsdriven);
                params.put("price_range_flag", "1");
                params.put("reg_no", reg_no);
                params.put("insurance_type", insc_type);
                params.put("listing_type", list_type);
                params.put("expected_price_range", "0");

                System.out.println(params);
                return params;
            }

        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(stringRequest);

    }

    private void getvariant() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.urlgetvariant + "vmodel_code=" + vmodel_code;

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

                /*if (!Global.vehiclevariantlist.isEmpty()) {
                    Global.editor.putString("vart_code", Global.vehiclevariantlist.get(0).get_code());
                    Global.editor.putString("vart_name", Global.vehiclevariantlist.get(0).get_name());
                    variantcode = Global.vehiclevariantlist.get(0).get_code();
                    VariantTV.setText(Global.vehiclevariantlist.get(0).get_name());
                    getvehiclecolour();
                } else {
                    Global.editor.putString("vart_code", "0");
                    Global.editor.putString("vart_name", "0");
                }*/

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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
}



 /*

    private void getrto() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = Global.urlgetrto;

        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Global.vehiclertolist = new ArrayList<zList>();
                //Commonclass = new zList();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {

                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Commonclass = new zList();
                    try {

                        Commonclass.set_code(e.getString("rto_code"));
                        Commonclass.set_name(e.getString("rto_name").toUpperCase());


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }


                    Global.vehiclertolist.add(Commonclass);
                    // Global.customtoast(requireActivity(),getLayoutInflater(),"added manufacture name in global vehiclemanufacturelist ");

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                if (error instanceof TimeoutError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "Request Time-Out getstock");
//                } else if (error instanceof NoConnectionError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "No Connection Found getstock");
//                } else if (error instanceof ServerError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "Server Error getstock");
//                } else if (error instanceof NetworkError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "Network Error getstock");
//                } else if (error instanceof ParseError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "Parse Error getstock");
//                }

                Global.customtoast(requireActivity(), getLayoutInflater(), "Failed to get vehicle variant list.." + error.getMessage());


                // Toast.makeText(getActivity(), "Failed to get vehicle manufacture list.." + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }


        };
        queue.add(jsonArrayrequest);

    }
    public void rtopopup() {


        zDialog = new Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.vehiclertolist == null || Global.vehiclertolist.size() == 0) {
            Global.customtoast(Update_VehicleActivity.this, getLayoutInflater(), "RTO list not found !! Please try again !!");
            return;
        }
        final RTOAdapter laStates = new RTOAdapter(Global.vehiclertolist);
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

    private class RTOAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public RTOAdapter(ArrayList<zList> arrayList) {
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
                        mFilteredList = Global.vehiclertolist;
                    } else {
                        for (zList dataList : Global.vehiclertolist) {
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
                //Vehiclerto.setText(Commonclass.get_name());
                rtocode = Commonclass.get_code();
                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("rto_code", Commonclass.get_code());
                zDialog.dismiss();

            });
            return v;
        }
    }


    private void Transmissiondropdown() {
        String[] transmission = {"Auto", "Manual", "Not Applicable"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, transmission);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        TransmissionSp.setAdapter(adapter);

        switch (Global.selectedvstock.getStockvtransmission()) {
            case "AUTOMATIC":
                TransmissionSp.setSelection(0);
                break;
            case "MANUAL":
                TransmissionSp.setSelection(1);
                break;
            case "NOT APPLICABLE":
                TransmissionSp.setSelection(2);
                break;
            default:
                break;
        }

        TransmissionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
                    case "Automatic":
                        transmissionValue = "A";
                        break;
                    case "Manual":
                        transmissionValue = "M";
                        break;
                    case "Not Applicable":
                        transmissionValue = "N";
                        break;
                    default:
                        transmissionValue = ""; // Handle other cases if needed

                        break;
                }

                // Save the selected type to shared preferences
                *//*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Update_VehicleActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("transmission_Value", transmissionValue);
                editor.apply();*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected, if needed
            }
        });


    }
    private void Ownershipdropdown() {

        String[] Ownershiptype = {"Single", "Second", "Third", "Fourth", "Multiple"};

        ArrayAdapter<String> ownershipadapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, Ownershiptype);
        ownershipadapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        OwnershipSp.setAdapter(ownershipadapter);

        switch (Global.selectedvstock.getStockvownership()) {
            case "SINGLE":
                OwnershipSp.setSelection(0);
                break;
            case "SECOND":
                OwnershipSp.setSelection(1);
                break;
            case "THIRD":
                OwnershipSp.setSelection(2);
                break;
            case "FOURTH":
                OwnershipSp.setSelection(3);
                break;
            case "MULTIPLE":
                OwnershipSp.setSelection(4);
                break;
            default:
                break;
        }
        OwnershipSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
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
                        ownershipValue = ""; // Handle other cases if needed

                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected, if needed
            }
        });

    }
    private void Numberplatedropdown() {
        String[] numberplate = {"White Board", "Yellow Board", "Green Board"};

        ArrayAdapter<String> numberplateadapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, numberplate);
        numberplateadapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        NumberplateSp.setAdapter(numberplateadapter);

        switch (Global.selectedvstock.getStocknumberplate()) {
            case "W":
                NumberplateSp.setSelection(0);
                break;
            case "Y":
                NumberplateSp.setSelection(1);
                break;
            case "G":
                NumberplateSp.setSelection(2);
                break;
            default:
                break;
        }
        NumberplateSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
                    case "White Board":
                        numberplateValue = "W";
                        break;
                    case "Yellow Board":
                        numberplateValue = "Y";
                        break;
                    case "Green Board":
                        numberplateValue = "G";
                        break;
                    default:
                        numberplateValue = ""; // Handle other cases if needed

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected, if needed
            }
        });


    }

    private void getfuel() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = Global.urlgetfuel;
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Global.vehiclefuellist = new ArrayList<zList>();

                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {

                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Commonclass = new zList();
                    try {

                        Commonclass.set_name(e.getString("fuel_name").toUpperCase());
                        Commonclass.set_code(e.getString("fuel_code"));


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }


                    Global.vehiclefuellist.add(Commonclass);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                if (error instanceof TimeoutError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "Request Time-Out fuel");
//                } else if (error instanceof NoConnectionError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "No Connection Found fuel");
//                } else if (error instanceof ServerError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "Server Error fueo");
//                } else if (error instanceof NetworkError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "Network Error fuel");
//                } else if (error instanceof ParseError) {
//                    Global.customtoast(getContext(), getLayoutInflater(), "Parse Error fuel");
//                }

                Global.customtoast(requireActivity(), getLayoutInflater(), "Failed to get fuel list.." + error.getMessage());


                // Toast.makeText(getActivity(), "Failed to get vehicle manufacture list.." + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }


        };
        queue.add(jsonArrayrequest);

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
                Vehiclefuel.setText(Commonclass.get_name());
                fuelcode = Commonclass.get_code();

                //Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Update_VehicleActivity.this);
                //Global.editor = Global.sharedPreferences.edit();
                //Global.editor.putString("fuel_code", Commonclass.get_code());
                //Global.editor.commit();

                zDialog.dismiss();
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
                    VehvariantTV.setText(Global.vehiclevariantlist.get(0).get_name());
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
                VehvariantTV.setText(Commonclass.get_name());
                variantcode = Commonclass.get_code();

                zDialog.dismiss();


            });
            return v;
        }
    }*/







