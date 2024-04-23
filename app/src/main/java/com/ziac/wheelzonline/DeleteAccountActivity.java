package com.ziac.wheelzonline;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ModelClasses.Global;
import ModelClasses.SMSBrodcastReciever;

public class DeleteAccountActivity extends AppCompatActivity {


    public  static  final  int REQ_USER_CONSENT=100;
    SMSBrodcastReciever smsBroadcastReceiver;
    final Context context = this;
    AppCompatButton GetOTP,ValidateOTP,Deleteaccount;
    AppCompatButton Submitdelete;
    String username,mobile,otp;
    EditText Username,Mobileno;
    String OTP;
    PinView pinView;
    ProgressBar progressBar;

    private int clickCounter = 0;

    private boolean isOTPReceived = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);


        GetOTP=findViewById(R.id.getotp);
        ValidateOTP=findViewById(R.id.validateOTP);
        Deleteaccount=findViewById(R.id.deleteaccountp);
        progressBar=findViewById(R.id.progressbarline);


        hideLoading();
       pinView=findViewById(R.id.pinviewdelete);
        startSmartUserConsent();

     /*   Submitdelete=findViewById(R.id.submitbutton);
        Submitdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialogmethod();
            }
        });
*/
        GetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getotpmethod();
            }
        });

        ValidateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateOTPmethod();
            }
        });




/*
        GetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the click counter
                clickCounter++;

                // Execute different methods based on the click counter value
                switch (clickCounter) {
                    case 1:
                        getotpmethod();
                        break;
                    case 2:
                       validatemethod();
                        break;
                    case 3:
                        alertdialogmethod();
                        break;
                    default:
                        // Reset the counter if it exceeds 3
                        clickCounter = 0;
                        break;
                }
            }
        });
*/


       /* Submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                if (!isOTPReceived) {
                    // If OTP is not received, call getotpmethod()
                    getotpmethod();
                } else {
                    // If OTP is received, call validateOTPmethod()
*//*                    validateOTPmethod();*//*
                    alertdialogmethod();

                    // Then call alertdialogmethod()
                   // alertdialogmethod();
                }
            }
        });*/




    }



    private void deleteaccountmethod() {
        showLoading();
        String url = Global.velidateanddeleteaccounturl;
        OTP=pinView.getText().toString();
        url=url+"OTP="+OTP;
        RequestQueue queue= Volley.newRequestQueue(DeleteAccountActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    if(issuccess.equals("true")){
                        hideLoading();;
                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);

                        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DeleteAccountActivity.this);
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.remove("access_token");
                        Global.editor.remove("refresh_token");
                        Global.editor.commit();
                        startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));
                        finish();


                    } else {
                        hideLoading();;
                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                if (error instanceof TimeoutError) {
                    Toast.makeText(DeleteAccountActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(DeleteAccountActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(DeleteAccountActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(DeleteAccountActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(DeleteAccountActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }
          /*  @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", Global.sharedPreferences.getString("userName",""));
                params.put("Mobile", Global.sharedPreferences.getString("Mobile1",""));
                Log.d("getHeaders", params.toString());
                return params;
            }*/
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

    private void validateOTPmethod() {

        otp=pinView.getText().toString();

        if (otp.isEmpty()) {
            Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), "OTP field is missing !!");
            pinView.requestFocus();
            return;
        }
        if (otp.length() != 6) {
            Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), "Enter a valid 6-digit OTP !!");
            pinView.requestFocus();
            return;
        }

/*

        ValidateOTP.setVisibility(View.GONE);
        Deleteaccount.setVisibility(View.VISIBLE);
*/

        alertdialogmethod();


      /*  String url = Global.validateOTPurl;
        RequestQueue queue= Volley.newRequestQueue(DeleteAccountActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    if(issuccess.equals("true")){

                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                        Deleteaccount.setVisibility(View.VISIBLE);
                        hideLoading();;
                    } else {
                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();;
                if (error instanceof TimeoutError) {
                    Toast.makeText(DeleteAccountActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(DeleteAccountActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(DeleteAccountActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(DeleteAccountActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(DeleteAccountActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
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
                params.put("UserName", Global.sharedPreferences.getString("userName",""));
                params.put("Mobile", Global.sharedPreferences.getString("Mobile1",""));
                // params.put("FPType", Global.sharedPreferences.getString("Mobile1",""));
                params.put("FPType", "M");
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
*/
}

    private void alertdialogmethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert_dailog, null);
        builder.setView(dialogView);

        TextView continuebtn = dialogView.findViewById(R.id.continuebtn);
        TextView cancelbtn = dialogView.findViewById(R.id.cancelbtn);

        final AlertDialog dialog = builder.create();
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteaccountmethod();

            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();;
            }
        });
        dialog.show();
    }

    private void getotpmethod() {
        showLoading();;

        String url = Global.otpfordeleteaccounturl;
        RequestQueue queue= Volley.newRequestQueue(DeleteAccountActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    if(issuccess.equals("true")){

                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                        GetOTP.setVisibility(View.GONE);
                        ValidateOTP.setVisibility(View.VISIBLE);
                        hideLoading();;
                    } else {
                        hideLoading();;
                        Global.customtoast(DeleteAccountActivity.this, getLayoutInflater(), error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();;
                if (error instanceof TimeoutError) {
                    Toast.makeText(DeleteAccountActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(DeleteAccountActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(DeleteAccountActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(DeleteAccountActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(DeleteAccountActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
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
                params.put("UserName", Global.sharedPreferences.getString("userName",""));
                params.put("Mobile", Global.sharedPreferences.getString("Mobile1",""));
                // params.put("FPType", Global.sharedPreferences.getString("Mobile1",""));
                params.put("FPType", "M");
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

    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT){

            if ((resultCode == RESULT_OK) && (data != null)){

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);


            }


        }

    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()){

            pinView.setText(matcher.group(0));



        }


    }

    private void registerBroadcastReceiver(){

        smsBroadcastReceiver = new SMSBrodcastReciever();

        smsBroadcastReceiver.smsBroadcastReceiverListener = new SMSBrodcastReciever.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent,REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver,intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }




    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);

    }
}