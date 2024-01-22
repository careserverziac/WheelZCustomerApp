package com.ziac.wheelzonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactUSActivity extends AppCompatActivity {

    RelativeLayout Contact_whatsapp;
    LinearLayout Facebook,Linkedin,Twitter,Instagram,Location,Call,Mail;
    FloatingActionButton Backbtn;
    boolean message = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_usactivity);

        Contact_whatsapp=findViewById(R.id.contact_whatsapp);
        Facebook=findViewById(R.id.facebookimg);
        Linkedin=findViewById(R.id.linkedinimg);
        Twitter=findViewById(R.id.twitterimg);
        Instagram=findViewById(R.id.instagramimg);
        Location=findViewById(R.id.location);
        Call=findViewById(R.id.call);
        Mail=findViewById(R.id.maillink);
        Backbtn=findViewById(R.id.conbackbtn);


        Instagram.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/ziacsoftwares/"))));
        Facebook.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/ziacsoft/"))));
        Twitter.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/ziacsoft"))));
        Linkedin.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/company/ziacsoft"))));
        Backbtn.setOnClickListener(v -> finish());

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

        // Update the package name for WhatsApp Business based on your findings
        String whatsappBusinessPackageName = "com.whatsapp.w4b";

        if (isPackageInstalled(whatsappBusinessPackageName, getPackageManager())) {
            intent.setPackage(whatsappBusinessPackageName);
            try {
                startActivity(intent);
                return;  // Successfully started WhatsApp Business intent
            } catch (ActivityNotFoundException ignored) {
                // WhatsApp Business intent not available, fall through to regular WhatsApp
            }
        }

        // Try launching regular WhatsApp intent
        if (isPackageInstalled("com.whatsapp", getPackageManager())) {
            intent.setPackage("com.whatsapp");
            try {
                startActivity(intent);
                return;  // Successfully started regular WhatsApp intent
            } catch (ActivityNotFoundException ignored) {
                // Regular WhatsApp intent not available
            }
        }

        // Handle the case where neither WhatsApp nor WhatsApp Business is available
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

    public void onLocateClicked(View view) {
        String address = "Ziac Software No. 5, 2nd Cross, CSI compound, Mission Rd, Bengaluru, Karnataka 560027";
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}