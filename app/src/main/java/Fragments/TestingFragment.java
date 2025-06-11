package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ziac.wheelzcustomer.R;


public class TestingFragment extends Fragment {

    LinearLayout Registervehicle;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_testing, container, false);



        Registervehicle =view.findViewById(R.id.registervehicle);
        Registervehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterVehicleFragment registerVehicleFragment =new RegisterVehicleFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, registerVehicleFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return view;
    }
}