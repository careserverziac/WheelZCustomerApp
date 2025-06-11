package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ziac.wheelzcustomer.R;
import ModelClasses.Global;

public class SubmitRequest extends Fragment {

    TextView Manufacturer,Modelname;
    ImageView Modelimage;
    String manufacture;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_submit_request, container, false);

        Manufacturer = view.findViewById(R.id.sbmanufacturer);
        Modelname = view.findViewById(R.id.sbmodelname);
        Modelimage = view.findViewById(R.id.veh_image);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Manufacturer.setText(Global.sharedPreferences.getString("manufacturer", ""));
        Modelname.setText(Global.sharedPreferences.getString("modelname", ""));

        return view;
    }
}