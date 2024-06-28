package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


public class VehicleRegistrationFragment extends Fragment {

    String regno,engineno,chassisno;
    EditText Registrationno,Engineno,Chassisno;
    AppCompatButton Addvehicle;
    FragmentManager fragmentManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_addvehicle, container, false);


        Registrationno=view.findViewById(R.id.regno);
        Engineno=view.findViewById(R.id.engineno);
        Chassisno=view.findViewById(R.id.chassisno);
        Addvehicle=view.findViewById(R.id.addvehicle);

        Addvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Updateprofiledetails();
            }
        });

        return view;
    }

  /*  private void Updateprofiledetails() {
        Toast.makeText(requireContext(),"Vehicle Registration Successful", Toast.LENGTH_SHORT).show();

        MyVehcileFragment myVehcileFragment = new MyVehcileFragment();
        fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }*/

    private void Updateprofiledetails() {

        regno = Registrationno.getText().toString();
        engineno = Engineno.getText().toString();
        chassisno = Chassisno.getText().toString();


    /*    if (regno.isEmpty()) {

            Toast.makeText(requireContext(), "Person name should not be empty!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (engineno.isEmpty()) {
            Toast.makeText(requireContext(), "Mobile number should not be empty !!", Toast.LENGTH_SHORT).show();

            return;
        }
        if (chassisno.length() < 10) {
            Toast.makeText(requireContext(), "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();

            return;
        }if (chassisno.length() < 10) {
            Toast.makeText(requireContext(), "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();

            return;
        }
*/


        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urladdvehicle,
                sresponse -> {

                    // progressBar.setVisibility(View.GONE);
                    JSONObject response = null;
                    try {
                        response = new JSONObject(sresponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

/*

                    Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("key_person", Registrationno.getText().toString());
                    Global.editor.putString("Mobile1", Engineno.getText().toString());
                    Global.editor.putString("Email", Chassisno.getText().toString());
                    Global.editor.putString("Email", Chassisno.getText().toString());
                    Global.editor.commit();
*/



                    try {
                        if (response.getBoolean("isSuccess")) {
//                                Toast.makeText(ProfileActivity.this, "Updated successfully !!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(requireContext(), response.getString("error"), Toast.LENGTH_SHORT).show();


                            MyVehcileFragment myVehcileFragment = new MyVehcileFragment();
                            fragmentManager = requireActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                            // Global.customtoast(requireContext(),getLayoutInflater(),"Updated successfully !!");

                       /*     Intent intent = new Intent(requireContext(), ProfileActivity.class);
                            startActivity(intent);
*/
                        } else {
                            //textViewError.setText(response.getString("error"));
                            Toast.makeText(requireContext(), response.getString("error"), Toast.LENGTH_SHORT).show();
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


}