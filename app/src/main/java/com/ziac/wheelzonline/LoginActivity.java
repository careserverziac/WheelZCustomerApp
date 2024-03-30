package com.ziac.wheelzonline;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import ModelClasses.AppStatus;
import ModelClasses.Global;

public class LoginActivity extends AppCompatActivity {

    TextView Signin,ForgotPasswordTXT,versionName;
    EditText Username,Password;
    AppCompatButton loginbtn;
    String username,pwd;
    private ProgressDialog progressDialog;

    private boolean passwordVisible = false;
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);


        Username = findViewById(R.id.loginusername);
        Password = findViewById(R.id.loginpassword);
        loginbtn = findViewById(R.id.loginbtn);
        Signin = findViewById(R.id.signin);
        ForgotPasswordTXT=findViewById(R.id.forgotpasswordtxt);
        versionName = findViewById(R.id.version);
        versionName.setText("Ver No:" + BuildConfig.VERSION_NAME);


        Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set importantForAutofill to auto if the EditText is clicked
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Username.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_AUTO);
                }
            }
        });

        Username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // Set importantForAutofill to no if the EditText loses focus
                if (!hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Username.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
                }
            }
        });


        loginbtn.setOnClickListener(v -> {


            username = Username.getText().toString();
            pwd = Password.getText().toString();
            if (username.isEmpty()) {
                Username.setError("Please enter the User name");
                Username.requestFocus();
                return;
            } else if (pwd.isEmpty()) {
                Password.setError("Please enter the Password");
                Password.requestFocus();
                return;
            }

            if (AppStatus.getInstance(this).isOnline()) {
                dologin();
            } else {
                Global.customtoast(LoginActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
            }

        });


        Signin.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,SignupActivity.class)));
        ForgotPasswordTXT.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class)));
        Password.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Password.getRight() - Password.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Password.getSelectionEnd();
                    if (passwordVisible) {
                        Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0);
                        Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_eyes, 0);
                        Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    Password.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

    }




    private void dologin() {
        progressDialog.show();

        String url = Global.tokenurl;
        RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject respObj = new JSONObject(response);
                //String issuccess = respObj.getString("isSuccess");
                //String error = respObj.getString("error");

                String access_token = respObj.getString("access_token");
                String refresh_token = respObj.getString("refresh_token");

                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("access_token", access_token);
                Global.editor.putString("refresh_token", refresh_token);
                Global.editor.commit();

                getuserprofile();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                   // Global.customtoast(LoginActivity.this, getLayoutInflater(),"Request Time-Out");
                }  else if (error instanceof NoConnectionError) {
                   // Global.customtoast(LoginActivity.this, getLayoutInflater(),"No Connection Found");
                }

                else if (error instanceof ServerError) {
                    String errorResponse = new String(error.networkResponse.data);

                    try {
                        JSONObject errorJson = new JSONObject(errorResponse);
                        String errorCode = errorJson.optString("error", "");
                        String errorDescription = errorJson.optString("error_description", "");
                       /* if ("invalid_grant".equals(errorCode)) {*/
                            Global.customtoast(LoginActivity.this, getLayoutInflater(), errorDescription);
                        /*} else {
                            Global.customtoast(LoginActivity.this, getLayoutInflater(), errorDescription);
                        }*/
                    } catch (JSONException e) {
                        Global.customtoast(LoginActivity.this, getLayoutInflater(), "An error occurred. Please try again later.");
                    }
                }
                else if (error instanceof ParseError) {
                    Global.customtoast(LoginActivity.this, getLayoutInflater(),"Parse Error ");
                }  else if (error instanceof AuthFailureError) {
                    Global.customtoast(LoginActivity.this, getLayoutInflater(), "AuthFailureError");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", pwd);
                params.put("grant_type", "password");
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


    private void getuserprofile() {

        String url = Global.getuserprofiledetails;
        RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
        //progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

            try {
                JSONObject respObj1 = new JSONObject(response);
                JSONObject respObj = new JSONObject(respObj1.getString("data"));

                String userName = respObj.getString("userName");
                String key_person = respObj.getString("key_person");
                String Code = respObj.getString("Code");
                String Email = respObj.getString("Email");
                String Image = respObj.getString("Image");
                String Mobile1 = respObj.getString("Mobile1");
                String Mobile2 = respObj.getString("Mobile2");
                String Approved = respObj.getString("Approved");
                String Ref_Code = respObj.getString("Ref_Code");
                String Active = respObj.getString("Active");
                String Type = respObj.getString("Type");


                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("userName", userName);
                Global.editor.putString("key_person", key_person);
                Global.editor.putString("Code", Code);
                Global.editor.putString("Email", Email);
                Global.editor.putString("Image", Image);
                Global.editor.putString("Mobile1", Mobile1);
                Global.editor.putString("Mobile2", Mobile2);
                Global.editor.putString("Approved", Approved);
                Global.editor.putString("Ref_Code", Ref_Code);
                Global.editor.putString("Active", Active);
                Global.editor.putString("Type", Type);
                Global.editor.commit();


                startActivity(new Intent(LoginActivity.this, MainActivty.class));
                progressDialog.dismiss();
                finish();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError) {
                    Toast.makeText(LoginActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(LoginActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(LoginActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("username", username);
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



}




/*

          COLOUR GRADIENT FOR THE TEXTVIEW

        Shader textShader = new LinearGradient(100, 100, 500, 100, new int[]{Color.RED, Color.BLUE, Color.BLUE},
                new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
        Textlogo.getPaint().setShader(textShader);
*/

      /*  Shader shader = new LinearGradient(100, 100, 500, 100, new int[]{Color.RED, Color.BLUE, Color.RED},
                new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
        Signin.getPaint().setShader(shader);
*/
       /* Shader shade = new LinearGradient(100, 100, 1200, 700,
                new int[]{Color.BLUE, Color.RED},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        Login.getPaint().setShader(shade);

*/
