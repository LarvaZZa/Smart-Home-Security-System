package com.smart.iseeyou2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Camera> cameraList;
    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter(ArrayList<Camera> cameraList, OnItemClickListener onItemClickListener){
        this.cameraList = cameraList;
        this.onItemClickListener = onItemClickListener;
    }
    
    

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameText;
        private Camera camera;
        private OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textCameraName);
            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);

            itemView.findViewById(R.id.editButton).setOnClickListener(view -> {
                onItemClickListener.onEditClick(getAdapterPosition());
            });

            itemView.findViewById(R.id.removeButton).setOnClickListener(view -> {
                onItemClickListener.onRemoveClick(getAdapterPosition());
            });
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //setting camera name to text view
        String name = cameraList.get(position).getName();
        holder.nameText.setText(name);

        //saving the specific camera to the holder
        holder.camera = cameraList.get(position);
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onEditClick(int position);
        void onRemoveClick(int position);
    }

    @Override
    public int getItemCount() {
        return cameraList.size();
    }

}
