package Fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import com.ziac.wheelzcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ModelClasses.CommonClass;
import ModelClasses.Global;

public class CommonFragment extends Fragment {
   /* String file_name, file_type, wuser_code, cveh_code, actual_name, actual_filename, result,doc_type,documentType;
    Documentlistadapter documentlistadapter;
    RecyclerView DocumentRV;

    ProgressBar progressBar;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common, container, false);
/*
        progressBar = view.findViewById(R.id.progressbarfiles);
         DocumentRV = view.findViewById(R.id.documentRV);
        DocumentRV.setLayoutManager(new LinearLayoutManager(requireContext()));
        wuser_code = Global.vehicledetails.getWuser_code();
        cveh_code = Global.vehicledetails.getCveh_code();

        if (getArguments() != null) {
             documentType = getArguments().getString("documentType");
            Log.d("ReceivedData", "Document Type: " + documentType);
            // Use the value as needed
        }

        getdocumentslist(documentType);

        DocumentRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //UploadfileBTN.setVisibility(View.VISIBLE);
                } else {
                   // UploadfileBTN.setVisibility(View.GONE);
                }
            }
        });*/

        return view;
    }
//    private void getdocumentslist(String documentType) {
//
//        showLoading();
//        String url = Global.UrlGetDocuments;
//        url = url + "wuser_code=" + wuser_code + "&cveh_code=" + cveh_code+"&doc_type=" + documentType;
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//
//        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
//            try {
//                JSONArray jsonArray = new JSONArray(response);
//                Global.alldealerslist = new ArrayList<>();
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    hideLoading();
//
//                    String imgdoc_code = jsonObject.getString("imgdoc_code");
//                    String imgdoc_path = jsonObject.getString("imgdoc_path");
//                    String file_type = jsonObject.getString("file_type");
//
//                    CommonClass commonClass = new CommonClass();
//
//                    commonClass.setImgdoc_code(imgdoc_code);
//                    commonClass.setImgdoc_path(imgdoc_path);
//                    commonClass.setFile_type(file_type);
//
//                    Global.alldealerslist.add(commonClass);
//
//
//                }
//
//                documentlistadapter = new Documentlistadapter(requireContext(), Global.alldealerslist);
//                DocumentRV.setAdapter(documentlistadapter);
//                documentlistadapter.notifyDataSetChanged();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                hideLoading();
//            }
//
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideLoading();
//                if (error instanceof TimeoutError) {
//                    Global.customtoast(getActivity(), getLayoutInflater(), "Request Time-Out");
//                } else if (error instanceof NoConnectionError) {
//                    Global.customtoast(getActivity(), getLayoutInflater(), "No Connection Found");
//                } else if (error instanceof ServerError) {
//                    Global.customtoast(getActivity(), getLayoutInflater(), "Server Error");
//                } else if (error instanceof NetworkError) {
//                    Global.customtoast(getActivity(), getLayoutInflater(), "Network Error");
//                } else if (error instanceof ParseError) {
//                    Global.customtoast(getActivity(), getLayoutInflater(), "Parse Error");
//                }
//
//                Global.customtoast(getActivity(), getLayoutInflater(), "No images listed");
//
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<String, String>();
//                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
//                headers.put("Authorization", "Bearer " + accesstoken);
//                return headers;
//            }
//
//            @NonNull
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                return params;
//            }
//        };
//
//        request.setRetryPolicy(new DefaultRetryPolicy(0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        requestQueue.add(request);
//    }
//
//    public class Documentlistadapter extends RecyclerView.Adapter<Documentlistadapter.ViewHolder> {
//
//        private List<CommonClass> documentList;
//        private Context context;
//
//        public Documentlistadapter(Context context, List<CommonClass> documentList) {
//            this.context = context;
//            this.documentList = documentList;
//        }
//
//        public void deleteItem(String imgdoc_code, int position) {
//            LayoutInflater inflater = LayoutInflater.from(getActivity());
//            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//            String url = Global.urldeletefiles;
//
//
//            url = url + "imgdoc_code=" + imgdoc_code;
//
//            @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                    response -> {
//                        JSONObject jsonResponse;
//                        try {
//                            jsonResponse = new JSONObject(response);
//                            String msg = jsonResponse.getString("error");
//                            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
//
//                            if (isSuccess) {
//                                documentList.remove(position);
//                                notifyDataSetChanged();
//
//
//                                Global.customtoast(context, inflater, msg);
//                                // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//                                Global.customtoast(context, inflater, msg);
//                            }
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                    },
//                    error -> {
//                        Toast.makeText(context, "Unable to delete documents!!!", Toast.LENGTH_SHORT).show();
//                    }) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> headers = new HashMap<>();
//                    String accessToken = Global.sharedPreferences.getString("access_token", "");
//                    headers.put("Authorization", "Bearer " + accessToken);
//                    return headers;
//                }
//
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//
//                    return params;
//                }
//            };
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    0, // timeout in milliseconds
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            ));
//            queue.add(stringRequest);
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(context).inflate(R.layout.file_list, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//
//            CommonClass document = documentList.get(position);
//            holder.Filename.setText(document.getImgdoc_path());
//            holder.DeleteFiles.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String imgdoc_code = documentList.get(position).getImgdoc_code();
//                    deleteItem(imgdoc_code, position);
//
//                }
//            });
//
//            holder.Selectfile1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String imgdoc_path = documentList.get(position).getImgdoc_path();
//                    openDocument(imgdoc_path, position);
//
//
//                    //openDocument(documentList.get(position).getImgdoc_code());
//
//                }
//            });
//
//            holder.Selectfile2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String imgdoc_path = documentList.get(position).getImgdoc_path();
//                    openDocument(imgdoc_path, position);
//
//                }
//            });
//
//
//        }
//
//        private void openDocument(String imgdoc_path, int position) {
//            String mimeType = getMimeType(imgdoc_path);
//            Uri uri = Uri.parse(Global.urlfilepath + imgdoc_path);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, mimeType);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            try {
//                context.startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//
//                Toast.makeText(context, "No app to open the document.", Toast.LENGTH_SHORT).show();
//                // Optionally, you can redirect the user to the Play Store to install a suitable app
//                Intent installIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + mimeType)); // Search for apps that support the MIME type
//                context.startActivity(installIntent);
//            }
//        }
//
//        private String getMimeType(String filePath) {
//            String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
//            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
//
//            if (mimeType == null) {
//                // If MIME type is null, handle the case accordingly
//                mimeType = "*/*"; // Default MIME type
//            }
//
//            return mimeType;
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return documentList.size();
//        }
//
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            TextView Filename;
//            ImageView DeleteFiles;
//            LinearLayout Selectfile1, Selectfile2;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                Filename = itemView.findViewById(R.id.filename);
//                DeleteFiles = itemView.findViewById(R.id.deletefile);
//                Selectfile1 = itemView.findViewById(R.id.selectfilelnr);
//                Selectfile2 = itemView.findViewById(R.id.selectfilelnr2);
//            }
//        }
//    }
//
//
//    private void showLoading() {
//        progressBar.setVisibility(View.VISIBLE);
//        //  DocumentRV.setVisibility(View.GONE);
//    }
//
//    private void hideLoading() {
//        progressBar.setVisibility(View.GONE);
//        // DocumentRV.setVisibility(View.VISIBLE);
//    }
}