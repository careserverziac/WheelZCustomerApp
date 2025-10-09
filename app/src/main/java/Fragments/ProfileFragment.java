package Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.wheelzcustomer.ChangePasswordActivity;
import com.ziac.wheelzcustomer.ContactUSActivity;
import com.ziac.wheelzcustomer.DeleteAccountActivity;
import com.ziac.wheelzcustomer.EditProfileActivity;
import com.ziac.wheelzcustomer.LoginActivity;
import com.ziac.wheelzcustomer.R;
import ModelClasses.AppStatus;
import ModelClasses.Global;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {


    LinearLayout Data_privacy,Faq,Editprofile,Sharebutton,Contactus,PrivacyPolicy,Terms_Conditions,Deleteaccount,Changepassword;
    Intent intent;
    TextView Username,Usermobile,Useremail,Account;
    CircleImageView circularImageView;
    Button Logout;
    String userimage;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             View view= inflater.inflate(R.layout.fragment_profile, container, false);


        if (AppStatus.getInstance(requireActivity()).isOnline()) {
        } else {
            Global.customtoast(requireActivity(),getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        userimage = Global.userimageurl + Global.sharedPreferences.getString("Image", "");

        circularImageView =view.findViewById(R.id.pro_image);
        Global.loadWithPicasso(requireContext(), circularImageView, userimage);
        Picasso.Builder builder = new Picasso.Builder(requireContext());
        Picasso picasso = builder.build();

        Data_privacy=view.findViewById(R.id.data_privacy);
        Account=view.findViewById(R.id.account);
        Editprofile=view.findViewById(R.id.editprofile);
        Sharebutton=view.findViewById(R.id.sharebutton);
        Contactus=view.findViewById(R.id.contactus);
        PrivacyPolicy=view.findViewById(R.id.privacy);
        Terms_Conditions=view.findViewById(R.id.terms_conditions);
        Deleteaccount=view.findViewById(R.id.deleteaccount);
        Changepassword=view.findViewById(R.id.changepasword);
        Logout=view.findViewById(R.id.logoutbtn);
        Username=view.findViewById(R.id.PRusername);
        Usermobile=view.findViewById(R.id.PRusermobile);
        Useremail=view.findViewById(R.id.PRusermail);
        Faq = view.findViewById(R.id.faq);

        String dealername = Global.sharedPreferences.getString("key_person", "");
        String user_mail = Global.sharedPreferences.getString("Email", "");
        String user_mobile = Global.sharedPreferences.getString("Mobile1", "");
        String user_name = Global.sharedPreferences.getString("userName", "");

        Username.setText(dealername);
        Usermobile.setText(user_mobile);
        Useremail.setText(user_mail);
        Account.setText("Account "+"("+ user_name+")");


        Editprofile.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), EditProfileActivity.class))));
        Contactus.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), ContactUSActivity.class))));
        Changepassword.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), ChangePasswordActivity.class))));


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



        Sharebutton.setOnClickListener(v ->  ShareIntent());
        PrivacyPolicy.setOnClickListener(v ->  privacyMethod());
        Terms_Conditions.setOnClickListener(v ->  TermsOfUseMethod());
        Deleteaccount.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), DeleteAccountActivity.class))));
        Logout.setOnClickListener(v ->  exitdialog());



        circularImageView.setOnClickListener(v -> {
            userimage = Global.userimageurl + Global.sharedPreferences.getString("Image", "");
            showImage(picasso, userimage);

        });

        return view;
    }

    public void showImage(Picasso picasso, String userimage) {
        Dialog builder = new Dialog(requireActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(dialogInterface -> {
            // Nothing
        });

        // Calculate display dimensions
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Load the image using Picasso
        picasso.load(Uri.parse(userimage)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageView imageView = new ImageView(requireActivity());

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
                int paddingInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingInDp,
                        requireActivity().getResources().getDisplayMetrics());

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
        ConnectivityManager conMgr = (ConnectivityManager)requireActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            Toast.makeText(requireActivity(), "Network not available !!", Toast.LENGTH_SHORT).show();

        } else {
            golink("https://www.ziacsoft.com/privacy.html");
        }

    }
    private void TermsOfUseMethod() {
        ConnectivityManager conMgr = (ConnectivityManager) requireActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

            Toast.makeText(requireActivity(), "Network not available !!", Toast.LENGTH_SHORT).show();

        } else {
            golink("https://www.ziacsoft.com/terms.html");
        }

    }
    private void golink(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void exitdialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((requireActivity()));
        alertDialogBuilder.setTitle("Logout Confirmation");
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setIcon(R.drawable.wheelz_logo);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                Global.editor = Global.sharedPreferences.edit();
               /* Global.editor.remove("access_token");
                Global.editor.remove("refresh_token");
                Global.editor.remove("message");*/
                Global.editor.clear();
                Global.editor.apply();

                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
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

/*        Picasso.get().load(image).into(circularImageView);*/
        Global.loadWithPicasso(requireContext(), circularImageView, image);
        Username.setText(dealername);
        Usermobile.setText(user_mobile);
        Useremail.setText(user_mail);
    }


}
