package Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.wheelzcustomer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ModelClasses.Global;


public class RegisterVehicleFragment extends Fragment {

    String regno,engineno,chassisno;
    EditText Registrationno,Engineno,Chassisno;
    Button Addvehicle;
    FragmentManager fragmentManager;
    LinearLayout RegLinear,Enginelinear,Chassislinear;
    Drawable backgroundgrey;
    ImageView Backbtn;


    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_register_vehicle, container, false);


        Registrationno=view.findViewById(R.id.regno);
        Engineno=view.findViewById(R.id.engineno);
        Chassisno=view.findViewById(R.id.chassisno);
        Addvehicle=view.findViewById(R.id.addvehicle);
        RegLinear=view.findViewById(R.id.reglnr);
        Enginelinear=view.findViewById(R.id.englnr);
        Chassislinear=view.findViewById(R.id.chasislnr);
        progressBar=view.findViewById(R.id.progressBar);
      //  Backbtn = view.findViewById(R.id.backbtn);
        hideLoading();
        backgroundgrey = ContextCompat.getDrawable(getActivity(), R.drawable.border_colour2);


        Registrationno.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String upperText = s.toString().toUpperCase();
                if (!s.toString().equals(upperText)) {
                    Registrationno.setText(upperText);
                    Registrationno.setSelection(upperText.length()); // move cursor to end
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        Registrationno.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Addvehicle.setText("Proceed");
            } else {
            }
        });

        Engineno.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Addvehicle.setText("Proceed");
            } else {
            }
        });

        Chassisno.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Addvehicle.setText("Proceed");
            } else {

            }
        });


        Addvehicle.setOnClickListener(v -> Updateprofiledetails());
       /* Backbtn.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });*/

        return view;
    }

    private void Updateprofiledetails() {
        showLoading();
        regno = Registrationno.getText().toString();
        engineno = Engineno.getText().toString();
        chassisno = Chassisno.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urladdvehicle,
                sresponse -> {


                    JSONObject response = null;
                    try {
                        response = new JSONObject(sresponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    try {
                        if (response.getBoolean("isSuccess")) {
                            hideLoading();
                            Toast.makeText(requireContext(), response.getString("error"), Toast.LENGTH_SHORT).show();
                            MyVehcileFragment myVehcileFragment = new MyVehcileFragment();
                            fragmentManager = requireActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        } else {
                            hideLoading();
                            Toast.makeText(requireContext(), response.getString("error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        hideLoading();
                        throw new RuntimeException(e);
                    }

                }, error -> {

                    hideLoading();
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
                params.put("reg_no",regno);
                params.put("engine_no",engineno);
                params.put("chasis_no",chassisno);
                params.put("wuser_code",  Global.sharedPreferences.getString("wuser_code",""));

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);


    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


}