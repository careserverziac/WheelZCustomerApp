package com.ziac.wheelzcustomer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import ModelClasses.AppStatus;
import ModelClasses.Global;


public class CreateNewPasswordActivity extends AppCompatActivity {

    EditText Newpassword;
    ProgressBar progressBar;
    AppCompatButton SubmitOTP,Cancelpage;
    PinView pinView;
    private boolean passwordVisible = false;
    String message;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        message=Global.sharedPreferences.getString("message","");
        pinView = findViewById(R.id.pinview);
        pinView.setText(message);
        Global.editor = Global.sharedPreferences.edit();
        Global.editor.remove("message");
        Global.editor.commit();


        if (AppStatus.getInstance(this).isOnline()) {
        } else {
            Global.customtoast(CreateNewPasswordActivity.this,getLayoutInflater(),"Internet connection unavailable !!");
        }


        Newpassword=findViewById(R.id.resetpassword);
        progressBar=findViewById(R.id.progressbarcnp);
        SubmitOTP=findViewById(R.id.submitotp);
        Cancelpage=findViewById(R.id.cancelpage);
        progressBar.setVisibility(View.GONE);


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

                    if(issuccess.equals("true")){
                        Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),error);
                        startActivity(new Intent(CreateNewPasswordActivity.this,LoginActivity.class));
                        finish();
                    }  if(issuccess.equals("false")){
                        Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),error);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateNewPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreateNewPasswordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError) {
                    Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),"Request Time-Out");
                }  else if (error instanceof NoConnectionError) {
                    Global.customtoast(CreateNewPasswordActivity.this, getLayoutInflater(),"Internet connection unavailable");
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
        super.onBackPressed();
        finish();
    }

}

