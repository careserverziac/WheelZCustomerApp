package Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziac.wheelzcustomer.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ModelClasses.Global;


public class ScheduledFragment extends Fragment {

    LinearLayout SelectDt, SelectTm, Proceed;
    TextView SelectedDate, Selectedtime, Name, Mobile, Email;
    String sqldateformat, selectedTime24, name, mobile, mail,manufacture,modelname,modelimage;
    String com_name,com_city,com_website,com_adress;

    ViewPager viewPager;

    @SuppressLint("MissingInflatedId")
    public ScheduledFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduled, container, false);

        SelectDt = view.findViewById(R.id.selectdate);
        SelectTm = view.findViewById(R.id.selecttime);
        SelectedDate = view.findViewById(R.id.selecteddate);
        Selectedtime = view.findViewById(R.id.selectedtime);
        Name = view.findViewById(R.id.tdname);
        Mobile = view.findViewById(R.id.tdmobile);
        Email = view.findViewById(R.id.tdmail);
        Proceed = view.findViewById(R.id.proceedbtn);



        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name = Global.sharedPreferences.getString("key_person", "");
        mobile = Global.sharedPreferences.getString("Mobile1", "");
        mail = Global.sharedPreferences.getString("Email", "");
        Name.setText(name);
        Mobile.setText(mobile);
        Email.setText(mail);



        manufacture=Global.vehicledetails.getManufacture();
        modelname=Global.vehicledetails.getModel_name();
        modelimage=Global.vehicledetails.getImage_path();

        com_name=Global.vehicledetails.getCom_name();
        com_city=Global.vehicledetails.getCity_name();
        com_website=Global.vehicledetails.getCom_website();
        com_adress=Global.vehicledetails.getCom_address();

        Global.editor = Global.sharedPreferences.edit();
        Global.editor.putString("manufacturer",manufacture);
        Global.editor.putString("modelname",modelname);
        Global.editor.putString("modelimage",modelimage);
        Global.editor.commit();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMMM yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat time24Format = new SimpleDateFormat("HH:mm", Locale.getDefault()); // 24-hour format

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());
        SelectedDate.setText(currentDate);
        Selectedtime.setText(currentTime);


        selectedTime24 = time24Format.format(calendar.getTime());
        sqldateformat = sqlDateFormat.format(calendar.getTime());

        SelectDt.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        String selectedDateString = dateFormat.format(selectedDate.getTime());
                        SelectedDate.setText(selectedDateString);


                        sqldateformat = String.format("%04d %02d %02d",
                                selectedDate.get(Calendar.YEAR),
                                selectedDate.get(Calendar.MONTH) + 1,
                                selectedDate.get(Calendar.DAY_OF_MONTH));

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        SelectTm.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm", Locale.getDefault());

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (view12, hourOfDay, minute) -> {
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);
                        String selectedTimeString12 = timeFormat12.format(selectedTime.getTime());
                        String selectedTimeString24 = timeFormat24.format(selectedTime.getTime());

                        Selectedtime.setText(selectedTimeString12);
                        selectedTime24 = selectedTimeString24;

                    }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });


        Proceed.setOnClickListener(v -> {
/*
            Global.editor = Global.sharedPreferences.edit();
            Global.editor.putString("manufacturer",manufacture);
            Global.editor.putString("modelname",modelname);
            Global.editor.putString("modelimage",modelimage);
            Global.editor.commit();*/

            if (viewPager != null) {
                viewPager.setCurrentItem(2); // Index of SubmitRequest Fragment
            }
        });

        return view;
    }
}