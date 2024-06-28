package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziac.wheelzcustomer.MainActivty;
import com.ziac.wheelzcustomer.R;

public class DashboardFragment extends Fragment {

    CardView Bookservice,Servicehistory,Vehdocuments,latestnews;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_dashboard, container, false);

        Bookservice = view.findViewById(R.id.servicebookCD);
        Servicehistory = view.findViewById(R.id.servicehistoryCD);
        Vehdocuments = view.findViewById(R.id.vehdocumentsCD);
        latestnews = view.findViewById(R.id.latestnewsCD);



        Bookservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DealersFragment dealersFragment=new DealersFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, dealersFragment);
                fragmentTransaction.commit();

                ((MainActivty) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_dealers);
            }
        });

        Servicehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyVehcileFragment myVehcileFragment=new MyVehcileFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
                fragmentTransaction.commit();

                ((MainActivty) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_vehicles);

            }
        });

        Vehdocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVehcileFragment myVehcileFragment=new MyVehcileFragment();
                fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, myVehcileFragment);
                fragmentTransaction.commit();

                ((MainActivty) requireActivity()).setBottomNavigationViewSelectedItem(R.id.bottom_vehicles);
            }
        });


        return view;

    }
}