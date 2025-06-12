package com.ziac.wheelzcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import ModelClasses.AppStatus;
import ModelClasses.Global;

public class ContactUSActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int YOUR_CALL_PERMISSION_REQUEST_CODE =2 ;
    RelativeLayout Contact_whatsapp;
    LinearLayout Facebook,Linkedin,Twitter,Instagram,Location,Call,Mail;
    RelativeLayout relativeLayout;
    boolean message = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_usactivity);

        if (AppStatus.getInstance(this).isOnline()) {
        } else {
            Global.customtoast(ContactUSActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }
        Contact_whatsapp=findViewById(R.id.contact_whatsapp);
        Facebook=findViewById(R.id.facebookimg);
        Linkedin=findViewById(R.id.linkedinimg);
        Twitter=findViewById(R.id.twitterimg);
        Instagram=findViewById(R.id.instagramimg);
        Location=findViewById(R.id.location);
        Call=findViewById(R.id.call);
        Mail=findViewById(R.id.maillink);
        relativeLayout=findViewById(R.id.locaterelative);


        Instagram.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/ziacsoftwares/"))));
        Facebook.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/ziacsoft/"))));
        Twitter.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/ziacsoft"))));
        Linkedin.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/company/ziacsoft"))));

        Call.setOnClickListener(v -> {Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:9008098101"));
            if (ActivityCompat.checkSelfPermission(ContactUSActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ContactUSActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            } else {
                try {
                    startActivity(callIntent);
                } catch (SecurityException e) {
                    Log.e("CallError", "Error starting call: " + e.getMessage());}}
        });

        Mail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ziacsupport@ziacsoft.com", null));
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(intent, "ziacsupport@ziacsoft.com"));
        });

    }
    public void openWhatsAppChat(View view) {
        String phoneNumber = "9972595464";
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);


        String whatsappBusinessPackageName = "com.whatsapp.w4b";

        if (isPackageInstalled(whatsappBusinessPackageName, getPackageManager())) {
            intent.setPackage(whatsappBusinessPackageName);
            try {
                startActivity(intent);
                return;  // Successfully started WhatsApp Business intent
            } catch (ActivityNotFoundException ignored) {

            }
        }


        if (isPackageInstalled("com.whatsapp", getPackageManager())) {
            intent.setPackage("com.whatsapp");
            try {
                startActivity(intent);
                return;  // Successfully started regular WhatsApp intent
            } catch (ActivityNotFoundException ignored) {
                // Regular WhatsApp intent not available
            }
        }


        Toast.makeText(this, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show();
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void locateAddress() {
        String address = "Ziac Software No. 5, 2nd Cross, CSI compound, Mission Rd, Bengaluru, Karnataka 560027";
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locateAddress();
            } else {
                Global.customtoast(ContactUSActivity.this,getLayoutInflater(),"Permission denied");
            }
        } else if (requestCode == YOUR_CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Global.customtoast(ContactUSActivity.this,getLayoutInflater(),"Permission denied");
            }
        }

    }
    private void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:9008098101"));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            Log.e("CallError", "Error starting call: " + e.getMessage());
        }
    }

    public void onLocateClicked(View view) {
        // Check if the app has permission to access the location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            // Permission already granted, proceed to locate
            locateAddress();
        }
    }
}