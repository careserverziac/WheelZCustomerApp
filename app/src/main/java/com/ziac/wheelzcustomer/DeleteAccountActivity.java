package com.ziac.wheelzcustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import ModelClasses.Global;

public class DeleteAccountActivity extends AppCompatActivity {


    public  static  final  int REQ_USER_CONSENT=100;
    final Context context = this;
    AppCompatButton Proceedbtn, ValidateandDelete;
    String OTP,otp,autoOTP;
    PinView pinView;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        Proceedbtn = findViewById(R.id.proceedbtn);
        ValidateandDelete = findViewById(R.id.validatedelete);
        progressBar = findViewById(R.id.progressbarline);



        hideLoading();
        pinView = findViewById(R.id.pinviewdelete);
       // startSmartUserConsent();

        Proceedbtn.setOnClickListener(v -> getotpmethod());
        ValidateandDelete.setOnClickListener(v -> validateOTPmethod());
    }
    private void validateOTPmethod() {

        otp=pinView.getText().toString();

        if (otp.isEmpty()) {
            Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), "OTP field is missing !!");
            pinView.requestFocus();
            return;
        }
        if (otp.length() != 6) {
            Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), "Enter a valid 6-digit OTP !!");
            pinView.requestFocus();
            return;
        }

        deleteaccountmethod();
    }
    private void deleteaccountmethod() {
        showLoading();
        String url = Global.velidateanddeleteaccounturl;
        OTP=pinView.getText().toString();
        url=url+"OTP="+OTP;
        RequestQueue queue= Volley.newRequestQueue(DeleteAccountActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    if(issuccess.equals("true")){
                        hideLoading();;
                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);

                        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DeleteAccountActivity.this);
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.remove("access_token");
                        Global.editor.remove("refresh_token");
                        Global.editor.commit();
                        startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));
                        finish();


                    } else {
                        hideLoading();;
                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                if (error instanceof TimeoutError) {
                    Toast.makeText(DeleteAccountActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(DeleteAccountActivity.this, "Internet connection unavailable", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(DeleteAccountActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(DeleteAccountActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(DeleteAccountActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }
          /*  @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", Global.sharedPreferences.getString("userName",""));
                params.put("Mobile", Global.sharedPreferences.getString("Mobile1",""));
                Log.d("getHeaders", params.toString());
                return params;
            }*/
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

    private void getotpmethod() {
        showLoading();;

        String url = Global.otpfordeleteaccounturl;
        RequestQueue queue= Volley.newRequestQueue(DeleteAccountActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    if(issuccess.equals("true")){

                        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        autoOTP=Global.sharedPreferences.getString("message","");


                        if (autoOTP.isEmpty()) {
                            showLoading();
                           //Toast.makeText(context, "auto OTP has value", Toast.LENGTH_SHORT).show();

                        } else {
                            //Toast.makeText(context, "auto OTP has no value is empty", Toast.LENGTH_SHORT).show();
                            Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            autoOTP=Global.sharedPreferences.getString("message","");
                            pinView.setText(autoOTP);


                            if (!TextUtils.isEmpty(pinView.getText().toString())) {
                                Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                                Proceedbtn.setVisibility(View.GONE);
                                pinView.setVisibility(View.VISIBLE);
                                ValidateandDelete.setVisibility(View.VISIBLE);
                                hideLoading();

                            }

                        }


                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                        Proceedbtn.setVisibility(View.GONE);
                        pinView.setVisibility(View.VISIBLE);
                        ValidateandDelete.setVisibility(View.VISIBLE);
                        hideLoading();;
                    } else {
                        hideLoading();;
                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();;
                if (error instanceof TimeoutError) {
                    Toast.makeText(DeleteAccountActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(DeleteAccountActivity.this, "Internet connection unavailable", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(DeleteAccountActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(DeleteAccountActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(DeleteAccountActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", Global.sharedPreferences.getString("userName",""));
                params.put("Mobile", Global.sharedPreferences.getString("Mobile1",""));
                // params.put("FPType", Global.sharedPreferences.getString("Mobile1",""));
                params.put("FPType", "M");
                Log.d("getHeaders", params.toString());
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



    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);

    }

}