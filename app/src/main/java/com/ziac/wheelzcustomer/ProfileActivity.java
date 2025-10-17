package com.ziac.wheelzcustomer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import ModelClasses.AppStatus;
import ModelClasses.Global;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    CardView Data_privacy, Faq, Sharebutton, Contactus, PrivacyPolicy, Terms_Conditions,
            Deleteaccount, Changepassword;
    Intent intent;
    TextView Username, Version,Usermobile, Useremail, Account;
    ImageView circularImageView;
    Button Logout;
    String userimage;
    ExtendedFloatingActionButton Editprofile;
    CollapsingToolbarLayout collapsingToolbar;


    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;


        if (AppStatus.getInstance(context).isOnline()) {
        } else {
            Global.customtoast(context, getLayoutInflater(), "Connected WIFI or Mobile data has no internet access!!");
        }

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userimage = Global.userimageurl + Global.sharedPreferences.getString("Image", "");

        circularImageView = findViewById(R.id.expanded_profile_image);
        Version = findViewById(R.id.versionNo);
        Version.setText("Version No:" + BuildConfig.VERSION_NAME);
        Global.loadWithPicasso(context, circularImageView, userimage);
        Picasso.Builder builder = new Picasso.Builder(context);
        Picasso picasso = builder.build();
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        Data_privacy = findViewById(R.id.data_privacy);
        Editprofile = findViewById(R.id.editprofile);
        Sharebutton = findViewById(R.id.sharebutton);
        Contactus = findViewById(R.id.contactus);
        PrivacyPolicy = findViewById(R.id.privacy);
        Terms_Conditions = findViewById(R.id.terms_conditions);
        Deleteaccount = findViewById(R.id.deleteaccount);
        Changepassword = findViewById(R.id.changepasword);
        Logout = findViewById(R.id.logoutbtn);
        Username = findViewById(R.id.expanded_profile_name);
        Usermobile = findViewById(R.id.PRusermobile);
        Useremail = findViewById(R.id.expanded_profile_email);
        Faq = findViewById(R.id.faq);

        String dealername = Global.sharedPreferences.getString("key_person", "");
        String user_mail = Global.sharedPreferences.getString("Email", "");
        String user_mobile = Global.sharedPreferences.getString("Mobile1", "");
        String user_name = Global.sharedPreferences.getString("userName", "");

        Username.setText(dealername + "(" + user_name + ")");
        Usermobile.setText(user_mobile);
        Useremail.setText(user_mail);


        Editprofile.setOnClickListener(v -> startActivity(new Intent(new Intent(context, EditProfileActivity.class))));
        Contactus.setOnClickListener(v -> startActivity(new Intent(new Intent(context, ContactUSActivity.class))));
        Changepassword.setOnClickListener(v -> startActivity(new Intent(new Intent(context, ChangePasswordActivity.class))));


        Faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://wheelzonline.co.in/FAQ/WheelzMobile";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                v.getContext().startActivity(intent);
            }
        });
        Data_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.ziacsoft.com/datapolicy.php";
                Intent d = new Intent(Intent.ACTION_VIEW);
                d.setData(Uri.parse(url));
                v.getContext().startActivity(d);
            }
        });


        Sharebutton.setOnClickListener(v -> ShareIntent());
        PrivacyPolicy.setOnClickListener(v -> privacyMethod());
        Terms_Conditions.setOnClickListener(v -> TermsOfUseMethod());
        Deleteaccount.setOnClickListener(v -> startActivity(new Intent(new Intent(this, DeleteAccountActivity.class))));
        Logout.setOnClickListener(v -> exitdialog());


        circularImageView.setOnClickListener(v -> {
            userimage = Global.userimageurl + Global.sharedPreferences.getString("Image", "");
            if (userimage == null || userimage.trim().isEmpty()) {
                circularImageView.setImageResource(R.drawable.no_image_available_icon);
            } else {
                Global.loadWithPicasso(context, circularImageView, userimage);
                showImage(picasso, userimage);
            }
        });
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(new Intent(context, MainActivity.class)));
                finish();
            }
        });
    }

    public void showImage(Picasso picasso, String fullUrl) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Nothing
            }
        });

        // Calculate display dimensions
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Load the image using Picasso
        picasso.load(Uri.parse(fullUrl)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageView imageView = new ImageView(context);

                // Calculate dimensions to fit the image within the screen
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                float aspectRatio = (float) imageWidth / imageHeight;

                int newWidth = screenWidth;
                int newHeight = (int) (screenWidth / aspectRatio);
                if (newHeight > screenHeight) {
                    newHeight = screenHeight;
                    newWidth = (int) (screenHeight * aspectRatio);
                }

                // Add padding values
                int paddingInDp = 16; // You can adjust the padding as per your requirement
                int paddingInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingInDp, context.getResources().getDisplayMetrics());

                // Adjust the newWidth and newHeight with padding
                newWidth -= 2 * paddingInPx; // Subtract padding from both sides
                newHeight -= 2 * paddingInPx; // Subtract padding from both top and bottom

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
                layoutParams.setMargins(paddingInPx, paddingInPx, paddingInPx, paddingInPx); // Set padding
                imageView.setLayoutParams(layoutParams);

                imageView.setImageBitmap(bitmap);

                builder.addContentView(imageView, layoutParams);
                builder.show();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle bitmap loading failure
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Prepare bitmap loading
            }
        });
    }


    private void privacyMethod() {
        ConnectivityManager conMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            Toast.makeText(this, "Network not available !!", Toast.LENGTH_SHORT).show();

        } else {
            golink("https://www.ziacsoft.com/privacy.html");
        }

    }

    private void TermsOfUseMethod() {
        ConnectivityManager conMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

            Toast.makeText(this, "Network not available !!", Toast.LENGTH_SHORT).show();

        } else {
            golink("https://www.ziacsoft.com/terms.html");
        }

    }

    private void golink(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void exitdialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((this));
        alertDialogBuilder.setTitle("Logout Confirmation");
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setIcon(R.drawable.wheelz_logo);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.clear();
                Global.editor.apply();

                startActivity(new Intent(context, LoginActivity.class));
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
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.ziac.wheelzcustomer\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose to share"));
        } catch (Exception e) {
            //e.toString();
        }
        return intent;
    }




    @Override
    public void onResume() {
        super.onResume();
        String image = Global.userimageurl + Global.sharedPreferences.getString("Image", "");
        String dealername = Global.sharedPreferences.getString("key_person", "");
        String user_mail = Global.sharedPreferences.getString("Email", "");
        String user_mobile = Global.sharedPreferences.getString("Mobile1", "");

        Global.loadWithPicasso(this, circularImageView, image);
        Username.setText(dealername);
        Usermobile.setText(user_mobile);
        Useremail.setText(user_mail);
    }
}