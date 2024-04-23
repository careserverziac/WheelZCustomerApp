package AdapterClass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.wheelzonline.R;

import java.util.List;

import Fragments.VehicleDetailFragment;
import ModelClasses.CommonClass;
import ModelClasses.Global;

public class DealersAdapter extends RecyclerView.Adapter<DealersAdapter.ViewHolder> {

    private final List<CommonClass> dealerList;
    private final Context context;
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
        holder.ComMobile.setText(dealerList.get(position).getCom_phone());
        holder.ComMail.setText(dealerList.get(position).getCom_email());
        holder.Comwebsite.setText(dealerList.get(position).getCom_website());
        holder.ComAddress.setText(dealerList.get(position).getCom_address());
        holder.DealerName.setText(dealerList.get(position).getCom_contact());
        holder.Dealermobno.setText(dealerList.get(position).getCom_contact_mobno());
        holder.Dealeremail.setText(dealerList.get(position).getCom_contact_email());


        holder.Company_phone.setOnClickListener(v -> {
            String phoneNumber = dealerList.get(position).getCom_phone();
            initiatePhoneCall(phoneNumber);
        });
        holder.DealerPhone.setOnClickListener(v -> {
            String phoneNumber = dealerList.get(position).getCom_contact_mobno();
            initiatePhoneCall(phoneNumber);
        });
        holder.ComMail.setOnClickListener(v -> {
            String mailid = dealerList.get(position).getCom_email();
            sendEmail(mailid);

        });
        holder.Comwebsite.setOnClickListener(v -> {
            String website = dealerList.get(position).getCom_website();
            openWebsite(website);
        });




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
        TextView Comyname,ComCity,ComMobile,ComMail,ComAddress;
        TextView DealerName,Dealermobno,Dealeremail,Comwebsite;
        LinearLayout Company_phone,DealerPhone;
       // CardView Modelscardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Dealer_image=itemView.findViewById(R.id.dealer_image);
            Comyname=itemView.findViewById(R.id.com_name);
            ComCity=itemView.findViewById(R.id.com_city);
            ComMobile=itemView.findViewById(R.id.com_phone);
            ComMail=itemView.findViewById(R.id.com_email);
            ComAddress=itemView.findViewById(R.id.com_address);
            DealerName=itemView.findViewById(R.id.dealername);
            Dealermobno=itemView.findViewById(R.id.dealer_mobno);
            Dealeremail=itemView.findViewById(R.id.dealer_email);
            Company_phone=itemView.findViewById(R.id.company_phone);
            DealerPhone=itemView.findViewById(R.id.dealerphone);
            Comwebsite=itemView.findViewById(R.id.com_website);


           // Modelscardview=itemView.findViewById(R.id.modelscardview);


            // CC=itemView.findViewById(R.id.veh_cc);

          /*  Category=itemView.findViewById(R.id.veh_category);
            BHP=itemView.findViewById(R.id.veh_bhp);*/
        }
    }

}
