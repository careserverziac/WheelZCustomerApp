package com.ziac.wheelzcustomer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import Fragments.ContainerFragment;

public class UploadViewFiles extends Fragment {

    CardView RC_cd,Permit_cd,Insurance_cd,Warranty_cd,Emission_cd;

    ProgressBar progressBar;

    ImageView Backbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document, container, false);

        progressBar = view.findViewById(R.id.progressbarfiles);
        Warranty_cd = view.findViewById(R.id.warranty);
        Emission_cd = view.findViewById(R.id.emission);
        Insurance_cd = view.findViewById(R.id.insurance);
        Permit_cd = view.findViewById(R.id.permit);
        RC_cd = view.findViewById(R.id.rcbook);
        Backbtn = view.findViewById(R.id.backbtn);

        View.OnClickListener documentClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentType = "";
                String documentname = "";

                switch (v.getId()) {
                    case R.id.rcbook:
                        documentType = "R";
                        documentname = "RC Book";
                        break;
                    case R.id.permit:
                        documentType = "P";
                        documentname = "Permit";
                        break;
                    case R.id.insurance:
                        documentType = "I";
                        documentname = "Insurance";
                        break;
                    case R.id.warranty:
                        documentType = "W";
                        documentname = "Warranty";
                        break;
                    case R.id.emission:
                        documentType = "E";
                        documentname = "Emission";
                        break;
                }

                openContainerFragment(documentType,documentname);
            }
        };

        RC_cd.setOnClickListener(documentClickListener);
        Permit_cd.setOnClickListener(documentClickListener);
        Insurance_cd.setOnClickListener(documentClickListener);
        Warranty_cd.setOnClickListener(documentClickListener);
        Emission_cd.setOnClickListener(documentClickListener);

        Backbtn.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        return view;
    }

    private void openContainerFragment(String documentType,String documentname) {
        ContainerFragment containerFragment = new ContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("documentType", documentType);
        bundle.putString("documentName", documentname);
        containerFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, containerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

}


/*private void toggleSubButtons() {
    if (areSubButtonsVisible) {
        hideSubButtonsWithAnimation();
    } else {
        showSubButtonsWithAnimation();
    }
    areSubButtonsVisible = !areSubButtonsVisible;
}*/

/*
private void showSubButtonsWithAnimation() {
    subButtonsContainer.setVisibility(View.VISIBLE);

    // Get all sub-buttons
    View warranty = Warranty.findViewById(R.id.warranty);
    View emission = Emission.findViewById(R.id.emission);
    View insurance = Insurance.findViewById(R.id.insurance);
    View rc = RC.findViewById(R.id.rc);
    View permit = Permit.findViewById(R.id.permit);

    // Initially set all buttons to invisible and scaled down
    View[] buttons = {warranty, emission, insurance, rc, permit};
    for (View button : buttons) {
        button.setAlpha(0f);
        button.setScaleX(0.5f);
        button.setScaleY(0.5f);
    }

    // Animate each button with delay
    for (int i = 0; i < buttons.length; i++) {
        final View button = buttons[i];
        button.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(100 * i)  // 100ms delay between each button
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }
}

private void hideSubButtonsWithAnimation() {
    // Get all sub-buttons
    View warranty = Warranty.findViewById(R.id.warranty);
    View emission = Emission.findViewById(R.id.emission);
    View insurance = Insurance.findViewById(R.id.insurance);
    View rc = RC.findViewById(R.id.rc);
    View permit = Permit.findViewById(R.id.permit);

    View[] buttons = {warranty, emission, insurance, rc, permit};

    // Animate each button with delay (reverse order)
    for (int i = buttons.length - 1; i >= 0; i--) {
        final View button = buttons[i];
        int finalI = i;
        button.animate()
                .alpha(0f)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .setStartDelay(100 * (buttons.length - 1 - i))
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (finalI == 0) {
                            subButtonsContainer.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .start();
    }
}*/
