package com.ziac.wheelzonline;

import android.app.ProgressDialog;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

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

public class SplashActivity extends AppCompatActivity {

    ImageView splashimage;
    //private ProgressDialog progressDialog;
    String username,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);




        /*progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);*/


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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

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

            }
        }, 3000);





    }
    private void dorefreshtokenVolley() {

        //progressDialog.show();

        String url = Global.tokenurl;

        //url = url + "?refresh_token="+Global.sharedPreferences.getString("refresh_token", "")+"&grant_type=refresh_token";
        RequestQueue queue= Volley.newRequestQueue(SplashActivity.this);
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

               /* if(issuccess.equals("true")){
                    Global.customtoast(SplashActivity.this, getLayoutInflater(), error);
                    *//*Global.customtoast(ForgotPasswordActivity.this, getLayoutInflater(), "OTP is send to your registered mobile number");*//*
                    startActivity(new Intent(SplashActivity.this,CreateNewPasswordActivity.class));
                } else {
                    Global.customtoast(SplashActivity.this, getLayoutInflater(), error);
                }*/

                startActivity(new Intent(SplashActivity.this, MainActivty.class));
                //progressDialog.dismiss();
                finish();
                // getuserdetails();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Global.customtoast(SplashActivity.this, getLayoutInflater(),"Internet is slow / Request Time-Out");
                }  else if (error instanceof NoConnectionError) {
                    Global.customtoast(SplashActivity.this, getLayoutInflater(),"No Connection / Internet not available");
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
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("refresh_token", Global.sharedPreferences.getString("refresh_token", ""));
                params.put("grant_type", "refresh_token");
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
