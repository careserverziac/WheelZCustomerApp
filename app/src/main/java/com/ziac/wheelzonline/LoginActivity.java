package com.ziac.wheelzonline;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ModelClasses.Global;
import ModelClasses.InternetChecker;

public class LoginActivity extends AppCompatActivity {

    TextView Login,Signin,Textlogo,ForgotPasswordTXT,versionName;
    EditText Username,Password;
    AppCompatButton loginbtn;
    String username,pwd;
    private ProgressDialog progressDialog;
     CheckBox RememberMe;
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
        RememberMe = findViewById(R.id.remember);
        loginbtn = findViewById(R.id.loginbtn);
        Signin = findViewById(R.id.signin);
        ForgotPasswordTXT=findViewById(R.id.forgotpasswordtxt);
        versionName = findViewById(R.id.version);
        versionName.setText("Ver No:" + BuildConfig.VERSION_NAME);



        InternetChecker.checkInternetConnection(this, new InternetChecker.InternetCheckListener() {
            @Override
            public void onInternetCheckResult(boolean isConnected) {
                if (!isConnected) {
                    Global.customtoast(LoginActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access");
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

            if (isNetworkAvailable()) {
                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();

                if (RememberMe.isChecked()) {
                    Global.editor.putString("username", username);
                    Global.editor.putString("password", pwd);
                } else {
                    Global.editor.putString("username", "");
                    Global.editor.putString("password", "");
                }
                Global.editor.commit();

                // Call login method only if there is internet connectivity
                dologinVolley();
            } else {
                Global.customtoast(LoginActivity.this, getLayoutInflater(), "No internet connection. Please check your network.");
            }

        });

        RememberMe.setChecked(false);
        try {
            Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            username = Global.sharedPreferences.getString("username", "");
            pwd = Global.sharedPreferences.getString("password", "");
            Username.setText(username);
            Password.setText(pwd);
            if (username.length() == 0) {
                RememberMe.setChecked(false);
            } else {
                RememberMe.setChecked(true);
            }

        } catch (Exception e) {
            RememberMe.setChecked(false);
        }

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
        ForgotPasswordTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

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


    private void dologinVolley() {
      /*  if (!isNetworkAvailable()) {
            Global.customtoast(LoginActivity.this, getLayoutInflater(), "Internet connection lost !!");
            return;
        }*/

        progressDialog.show();
      //  new InternetCheckTask().execute();

        String url = Global.tokenurl;
        RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject respObj = new JSONObject(response);

                String access_token = respObj.getString("access_token");
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("access_token", access_token);
                Global.editor.commit();


                Global.customtoast(LoginActivity.this, getLayoutInflater(),"Login Successfull ");

                startActivity(new Intent(LoginActivity.this, MainActivty.class));
                progressDialog.dismiss();
               // getuserdetails();


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Global.customtoast(LoginActivity.this, getLayoutInflater(),"Request Time-Out");
                }  else if (error instanceof NoConnectionError) {
                    Global.customtoast(LoginActivity.this, getLayoutInflater(),"No Connection Found");
                }else if (error instanceof ServerError) {
                    Global.customtoast(LoginActivity.this, getLayoutInflater(),"Invalid username or password");
                }  else if (error instanceof ParseError) {
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


   /* private void getuserdetails() {

        String url = Global.getuserdetailsurl;
        RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
        //progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

            try {
                JSONObject respObj1 = new JSONObject(response);
                JSONObject respObj = new JSONObject(respObj1.getString("data"));

                Global.customtoast(LoginActivity.this, getLayoutInflater(),"Login Successfull ");

                String com_code = respObj.getString("com_code");
                String refcode = respObj.getString("ref_code");
                String userName = respObj.getString("UserName");
                String user_image = respObj.getString("user_image");
                String user_mobile = respObj.getString("user_mobile");
                String user_email = respObj.getString("user_email");
                String Person_name = respObj.getString("person_name");

                //  String com_name = respObj.getString("com_name");


//                 Log.d("MyApp", "Person_name: " + Person_name);

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("com_code", com_code);
                Global.editor.putString("ref_code", refcode);
                Global.editor.putString("Username", userName);
                Global.editor.putString("user_image", user_image);
                Global.editor.putString("user_mobile", user_mobile);
                Global.editor.putString("user_email", user_email);
                Global.editor.putString("person_names", Person_name);
                Global.editor.commit();

                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

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
                params.put("username", username);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        queue.add(request);
    }*/



  /*  private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private class InternetCheckTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);} catch (IOException e) {e.printStackTrace();return false;}}

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                //  showToast("Internet access is available.");
            } else {
                progressDialog.dismiss();
                showToast("Connected WIFI or Mobile data has no internet access");
            }
        }}*/
  /*  private void showToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        Global.customtoast(LoginActivity.this, inflater, message);

    }*/

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
