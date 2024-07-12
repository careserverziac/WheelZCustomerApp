package AdapterClass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ziac.wheelzcustomer.GoogleMapActivity;
import com.ziac.wheelzcustomer.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import Fragments.BookServiceFragment;
import Fragments.VehicleDetailFragment;
import ModelClasses.CommonClass;
import ModelClasses.Global;

public class DealersAdapter extends RecyclerView.Adapter<DealersAdapter.ViewHolder> {

    private final List<CommonClass> dealerList;
    private final Context context;
    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;
    private String currentLocationString;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    public DealersAdapter(List<CommonClass> dealerList, Context context) {
        this.context = context;
        this.dealerList = dealerList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealerlist_layout, parent, false);

        DealersAdapter.ViewHolder viewHolder = new DealersAdapter.ViewHolder(view);


        return viewHolder;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Global.loadWithPicasso(context, holder.Dealer_image, Global.companyimageurl + dealerList.get(position).getImage_path());

        holder.Comyname.setText(dealerList.get(position).getCom_name());
        holder.ComCity.setText(dealerList.get(position).getCity_name());
       // holder.ComMobile.setText(dealerList.get(position).getCom_phone());
       // holder.ComMail.setText(dealerList.get(position).getCom_email());
        holder.Comwebsite.setText(dealerList.get(position).getCom_website());
        holder.ComAddress.setText(dealerList.get(position).getCom_address());
        holder.DealerName.setText(dealerList.get(position).getCom_contact());
        holder.Dealermobno.setText(dealerList.get(position).getCom_contact_mobno());
        holder.Dealeremail.setText(dealerList.get(position).getCom_contact_email());


        holder.Dealermobno.setOnClickListener(v -> {
            String phoneNumber = dealerList.get(position).getCom_phone();
            initiatePhoneCall(phoneNumber);
        });
        holder.DealerPhone.setOnClickListener(v -> {
            String phoneNumber = dealerList.get(position).getCom_contact_mobno();
            initiatePhoneCall(phoneNumber);
        });
        holder.Dealeremail.setOnClickListener(v -> {
            String mailid = dealerList.get(position).getCom_email();
            sendEmail(mailid);

        });
        holder.Comwebsite.setOnClickListener(v -> {
            String website = dealerList.get(position).getCom_website();
            openWebsite(website);
        });

       /* holder.Bookaservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View view1 = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dailog, null);
                bottomSheetDialog.setContentView(view1);

                // Initialize the CalendarView and TimePicker
                CalendarView calendarView = view1.findViewById(R.id.calendarView);
                TimePicker timePicker = view1.findViewById(R.id.timePicker);
                TextView selectedDateTime = view1.findViewById(R.id.selecteddatetime);
                AppCompatButton confirmBooking = view1.findViewById(R.id.confirmbooking);

                // Set up a listener to capture the selected date from the CalendarView
                final int[] selectedYear = new int[1];
                final int[] selectedMonth = new int[1];
                final int[] selectedDay = new int[1];
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        selectedYear[0] = year;
                        selectedMonth[0] = month + 1; // Month is 0-indexed
                        selectedDay[0] = dayOfMonth;
                        selectedDateTime.setText("Selected date: " + selectedDay[0] + "/" + selectedMonth[0] + "/" + selectedYear[0]);
                    }
                });

                // Set up the TimePicker to use 24-hour format
                timePicker.setIs24HourView(true);

                confirmBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        String selectedTime = hour + ":" + minute;
                        String selectedDate = selectedDay[0] + "/" + selectedMonth[0] + "/" + selectedYear[0];

                        // Send the selected date and time to the server
                        sendDateTimeToServer(selectedDate, selectedTime);
                        bottomSheetDialog.cancel();;
                    }
                });

                bottomSheetDialog.show();
            }
        });*/


        holder.Bookaservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.dealersdetails = dealerList.get(position);

                BookServiceFragment bookServiceFragment = new BookServiceFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, bookServiceFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    private void sendDateTimeToServer(String selectedDate, String selectedTime) {

        Toast.makeText(context, selectedDate+" "+selectedTime, Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return dealerList.size();
    }





    private void initiatePhoneCall(String phoneNumber) {
        // Check if the device has the required permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request the CALL_PHONE permission
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 123);
            return;
        } else {
            // Permission already granted, initiate the phone call
            makePhoneCall(phoneNumber);
        }
    }

    private void makePhoneCall(String phoneNumber) {
        // Create an Intent to initiate a phone call
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        // Check if there is an activity that can handle this intent
        if (callIntent.resolveActivity(context.getPackageManager()) != null) {
            // Start the phone call
            context.startActivity(callIntent);
        } else {
            Toast.makeText(context, "No app can handle this action", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(String mailId) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + mailId));
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

    private void openWebsite(String websiteUrl) {
        // Check if the website URL is not empty
        if (!TextUtils.isEmpty(websiteUrl)) {
            // Create an Intent to open the website
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
            websiteIntent.setData(Uri.parse(websiteUrl));

            // Check if there is an activity that can handle this intent
            if (websiteIntent.resolveActivity(context.getPackageManager()) != null) {
                // Start the activity to open the website
                context.startActivity(websiteIntent);
            } else {
                Toast.makeText(context, "No app can handle this action", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Website URL is not available", Toast.LENGTH_SHORT).show();
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Dealer_image;
        TextView Comyname,ComCity,ComMobile,ComMail,ComAddress,Bookaservice;
        TextView DealerName,Dealermobno,Dealeremail,Comwebsite;
        LinearLayout Company_phone,DealerPhone;
        BottomSheetDialog bottomSheetDialog;
       // CardView Modelscardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Dealer_image=itemView.findViewById(R.id.dealer_image);
            Comyname=itemView.findViewById(R.id.com_name);
            ComCity=itemView.findViewById(R.id.com_city);
            ComAddress=itemView.findViewById(R.id.com_address);
            DealerName=itemView.findViewById(R.id.dealername);
            Dealermobno=itemView.findViewById(R.id.dealer_mobno);
            Dealeremail=itemView.findViewById(R.id.dealer_email);
            DealerPhone=itemView.findViewById(R.id.dealerphone);
            Comwebsite=itemView.findViewById(R.id.com_website);
            Bookaservice=itemView.findViewById(R.id.bookaservice);



            // CC=itemView.findViewById(R.id.veh_cc);

          /*  Category=itemView.findViewById(R.id.veh_category);
            BHP=itemView.findViewById(R.id.veh_bhp);*/
        }
    }

}
