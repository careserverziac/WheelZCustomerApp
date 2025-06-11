package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ziac.wheelzcustomer.R;
import ModelClasses.Global;

public class VehicleDetailFragment extends Fragment {


    ImageView Veh_image;
    TextView Topspeed,Fuelname,Bodytype,Bhp,Category,Manufacturer,Modelname,Saleprice,Chargingtime;



    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_vehicle_detail, container, false);

        Veh_image=view.findViewById(R.id.veh_imageindetail);
        Topspeed=view.findViewById(R.id.topspeed);
        Fuelname=view.findViewById(R.id.fuelname);
        Bodytype=view.findViewById(R.id.bodytype);
        Bhp=view.findViewById(R.id.bhp);
        Category=view.findViewById(R.id.category);
        Manufacturer=view.findViewById(R.id.manufacturer);
        Modelname=view.findViewById(R.id.modelname);
        Saleprice=view.findViewById(R.id.saleprice);
        Chargingtime=view.findViewById(R.id.chargingtime);

        Global.loadWithPicasso(requireActivity(), Veh_image, Global.modelsimageurl + Global.modellist.getImage_path());


        Category.setText(Global.modellist.getCategory());
        Fuelname.setText(Global.modellist.getFuelname());
        Bodytype.setText(Global.modellist.getBodytype());
        Bhp.setText(Global.modellist.getBhp());
        Topspeed.setText(Global.modellist.getTopspeed());
        Manufacturer.setText(Global.modellist.getManufacture());
        Modelname.setText(Global.modellist.getModel_name());
        Saleprice.setText(Global.modellist.getSaleprice());
        Chargingtime.setText(Global.modellist.getChargingtime());

        return view;
    }
}