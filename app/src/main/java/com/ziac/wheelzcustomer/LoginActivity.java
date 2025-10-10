package com.ziac.wheelzcustomer;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import ModelClasses.AppStatus;
import ModelClasses.Global;
import ModelClasses.VolleyRequestHelper;

public class LoginActivity extends AppCompatActivity {

    TextView Signin,ForgotPasswordTXT,versionName;
    EditText Username,Password;
    Button loginbtn;
    private ProgressDialog progressDialog;
    String username, password, access_token,refresh_token;

    private boolean passwordVisible = false;

    Context context;
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        context = this;

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Username.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_AUTO);
                }
            }
        });

        Username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Username.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
                }
            }
        });


        loginbtn.setOnClickListener(v -> {
            username = Username.getText().toString();
            password = Password.getText().toString();

            if (username.isEmpty()) {
                Username.setError("Please enter the user name");
                Username.requestFocus();
                return;
            } else if (password.isEmpty()) {
                Password.setError("Please enter the password");
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
      if (!AppStatus.getInstance(this).isOnline()) {
          Global.customtoast(LoginActivity.this, getLayoutInflater(), "Network unavailable !!");
          return;
      }
      progressDialog.show();
      VolleyRequestHelper volleyHelper = new VolleyRequestHelper(LoginActivity.this);
      String url = Global.tokenurl;
      Map<String, String> params = new HashMap<>();

      if(access_token == null || access_token.isEmpty() || refresh_token == null || refresh_token.isEmpty()){
          params.put("username", username);
          params.put("password", password);
          params.put("grant_type", "password");
          access_token = null;
      }
      else{
          params.put("refresh_token", refresh_token);
          params.put("grant_type", "refresh_token");
      }

      volleyHelper.makePostRequest(url, params, access_token, new VolleyRequestHelper.VolleyCallback() {
          @Override
          public void onSuccess(JSONObject response) {
              try {
                  access_token = response.getString("access_token");
                  String refresh_token = response.getString("refresh_token");
                  username = response.getString("userName");

                  Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                  Global.editor = Global.sharedPreferences.edit();
                  Global.editor.putString("access_token", access_token);
                  Global.editor.putString("refresh_token", refresh_token);
                  Global.editor.putString("username", username);
                  Global.editor.commit();

                  getuserprofile();

              } catch (JSONException e) {
                  progressDialog.dismiss();
                  Global.customtoast(LoginActivity.this, getLayoutInflater(), "Login failed: Invalid response");
                  e.printStackTrace();

              }
          }

          @Override
          public void onError(String error) {
              progressDialog.dismiss();
              // Handle specific error cases
              if (error != null) {
                  if (error.contains("The user name or password is incorrect")) {
                      Global.customtoast(LoginActivity.this, getLayoutInflater(), "Invalid username or password");
                  } else if (error.contains("invalid_grant")) {
                      Global.customtoast(LoginActivity.this, getLayoutInflater(), "Login failed: Invalid credentials");
                  } else if (error.contains("Network is unavailable")) {
                      Global.customtoast(LoginActivity.this, getLayoutInflater(), "Network unavailable");
                  } else {
                      Global.customtoast(LoginActivity.this, getLayoutInflater(), "Login failed: " + error);
                  }
              } else {
                  Global.customtoast(LoginActivity.this, getLayoutInflater(), "Login failed: Unknown error");
              }

              // Clear tokens on auth failure to force fresh login
              if (error != null && (error.contains("invalid_grant") || error.contains("user name or password is incorrect"))) {
                  clearStoredTokens();
              }

          }
      });
  }


    private void getuserprofile() {
        VolleyRequestHelper volleyHelper = new VolleyRequestHelper(LoginActivity.this);
        String url = Global.getuserprofiledetails;
        Map<String, String> params = new HashMap<>();

        volleyHelper.makePostRequest(url, params, access_token, new VolleyRequestHelper.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    boolean success = response.getBoolean("isSuccess");

                    if (success) {
                        // Get the data object correctly
                        JSONObject respObj = response.getJSONObject("data");

                        String com_code = respObj.getString("com_code");
                        String userName = respObj.getString("userName");
                        String key_person = respObj.getString("key_person");
                        String Email = respObj.getString("Email");
                        String Image = respObj.getString("Image");
                        String Mobile1 = respObj.getString("Mobile1");
                        String Mobile2 = respObj.getString("Mobile2");
                        String Approved = respObj.getString("Approved");
                        String Ref_Code = respObj.getString("Ref_Code");
                        String Active = respObj.getString("Active");
                        String Type = respObj.getString("Type");
                        String wuser_code = respObj.getString("wuser_code");
                        String cveh_code = respObj.getString("cveh_code");
                        String imgdoc_path = respObj.getString("imgdoc_path");
                        String country_code = respObj.getString("country_code");
                        String state_code = respObj.getString("state_code");
                        String city_code = respObj.getString("city_code");
                        String country_name = respObj.getString("country_name");
                        String state_name = respObj.getString("state_name");
                        String city_name = respObj.getString("city_name");
                        String createdby = respObj.getString("createdby");



                        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("com_code", com_code);
                        Global.editor.putString("userName", userName);
                        Global.editor.putString("key_person", key_person);
                        Global.editor.putString("Email", Email);
                        Global.editor.putString("Image", Image);
                        Global.editor.putString("Mobile1", Mobile1);
                        Global.editor.putString("Mobile2", Mobile2);
                        Global.editor.putString("Approved", Approved);
                        Global.editor.putString("Ref_Code", Ref_Code);
                        Global.editor.putString("Active", Active);
                        Global.editor.putString("Type", Type);
                        Global.editor.putString("wuser_code", wuser_code);
                        Global.editor.putString("cveh_code", cveh_code);
                        Global.editor.putString("country_code", country_code);
                        Global.editor.putString("state_code", state_code);
                        Global.editor.putString("city_code", city_code);
                        Global.editor.putString("country_name", country_name);
                        Global.editor.putString("state_name", state_name);
                        Global.editor.putString("city_name", city_name);
                        Global.editor.putString("imgdoc_path", imgdoc_path);
                        Global.editor.putString("createdby", createdby);
                        Global.editor.commit();

                        progressDialog.dismiss();
                        Global.customtoast(LoginActivity.this, getLayoutInflater(), "Logged in successfully !!");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        // Handle error response
                        String error = response.optString("error", "Something went wrong");
                        Global.customtoast(LoginActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Add detailed error logging
                    Global.customtoast(LoginActivity.this, getLayoutInflater(), "Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, "Login failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }



    private void clearStoredTokens() {
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Global.editor = Global.sharedPreferences.edit();
        Global.editor.remove("access_token");
        Global.editor.remove("refresh_token");
        Global.editor.apply();

        // Reset local variables
        access_token = null;
        refresh_token = null;
    }

}

