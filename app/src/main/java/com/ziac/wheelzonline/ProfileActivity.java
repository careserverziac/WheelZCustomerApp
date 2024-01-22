package com.ziac.wheelzonline;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout Editprofile,Sharebutton,Contactus,PrivacyPolicy,Terms_Conditions,Logout,Deleteaccount;
    FloatingActionButton BackButton;
    Intent intent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Editprofile=findViewById(R.id.editprofile);
        BackButton=findViewById(R.id.Pbackbtn);
        Sharebutton=findViewById(R.id.sharebutton);
        Contactus=findViewById(R.id.contactus);
        PrivacyPolicy=findViewById(R.id.privacy);
        Terms_Conditions=findViewById(R.id.terms_conditions);
        Deleteaccount=findViewById(R.id.deleteaccount);
        Logout=findViewById(R.id.logoutbtn);

        Editprofile.setOnClickListener(v -> startActivity(new Intent(new Intent(ProfileActivity.this,EditProfileActivity.class))));
        Contactus.setOnClickListener(v -> startActivity(new Intent(new Intent(ProfileActivity.this,ContactUSActivity.class))));
        BackButton.setOnClickListener(v -> finish());
        Sharebutton.setOnClickListener(v ->  ShareIntent());
        PrivacyPolicy.setOnClickListener(v ->  privacyMethod());
        Terms_Conditions.setOnClickListener(v ->  TermsOfUseMethod());
        Deleteaccount.setOnClickListener(v ->  deleteaccount());
        Logout.setOnClickListener(v ->  exitdialog());


    }

    private void deleteaccount() {Toast.makeText(this, "Method not done", Toast.LENGTH_SHORT).show();}

    private void privacyMethod() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            Toast.makeText(ProfileActivity.this, "Network not available !!", Toast.LENGTH_SHORT).show();

        } else {
            golink("https://www.ziacsoft.com/privacy.html");
        }

    }
    private void TermsOfUseMethod() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

            Toast.makeText(ProfileActivity.this, "Network not available !!", Toast.LENGTH_SHORT).show();

        } else {
            golink("https://www.ziacsoft.com/terms.html");
        }

    }
    private void golink(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void exitdialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((ProfileActivity.this));
        alertDialogBuilder.setTitle("Logout Confirmation");
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setIcon(R.drawable.wheelz_logo);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private Intent ShareIntent() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            // i.putExtra(Intent.EXTRA_SUBJECT, "Portaz-DFC");
            String sAux = "\nClick the link to download the app from the Google Play Store\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.ziac.divinecrmapp\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose to share"));
        } catch (Exception e) {
            //e.toString();
        }
        return intent;
    }
}