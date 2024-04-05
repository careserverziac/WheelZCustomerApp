package com.ziac.wheelzonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.android.volley.AuthFailureError;
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
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ModelClasses.AppStatus;
import ModelClasses.Global;
import ModelClasses.SMSBrodcastReciever;

public class CreateNewPasswordActivity extends AppCompatActivity {

    public  static  final  int REQ_USER_CONSENT=100;
    SMSBrodcastReciever smsBroadcastReceiver;
    EditText Newpassword;
    ProgressBar progressBar;
    AppCompatButton SubmitOTP,Cancelpage;
    String username,mobile,email;

    PinView pinView;

    private boolean passwordVisible = false;
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);


        if (AppStatus.getInstance(this).isOnline()) {
        } else {
            Global.customtoast(CreateNewPasswordActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }

        Newpassword=findViewById(R.id.newpassword);
        progressBar=findViewById(R.id.progressbarcnp);
        SubmitOTP=findViewById(R.id.submitotp);
        Cancelpage=findViewById(R.id.cancelpage);
        pinView=findViewById(R.id.pinview);
        hideLoading();
        startSmartUserConsent();

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        SubmitOTP.setOnClickListener(v ->   NewPasswordandSubmit());
        Cancelpage.setOnClickListener(v -> {startActivity(new Intent(CreateNewPasswordActivity.this,LoginActivity.class));finish();});


        Newpassword.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Newpassword.getRight() - Newpassword.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Newpassword.getSelectionEnd();
                    if (passwordVisible) {
                        Newpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0);
                        Newpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        Newpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_eyes, 0);
                        Newpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    Newpassword.setSelection(selection);
                    return true;
                }
            }
            return false;
        });
    }

    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT){

            if ((resultCode == RESULT_OK) && (data != null)){

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);


            }


        }

    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()){

            pinView.setText(matcher.group(0));

        }


    }

    private void registerBroadcastReceiver(){

        smsBroadcastReceiver = new SMSBrodcastReciever();

        smsBroadcastReceiver.smsBroadcastReceiverListener = new SMSBrodcastReciever.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent,REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver,intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }



    private void NewPasswordandSubmit() {

        String otp,password;
        otp=pinView.getText().toString();
        password = Newpassword.getText().toString();

        if (otp.isEmpty()) {
            Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(), "OTP field is missing !!");
            pinView.requestFocus();
            return;
        }
        if (otp.length() != 6) {
            Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(), "Enter a valid 6-digit OTP !!");
            pinView.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Newpassword.setError("Password field is missing !!");
            Newpassword.requestFocus();
            return;


        }  /* if (!isValidPassword(password)) {
            Global.customtoast(OTPActivity.this, getLayoutInflater(), "Password must contain one upper case, one lower case, one number, and one special character !!");
            return;
        }

        if (password.length() < 6) {
            Global.customtoast(OTPActivity.this, getLayoutInflater(), "Password must contain one upper case,one lower case,one number and one special character !!");
            return;
        }*/

        String url = Global.validateotpurl;

        RequestQueue queue= Volley.newRequestQueue(CreateNewPasswordActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);

                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                  //  System.out.println(respObj);


                    if(issuccess.equals("true")){
                        Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),error);
                       // Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),"Password Reset Successfully");
                        startActivity(new Intent(CreateNewPasswordActivity.this,LoginActivity.class));
                        finish();
                    }  if(issuccess.equals("false")){
                        Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),error);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateNewPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //  progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateNewPasswordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError) {
                    Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),"Request Time-Out");
                }  else if (error instanceof NoConnectionError) {
                    Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),"No Connection Found");
                }else if (error instanceof ServerError) {
                    Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),"ServerError");
                }  else if (error instanceof ParseError) {
                    Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),"Parse Error ");
                }  else if (error instanceof AuthFailureError) {
                    Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(), "AuthFailureError");
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName",Global.sharedPreferences.getString("usernamefp",""));
                params.put("Mobile",Global.sharedPreferences.getString("mobilefp",""));
                params.put("Email",Global.sharedPreferences.getString("emailfp",""));
                params.put("otp", otp);
                params.put("NewPassword",password);
                params.put("FPType",Global.sharedPreferences.getString("fptype",""));

               // Log.d("getParams", params.toString());
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

    private void getotpmethod() {
        String url = Global.forgotpasswordurl;
        RequestQueue queue= Volley.newRequestQueue(CreateNewPasswordActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");


                    if(issuccess.equals("true")){
                        startSmartUserConsent();
                        Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(), error);
                    } else {
                        Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(CreateNewPasswordActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(CreateNewPasswordActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(CreateNewPasswordActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(CreateNewPasswordActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(CreateNewPasswordActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", username);
                params.put("Mobile", mobile);
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