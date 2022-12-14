package com.vms.antplay.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.vms.antplay.R;

import com.vms.antplay.interfaces.ShutdownVMListener;
import com.vms.antplay.model.responseModal.GetVMResponseModal;
import com.vms.antplay.utils.Const;
import com.vms.antplay.utils.SharedPreferenceUtils;
import com.vms.antplay.vnc.MainVNCActivity;

import java.util.List;

public class Computer_available_Adapter extends RecyclerView.Adapter<Computer_available_Adapter.MyViewHolder> {

    private Context context;
    private List<GetVMResponseModal.Datum> computer_availableModals;
    private ShutdownVMListener vmTimeListener;


    public Computer_available_Adapter(Context context, List<GetVMResponseModal.Datum> computer_availableModals, ShutdownVMListener vmTimeListener) {
        this.context = context;
        this.computer_availableModals = computer_availableModals;
        this.vmTimeListener = vmTimeListener;
    }

    @NonNull
    @Override
    public Computer_available_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.computers_available_list, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Computer_available_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetVMResponseModal.Datum modal = computer_availableModals.get(position);
        holder.computerName.setText(modal.getVmname());
        holder.computerDesc.setText(modal.getStatus());
      //  holder.computerImage.setImageResource(modal.getImage());

        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Clicked"+position, Toast.LENGTH_SHORT).show();
                SharedPreferenceUtils.saveLong(context, Const.VM_ID, modal.getVmid());
                SharedPreferenceUtils.saveLong(context, Const.REMAINING_TIME,modal.getTimeRemaining());
                Intent intent = new Intent(context, MainVNCActivity.class);
                context.startActivity(intent);
            }
        });

        holder.btnShutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call Shutdown VM API here
                vmTimeListener.shutDownVMRequest(modal.getVmid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return computer_availableModals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView computerName;
        ImageView computerImage;
        TextView computerDesc;
        Button btn_share,btnShutdown;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            computerName = (TextView) itemView.findViewById(R.id.comp_name);
            computerDesc = (TextView) itemView.findViewById(R.id.comp_desc);
            computerImage = (ImageView) itemView.findViewById(R.id.comp_image);
            btn_share = (Button) itemView.findViewById(R.id.comp_btn);
            btnShutdown = (Button) itemView.findViewById(R.id.comp_btn_shutdown);
        }
    }
}
