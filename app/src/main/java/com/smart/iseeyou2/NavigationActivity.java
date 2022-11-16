package com.smart.iseeyou2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Map;

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

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        settings = getApplicationContext().getSharedPreferences("Camera", 0);

        updateCameraList();
        setAdapter();

        addButton.setOnClickListener(view -> {
            createNewDialog();
            activateAddDialog();
        });

    }

    private void updateCameraList() {
        for (Map.Entry<String, ?> entry : settings.getAll().entrySet()){
            cameraList.add(new Camera(entry.getKey(), (String) entry.getValue()));
        }
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
        popup_ipAddress.setHint("IP Address");
        popup_name.setHint("Location");
        popup_save.setText("add");
        popup_save.setOnClickListener(view -> {
            String IPAddress = popup_ipAddress.getText().toString();
            String name = popup_name.getText().toString();
            //shared preferences
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(IPAddress, name);
            editor.apply();
            //camera list
            cameraList.add(new Camera(IPAddress, name));
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
            String IPAddress = popup_ipAddress.getText().toString();
            String name = popup_name.getText().toString();
            //shared preferences
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(cameraList.get(position).getIPAddress());
            editor.putString(IPAddress, name);
            editor.apply();
            //camera list
            cameraList.get(position).setName(name);
            cameraList.get(position).setIPAddress(IPAddress);
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
        //shared preferences
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(cameraList.get(position).getIPAddress());
        editor.apply();
        //camera list
        cameraList.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }
}
