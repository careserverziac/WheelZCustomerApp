package AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.wheelzcustomer.R;

import java.util.List;

import ModelClasses.CommonClass;
import ModelClasses.Global;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.Vehicleviewholder> {

    private final List<CommonClass> commonClassList;
    private final Context context;

    public VehiclesAdapter(List<CommonClass> commonClassList, Context context) {
        this.commonClassList = commonClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public Vehicleviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myvehicle_layout,parent,false);
        VehiclesAdapter.Vehicleviewholder vehicleviewholder=new Vehicleviewholder(view);
        return vehicleviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Vehicleviewholder holder, int position) {

        Global.loadWithPicasso(context, holder.Veh_image, Global.modelsimageurl + commonClassList.get(position).getLogo_image());
        holder.Regis_No.setText(commonClassList.get(position).getRegistrationno());
        holder.Model_name.setText(commonClassList.get(position).getVehiclemodelname());


    }

    @Override
    public int getItemCount() {
        return commonClassList.size();
    }

    public class Vehicleviewholder extends RecyclerView.ViewHolder {
        ImageView Veh_image;
        TextView Regis_No,Model_name,Documents,Service;
        CardView Modelscardview;

        public Vehicleviewholder(@NonNull View itemView) {
            super(itemView);

            Veh_image=itemView.findViewById(R.id.veh_image);
            Regis_No=itemView.findViewById(R.id.manufacture_name);
            Model_name=itemView.findViewById(R.id.model_name);
            Modelscardview=itemView.findViewById(R.id.modelscardview);
            Documents=itemView.findViewById(R.id.documents);
            Service=itemView.findViewById(R.id.service);
        }


    }

}
