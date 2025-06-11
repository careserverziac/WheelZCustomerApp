package com.ziac.wheelzcustomer;

import static android.app.Activity.RESULT_OK;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
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
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ModelClasses.CommonClass;
import ModelClasses.Global;


public class UploadViewFiles extends Fragment {

    ImageButton UploadfileBTN;
    RecyclerView DocumentRV;
    private static final int YOUR_REQUEST_CODE = 1;
    String file_name,file_type,wuser_code,cveh_code,actual_name,actual_filename,result;
    Documentlistadapter documentlistadapter;
    ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_document, container, false);


        UploadfileBTN = view.findViewById(R.id.imagebuttonRP);
        progressBar=view.findViewById(R.id.progressbarfiles);
        DocumentRV = view.findViewById(R.id.documentRV);
        DocumentRV.setLayoutManager(new LinearLayoutManager(requireContext()));

        wuser_code = Global.vehicledetails.getWuser_code();
        cveh_code =  Global.vehicledetails.getCveh_code();


        getdocumentslist();

        DocumentRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    UploadfileBTN.setVisibility(View.VISIBLE);
                } else {
                    UploadfileBTN.setVisibility(View.GONE);
                }
            }
        });

        UploadfileBTN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{ "image/*","application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                if (fileUri != null) {
                    String mimeType = getActivity().getContentResolver().getType(fileUri);
                    file_name = null;
                    file_name = convertFileToBase64(fileUri);
                    if (mimeType != null) {
                        if (mimeType.equals("application/pdf")) {
                            file_type = "PDF";

                        } else if (mimeType.equals("application/msword")) {
                            file_type = "DOC";
                        } else if(mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                            file_type = "DOCX";
                        }
                        else if(mimeType.equals("application/vnd.ms-excel")){
                            file_type = "XLS";
                        }
                        else if (mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                            file_type = "XLSX";
                        }else if (mimeType.startsWith("image/")) {
                            file_type = "IMG";

                        } else {
                            Toast.makeText(getActivity(), "You selected an unknown file type", Toast.LENGTH_LONG).show();
                        }
                    }
                    actual_filename= getFileName(fileUri);


                    uploadfiletoserver();
                }
            }
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String convertFileToBase64(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(inputStream);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void uploadfiletoserver() {
        showLoading();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Global.urluploadfiles;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resp = new JSONObject(response);
                    if (resp.getBoolean("success")) {
                        Global.customtoast(getActivity(), getLayoutInflater(), resp.getString("error"));
                        hideLoading();
                        getdocumentslist();

                    } else {

                        if (resp.has("error")) {

                            Global.customtoast(getActivity(), getLayoutInflater(), resp.getString("error"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideLoading();
                if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    String errorResponse = new String(error.networkResponse.data);
                    try {
                        JSONObject errorJson = new JSONObject(errorResponse);
                        String errorDescription = errorJson.optString("error_description", "");
                        Global.customtoast(getActivity(), getLayoutInflater(), errorDescription);
                    } catch (JSONException e) {
                        Global.customtoast(getActivity(), getLayoutInflater(), "An error occurred. Please try again later.");
                    }
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(), "Parse Error", Toast.LENGTH_LONG).show();
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("cveh_code",cveh_code);
                params.put("wuser_code",wuser_code);
                params.put("file_type",file_type);
                params.put("imgdoc_path", file_name);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }

    private void  getdocumentslist() {

        showLoading();
        String url = Global.UrlGetDocuments;
        url = url+"wuser_code="+wuser_code+"&cveh_code="+cveh_code;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                Global.alldealerslist = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    hideLoading();

                    String imgdoc_code = jsonObject.getString("imgdoc_code");
                    String imgdoc_path = jsonObject.getString("imgdoc_path");
                    String file_type = jsonObject.getString("file_type");

                    CommonClass commonClass = new CommonClass();
                    
                    commonClass.setImgdoc_code(imgdoc_code);
                    commonClass.setImgdoc_path(imgdoc_path);
                    commonClass.setFile_type(file_type);

                    Global.alldealerslist.add(commonClass);


                }

                documentlistadapter = new Documentlistadapter(requireContext(), Global.alldealerslist);
                DocumentRV.setAdapter(documentlistadapter);
                documentlistadapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                hideLoading();
            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                if (error instanceof TimeoutError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "No Connection Found");
                } else if (error instanceof ServerError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(getActivity(), getLayoutInflater(), "Parse Error");
                }

                Global.customtoast(getActivity(), getLayoutInflater(), "No images listed");

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }



    public class Documentlistadapter extends RecyclerView.Adapter<Documentlistadapter.ViewHolder>{

        private List<CommonClass> documentList;
        private Context context;

        public Documentlistadapter(Context context, List<CommonClass> documentList) {
            this.context = context;
            this.documentList = documentList;
        }

        public void deleteItem(String imgdoc_code,int position)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String url = Global.urldeletefiles;


            url = url+"imgdoc_code="+imgdoc_code;

            @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        JSONObject jsonResponse;
                        try {
                            jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("error");
                            boolean isSuccess = jsonResponse.getBoolean("isSuccess");

                            if (isSuccess) {
                                documentList.remove(position);
                                notifyDataSetChanged();


                                Global.customtoast(context, inflater, msg);
                                // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                            } else {
                                // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                Global.customtoast(context, inflater, msg);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    },
                    error -> {
                        Toast.makeText(context, "Unable to delete documents!!!", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String accessToken = Global.sharedPreferences.getString("access_token", "");
                    headers.put("Authorization", "Bearer " + accessToken);
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0, // timeout in milliseconds
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            queue.add(stringRequest);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.file_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            CommonClass document = documentList.get(position);
            holder.Filename.setText(document.getImgdoc_path());
            holder.DeleteFiles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgdoc_code = documentList.get(position).getImgdoc_code();
                    deleteItem(imgdoc_code,position);

                }
            });

            holder.Selectfile1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgdoc_path = documentList.get(position).getImgdoc_path();
                    openDocument(imgdoc_path,position);


                    //openDocument(documentList.get(position).getImgdoc_code());

                }
            });

            holder.Selectfile2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgdoc_path = documentList.get(position).getImgdoc_path();
                    openDocument(imgdoc_path,position);

                }
            });


        }

        private void openDocument(String imgdoc_path, int position) {
            String mimeType = getMimeType(imgdoc_path);
            Uri uri = Uri.parse(Global.urlfilepath +imgdoc_path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, mimeType);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {

                Toast.makeText(context, "No app to open the document.", Toast.LENGTH_SHORT).show();
                // Optionally, you can redirect the user to the Play Store to install a suitable app
                Intent installIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + mimeType)); // Search for apps that support the MIME type
                context.startActivity(installIntent);
            }
        }

        private String getMimeType(String filePath) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());

            if (mimeType == null) {
                // If MIME type is null, handle the case accordingly
                mimeType = "*/*"; // Default MIME type
            }

            return mimeType;
        }


        @Override
        public int getItemCount() {
            return documentList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView Filename;
            ImageView DeleteFiles;
            LinearLayout Selectfile1,Selectfile2;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Filename = itemView.findViewById(R.id.filename);
                DeleteFiles = itemView.findViewById(R.id.deletefile);
                Selectfile1 = itemView.findViewById(R.id.selectfilelnr);
                Selectfile2 = itemView.findViewById(R.id.selectfilelnr2);
            }
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        DocumentRV.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        DocumentRV.setVisibility(View.VISIBLE);
    }

}