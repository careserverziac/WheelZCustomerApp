package com.ziac.wheelzcustomer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import ModelClasses.AppStatus;
import ModelClasses.Global;

public class ForgotPasswordActivity extends AppCompatActivity {

    AppCompatButton GetOTp;
    EditText Username,Mobileno,Email;
    String username,mobile,email,fptype;
    ProgressBar progressBar;
    LinearLayout ForgetLinearLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password);


        if (AppStatus.getInstance(this).isOnline()) {
        } else {
            Global.customtoast(ForgotPasswordActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }


        requestSMSPermission();

        GetOTp =findViewById(R.id.getotp);
        Username=findViewById(R.id.fusername);
        Mobileno=findViewById(R.id.fmobile);
        Email=findViewById(R.id.femail);
        progressBar=findViewById(R.id.progressbarfp);
        ForgetLinearLayout=findViewById(R.id.forgetlinearlayout);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        username = Global.sharedPreferences.getString("username", "");
        Username.setText(username);

        hideLoading();

     /*   int newWidthInPixels = 100;
        int newHeightInPixels = 100;
        ViewGroup.LayoutParams params = progressBar.getLayoutParams();
        params.width = newWidthInPixels;
        params.height = newHeightInPixels;
        progressBar.setLayoutParams(params);*/


        GetOTp.setOnClickListener(v -> {
            username = Username.getText().toString();
            mobile = Mobileno.getText().toString();
            email = Email.getText().toString();

            if (!username.isEmpty()) {
                fptype = "U";
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("fptype", fptype);
                Global.editor.putString("usernamefp", username);
                Global.editor.commit();

            }
            if (!mobile.isEmpty()) {
                fptype = "M";
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("fptype", fptype);
                Global.editor.putString("mobilefp", mobile);
                Global.editor.commit();

            } if (!email.isEmpty()) {
                fptype = "E";
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("fptype", fptype);
                Global.editor.putString("emailfp", email);
                Global.editor.commit();

            }

            Global.editor = Global.sharedPreferences.edit();
            Global.editor.putString("otpusername", username);
            Global.editor.putString("otpmobile",mobile);
            Global.editor.commit();

            getotpmethod();


        });
    }
    private void  requestSMSPermission()
    {
        String permission = Manifest.permission.RECEIVE_SMS;

        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
            permission_list[0] = permission;

            ActivityCompat.requestPermissions(this, permission_list,1);
        }
    }

   /* private void requestSMSPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;

        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;

            ActivityCompat.requestPermissions(this, permission_list, 1);
        } else {
            getotpmethod();
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getotpmethod();
            } else {
                Toast.makeText(this, "Permission denied cant proceed further", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void getotpmethod() {
        showLoading();
        String url = Global.forgotpasswordurl;
        RequestQueue queue= Volley.newRequestQueue(ForgotPasswordActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

            try {
                JSONObject respObj = new JSONObject(response);
                String issuccess = respObj.getString("isSuccess");
                String error = respObj.getString("error");

                if (issuccess.equals("true")) {

                    Global.customtoast(ForgotPasswordActivity.this, getLayoutInflater(), error);
                    startActivity(new Intent(ForgotPasswordActivity.this, CreateNewPasswordActivity.class));
                    finish();
                    hideLoading();
                } else {
                    Global.customtoast(ForgotPasswordActivity.this, getLayoutInflater(), error);
                    hideLoading();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            hideLoading();
            if (error instanceof TimeoutError) {
                Toast.makeText(ForgotPasswordActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
            } else if (error instanceof NoConnectionError) {
                Toast.makeText(ForgotPasswordActivity.this, "Internet connection unavailable", Toast.LENGTH_LONG).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(ForgotPasswordActivity.this, "Server Error", Toast.LENGTH_LONG).show();
            } else if (error instanceof NetworkError) {
                Toast.makeText(ForgotPasswordActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            } else if (error instanceof ParseError) {
                Toast.makeText(ForgotPasswordActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("UserName", username);
                params.put("Mobile", mobile);
                params.put("Email", email);
                params.put("FPType", fptype);

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

    @SuppressLint("ResourceAsColor")
    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}