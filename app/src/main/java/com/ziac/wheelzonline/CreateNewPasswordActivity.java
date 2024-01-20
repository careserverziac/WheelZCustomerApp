package com.ziac.wheelzonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ModelClasses.Global;

public class CreateNewPasswordActivity extends AppCompatActivity {

    FloatingActionButton NPbackbtn;
    EditText OTP,Newpassword,Mobile;
    TextView UsernameTxt,MobileTxt,Resendotp;
    AppCompatButton SubmitOTP;
    String username,mobile,otp,password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        NPbackbtn=findViewById(R.id.CPbackbtn);

       // OTP=findViewById(R.id.otp);
        Newpassword=findViewById(R.id.newpassword);
        UsernameTxt=findViewById(R.id.usernametxt);
        MobileTxt=findViewById(R.id.mobiletxt);
        Resendotp=findViewById(R.id.resendotp);
        SubmitOTP=findViewById(R.id.submitotp);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        username = Global.sharedPreferences.getString("username", "");
        mobile = Global.sharedPreferences.getString("otpmobile", "");
        UsernameTxt.setText(username);
        MobileTxt.setText(mobile);


        NPbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateNewPasswordActivity.this,ForgotPasswordActivity.class));
            }
        });

        SubmitOTP.setOnClickListener(v ->   submitotpUsingVolley());
        Resendotp.setOnClickListener(v ->   getotpmethod());
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
        queue.add(request);
    }
    private void submitotpUsingVolley() {

        String usrnme,usermobile,otp,password;
       // usrnme = OtpUsrName.getText().toString();
        usermobile = Mobile.getText().toString();
       // otp = OTP.getText().toString();
        password = Newpassword.getText().toString();

     /*   if (otp.isEmpty()) {
            OTP.setError("OTP field is missing !!");
            OTP.requestFocus();
            return;

        }*/
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



                    if(issuccess.equals("true")){
//                        Global.customtoast(OTPActivity.this, getLayoutInflater(),issuccess);
                        Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),"Password Reset Successfully");
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
                params.put("UserName", Global.sharedPreferences.getString("otpusername",""));
                params.put("Mobile", Global.sharedPreferences.getString("otpmobile",""));
               // params.put("otp", otp);
                params.put("NewPassword",password);

                Log.d("getParams", params.toString());
                return params;
            }
        };

        queue.add(request);
    }
}