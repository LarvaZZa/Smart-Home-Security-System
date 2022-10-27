package com.smart.iseeyou2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NavigationActivity extends Activity implements RecyclerAdapter.OnItemClickListener {

    public static int GLOBAL_PORT = 8088;
    private ArrayList<Camera> cameraList = new ArrayList<>();
    private FloatingActionButton addButton;

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popup_ipAddress, popup_name;
    private Button popup_save, popup_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);

        setCameraList();
        setAdapter();

        addButton.setOnClickListener(view -> {
            createNewDialog();
            activateAddDialog();
        });
    }

    private void setCameraList() {
        cameraList.add(new Camera("130.89.143.216", "Living room"));
        cameraList.add(new Camera("130.89.173.123", "Work room"));
    }

    private void setAdapter() {
        recyclerAdapter = new RecyclerAdapter(cameraList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void createNewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopup = getLayoutInflater().inflate(R.layout.popup, null);
        popup_name = contactPopup.findViewById(R.id.popup_name);
        popup_ipAddress = contactPopup.findViewById(R.id.popup_ipAddress);
        popup_save = contactPopup.findViewById(R.id.popup_save);
        popup_cancel = contactPopup.findViewById(R.id.popup_cancel);

        dialogBuilder.setView(contactPopup);
        dialog = dialogBuilder.create();
    }

    public void activateAddDialog(){
        dialog.show();
        popup_save.setText("add");
        popup_save.setOnClickListener(view -> {
            cameraList.add(new Camera(popup_ipAddress.getText().toString(), popup_name.getText().toString()));
            recyclerAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        popup_cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

    public void activateEditDialog(int position){
        dialog.show();
        popup_name.setText(cameraList.get(position).getName());
        popup_ipAddress.setText(cameraList.get(position).getIPAddress());
        popup_save.setOnClickListener(view -> {
            cameraList.get(position).setName(popup_name.getText().toString());
            cameraList.get(position).setIPAddress(popup_ipAddress.getText().toString());
            recyclerAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        popup_cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }


    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(this, CameraActivity.class).putExtra("target",cameraList.get(position).getIPAddress()));
    }

    @Override
    public void onEditClick(int position) {
        createNewDialog();
        activateEditDialog(position);
    }

    @Override
    public void onRemoveClick(int position) {
        createNewDialog();
        cameraList.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }
}
