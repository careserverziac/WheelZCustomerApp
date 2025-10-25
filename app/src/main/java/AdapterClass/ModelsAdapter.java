package AdapterClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.ziac.wheelzcustomer.R;
import com.ziac.wheelzcustomer.TestDriveActivity;

import java.util.ArrayList;
import java.util.List;

import Fragments.VehicleDetailFragment;
import ModelClasses.Global;
import ModelClasses.CommonClass;

public class ModelsAdapter extends RecyclerView.Adapter<ModelsAdapter.ViewHolder> {

    Context context;
    FragmentManager fragmentManager;
    private List<CommonClass> originalList;  // Original unfiltered list
    private List<CommonClass> filteredList;  // List that's actually displayed

    public ModelsAdapter(Context context, FragmentManager fragmentManager, List<CommonClass> commonClassList) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.originalList = new ArrayList<>(commonClassList);
        this.filteredList = new ArrayList<>(commonClassList);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<CommonClass> filteredItems = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    // If no constraint, return the original list
                    filteredItems.addAll(originalList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (CommonClass item : originalList) {
                        // Check both name and registration number
                        if (item.getApp_model_name() != null && item.getApp_model_name().toLowerCase().contains(filterPattern) ||
                                item.getModel_name() != null && item.getModel_name().toLowerCase().contains(filterPattern)) {
                            filteredItems.add(item);
                        }
                    }
                }

                results.values = filteredItems;
                results.count = filteredItems.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<CommonClass>) results.values);
                notifyDataSetChanged();
            }
        };
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bike, parent, false);

        ModelsAdapter.ViewHolder viewHolder = new ModelsAdapter.ViewHolder(view);
        return viewHolder;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CommonClass model = filteredList.get(position);  // Use filtered list here

        Global.loadWithPicasso(context, holder.Veh_image, Global.brandsimageurl +
                model.getImage_path());

        //String image=commonClassList.get(position).getImage();
        holder.Manufacturer.setText(model.getManufacture());

        String ccString = model.getCc();
        float cc = Float.parseFloat(ccString);
        int ccInteger = (int) cc;

        holder.Model_name.setText(model.getApp_model_name());

        holder.Modelscardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.vehicledetails = model;
                VehicleDetailFragment vehicleDetailFragment = new VehicleDetailFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, vehicleDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        holder.Testdrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.modellist = model;
                Intent intent = new Intent(context, TestDriveActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void updateList(List<CommonClass> newList) {
        this.originalList = new ArrayList<>(newList);
        this.filteredList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Veh_image;
        TextView Manufacturer,Model_name,CC,BHP;
        MaterialCardView Modelscardview;
        MaterialButton Testdrive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Veh_image=itemView.findViewById(R.id.veh_image);
            Manufacturer=itemView.findViewById(R.id.manufacture_name);
            Model_name=itemView.findViewById(R.id.model_name);
            Modelscardview=itemView.findViewById(R.id.modelscardview);
            Testdrive=itemView.findViewById(R.id.btn_book_test_drive);

        }
    }

}
