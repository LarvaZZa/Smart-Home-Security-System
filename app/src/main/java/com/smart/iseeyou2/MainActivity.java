package com.smart.iseeyou2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Camera> cameraList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        cameraList = new ArrayList<>();

        setCameraList();
        setAdapter();
    }

    private void setAdapter() {
        recyclerAdapter recyclerAdapter = new recyclerAdapter(cameraList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setCameraList() {
        cameraList.add(new Camera("130.89.143.216", "8088"));
        cameraList.add(new Camera("130.89.143.216", "8088"));
        cameraList.add(new Camera("130.89.143.216", "8088"));
        cameraList.add(new Camera("130.89.143.216", "8088"));
        cameraList.add(new Camera("130.89.143.216", "8088"));
        cameraList.add(new Camera("130.89.143.216", "8088"));
        cameraList.add(new Camera("130.89.143.216", "8088"));
    }


}