package com.ziac.wheelzonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import Fragments.BlankFragment;
import Fragments.DemoFragment;
import Fragments.FinalFragment;
import Fragments.DashboardFragment;
import Fragments.TestFragment;

public class MainActivty extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    ImageView Toolbaricon;
    FragmentManager fragmentManager;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        frameLayout = findViewById(R.id.framelayout);
        Toolbaricon = findViewById(R.id.profileicon);


        openFragment(new DashboardFragment());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.red));
        toggle.syncState();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();

                if (itemid == R.id.bottom_dashboard) {
                    openFragment(new DashboardFragment());
                    return true;
                } else if (itemid == R.id.bottom_crm) {
                    openFragment(new FinalFragment());
                    return true;
                } else if (itemid == R.id.bottom_masters) {
                    openFragment(new BlankFragment());
                    return true;
                } else if (itemid == R.id.bottom_vehicles) {
                    openFragment(new DemoFragment());
                    return true;
                } else if (itemid == R.id.bottom_worskshop) {
                    openFragment(new TestFragment());
                    return true;
                }

                return false;
            }
        });


        Toolbaricon.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainActivty.this, v);
            popup.getMenuInflater().inflate(R.menu.menu_profile, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_profiles) {
                    startActivity(new Intent(MainActivty.this, ProfileActivity.class));
                    return true;
                } else if (itemId == R.id.menu_settings) {

                    openFragment(new FinalFragment());
                    return true;

                } else if (itemId == R.id.menu_log) {
                    exitdialog();
                    return true;
                }
                return false;
            });
            popup.show();
        });

    }

    private void openFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemIDd = item.getItemId();

        if (itemIDd == R.id.nav_companydtls) {

            openFragment(new DashboardFragment());

        }
        else if (itemIDd == R.id.nav_aboutus) {

            startActivity(new Intent(MainActivty.this, SignupActivity.class));

        } else if (itemIDd == R.id.nav_share) {

            //ShareIntent();

        } else if (itemIDd == R.id.nav_logout) {

           // exitdialog();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //  Get the total number of fragments in the back stack
        int count = getSupportFragmentManager().getBackStackEntryCount();

        // Handle the back navigation based on the fragment count
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((MainActivty.this));
        alertDialogBuilder.setTitle("Logout Confirmation");
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setIcon(R.drawable.wheelz_logo);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivty.this, LoginActivity.class));
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