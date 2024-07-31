package com.ziac.wheelzcustomer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ModelClasses.AppStatus;
import ModelClasses.Global;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton EPbackbtn,Camera;
    EditText Name,Mobilenumber,Email;
    CircleImageView circularImageView;
    AppCompatButton UpdateProfilebtn;
    Bitmap imageBitmap;
    String image,name,mobile,user_mail;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (AppStatus.getInstance(this).isOnline()) {
            //Toast.makeText(this,"You are online!!!!", Toast.LENGTH_SHORT).show();
        } else {
            Global.customtoast(ProfileActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
        }

        image = Global.userimageurl + Global.sharedPreferences.getString("Image", "");
        circularImageView=findViewById(R.id.profile_images);
        Picasso.Builder builder=new Picasso.Builder(getApplication());
        Picasso picasso=builder.build();
       // picasso.load(Uri.parse(image)).into(circularImageView );
        Global.loadWithPicasso(this, circularImageView, image);



        Name=findViewById(R.id.name);
        Mobilenumber=findViewById(R.id.mobile);
        Email=findViewById(R.id.email);
        UpdateProfilebtn=findViewById(R.id.updateprofile);
        Camera=findViewById(R.id.fab);



         name = Global.sharedPreferences.getString("key_person", "");
         mobile = Global.sharedPreferences.getString("Mobile1", "");
         user_mail = Global.sharedPreferences.getString("Email", "");


       // Picasso.get().load(image).into(ProfileImage);
        Name.setText(name);
        Mobilenumber.setText(mobile);
        Email.setText(user_mail);



      /*  EPbackbtn.setOnClickListener(view -> {

           // startActivity(new Intent(new Intent(ProfileActivity.this,ProfileActivity.class)));
            finish();
        });*/




        UpdateProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(ProfileActivity.this).isOnline()) {
                    Updateprofiledetails();
                } else {
                    Global.customtoast(ProfileActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
                }
            }
        });

        Camera = findViewById(R.id.fab);
        Camera.setOnClickListener(v -> openCamera());
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(ProfileActivity.this).isOnline()) {
                    openCamera();
                } else {
                    Global.customtoast(ProfileActivity.this,getLayoutInflater(),"Connected WIFI or Mobile data has no internet access!!");
                }
            }
        });




        circularImageView.setOnClickListener(v -> {
            image = Global.userimageurl + Global.sharedPreferences.getString("Image", "");
            showImage(picasso,image);

        });

    }


    private void Updateprofiledetails() {

        String personname,mobile,email;

        personname = Name.getText().toString();
        mobile = Mobilenumber.getText().toString();
        email = Email.getText().toString();


        if (personname.isEmpty()) {

            Toast.makeText(ProfileActivity.this, "Person name should not be empty!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Mobile number should not be empty !!", Toast.LENGTH_SHORT).show();

            return;
        }
        if (mobile.length() < 10) {
            Toast.makeText(ProfileActivity.this, "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();

            return;
        }if (email.length() < 10) {
            Toast.makeText(ProfileActivity.this, "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();

            return;
        }



        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.urlupdateprofile,
                sresponse -> {

                    // progressBar.setVisibility(View.GONE);
                    JSONObject response = null;
                    try {
                        response = new JSONObject(sresponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("key_person", Name.getText().toString());
                    Global.editor.putString("Mobile1", Mobilenumber.getText().toString());
                    Global.editor.putString("Email", Email.getText().toString());
                    Global.editor.commit();



                    try {
                        if (response.getBoolean("isSuccess")) {
//                                Toast.makeText(ProfileActivity.this, "Updated successfully !!", Toast.LENGTH_SHORT).show();
                            Global.customtoast(ProfileActivity.this,getLayoutInflater(),"Updated successfully !!");
                            finish();
                            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(intent);

                        } else {
                            //textViewError.setText(response.getString("error"));
                            Toast.makeText(ProfileActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                            //textViewError.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, error -> {

                    //  progressBar.setVisibility(View.GONE);
                    //  Toast.makeText(ProfileActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    /*textViewError.setText(error.getLocalizedMessage());
                    textViewError.setVisibility(View.VISIBLE);*/

                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("key_person",personname);
                params.put("username", Global.sharedPreferences.getString("userName",""));
                params.put("wuser_mobile1",mobile);
                params.put("wuser_mobile2", Mobilenumber.getText().toString());
                params.put("wuser_email",email);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);


    }

    private void openCamera() {
        // Check if the CAMERA permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the CAMERA permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            // Permission is already granted, start the camera intent
            startCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // CAMERA permission granted, start the camera intent
                startCameraIntent();
            } else {
                // CAMERA permission denied, handle accordingly (e.g., show a message to the user)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCameraIntent() {
        ImagePicker.with(ProfileActivity.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(10);


    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            //imageList.add(uri);
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Uploadselectedimage();
        }
    }

    private void Uploadselectedimage() {

        if (imageBitmap == null) {return;}

        String url = Global.urlUpdateprofileImage;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            JSONObject resp;
            try {
                resp = new JSONObject(response);

                System.out.println(resp);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            try {
                if (resp.getBoolean("success")) {
                    String Message = resp.getString("message");
                    String uploadimage= resp.getString("data");

                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("Image", uploadimage);
                    Global.editor.commit();

                    String image = Global.userimageurl +uploadimage;
                    /*Picasso.get().load(image).into(circularImageView);*/
                    Global.loadWithPicasso(this, circularImageView, image);
                    Global.customtoast(ProfileActivity.this, getLayoutInflater(),Message);

                } else {
                    if (resp.has("error")) {

                        String errorMessage = resp.getString("error");
                        Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                       // Toast.makeText(ProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();


                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }


        }, error -> {
            //  progressBar.setVisibility(View.GONE);

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }


            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String image = imageToString(imageBitmap);

                params.put("fileName",image);
                 Log.d("YourTag", "File Name: " + params.get("fileName"));

                // params.put("vehmas_code",Global.selectedvstock.getStockItemId());


                return params;
            }
        };

        // Add the stringRequest to the requestQueue to send the data to the server
        requestQueue.add(stringRequest);
    }

    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }



    public void showImage(Picasso picasso, String userimage) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(dialogInterface -> {
            // Nothing
        });

        // Calculate display dimensions
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Load the image using Picasso
        picasso.load(Uri.parse(userimage)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageView imageView = new ImageView(getApplicationContext());

                // Calculate dimensions to fit the image within the screen
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                float aspectRatio = (float) imageWidth / imageHeight;

                int newWidth = screenWidth;
                int newHeight = (int) (screenWidth / aspectRatio);
                if (newHeight > screenHeight) {
                    newHeight = screenHeight;
                    newWidth = (int) (screenHeight * aspectRatio);
                }

                // Add padding values
                int paddingInDp = 16; // You can adjust the padding as per your requirement
                int paddingInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingInDp, ProfileActivity.this.getResources().getDisplayMetrics());

                // Adjust the newWidth and newHeight with padding
                newWidth -= 2 * paddingInPx; // Subtract padding from both sides
                newHeight -= 2 * paddingInPx; // Subtract padding from both top and bottom

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
                layoutParams.setMargins(paddingInPx, paddingInPx, paddingInPx, paddingInPx); // Set padding
                imageView.setLayoutParams(layoutParams);

                imageView.setImageBitmap(bitmap);

                builder.addContentView(imageView, layoutParams);
                builder.show();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle bitmap loading failure
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Prepare bitmap loading
            }
        });
    }


    @Override
    public void onBackPressed() {

        finish();

        super.onBackPressed();
    }


}