package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.ziac.wheelzcustomer.R;
import ModelClasses.Global;

public class VehicleDetailFragment extends Fragment {


    ImageView Veh_image;
    ImageButton Btn_back;
    TextView Myear,VModelname,Modelcc,Topspeed,Fuelname,Bodytype,Bhp,Category,Manufacturer,Modelname,Saleprice,Chargingtime;



    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_vehicle_detail, container, false);

        Btn_back=view.findViewById(R.id.btn_back);
        Myear=view.findViewById(R.id.myear);
        Veh_image=view.findViewById(R.id.veh_imageindetail);
        Topspeed=view.findViewById(R.id.topspeed);
        Fuelname=view.findViewById(R.id.fuelname);
        Bodytype=view.findViewById(R.id.bodytype);
        Bhp=view.findViewById(R.id.bhp);
        Category=view.findViewById(R.id.category);
        Manufacturer=view.findViewById(R.id.manufacturer);
        Modelname=view.findViewById(R.id.modelname);
        VModelname=view.findViewById(R.id.vehicle_name);
        Saleprice=view.findViewById(R.id.saleprice);
        Chargingtime=view.findViewById(R.id.chargingtime);
        Modelcc=view.findViewById(R.id.modelcc);

        Global.loadWithPicasso(requireActivity(), Veh_image, Global.modelsimageurl + Global.vehicledetails.getImage_path());



        Category.setText(Global.vehicledetails.getCategory());
        Myear.setText(Global.vehicledetails.getMaf_date());
        Fuelname.setText(Global.vehicledetails.getFuelname());
        Bodytype.setText(Global.vehicledetails.getBodytype());
        Bhp.setText(Global.vehicledetails.getBhp());
        Topspeed.setText(Global.vehicledetails.getTopspeed());
        Manufacturer.setText(Global.vehicledetails.getManufacture());
        Modelname.setText(Global.vehicledetails.getModel_name());
        Saleprice.setText(Global.vehicledetails.getSaleprice());
        Chargingtime.setText(Global.vehicledetails.getChargingtime());
        Modelcc.setText(Global.vehicledetails.getCc());
        VModelname.setText(Global.vehicledetails.getModel_name());


        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, new ModelBlankFragment()) // replace with your fragment class
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}