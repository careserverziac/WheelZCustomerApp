package com.ziac.wheelzcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.BookServiceFragment;
import Fragments.DealersFragment;
import Fragments.DashboardFragment;
import Fragments.ModelsFragment;
import Fragments.MyVehcileFragment;
import Fragments.ProfileFragment;
import ModelClasses.AppStatus;
import ModelClasses.Global;

public class MainActivity extends AppCompatActivity  {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    ImageView Toolbaricon;
    FragmentManager fragmentManager;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    SpannableString spannableString;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (AppStatus.getInstance(this).isOnline()) {
        } else {
            Global.customtoast(MainActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("flag"))
            flag = extras.getBoolean("flag");
        if (flag) {
            openFragment(getOneFragment());
        } else {
            openFragment(new DashboardFragment());
        }


        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        frameLayout = findViewById(R.id.framelayout);

        openFragment(new DashboardFragment());

        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            spannableString = new SpannableString(menuItem.getTitle());
            spannableString.setSpan(new AbsoluteSizeSpan(10, true), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannableString);
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();

                if (itemid == R.id.bottom_home) {
                    openFragment(new DashboardFragment());
                    return true;
                } else if (itemid == R.id.bottom_models) {
                    openFragment(new ModelsFragment());
                    return true;
                } else if (itemid == R.id.bottom_dealers) {
                    openFragment(new DealersFragment());
                    return true;
                } else if (itemid == R.id.bottom_vehicles) {
                    openFragment(new MyVehcileFragment());
                    return true;
                }

                return false;
            }
        });




    }
    private Fragment getOneFragment() {
        return new BookServiceFragment();
    }

    private void openFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
    public void setBottomNavigationViewSelectedItem(int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        switch (count) {
            case 0:
                // super.onBackPressed(); // If no fragments in the back stack, allow default back behavior
                break;
            case 1:
                // Handle back press for the first fragment (Home or the initial fragment)
                // You can use finish() here to close the activity if desired
                super.onBackPressed();
                break;
            case 2:
                // Handle back press for the second fragment (Statistics)
                getSupportFragmentManager().popBackStack(); // Remove the top fragment from back stack
                break;
            case 3:
                // Handle back press for the third fragment (Stock)
                getSupportFragmentManager().popBackStack(); // Remove the top fragment from back stack
                break;
            case 4:
                // Handle back press for the fourth fragment (Settings)
                getSupportFragmentManager().popBackStack(); // Remove the top fragment from back stack
                break;
            case 5:
                // Handle back press for the fourth fragment (Settings)
                getSupportFragmentManager().popBackStack(); // Remove the top fragment from back stack
                break;
            case 6:
                // Handle back press for the fourth fragment (Settings)
                getSupportFragmentManager().popBackStack(); // Remove the top fragment from back stack
                break;
            case 7:
                // Handle back press for the fourth fragment (Settings)
                getSupportFragmentManager().popBackStack(); // Remove the top fragment from back stack
                break;
            default:
                // If there are more than 4 fragments in the back stack, just pop the top fragment
                getSupportFragmentManager().popBackStack();
                break;
        }
    }



    public void exitdialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((MainActivity.this));
        alertDialogBuilder.setTitle("Logout Confirmation");
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setIcon(R.drawable.wheelz_logo);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.remove("access_token");
                Global.editor.remove("refresh_token");
                Global.editor.commit();


                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
}