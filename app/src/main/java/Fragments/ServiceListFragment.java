package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.wheelzcustomer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ModelClasses.Global;
import ModelClasses.ServiceClass;

public class ServiceListFragment extends Fragment {

    RecyclerView CategoriesRV,ProductlistRV;
    CategoryAdapter categoryAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    FragmentManager fragmentManager;
    ProductListAdapter productlistAdapter;
    LottieAnimationView lottieAnimationView;
    Context context;
    String url, URL, com_code, ayear,query,prd_cat_code,wuser_code;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        context=requireContext();

        CategoriesRV = view.findViewById(R.id.categoriesRV);
        ProductlistRV = view.findViewById(R.id.productlistRV);
        swipeRefreshLayout = view.findViewById(R.id.refresh);


        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        wuser_code = Global.sharedPreferences.getString("wuser_code", "");

        GetAllCategory();
        swipeRefreshLayout.setOnRefreshListener(this::GetAllCategory);
        GetAllProducts("");
        swipeRefreshLayout.setOnRefreshListener(() -> GetAllProducts(""));



        hideLoading();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        ProductlistRV.setLayoutManager(gridLayoutManager);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        CategoriesRV.setLayoutManager(linearLayoutManager);
        CategoriesRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        return view;
    }
    private void GetAllCategory() {

        showLoading();
        RequestQueue queue = Volley.newRequestQueue(context);

        com_code =Global.sharedPreferences.getString("Code","");
        url = Global.getCategoriesList;
        URL = url + "comcode=" + com_code;

        @SuppressLint("NotifyDataSetChanged")
        StringRequest request = new StringRequest(Request.Method.POST, URL, response -> {
            try {
                JSONObject responseObject = new JSONObject(response);
                boolean success = responseObject.getBoolean("isSuccess");

                if (success) {
                    JSONArray dataArray = responseObject.getJSONArray("data");
                    Global.commonarraylist = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject jsonObject = dataArray.getJSONObject(i);

                        swipeRefreshLayout.setRefreshing(false);
                        String com_code = jsonObject.getString("com_code");
                        String prd_cat_code = jsonObject.getString("vcate_code");
                        String prd_cat_name = jsonObject.getString("vcate_name");

                        ServiceClass serviceClass = new ServiceClass();

                        serviceClass.setCom_code(com_code);
                        serviceClass.setPrd_cat_code(prd_cat_code);
                        serviceClass.setPrd_cat_name(prd_cat_name);

                        Global.commonarraylist.add(serviceClass);
                    }

                    //Collections.sort(Global.commonarraylist, (o1, o2) -> o1.getPrd_name().compareToIgnoreCase(o2.getPrd_name()));

                    if (!Global.commonarraylist.isEmpty()) {
                        categoryAdapter = new CategoryAdapter(Global.commonarraylist, getContext());
                        CategoriesRV.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    hideLoading();
                } else {
                    hideLoading();
                    swipeRefreshLayout.setRefreshing(false);
                }
                hideLoading();
            } catch (JSONException e) {
                e.printStackTrace();
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, error -> {
            swipeRefreshLayout.setRefreshing(false);
            hideLoading();
            if (error instanceof NoConnectionError) {
                if (error instanceof TimeoutError) {
                    Global.customtoast(context, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(context, getLayoutInflater(), "No Connection Found");
                } else if (error instanceof ServerError) {
                    Global.customtoast(context, getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(context, getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(context, getLayoutInflater(), "Parse Error");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }
    private void GetAllProducts(String query) {

        showLoading();
        RequestQueue queue = Volley.newRequestQueue(context);

        com_code =Global.sharedPreferences.getString("com_code","");
        url = Global.ProductList;
        URL = url + "comcode=" + com_code ;

        @SuppressLint("NotifyDataSetChanged")
        StringRequest request = new StringRequest(Request.Method.POST, URL, response -> {
            if (!isAdded()) {
                return;
            }
            try {
                JSONObject responseObject = new JSONObject(response);
                boolean success = responseObject.getBoolean("isSuccess");

                if (success) {
                    JSONArray dataArray = responseObject.getJSONArray("data");
                    Global.commonarraylist = new ArrayList<>();

                    if (dataArray.length() == 0) {
                        if (isAdded()) {
                            Global.customtoast(requireContext(), getLayoutInflater(), "No items found !!");
                            hideLoading();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        return;
                    }


                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject jsonObject = dataArray.getJSONObject(i);

                        String com_code = jsonObject.getString("com_code");
                        String item_code = jsonObject.getString("item_code");
                        String prd_name = jsonObject.getString("prd_name");
                        String prd_desc = jsonObject.getString("prd_desc");
                        String mrp_price = jsonObject.getString("mrp_price");
                        String gst_pert = jsonObject.getString("gst_pert");
                        String prd_cat_name = jsonObject.getString("vcate_name");
                        String prd_family_name = jsonObject.getString("prd_family_name");
                        String specification = jsonObject.getString("specification");
                        String ayear = jsonObject.getString("ayear");
                        String sp_photo = jsonObject.getString("sp_photo");
                        String part_no = jsonObject.getString("part_no");
                        String hsn = jsonObject.getString("HSN");

                        ServiceClass serviceClass = new ServiceClass();

                        serviceClass.setCom_code(com_code);
                        serviceClass.setItem_code(item_code);
                        serviceClass.setPrd_name(prd_name);
                        serviceClass.setPrd_desc(prd_desc);
                        serviceClass.setPrd_mrp(mrp_price);
                        serviceClass.setPrd_gst(gst_pert);
                        serviceClass.setPrd_cat_name(prd_cat_name);
                        serviceClass.setPrd_family_name(prd_family_name);
                        serviceClass.setAyear(ayear);
                        serviceClass.setPrd_image(sp_photo);
                        serviceClass.setPart_number(part_no);
                        serviceClass.setHsncode(hsn);

                        Global.commonarraylist.add(serviceClass);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    Collections.sort(Global.commonarraylist, (o1, o2) -> o1.getPrd_name().compareToIgnoreCase(o2.getPrd_name()));

                    if (!Global.commonarraylist.isEmpty()) {
                        if (isAdded()) {
                            productlistAdapter = new ProductListAdapter(Global.commonarraylist, getContext());
                            ProductlistRV.setAdapter(productlistAdapter);
                            productlistAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (isAdded()) {
                            productlistAdapter = new ProductListAdapter(Global.commonarraylist, getContext());
                            ProductlistRV.setAdapter(productlistAdapter);
                            productlistAdapter.notifyDataSetChanged();
                            Global.customtoast(requireContext(), getLayoutInflater(), "No products found !!");
                        }
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    hideLoading();
                } else {
                    if (isAdded()) {
                        hideLoading();
                        Global.customtoast(context, getLayoutInflater(), "No data available !!");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (isAdded()) {
                    hideLoading();
                }
            }
        }, error -> {
            if (isAdded()) {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
                if (error instanceof NoConnectionError) {
                    if (error instanceof TimeoutError) {
                        Global.customtoast(context, getLayoutInflater(), "Request Time-Out");
                    } else if (error instanceof NoConnectionError) {
                        Global.customtoast(context, getLayoutInflater(), "No Connection Found");
                    } else if (error instanceof ServerError) {
                        Global.customtoast(context, getLayoutInflater(), "Server Error");
                    } else if (error instanceof NetworkError) {
                        Global.customtoast(context, getLayoutInflater(), "Network Error");
                    } else if (error instanceof ParseError) {
                        Global.customtoast(context, getLayoutInflater(), "Parse Error");
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

    private void showLoading() {
//        lottieAnimationView.setVisibility(View.VISIBLE);
        //ProductlistRV.setVisibility(View.GONE);
        // progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
       // lottieAnimationView.setVisibility(View.GONE);
       // ProductlistRV.setVisibility(View.VISIBLE);
        //progressBar.setVisibility(View.GONE);
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {

        private final List<ServiceClass> productlist;
        private final Context context;
        private int selectedPosition = 0;


        public CategoryAdapter(List<ServiceClass> productlist, Context context) {
            this.productlist = productlist;
            this.context = context;
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, @SuppressLint("RecyclerView") int position) {

            if (position == 0) {

                int paddingInDp = 10; // amount of padding to make image appear smaller

// Convert dp to pixels
                float scale = holder.Prd_image.getContext().getResources().getDisplayMetrics().density;
                int paddingInPx = (int) (paddingInDp * scale + 0.5f);

// Apply padding
                holder.Prd_image.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);
                holder.Prd_image.setScaleType(ImageView.ScaleType.FIT_CENTER); // Optional but helps fit the image
                holder.Prd_image.setImageResource(R.drawable.all_icon);
                holder.Prd_name.setText("All");

                if (selectedPosition == 0) {
                    holder.Indicator.setVisibility(View.VISIBLE);
                } else {
                    holder.Indicator.setVisibility(View.GONE);
                }

                holder.Prd_image.setOnClickListener(v -> {
                    selectedPosition = 0;
                    notifyDataSetChanged();
                    GetAllProducts("");
                });
            }else {

                Global.loadWithPicasso(context, holder.Prd_image, Global.productimageurl + productlist.get(position).getPrd_image());

                String productName = productlist.get(position).getPrd_cat_name();
                if (productName.length() > 10) {
                    productName = productName.substring(0, 10) + "...";
                }
                holder.Prd_name.setText(productName);


                if (selectedPosition == position) {
                    holder.Indicator.setVisibility(View.VISIBLE);
                } else {
                    holder.Indicator.setVisibility(View.GONE);
                }

                // Handle image click to select the item
                holder.Prd_image.setOnClickListener(v -> {
                    selectedPosition = position; // Update selected position
                    String prd_cat_code = productlist.get(position).getPrd_cat_code();
                    selected_CatProducts(prd_cat_code); // Call the function to fetch products
                    notifyDataSetChanged(); // Refresh RecyclerView to update the indicator
                });


            }
        }

        private void selected_CatProducts(String prd_cat_code) {

            RequestQueue queue = Volley.newRequestQueue(context);
            com_code =Global.sharedPreferences.getString("Code","");
            url = Global.ProductCatPrdFilter;
            URL = url + "comcode=" + com_code + "&vcate_code=" + prd_cat_code ;

            @SuppressLint("NotifyDataSetChanged")
            StringRequest request = new StringRequest(Request.Method.POST, URL, response -> {
                if (!isAdded()) {
                    // The fragment is not attached to the activity anymore, so we shouldn't proceed
                    return;
                }

                try {
                    JSONObject responseObject = new JSONObject(response);
                    boolean success = responseObject.getBoolean("isSuccess");

                    if (success) {
                        JSONArray dataArray = responseObject.getJSONArray("data");
                        Global.commonarraylist = new ArrayList<>();

                        if (dataArray.length() == 0) {
                            if (isAdded()) {
                                Global.customtoast(requireContext(), getLayoutInflater(), "No items found !!");
                                hideLoading();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            return; // Stop further processing if no data is available
                        }

                        // Loop through the array to extract product details
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);

                            String com_code = jsonObject.getString("com_code");
                            String item_code = jsonObject.getString("item_code");
                            String prd_name = jsonObject.getString("prd_name");
                            String prd_desc = jsonObject.getString("prd_desc");
                            String mrp_price = jsonObject.getString("mrp_price");
                            String gst_pert = jsonObject.getString("gst_pert");
                            String prd_cat_name = jsonObject.getString("vcate_name");
                            String specification = jsonObject.getString("specification");
                            String ayear = jsonObject.getString("ayear");
                            String sp_photo = jsonObject.getString("sp_photo");
                            String part_no = jsonObject.getString("part_no");
                            String hsn = jsonObject.getString("HSN");
                            ServiceClass serviceClass = new ServiceClass();

                            serviceClass.setCom_code(com_code);
                            serviceClass.setItem_code(item_code);
                            serviceClass.setPrd_name(prd_name);
                            serviceClass.setPrd_desc(prd_desc);
                            serviceClass.setPrd_mrp(mrp_price);
                            serviceClass.setPrd_gst(gst_pert);
                            serviceClass.setPrd_cat_name(prd_cat_name);
                            serviceClass.setAyear(ayear);
                            serviceClass.setPrd_image(sp_photo);
                            serviceClass.setPart_number(part_no);
                            serviceClass.setHsncode(hsn);

                            Global.commonarraylist.add(serviceClass);
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        Collections.sort(Global.commonarraylist, (o1, o2) -> o1.getPrd_name().compareToIgnoreCase(o2.getPrd_name()));

                        if (!Global.commonarraylist.isEmpty()) {
                            if (isAdded()) {
                                productlistAdapter = new ProductListAdapter(Global.commonarraylist, getContext());
                                ProductlistRV.setAdapter(productlistAdapter);
                                productlistAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (isAdded()) {
                                productlistAdapter = new ProductListAdapter(Global.commonarraylist, getContext());
                                ProductlistRV.setAdapter(productlistAdapter);
                                productlistAdapter.notifyDataSetChanged();
                                Global.customtoast(requireContext(), getLayoutInflater(), "No products found !!");
                            }
                        }

                        swipeRefreshLayout.setRefreshing(false);
                        hideLoading();
                    } else {
                        if (isAdded()) {
                            hideLoading();
                            Global.customtoast(context, getLayoutInflater(), "No data available !!");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (isAdded()) {
                        hideLoading();
                    }
                }
            }, error -> {
                if (isAdded()) {
                    hideLoading();
                    swipeRefreshLayout.setRefreshing(false);
                    if (error instanceof NoConnectionError) {
                        if (error instanceof TimeoutError) {
                            Global.customtoast(context, getLayoutInflater(), "Request Time-Out");
                        } else if (error instanceof NoConnectionError) {
                            Global.customtoast(context, getLayoutInflater(), "No Connection Found");
                        } else if (error instanceof ServerError) {
                            Global.customtoast(context, getLayoutInflater(), "Server Error");
                        } else if (error instanceof NetworkError) {
                            Global.customtoast(context, getLayoutInflater(), "Network Error");
                        } else if (error instanceof ParseError) {
                            Global.customtoast(context, getLayoutInflater(), "Parse Error");
                        }
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String accesstoken = Global.sharedPreferences.getString("access_token", null);
                    headers.put("Authorization", "Bearer " + accesstoken);
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            queue.add(request);


        }

        @Override
        public int getItemCount() {
            return productlist != null ? productlist.size() : 0;
        }

        public static class viewholder extends RecyclerView.ViewHolder {

            ImageView Prd_image;
            TextView Prd_name;
            View Indicator;

            public viewholder(@NonNull View itemView) {
                super(itemView);
                Prd_image = itemView.findViewById(R.id.prd_image);
                Prd_name = itemView.findViewById(R.id.prd_name);
                Indicator = itemView.findViewById(R.id.indicator);

            }
        }
    }

    public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.viewholder> {

        private final List<ServiceClass> productlist;
        private final Context context;
        private FragmentManager fragmentManager;



        public ProductListAdapter(List<ServiceClass> productlist, Context context) {
            this.productlist = productlist;
            this.context = context;
            //this.fragmentManager = fragmentManager;
        }

        @NonNull
        @Override
        public ProductListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list2, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, @SuppressLint("RecyclerView") int position) {

            //Picasso.Builder builder = new Picasso.Builder(context);

            Global.loadWithPicasso(context, holder.Prd_image, Global.productimageurl + productlist.get(position).getPrd_image());

            String productName = productlist.get(position).getPrd_name();
            if (productName.length() > 22) {
                productName = productName.substring(0, 22) + "...";
            }
            holder.Prd_name.setText(productName);


            holder.Prd_image.setOnClickListener(v -> {
               // Global.productflag = "CategoryPage";
                Global.Commonclass = productlist.get(position);
               /* Intent intent = new Intent(context, ProductInDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
                Toast.makeText(context, "Still page not defined", Toast.LENGTH_SHORT).show();
            });

            holder.Add.setOnClickListener(v -> {

                try {
                    Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                    if (Global.sharedPreferences.contains("access_token") && Global.sharedPreferences.contains("refresh_token")) {
                        ayear = Global.sharedPreferences.getString("ayear", "").toString();
                        String itemcode = productlist.get(position).getItem_code();
                        addToCartMethod(itemcode);
                    } else {

                        /*Toast.makeText(context, "Please login to add items to cart", Toast.LENGTH_SHORT).show();*/
                        Global.customtoast(requireContext(), getLayoutInflater(), "Please login to add items to cart");
                       /* Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        startActivity(intent);*/

                    }
                }catch (Exception ex){
                   /* Intent intent = new Intent(context.getApplicationContext(),LoginActivity.class);
                    startActivity(intent);*/

                }




            });

        }

        @Override
        public int getItemCount() {
            return productlist != null ? productlist.size() : 0;
        }


        private void addToCartMethod(String item_code) {

            RequestQueue queue = Volley.newRequestQueue(context);
            com_code = Global.sharedPreferences.getString("com_code","");
           // url = Global.addtocartAPI;
            URL = url + "comcode=" + com_code + "&ayear=" + ayear+"&itemcode=" + item_code+ "&cuscode=" + "0" ;


            @SuppressLint("NotifyDataSetChanged")
            StringRequest request = new StringRequest(Request.Method.POST, URL, response -> {


                try {
                    JSONObject responseObject = new JSONObject(response);
                    boolean success = responseObject.getBoolean("success");
                    String message = responseObject.getString("message");


                    if (success) {
                      //  Global.Getcartcount(context);

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Global.customtoast(getContext(), getLayoutInflater(), message);


                    }else {
                        Global.customtoast(context, getLayoutInflater(), message);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }, error -> {

                hideLoading();

                if (error instanceof NoConnectionError) {
                    if (error instanceof TimeoutError) {
                        Global.customtoast(context, getLayoutInflater(), "Request Time-Out");
                    } else if (error instanceof NoConnectionError) {
                        Global.customtoast(context, getLayoutInflater(), "No Connection Found");
                    } else if (error instanceof ServerError) {
                        Global.customtoast(context, getLayoutInflater(), "Server Error");
                    } else if (error instanceof NetworkError) {
                        Global.customtoast(context, getLayoutInflater(), "Network Error");
                    } else if (error instanceof ParseError) {
                        Global.customtoast(context, getLayoutInflater(), "Parse Error");
                    }
                }

            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String accesstoken = Global.sharedPreferences.getString("access_token", null);
                    headers.put("Authorization", "Bearer " + accesstoken);
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            queue.add(request);


        }



/*

        public void showImage(Context context, Picasso picasso, String userimage) {
            Dialog builder = new Dialog(context);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                }
            });

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            picasso.load(Uri.parse(userimage)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    ImageView imageView = new ImageView(context);

                    int imageWidth = bitmap.getWidth();
                    int imageHeight = bitmap.getHeight();
                    float aspectRatio = (float) imageWidth / imageHeight;

                    int newWidth = screenWidth;
                    int newHeight = (int) (screenWidth / aspectRatio);
                    if (newHeight > screenHeight) {
                        newHeight = screenHeight;
                        newWidth = (int) (screenHeight * aspectRatio);
                    }

                    int paddingInDp = 16;
                    int paddingInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingInDp, context.getResources().getDisplayMetrics());

                    newWidth -= 2 * paddingInPx;
                    newHeight -= 2 * paddingInPx;

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
                    layoutParams.setMargins(paddingInPx, paddingInPx, paddingInPx, paddingInPx); // Set padding
                    imageView.setLayoutParams(layoutParams);

                    imageView.setImageBitmap(bitmap);

                    builder.addContentView(imageView, layoutParams);
                    builder.show();
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
*/

        public static class viewholder extends RecyclerView.ViewHolder {

            ImageView Prd_image;
            TextView Prd_name;
            RelativeLayout Add;

            public viewholder(@NonNull View itemView) {
                super(itemView);

                Prd_image = itemView.findViewById(R.id.prd_image);
                Prd_name = itemView.findViewById(R.id.prd_name);
                Add = itemView.findViewById(R.id.add);



            }


        }
    }



}






/*ServicelistRV = view.findViewById(R.id.servicelist);
        ServicelistRV.setLayoutManager(new LinearLayoutManager(requireContext()));

Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
wuser_code = Global.sharedPreferences.getString("wuser_code", "");
Servicehistory();*/



/* private void Servicehistory() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = Global.getGetBookings;
        String Url = url + "wuser_code=" + wuser_code;

        @SuppressLint("NotifyDataSetChanged")
        StringRequest request = new StringRequest(Request.Method.GET, Url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                Global.allleadslist = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String service_code = jsonObject.getString("service_code");
                    String com_code = jsonObject.getString("com_code");
                    String service_date = jsonObject.getString("service_date");
                    String service_time = jsonObject.getString("service_time");
                    String kms_done = jsonObject.getString("kms_done");
                    String service_type = jsonObject.getString("service_type");
                    String pick_flag = jsonObject.getString("pick_flag");
                    String drop_flag = jsonObject.getString("drop_flag");
                    String additional_info = jsonObject.getString("additional_info");
                    String location_map = jsonObject.getString("location_map");
                    String cveh_code = jsonObject.getString("cveh_code");
                    String vehhis_code = jsonObject.getString("vehhis_code");
                    String wuser_code = jsonObject.getString("wuser_code");
                    String service_address = jsonObject.getString("service_address");
                    String veh_image1 = jsonObject.getString("veh_image1");
                    String model_name = jsonObject.getString("model_name");
                    String com_name = jsonObject.getString("com_name");
                    String com_email = jsonObject.getString("com_email");
                    String com_phone = jsonObject.getString("com_phone");
                    String com_address = jsonObject.getString("com_address");

                    CommonClass commonClass = new CommonClass();
                    commonClass.setService_code(service_code);
                    commonClass.setCom_code(com_code);
                    commonClass.setService_date(service_date);
                    commonClass.setService_time(service_time);
                    commonClass.setKms_done(kms_done);
                    commonClass.setService_type(service_type);
                    commonClass.setCom_name(com_name);
                    commonClass.setPick_flag(pick_flag);
                    commonClass.setDrop_flag(drop_flag);
                    commonClass.setKms_done(kms_done);
                    commonClass.setAdditional_info(additional_info);
                    commonClass.setLocation_map(location_map);
                    commonClass.setCveh_code(cveh_code);
                    commonClass.setKms_done(kms_done);
                    commonClass.setVehhis_code(vehhis_code);
                    commonClass.setCom_name(com_name);
                    commonClass.setWuser_code(wuser_code);
                    commonClass.setService_address(service_address);
                    commonClass.setImage_path(veh_image1);
                    commonClass.setModel_name(model_name);
                    commonClass.setCom_name(com_name);
                    commonClass.setModel_name(com_email);
                    commonClass.setCom_phone(com_phone);
                    commonClass.setCom_address(com_address);

                    Global.allleadslist.add(commonClass);

                }
                adapter = new ServiceListAdapter(requireContext(), Global.allleadslist);
                ServicelistRV.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

            if (error instanceof NoConnectionError) {
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
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

    public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {

        private List<CommonClass> serviceHistoryList;
        private Context context;

        public ServiceListAdapter(Context context, List<CommonClass> serviceHistoryList) {
            this.context = context;
            this.serviceHistoryList = serviceHistoryList;
        }
        @NonNull
        @Override
        public ServiceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.service_list, parent, false);
            return new ServiceListAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ServiceListAdapter.ViewHolder holder, int position) {
            CommonClass service = serviceHistoryList.get(position);

            holder.Modelname.setText(service.getModel_name());
            holder.Servicetype.setText(service.getService_type());
            double kmsDoneValue = Double.parseDouble(service.getKms_done());
            String kmsDone = (kmsDoneValue % 1 == 0) ? String.valueOf((int) kmsDoneValue) : String.valueOf(kmsDoneValue);
            holder.Kmdone.setText(kmsDone + " Km");

            String originalDateStr = service.getService_date();
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yy");
            Date date = null;
            try {
                date = originalFormat.parse(originalDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDateTimeStr = targetFormat.format(date);
            holder.Dateandtime.setText(formattedDateTimeStr);
        }
        @Override
        public int getItemCount() {
            return serviceHistoryList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView Modelname, Servicetype, Totalamount, Kmdone, Dateandtime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Modelname = itemView.findViewById(R.id.modelname);
                Servicetype = itemView.findViewById(R.id.servicetype);
                Totalamount = itemView.findViewById(R.id.totalamount);
                Kmdone = itemView.findViewById(R.id.kmdone);
                Dateandtime = itemView.findViewById(R.id.dateandtime);
            }
        }
    }*/























