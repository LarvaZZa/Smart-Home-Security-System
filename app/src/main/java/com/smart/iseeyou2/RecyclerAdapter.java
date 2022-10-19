package com.smart.iseeyou2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Camera> cameraList;

    public RecyclerAdapter(ArrayList<Camera> cameraList){
        this.cameraList = cameraList;
    }
    
    

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView IPAddressText;
        private TextView portText;
        private Camera camera;

        public MyViewHolder(View itemView) {
            super(itemView);
            IPAddressText = itemView.findViewById(R.id.IPAddressText);
            portText = itemView.findViewById(R.id.portText);

            itemView.findViewById(R.id.liveFootageButton).setOnClickListener(view -> {
                Log.d("demo", ""+camera.getIPAddress());
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        //setting IPAddress to text view
        String IPAddress = cameraList.get(position).getIPAddress();
        holder.IPAddressText.setText(IPAddress);

        //setting port to text view
        String port = cameraList.get(position).getPort();
        holder.portText.setText(port);

        //saving the specific camera to the holder
        holder.camera = cameraList.get(position);
    }

    @Override
    public int getItemCount() {
        return cameraList.size();
    }
}
