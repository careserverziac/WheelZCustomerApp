package com.ziac.wheelzonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

import ModelClasses.AppStatus;
import ModelClasses.Global;

public class ForgotPasswordActivity extends AppCompatActivity {

    AppCompatButton SendOTp;
    EditText Username,Mobileno;
   // FloatingActionButton Forgotbackbtn;
    String username,mobile;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password);

        if (AppStatus.getInstance(this).isOnline()) {
            //Toast.makeText(this,"You are online!!!!", Toast.LENGTH_SHORT).show();
        } else {
            Global.customtoast(ForgotPasswordActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }

        SendOTp=findViewById(R.id.sendotp);
       // Forgotbackbtn=findViewById(R.id.forgotbackbtn);
        Username=findViewById(R.id.fusername);
        Mobileno=findViewById(R.id.fmobile);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        username = Global.sharedPreferences.getString("username", "");
        Username.setText(username);
        /*Forgotbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });*/
        SendOTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = Username.getText().toString();
                mobile = Mobileno.getText().toString();
                if (username.isEmpty()) {
                    Username.setError("Please enter username");
                    Mobileno.requestFocus();
                    return;
                } else if (mobile.isEmpty()) {
                    Mobileno.setError("Please enter mobile number");
                    Mobileno.requestFocus();
                    return;
                } else if(mobile.length() < 10 ){
                    Global.customtoast(ForgotPasswordActivity.this, getLayoutInflater(), "Mobile number should not be less than 10 digits !!");
                    return;}

                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("otpusername", username);
                Global.editor.putString("otpmobile",mobile);
                Global.editor.commit();
                getotpmethod();
            }
        });
    }
    private void getotpmethod() {
        String url = Global.forgotpasswordurl;
        RequestQueue queue= Volley.newRequestQueue(ForgotPasswordActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    if(issuccess.equals("true")){
                        Global.customtoast(ForgotPasswordActivity.this, getLayoutInflater(), error);
                        /*Global.customtoast(ForgotPasswordActivity.this, getLayoutInflater(), "OTP is send to your registered mobile number");*/
                        startActivity(new Intent(ForgotPasswordActivity.this,CreateNewPasswordActivity.class));
                    } else {
                        Global.customtoast(ForgotPasswordActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(ForgotPasswordActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ForgotPasswordActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ForgotPasswordActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ForgotPasswordActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(ForgotPasswordActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
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
                params.put("UserName", username);
                params.put("Mobile", mobile);
                Log.d("getHeaders", params.toString());
                return params;
            }
        };
        queue.add(request);
    }
}