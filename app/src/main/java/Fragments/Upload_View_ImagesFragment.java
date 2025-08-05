package Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.ziac.wheelzcustomer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ModelClasses.CommonClass;
import ModelClasses.Global;
import ModelClasses.ModelsClass;

public class Upload_View_ImagesFragment extends Fragment {


    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;
    private String currentUploadType;
    RecyclerView ImageListRV;
    TextView Image_type;
    String image_type,vehmas_code;
    VehimgAdapter  vehimageAdapter;
    CommonClass commonClass;
    LinearLayout Upload;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          View view= inflater.inflate(R.layout.fragment_upload_view_image, container, false);

        ImageListRV = view.findViewById(R.id.imagelistRV);
        Image_type = view.findViewById(R.id.image_type);
        Upload = view.findViewById(R.id.upload);

        vehmas_code = Global.commonVehClass.getVehmas_code();
        if (getArguments() != null) {
            image_type = getArguments().getString("img_type");
        }
        switch (image_type){
            case "F":
                Image_type.setText("Front View Images");
                break;
            case "B":
                Image_type.setText("Back View Images");
                break;
            case "L":
                Image_type.setText("Left View Images");
                break;
            case "R":
                Image_type.setText("Right View Images");
                break;
            case "I":
                Image_type.setText("Interior Image");
                break;
            default:
                // Code for unknown image_type
                break;
        }

        Getallimages();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        ImageListRV.setLayoutManager(linearLayoutManager);
        ImageListRV.setAdapter(vehimageAdapter);

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        return view;
    }


    private void Getallimages() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.getallvehimages + "vehmas_code=" + vehmas_code + "&image_type=" + image_type;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    try {
                        boolean isSuccess = response.getBoolean("isSuccess");
                        String message = response.getString("message");

                        // Show success or error message
                       // Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();

                        if (isSuccess) {
                            JSONArray dataArray = response.getJSONArray("data");

                            Global.modelsList = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject e = dataArray.getJSONObject(i);

                                ModelsClass modelsClass = new ModelsClass();
                                modelsClass.setImg_code(e.getString("Image_code"));
                                modelsClass.setImg_path(e.getString("Image_path"));

                                Global.modelsList.add(modelsClass);

                            }

                            vehimageAdapter = new VehimgAdapter(Global.modelsList, getContext());
                            ImageListRV.setAdapter(vehimageAdapter);
                            vehimageAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireActivity(), "Parsing error!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(requireActivity(), "Network error!", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonObjectRequest);
    }
    public class VehimgAdapter extends RecyclerView.Adapter<VehimgAdapter.vehimgviewholder> {

        private final List<ModelsClass> modelsClasses;
        Context context;

        public VehimgAdapter(List<ModelsClass> modelsClasses, Context context) {
            this.modelsClasses = modelsClasses;
            this.context = context;

        }

        @NonNull
        @Override
        public vehimgviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_list, parent, false);
            return new vehimgviewholder(view);
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull vehimgviewholder holder, @SuppressLint("RecyclerView") int position) {

            holder.Count.setText(String.valueOf(position + 1));
            Global.loadWithPicasso(context, holder.veh_image, Global.vehimageurl + modelsClasses.get(position).getImg_path());




           /* holder.Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImagePicker();
                }
            });*/

            holder.Delete_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Image_code=modelsClasses.get(position).getImg_path();
                    delete_vehimg(Image_code);
                }
            });
        }


        @Override
        public int getItemCount() {
            return modelsClasses.size();
        }

        public  class vehimgviewholder extends RecyclerView.ViewHolder {

            ImageView veh_image,Delete_img;
            TextView Count;
            //LinearLayout Upload;
            public vehimgviewholder(@NonNull View itemView) {
                super(itemView);
                veh_image = itemView.findViewById(R.id.vehicle_image);
                Count = itemView.findViewById(R.id.count);
               // Upload = itemView.findViewById(R.id.upload);
                Delete_img = itemView.findViewById(R.id.delete_img);

            }
        }
    }

    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            startImagePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImagePicker();
            } else {
                Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startImagePicker() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (requestCode == REQUEST_IMAGE_PICK && fileUri != null) {
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), fileUri);

                    // Set the image to the appropriate ImageView
                    setImageToView(imageBitmap);

                    // Upload the image
                    uploadImageToServer(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setImageToView(Bitmap imageBitmap) {
        /*switch (currentUploadType) {
            case FRONT:
                frontImage.setImageBitmap(imageBitmap);
                break;
            case BACK:
                backImage.setImageBitmap(imageBitmap);
                break;
            case LEFT:
                leftImage.setImageBitmap(imageBitmap);
                break;
            case RIGHT:
                rightImage.setImageBitmap(imageBitmap);
                break;
            case INTERIOR:
                interiorImage.setImageBitmap(imageBitmap);
                break;
        }*/
    }

    private void uploadImageToServer(Bitmap imageBitmap) {
        if (imageBitmap == null) return;

        String url = Global.uploadVehImage;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject resp = new JSONObject(response);
                        if (resp.getBoolean("isSuccess")) {
                            // Update upload status
                           // uploadStatus.put(currentUploadType, true);
                          //  uploadedCount++;

                            String message = resp.getString("message");
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            Getallimages();
                            // Update UI
                            //updateUI();
                        } else {
                            String error = resp.getString("message");
                            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    handleNetworkError(error);
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
                params.put("imgdoc_path", bitmapToBase64(imageBitmap));
                params.put("file_type", "IMG");
                params.put("image_type", image_type);
                params.put("vehmas_code",vehmas_code);
                params.put("Image_code", "0");

                return params;
            }
        };

        // Set retry policy for the request
        request.setRetryPolicy(new DefaultRetryPolicy(
                0, // 10 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(requireContext()).add(request);
    }
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void handleNetworkError(VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(requireActivity(), "Request Time-Out", Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(requireActivity(), "No Connection Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(requireActivity(), "Server Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(requireActivity(), "Network Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(requireActivity(), "Parse Error", Toast.LENGTH_LONG).show();
        }
    }

    private void delete_vehimg(String Image_code) {

        String url = Global.deletevehimages + "Image_code=" + Image_code;
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject respObj = new JSONObject(response.trim());

                        boolean isSuccess = respObj.getBoolean("isSuccess");
                        String message = respObj.getString("message");

                        if (isSuccess) {
                            // Show success message
                            Global.customtoast(requireActivity(), getLayoutInflater(), message);
                        } else {
                            // Show failure message from server
                            Global.customtoast(requireActivity(), getLayoutInflater(), message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Global.customtoast(requireActivity(), getLayoutInflater(), "Error parsing server response");
                    }
                },
                error -> {
                    String errorMsg;

                    if (error instanceof TimeoutError) {
                        errorMsg = "Request Timed Out";
                    } else if (error instanceof NoConnectionError) {
                        errorMsg = "No Internet Connection";
                    } else if (error instanceof ServerError) {
                        errorMsg = "Server Error";
                    } else if (error instanceof NetworkError) {
                        errorMsg = "Network Error";
                    } else if (error instanceof ParseError) {
                        errorMsg = "Response Parse Error";
                    } else {
                        errorMsg = "Unexpected Error Occurred";
                    }

                    // Show error message
                    Global.customtoast(requireActivity(), getLayoutInflater(), errorMsg);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>(); // or send params if required
            }
        };

        // Retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

}
