package AdapterClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.wheelzcustomer.UploadViewFiles;
import com.ziac.wheelzcustomer.R;
import com.ziac.wheelzcustomer.ServiceHistory;

import java.util.List;

import ModelClasses.CommonClass;
import ModelClasses.Global;

  public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.Vehicleviewholder> {

    private static final int YOUR_REQUEST_CODE =12 ;
    private final List<CommonClass> vehicledetailsList;
    private final Context context;


    public VehiclesAdapter(List<CommonClass> commonClassList, Context context) {
        this.vehicledetailsList = commonClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public Vehicleviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicledetail_layout,parent,false);
        VehiclesAdapter.Vehicleviewholder vehicleviewholder=new Vehicleviewholder(view);
        return vehicleviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Vehicleviewholder holder, @SuppressLint("RecyclerView") int position) {

        Global.loadWithPicasso(context, holder.Veh_image, Global.modelsimageurl + vehicledetailsList.get(position).getImage_path());
        holder.Manufacturer.setText(vehicledetailsList.get(position).getMfg_name());
        holder.Model_name.setText(vehicledetailsList.get(position).getVehiclemodelname());
        holder.Chassisno.setText(vehicledetailsList.get(position).getChassis_no());
        holder.Engineno.setText(vehicledetailsList.get(position).getEngine_no());
        holder.Regisno.setText(vehicledetailsList.get(position).getRegis_no());
        holder.Batteryno.setText(vehicledetailsList.get(position).getBatt_no());
        holder.Vehcolor.setText(vehicledetailsList.get(position).getVcol_name());
        holder.Prvservice.setText(vehicledetailsList.get(position).getPrv_serdt());
        holder.Nextservice.setText(vehicledetailsList.get(position).getNxt_serdt());


        holder.Documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.vehicledetails = vehicledetailsList.get(position);

                UploadViewFiles uploadViewFiles = new UploadViewFiles();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, uploadViewFiles);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        holder.Service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.vehicledetails = vehicledetailsList.get(position);

                ServiceHistory documentFragment = new ServiceHistory();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, documentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }


    @Override
    public int getItemCount() {
        return vehicledetailsList.size();
    }

    public class Vehicleviewholder extends RecyclerView.ViewHolder {
        ImageView Veh_image;
        TextView Manufacturer,Model_name,Chassisno,Engineno,Regisno,Batteryno,Vehcolor,Prvservice,Nextservice,Documents,Service;
        CardView Modelscardview;



        public Vehicleviewholder(@NonNull View itemView) {
            super(itemView);

            Veh_image=itemView.findViewById(R.id.veh_image);
            Manufacturer =itemView.findViewById(R.id.manufacturerMV);
            Model_name=itemView.findViewById(R.id.modelnameMV);
            Chassisno=itemView.findViewById(R.id.chasis_no);
            Engineno=itemView.findViewById(R.id.engine_no);
            Regisno=itemView.findViewById(R.id.reg_no);
            Batteryno=itemView.findViewById(R.id.batt_no);
            Vehcolor=itemView.findViewById(R.id.vcol_name);
            Prvservice=itemView.findViewById(R.id.prv_serdt);
            Nextservice=itemView.findViewById(R.id.nxt_serdt);
            Modelscardview=itemView.findViewById(R.id.modelscardview);
            Documents=itemView.findViewById(R.id.documents);
            Service=itemView.findViewById(R.id.servicehistory);
        }
    }



}


