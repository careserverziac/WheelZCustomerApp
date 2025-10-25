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
import android.widget.Button;
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

    Context context;
    FragmentManager fragmentManager;
    private List<CommonClass> originalList;  // Original unfiltered list

    public DealersAdapter(List<CommonClass> dealerList, Context context) {
        this.context = context;
        this.originalList = dealerList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealerlist_layout2, parent, false);

        DealersAdapter.ViewHolder viewHolder = new DealersAdapter.ViewHolder(view);
        return viewHolder;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CommonClass dealerList = originalList.get(position);  // Use filtered list here


        Global.loadWithPicasso(context, holder.Dealer_image, Global.companyimageurl +
                dealerList.getImage_path());

        holder.Comyname.setText(dealerList.getCom_name());
        holder.State.setText(dealerList.getState_name());
        holder.ComCity.setText(dealerList.getCity_name());
        holder.Comwebsite.setText(dealerList.getCom_website());
        holder.ComAddress.setText(dealerList.getCom_address());
        holder.DealerName.setText(dealerList.getCom_contact());
        holder.Dealermobno.setText(dealerList.getCom_contact_mobno());
        holder.Dealeremail.setText(dealerList.getCom_contact_email());


        holder.Dealermobno.setOnClickListener(v -> {
            String phoneNumber = dealerList.getCom_phone();
            initiatePhoneCall(phoneNumber);
        });
        holder.DealerPhone.setOnClickListener(v -> {
            String phoneNumber = dealerList.getCom_contact_mobno();
            initiatePhoneCall(phoneNumber);
        });
        holder.Dealeremail.setOnClickListener(v -> {
            String mailid = dealerList.getCom_email();
            sendEmail(mailid);

        });
        holder.Comwebsite.setOnClickListener(v -> {
            String website = dealerList.getCom_website();
            openWebsite(website);
        });

        holder.Bookaservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.dealersdetails = dealerList;

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
        return originalList.size();
    }


    private void initiatePhoneCall(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 123);
            return;
        } else {
            makePhoneCall(phoneNumber);
        }
    }

    private void makePhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (callIntent.resolveActivity(context.getPackageManager()) != null) {
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
        if (!TextUtils.isEmpty(websiteUrl)) {
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
            websiteIntent.setData(Uri.parse(websiteUrl));

            if (websiteIntent.resolveActivity(context.getPackageManager()) != null) {
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
        TextView Comyname,ComCity,ComAddress;
        TextView DealerName,Dealermobno,Dealeremail,Comwebsite,State;
        Button Bookaservice;
        LinearLayout DealerPhone;

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
            State=itemView.findViewById(R.id.state);

        }
    }

}
