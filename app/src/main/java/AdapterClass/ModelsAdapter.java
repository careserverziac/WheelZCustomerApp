package AdapterClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.wheelzcustomer.R;
import com.ziac.wheelzcustomer.TestDriveActivity;

import java.util.List;

import Fragments.VehicleDetailFragment;
import ModelClasses.Global;
import ModelClasses.CommonClass;

public class ModelsAdapter extends RecyclerView.Adapter<ModelsAdapter.ViewHolder> {

     List<CommonClass> commonClassList;
    private final Context context;


    public ModelsAdapter(List<CommonClass> commonClassList, Context context) {
        this.context = context;
        this.commonClassList = commonClassList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);

        ModelsAdapter.ViewHolder viewHolder = new ModelsAdapter.ViewHolder(view);
        return viewHolder;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Global.loadWithPicasso(context, holder.Veh_image, Global.brandsimageurl + commonClassList.get(position).getImage_path());

        //String image=commonClassList.get(position).getImage();
        holder.Manufacturer.setText(commonClassList.get(position).getManufacture());

        String ccString = commonClassList.get(position).getCc();
        float cc = Float.parseFloat(ccString);
        int ccInteger = (int) cc;

        holder.Model_name.setText(commonClassList.get(position).getModel_name() + " - " + ccInteger);

        holder.Modelscardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.vehicledetails = commonClassList.get(position);
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

                Global.modellist = commonClassList.get(position);
                Intent intent = new Intent(context, TestDriveActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return commonClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Veh_image;
        TextView Manufacturer,Model_name,CC,BHP;
        CardView Modelscardview;
        Button Testdrive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Veh_image=itemView.findViewById(R.id.veh_image);
            Manufacturer=itemView.findViewById(R.id.manufacture_name);
            Model_name=itemView.findViewById(R.id.model_name);
            Modelscardview=itemView.findViewById(R.id.modelscardview);
            Testdrive=itemView.findViewById(R.id.testdrive);

        }
    }

}
