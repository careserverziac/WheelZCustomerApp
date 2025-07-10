package com.ziac.wheelzcustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import java.util.concurrent.TimeUnit;
import ModelClasses.Global;

public class ChangePasswordActivity extends AppCompatActivity {


    ImageView Backbtn;
    EditText Newpassword,Cpassword;
    LinearLayout Updatepassword;
    private boolean passwordVisible = false;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Newpassword=findViewById(R.id.newpassword);
        Cpassword=findViewById(R.id.cpassword);
        Updatepassword=findViewById(R.id.updatebtn);
        Backbtn=findViewById(R.id.backbtn);


        Updatepassword.setOnClickListener(view -> ChangePasswordMethod());


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
        Cpassword.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Cpassword.getRight() - Cpassword.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Cpassword.getSelectionEnd();
                    if (passwordVisible) {
                        Cpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0);
                        Cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        Cpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_eyes, 0);
                        Cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    Cpassword.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        Backbtn.setOnClickListener(v -> {
            finish();
        });


    }



    private void ChangePasswordMethod() {

        String password,confirmpasword;
        password =Newpassword.getText().toString();
        confirmpasword = Cpassword.getText().toString();


        if (password.isEmpty()) {
            Newpassword.setError("Password field is missing !!");
            Newpassword.requestFocus();
            return;
        }
        if (password.contains(" ")) {
            Newpassword.setError("Password cannot contain spaces !!");
            Newpassword.requestFocus();
            return;
        }
        if (confirmpasword.isEmpty()) {
            Cpassword.setError("Confirm pasword field is missing !!");
            Cpassword.requestFocus();
            return;
        }
        if (confirmpasword.contains(" ")) {
            Cpassword.setError("Confirm password cannot contain spaces !!");
            Cpassword.requestFocus();
            return;
        }if (!isValidPassword(password)) {
            Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Password must contain one upper case, one lower case, one number, and one special character !!");
            return;
        }

        if (password.length() < 6) {
            Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Password must contain one upper case,one lower case,one number and one special character !!");
            return;
        }


        if (!password.equals(confirmpasword)) {
            Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "The given password and confirm password does not match !!");
            return;

        }

        RequestQueue requestQueue= Volley.newRequestQueue(ChangePasswordActivity.this);

        String url= Global.changepasswordurl;
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url, sresponse -> {

            try {

                JSONObject response;
                try {
                    response = new JSONObject(sresponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Log.d("Register", sresponse);
//

                try {
                    if (response.getBoolean("isSuccess")) {
                        Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(),response.getString("error"));
                        finish();
                    } else {
                        Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(),response.getString("error"));

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, error -> {
            if (error instanceof TimeoutError) {
                Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Request Time-Out");
            } else if (error instanceof NoConnectionError) {
                Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Internet connection unavailable");
            } else if (error instanceof ServerError) {
                Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Server Error");
            } else if (error instanceof NetworkError) {
                Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Network Error");
            } else if (error instanceof ParseError) {
                Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Parse Error");
            }

        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("UserName",Global.sharedPreferences.getString("userName",""));
                params.put("NewPassword",password);
                Log.d("params",params.toString());
                return params;


            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                  requestQueue.add(stringRequest);



    }

    private boolean isValidPassword(String password) {

        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        String numbers = "(.*[0-9].*)";
        String specialChars = "(.*[@#$%^&+=!].*)";

        return password.matches(upperCaseChars) &&
                password.matches(lowerCaseChars) &&
                password.matches(numbers) &&
                password.matches(specialChars);
    }
}