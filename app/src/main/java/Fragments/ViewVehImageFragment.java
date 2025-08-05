package Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.ziac.wheelzcustomer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ModelClasses.Global;


public class ViewVehImageFragment extends Fragment {



    private static final String FRONT = "F";
    private static final String BACK = "B";
    private static final String LEFT = "L";
    private static final String RIGHT = "R";
    private static final String INTERIOR = "I";

    // UI elements
   // private LinearLayout frontUploadBtn, backUploadBtn, leftUploadBtn, rightUploadBtn, interiorUploadBtn;
    private ImageView frontImage, backImage, leftImage, rightImage, interiorImage;
    private LinearLayout frontview, backview, leftview, rightview, interiorview;
    private TextView frontStatus, progressText;
    private ProgressBar uploadProgress;
    private CardView continueBtnCard;

    // State tracking
    private String currentUploadType;
    private int uploadedCount = 0;
    private Map<String, Boolean> uploadStatus = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_veh_image, container, false);

        // Initialize views
        initializeViews(view);

        // Set click listeners for all upload buttons
        setUploadButtonListeners();

        // Initialize upload status
        initializeUploadStatus();

        getvehimages();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Now it's safe to update UI as views are created
       // updateUI();
    }

    private void initializeViews(View view) {
       /* frontUploadBtn = view.findViewById(R.id.front_upload_btn);
        backUploadBtn = view.findViewById(R.id.back_upload_btn);
        leftUploadBtn = view.findViewById(R.id.left_upload_btn);
        rightUploadBtn = view.findViewById(R.id.right_upload_btn);
        interiorUploadBtn = view.findViewById(R.id.interior_upload_btn);*/


        frontview = view.findViewById(R.id.front_view);
        backview = view.findViewById(R.id.back_view);
        leftview = view.findViewById(R.id.left_view);
        rightview = view.findViewById(R.id.right_view);
        interiorview = view.findViewById(R.id.interior_view);

        frontImage = view.findViewById(R.id.front_image);
        backImage = view.findViewById(R.id.back_image);
        leftImage = view.findViewById(R.id.left_image);
        rightImage = view.findViewById(R.id.right_image);
        interiorImage = view.findViewById(R.id.interior_image);

       /* //frontStatus = view.findViewById(R.id.front_status);
        progressText = view.findViewById(R.id.progress_text);
        uploadProgress = view.findViewById(R.id.upload_progress);
        continueBtnCard = view.findViewById(R.id.continue_btn_card);*/

        // Set continue button click listener
       /* continueBtnCard.setOnClickListener(v -> {
            if (uploadedCount == 5) {
                // All images uploaded, proceed to next step
                Toast.makeText(requireContext(), "All images uploaded successfully!", Toast.LENGTH_SHORT).show();
                // Add navigation logic here
            } else {
                Toast.makeText(requireContext(), "Please upload all required images first", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void setUploadButtonListeners() {
       /* frontUploadBtn.setOnClickListener(v -> {
            currentUploadType = FRONT;
            openImagePicker();
        });

        backUploadBtn.setOnClickListener(v -> {
            currentUploadType = BACK;
            openImagePicker();
        });

        leftUploadBtn.setOnClickListener(v -> {
            currentUploadType = LEFT;
            openImagePicker();
        });

        rightUploadBtn.setOnClickListener(v -> {
            currentUploadType = RIGHT;
            openImagePicker();
        });

        interiorUploadBtn.setOnClickListener(v -> {
            currentUploadType = INTERIOR;
            openImagePicker();
        });*/


        frontview.setOnClickListener(v -> {
            currentUploadType = BACK;
            Upload_View_ImagesFragment fragment = new Upload_View_ImagesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("img_type", "F");
            fragment.setArguments(bundle);
            openFragment(fragment);
        });

        backview.setOnClickListener(v -> {
            currentUploadType = LEFT;
            Upload_View_ImagesFragment fragment = new Upload_View_ImagesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("img_type", "B");
            fragment.setArguments(bundle);
            openFragment(fragment);
        });

        leftview.setOnClickListener(v -> {
            currentUploadType = RIGHT;
            Upload_View_ImagesFragment fragment = new Upload_View_ImagesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("img_type", "L");
            fragment.setArguments(bundle);
            openFragment(fragment);
        });

        rightview.setOnClickListener(v -> {
            currentUploadType = INTERIOR;
            Upload_View_ImagesFragment fragment = new Upload_View_ImagesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("img_type", "R");
            fragment.setArguments(bundle);
            openFragment(fragment);
        });

        interiorview.setOnClickListener(v -> {
            currentUploadType = INTERIOR;
            Upload_View_ImagesFragment fragment = new Upload_View_ImagesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("img_type", "I");
            fragment.setArguments(bundle);
            openFragment(fragment);
        });
    }

    private void initializeUploadStatus() {
        uploadStatus.put(FRONT, false);
        uploadStatus.put(BACK, false);
        uploadStatus.put(LEFT, false);
        uploadStatus.put(RIGHT, false);
        uploadStatus.put(INTERIOR, false);
    }



   /* private void updateUI() {
        // Update progress text and bar
        int progressPercentage = (int) ((uploadedCount / 5.0) * 100);
        progressText.setText(uploadedCount + "/5 (" + progressPercentage + "%)");
        uploadProgress.setProgress(uploadedCount);

        // Update front status
        *//*if (uploadStatus.get(FRONT)) {
            frontStatus.setText("UPLOADED");
            frontStatus.setTextColor(Color.GREEN);
            frontStatus.setBackgroundColor(Color.parseColor("#3333FF33"));
        }*//*

        // Update indicator dots for other images
       // updateIndicator(R.id.back_indicator, uploadStatus.get(BACK));
       // updateIndicator(R.id.left_indicator, uploadStatus.get(LEFT));
       // updateIndicator(R.id.right_indicator, uploadStatus.get(RIGHT));
        //updateIndicator(R.id.interior_indicator, uploadStatus.get(INTERIOR));

        // Enable/disable continue button
        continueBtnCard.setEnabled(uploadedCount == 5);
        continueBtnCard.setCardBackgroundColor(uploadedCount == 5 ?
                Color.parseColor("#FFD700") : Color.parseColor("#808080"));
    }*/

    private void updateIndicator(int viewId, boolean isUploaded) {
        View indicator = requireView().findViewById(viewId);
        if (isUploaded) {
            indicator.setBackgroundColor(Color.GREEN);
        } else {
            indicator.setBackgroundColor(Color.RED);
        }
    }




    private void getvehimages() {

        String url = Global.getvehimages + "vehmas_code=" + Global.commonVehClass.getVehmas_code();
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject respObj1 = new JSONObject(response);
                boolean isSuccess = respObj1.getBoolean("isSuccess");

                if (isSuccess) {
                    JSONArray dataArray = respObj1.getJSONArray("data");

                    if (dataArray.length() > 0) {
                        JSONObject respObj = dataArray.getJSONObject(0);

                        String right_image = respObj.getString("right_image");
                        String left_image = respObj.getString("left_image");
                        String front_image = respObj.getString("front_image");
                        String back_image = respObj.getString("back_image");
                        String interior_image = respObj.getString("interior_image");

                        String img=Global.vehimageurl + front_image;
                        Global.loadWithPicasso(requireActivity(), frontImage, img);
                        Global.loadWithPicasso(requireActivity(), backImage, Global.vehimageurl + back_image);
                        Global.loadWithPicasso(requireActivity(), leftImage, Global.vehimageurl + left_image);
                        Global.loadWithPicasso(requireActivity(), rightImage, Global.vehimageurl + right_image);
                        Global.loadWithPicasso(requireActivity(), interiorImage, Global.vehimageurl + interior_image);

                        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("right_image", right_image);
                        Global.editor.putString("left_image", left_image);
                        Global.editor.putString("front_image", front_image);
                        Global.editor.putString("back_image", back_image);
                        Global.editor.putString("interior_image", interior_image);
                        Global.editor.commit();

                    }

                } else {
                    String errorMessage = respObj1.optString("error", "Something went wrong");
                    Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(requireActivity(), "Parsing error occurred", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            if (error instanceof TimeoutError) {
                Toast.makeText(requireActivity(), "Request Time-Out", Toast.LENGTH_LONG).show();
            } else if (error instanceof NoConnectionError) {
                Toast.makeText(requireActivity(), "Internet connection unavailable", Toast.LENGTH_LONG).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(requireActivity(), "Server Error", Toast.LENGTH_LONG).show();
            } else if (error instanceof NetworkError) {
                Toast.makeText(requireActivity(), "Network Error", Toast.LENGTH_LONG).show();
            } else if (error instanceof ParseError) {
                Toast.makeText(requireActivity(), "Parse Error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}