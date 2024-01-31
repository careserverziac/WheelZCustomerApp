package com.ziac.wheelzonline;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ModelClasses.AppStatus;
import ModelClasses.Global;

public class SignupActivity extends AppCompatActivity {
    FloatingActionButton Signupbackbtn;
    EditText Name,Email,Mobile,Username,Password,Cpassword;
    CheckBox checkBox;
    TextView TermsandConditions;
    AppCompatButton Register;
    Context context;
    boolean passwordvisible;
    private boolean passwordVisible = false;
    private ProgressDialog progressDialog;
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Context context=this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);

        if (AppStatus.getInstance(this).isOnline()) {
            //Toast.makeText(this,"You are online!!!!", Toast.LENGTH_SHORT).show();
        } else {
            Global.customtoast(SignupActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }

        Signupbackbtn=findViewById(R.id.signupbackbtn);
        Name=findViewById(R.id.name);
        Email=findViewById(R.id.email);
        Mobile=findViewById(R.id.mobile);
        Username=findViewById(R.id.username);
        Password=findViewById(R.id.password);
        Cpassword=findViewById(R.id.cpassword);
        checkBox=findViewById(R.id.checkbox);
        TermsandConditions=findViewById(R.id.termsandconditions);
        Register=findViewById(R.id.register);
        Signupbackbtn.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
        Register.setOnClickListener(v -> {// startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

        Register.setEnabled(false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    Register.setTextColor(Color.WHITE);
                    Register.setText("Continue");
                    Register.setBackgroundResource(R.drawable.border_colour);
                    Register.setEnabled(true);
                } else {

                    Register.setTextColor(Color.WHITE);
                    Register.setText("Register");
                    Register.setBackgroundResource(R.drawable.border_colour2);

                }
            }
        });
        Register.setOnClickListener(v -> createnewuser());


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
        Cpassword.setOnTouchListener((v, event) -> {

            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Cpassword.getRight() - Cpassword.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Cpassword.getSelectionEnd();
                    if (passwordvisible) {
                        Cpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0);
                        Cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;

                    } else {
                        Cpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_eyes, 0);
                        Cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;


                    }
                    Cpassword.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        String text = TermsandConditions.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TermsandConditions.setText(spannableString);

        TermsandConditions.setOnClickListener(v -> golink());

    }

    private void createnewuser() {


        String name,email,mobile,username,password,confirmpasword;

        name = Name.getText().toString();
        email = Email.getText().toString();
        mobile = Mobile.getText().toString();
        username = Username.getText().toString();
        password = Password.getText().toString();
        confirmpasword = Cpassword.getText().toString();


        if (name.isEmpty()) {
            Name.setError("Name field is missing !!");
            Name.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Email.setError("Email field is missing !!");
            Email.requestFocus();
            return;

        }
        if (!email.contains("@") || !email.endsWith(".com") || email.contains(" ")) {
           // Global.customtoast(RegisterActivity.this, getLayoutInflater(), "The provided email format does not exist !!");
            return;
        }
        if (mobile.isEmpty()) {
            Mobile.setError("Mobile no field is missing !!");
            Mobile.requestFocus();
            return;
        }
        if (mobile.length() < 10) {
           // Global.customtoast(RegisterActivity.this, getLayoutInflater(), "Mobile number should not be less than 10 digits !!");
            return;
        }
        if (username.contains(" ")) {
            Username.setError("Admin User Name cannot allow space in between !!");
            Username.requestFocus();
            return;
        }
        if (username.endsWith(".")) {
           // Global.customtoast(RegisterActivity.this, getLayoutInflater(), "Admin User Name cannot end with .");
            return;

        }

        if (username.contains("..")) {
           // Global.customtoast(RegisterActivity.this, getLayoutInflater(), "Admin User Name cannot allow two ..");
            return;
        }

        if (username.startsWith(".")) {
           // Global.customtoast(RegisterActivity.this, getLayoutInflater(), "Admin User Name cannot start with .");
            return;

        }
        if (username.length() < 6) {
            //Global.customtoast(RegisterActivity.this, getLayoutInflater(), "The Admin User Name must contain 6 to 20 characters !!");
            return;

//        }if (!adminUserName.matches(".*\\d.*")) {
//            Global.customtoast(RegisterActivity.this, getLayoutInflater(), "The Admin User Name must contain atleast one number !!");
//            return;
//
        }

        if (password.isEmpty()) {
            Password.setError("Password field is missing !!");
            Password.requestFocus();
            return;
        }
        if (confirmpasword.isEmpty()) {
            Cpassword.setError("Confirm pasword field is missing !!");
            Cpassword.requestFocus();
            return;
        }



        if (password.isEmpty()) {
            Password.setError("Password field is missing !!");
            Password.requestFocus();
            return;


        }
        if (!isValidPassword(password)) {
            Global.customtoast(SignupActivity.this, getLayoutInflater(), "Password must contain one upper case, one lower case, one number, and one special character !!");
            return;
        }

        if (password.length() < 6) {
            Global.customtoast(SignupActivity.this, getLayoutInflater(), "Password must contain one upper case,one lower case,one number and one special character !!");
            return;
        }

        if (confirmpasword.isEmpty()) {
            Cpassword.setError("Confirm password field is missing !!");
            Cpassword.requestFocus();
            return;
        }
        if (!password.equals(confirmpasword)) {
            Global.customtoast(SignupActivity.this, getLayoutInflater(), "The given password and confirm password does not match !!");
            return;

        }

        if (!checkBox.isChecked()) {
            Global.customtoast(SignupActivity.this, getLayoutInflater(), "You are not agree with the terms and conditions of Divine CRM to move further !!");
            return;
        }
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urlregistration,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String sresponse) {


                        JSONObject response = null;
                        try {
                            response = new JSONObject(sresponse);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        Log.d("Register", sresponse);

                        try {
                            if (response.getBoolean("isSuccess")) {
                                Global.customtoast(SignupActivity.this, getLayoutInflater(),response.getString("error"));
                                finish();
                            } else {
                                Global.customtoast(SignupActivity.this, getLayoutInflater(),response.getString("error"));

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // Toast.makeText(RegisterActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
              //  Global.customtoast(context, getLayoutInflater(), error.getLocalizedMessage());

                if (error instanceof TimeoutError) {
                    Global.customtoast(SignupActivity.this, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(SignupActivity.this, getLayoutInflater(), "No Connection Found");
                } else if (error instanceof ServerError) {
                    Global.customtoast(SignupActivity.this, getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(SignupActivity.this, getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(SignupActivity.this, getLayoutInflater(), "Parse Error");
                }


            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                Log.d("getHeaders", params.toString());
                return params;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


//                String emailValue = Email.getText().toString();
//                if (emailValue.isEmpty()) {
//                    emailValue = "notprovided@gmail.com";
//                }


                params.put("key_person",name);
                params.put("wuser_email",email);
                params.put("wuser_mobile1",mobile);
                params.put("username",username);
                params.put("password",password);
               // params.put("confirm_password",confirmpasword);
                Log.d("params",params.toString());
                return params;

//                params.put("com_email", emailValue);
//                params.put("state_code", String.valueOf(statename.get_code()));
//                params.put("city_code", String.valueOf(cityname.get_code()));

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

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
    private void golink() {
        Uri uri = Uri.parse("https://www.ziacsoft.com/terms.html");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}