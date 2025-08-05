package Fragments;

import static android.app.Activity.RESULT_OK;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.wheelzcustomer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ModelClasses.CommonClass;
import ModelClasses.Global;


public class ContainerFragment extends Fragment {

    private static final int YOUR_REQUEST_CODE = 1;
    String documentType,documentname,wuser_code,cveh_code,filepath,doc_type,file_name,file_type,actual_filename,fileUrl;
    ProgressBar progressBar;
    RecyclerView Doc_listRV;
    Documentlistadapter docAdapter;
    Context context;
    CommonClass commonClass;
    TextView Doc_pref;
    ImageView Backbtn;
    ImageButton Uploadfile;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the documentType from arguments
        if (getArguments() != null) {
            documentType = getArguments().getString("documentType");
            documentname = getArguments().getString("documentName");
        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          view= inflater.inflate(R.layout.fragment_container, container, false);

         context=requireActivity();

        progressBar = view.findViewById(R.id.progressbarfiles);
        Doc_listRV = view.findViewById(R.id.doc_listRV);
        Doc_pref = view.findViewById(R.id.doc_pref);
        Uploadfile = view.findViewById(R.id.imagebuttonRP);
        Backbtn = view.findViewById(R.id.backbtn);

        wuser_code=Global.vehicledetails.getWuser_code();
        cveh_code=Global.vehicledetails.getCveh_code();


        Doc_listRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        handleDocumentType(view, documentType);


        Uploadfile.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            startActivityForResult(intent, 1);
        });

        Backbtn.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });


        return view;
    }

    private void handleDocumentType(View view, String docType) {
        switch (docType) {
            case "R":
                getDocumentlist("R");
                doc_type="R";
                filepath=Global.rcpath;
                Doc_pref.setText("RC Book");
                break;

            case "P":
                getDocumentlist("P");
                doc_type="P";
                filepath=Global.permitpath;
                Doc_pref.setText("Permit");
                break;

            case "I":
                getDocumentlist("I");
                doc_type="I";
                filepath=Global.insurancepath;
                Doc_pref.setText("Insurance");
                break;

            case "W":
                getDocumentlist("W");
                doc_type="W";
                filepath=Global.warrantypath;
                Doc_pref.setText("Warranty");
                break;

            case "E":
                getDocumentlist("E");
                doc_type="E";
                filepath=Global.emissionpath;
                Doc_pref.setText("Emission");
                break;

            default:
                Toast.makeText(getContext(), "Unknown Document", Toast.LENGTH_SHORT).show();
                break;
        }
}

    private void getDocumentlist(String doc_type) {
        showLoading();
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String Url = Global.GetDocumentList+"wuser_code="+wuser_code+"&cveh_code="+cveh_code+"&doc_type="+doc_type;
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.POST,Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Global.dealersarraylist = new ArrayList<CommonClass>();
                commonClass = new CommonClass();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject jsonObject;
                    try {

                        jsonObject = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    commonClass = new CommonClass();
                    try {

                        commonClass.setImgdoc_code(jsonObject.getString("imgdoc_code"));
                        commonClass.setCveh_code(jsonObject.getString("cveh_code"));
                        commonClass.setWuser_code(jsonObject.getString("wuser_code"));
                        commonClass.setImgdoc_path(jsonObject.getString("imgdoc_path"));
                        commonClass.setCreatedby(jsonObject.getString("createdby"));
                        commonClass.setCreatedon(jsonObject.getString("createdon"));
                        commonClass.setFile_type(jsonObject.getString("file_type"));
                        commonClass.setDoc_type(jsonObject.getString("doc_type"));

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.dealersarraylist.add(commonClass);
                    //swipeRefreshLayout.setRefreshing(false);
                }

                docAdapter = new Documentlistadapter(getContext(), Global.dealersarraylist);
                Doc_listRV.setAdapter(docAdapter);
                docAdapter.notifyDataSetChanged();
                //swipeRefreshLayout.setRefreshing(false);
                hideLoading();

            }
        },  error -> {

            if (error instanceof NoConnectionError) {
                hideLoading();
                if (error instanceof TimeoutError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Internet connection unavailable");
                } else if (error instanceof ServerError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(requireActivity(), getLayoutInflater(), "Parse Error");
                }
               // swipeRefreshLayout.setRefreshing(false);
            }
            // Global.customtoast(getApplicationContext(),getLayoutInflater(),"Technical error : Unable to get dashboard data !!" + error);

        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }


        };

        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonArrayrequest);

    }

    public class Documentlistadapter extends RecyclerView.Adapter<Documentlistadapter.ViewHolder> {

        private List<CommonClass> documentList;
        private Context context;

        public Documentlistadapter(Context context, List<CommonClass> documentList) {
            this.context = context;
            this.documentList = documentList;
        }

        public void deleteItem(String imgdoc_code, int position) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String url = Global.urldeletefiles;


            url = url + "imgdoc_code=" + imgdoc_code;

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
        public Documentlistadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.file_list, parent, false);
            return new Documentlistadapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Documentlistadapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            CommonClass document = documentList.get(position);
            holder.Filename.setText(document.getImgdoc_path());
            holder.DeleteFiles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgdoc_code = documentList.get(position).getImgdoc_code();
                    deleteItem(imgdoc_code, position);

                }
            });

            holder.fileview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgdoc_path = documentList.get(position).getImgdoc_path();
                    openDocument(imgdoc_path, position);

                }
            });

            holder.fileview2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgdoc_path = documentList.get(position).getImgdoc_path();
                    openDocument(imgdoc_path, position);

                }
            });


        }

        private void openDocument(String imgdoc_path, int position) {
            fileUrl = filepath+imgdoc_path;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
            startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return documentList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView Filename;
            ImageView DeleteFiles;
            LinearLayout fileview1, fileview2;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Filename = itemView.findViewById(R.id.filename);
                DeleteFiles = itemView.findViewById(R.id.deletefile);
                fileview1 = itemView.findViewById(R.id.file1);
                fileview2 = itemView.findViewById(R.id.file2);
            }
        }
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
                        } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                            file_type = "DOCX";
                        } else if (mimeType.equals("application/vnd.ms-excel")) {
                            file_type = "XLS";
                        } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                            file_type = "XLSX";
                        } else if (mimeType.startsWith("image/")) {
                            file_type = "IMG";

                        } else {
                            Toast.makeText(getActivity(), "You selected an unknown file type", Toast.LENGTH_LONG).show();
                        }
                    }
                    actual_filename = getFileName(fileUri);


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
                    if (resp.getBoolean("isSuccess")) {
                        Global.customtoast(getActivity(), getLayoutInflater(), resp.getString("message"));
                        hideLoading();
                        handleDocumentType(view, documentType);

                    } else {

                        if (resp.has("message")) {
                            Global.customtoast(getActivity(), getLayoutInflater(), resp.getString("message"));
                            hideLoading();
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

                params.put("cveh_code", cveh_code);
                params.put("wuser_code", wuser_code);
                params.put("file_type", file_type);
                params.put("imgdoc_path", file_name);
                params.put("doc_type", doc_type);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }



    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

}

