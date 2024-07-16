package com.ziac.wheelzcustomer;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
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
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import ModelClasses.Global;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {


    ImageView splashimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        splashimage = findViewById(R.id.splash_logo);

        // Create a set of animations
        AnimationSet animationSet = new AnimationSet(true);

        // Scale animation
        Animation scaleAnimation = new ScaleAnimation(0.5f, 1.5f, 0.5f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(800);

        // Rotation animation
        Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1200);

        // Bounce animation
        Animation bounceAnimation = new TranslateAnimation(0, 0, 0, -50);
        bounceAnimation.setInterpolator(new BounceInterpolator());
        bounceAnimation.setDuration(800);

        // Fade-in animation
        Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(800);


        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(bounceAnimation);
        animationSet.addAnimation(fadeInAnimation);


        splashimage.startAnimation(animationSet);


        Handler handler = new Handler();
        handler.postDelayed(() -> {

            try {
                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                if (Global.sharedPreferences.contains("access_token") && Global.sharedPreferences.contains("refresh_token")) {


                    dorefreshtokenVolley();
                } else {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }catch (Exception ex){
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

        }, 2000);



    }


    private void dorefreshtokenVolley() {
        String url = Global.tokenurl;
        RequestQueue queue= Volley.newRequestQueue(SplashActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject respObj = new JSONObject(response);

                String access_token = respObj.getString("access_token");
                String refresh_token = respObj.getString("refresh_token");

                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("access_token", access_token);
                Global.editor.putString("refresh_token", refresh_token);
                Global.editor.commit();

                getuserprofile();

                startActivity(new Intent(SplashActivity.this, MainActivty.class));
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            //progressDialog.dismiss();
            if (error instanceof TimeoutError) {
                Global.customtoast(SplashActivity.this, getLayoutInflater(),"Internet is slow / Request Time-Out");
            }  else if (error instanceof NoConnectionError) {
                Global.customtoast(SplashActivity.this, getLayoutInflater(),"Internet connection unavailable");
            }else if (error instanceof ServerError) {
                Log.e("ServerError", "Server error response: " + new String(error.networkResponse.data));
                Global.customtoast(SplashActivity.this, getLayoutInflater(),"Unable to get authenticate to Server");
            }  else if (error instanceof ParseError) {
                Global.customtoast(SplashActivity.this, getLayoutInflater(),"Data Parse Error ");
            }  else if (error instanceof AuthFailureError) {
                Global.customtoast(SplashActivity.this, getLayoutInflater(), "Authorization Failure");
            }

            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("refresh_token", Global.sharedPreferences.getString("refresh_token", ""));
                params.put("grant_type", "refresh_token");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }
    private void getuserprofile() {

        String url = Global.getuserprofiledetails;
        RequestQueue queue= Volley.newRequestQueue(SplashActivity.this);
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



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {


            if (error instanceof TimeoutError) {
                Toast.makeText(SplashActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
            } else if (error instanceof NoConnectionError) {
                Toast.makeText(SplashActivity.this, "Internet connection unavailable", Toast.LENGTH_LONG).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(SplashActivity.this, "Server Error", Toast.LENGTH_LONG).show();
            } else if (error instanceof NetworkError) {
                Toast.makeText(SplashActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            } else if (error instanceof ParseError) {
                Toast.makeText(SplashActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        queue.add(request);
    }
}
