package Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.wheelzcustomer.ChangePasswordActivity;
import com.ziac.wheelzcustomer.ContactUSActivity;
import com.ziac.wheelzcustomer.DeleteAccountActivity;
import com.ziac.wheelzcustomer.ProfileActivity;
import com.ziac.wheelzcustomer.LoginActivity;
import com.ziac.wheelzcustomer.R;

import ModelClasses.AppStatus;
import ModelClasses.Global;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {


    LinearLayout Editprofile,Sharebutton,Contactus,PrivacyPolicy,Terms_Conditions,Logout,Deleteaccount,Changepassword,Editicon;
    FloatingActionButton BackButton;
    Intent intent;
    TextView Username,Usermobile,Useremail,Account;
    CircleImageView circularImageView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             View view= inflater.inflate(R.layout.fragment_profile, container, false);


        if (AppStatus.getInstance(requireActivity()).isOnline()) {
            //Toast.makeText(this,"You are online!!!!", Toast.LENGTH_SHORT).show();
        } else {
            Global.customtoast(requireActivity(),getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }

       /* String image = Global.userimageurl + Global.sharedPreferences.getString("Image", "");
        Picasso.Builder builder=new Picasso.Builder(requireActivity());
        ProfileImage=view.findViewById(R.id.pro_image);
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(image)).into(ProfileImage );*/

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String userimage = Global.userimageurl + Global.sharedPreferences.getString("user_image", "");

        circularImageView =view.findViewById(R.id.pro_image);
        Global.loadWithPicasso(requireContext(), circularImageView, userimage);



        Account=view.findViewById(R.id.account);
        Editprofile=view.findViewById(R.id.editprofile);
       // BackButton=view.findViewById(R.id.Pbackbtn);
        Sharebutton=view.findViewById(R.id.sharebutton);
        Contactus=view.findViewById(R.id.contactus);
        PrivacyPolicy=view.findViewById(R.id.privacy);
        Terms_Conditions=view.findViewById(R.id.terms_conditions);
        Deleteaccount=view.findViewById(R.id.deleteaccount);
        Changepassword=view.findViewById(R.id.changepasword);
        Logout=view.findViewById(R.id.logoutbtn);
        Editicon=view.findViewById(R.id.editicon);
       // swipeRefreshLayout=view.findViewById(R.id.refreshprofile);

        Username=view.findViewById(R.id.PRusername);
        Usermobile=view.findViewById(R.id.PRusermobile);
        Useremail=view.findViewById(R.id.PRusermail);


        String dealername = Global.sharedPreferences.getString("key_person", "");
        String user_mail = Global.sharedPreferences.getString("Email", "");
        String user_mobile = Global.sharedPreferences.getString("Mobile1", "");
        String user_name = Global.sharedPreferences.getString("userName", "");

       //Picasso.get().load(image).into(circularImageView);
        Username.setText(dealername);
        Usermobile.setText(user_mobile);
        Useremail.setText(user_mail);
        Account.setText("Account "+"("+ user_name+")");


        Editprofile.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), ProfileActivity.class))));
        Editicon.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), ProfileActivity.class))));
        Contactus.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), ContactUSActivity.class))));
        Changepassword.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), ChangePasswordActivity.class))));


     /*   swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                String dealername = Global.sharedPreferences.getString("key_person", "");
                String user_mail = Global.sharedPreferences.getString("Email", "");
                String user_mobile = Global.sharedPreferences.getString("Mobile1", "");

                Picasso.get().load(image).into(ProfileImage);
                Username.setText(dealername);
                Usermobile.setText(user_mobile);
                Useremail.setText(user_mail);

                swipeRefreshLayout.setRefreshing(false);
            }
        });*/


        Sharebutton.setOnClickListener(v ->  ShareIntent());
        PrivacyPolicy.setOnClickListener(v ->  privacyMethod());
        Terms_Conditions.setOnClickListener(v ->  TermsOfUseMethod());
        Deleteaccount.setOnClickListener(v -> startActivity(new Intent(new Intent(requireActivity(), DeleteAccountActivity.class))));
        Logout.setOnClickListener(v ->  exitdialog());

        return view;
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
                Global.editor.remove("access_token");
                Global.editor.remove("refresh_token");
                Global.editor.remove("message");
                Global.editor.commit();

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
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.ziac.divinecrmapp\n\n";
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
