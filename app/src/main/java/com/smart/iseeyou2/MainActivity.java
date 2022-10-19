package com.smart.iseeyou2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    private ArrayList<Camera> cameraList = new ArrayList<>();;
    private RecyclerView recyclerView;
    private WebView webView;

    private AudioRecorder audioRecorder;

    private String notificationToken = "fwiBZS2yQFG4tvJV5OgSjt:APA91bHV3SRhKGJxh6CFyexodOwls395MpVRCsRYzR52_MxT1FwRxzOovpSRHRK1M-W2r8UNtCXGqjoJtP9UG7ZIJYxyi2i4hgI40fDSFcRa6FVkx2IXyAiWDpJTfjXF8QrE5kaBceI4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        webView = findViewById(R.id.webView);

        setCameraList();
        setAdapter();
        setWebView();
    }

    //only used to generate the token of the app for notifications
    private void generateNotificaitonToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase: ", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        notificationToken = token;
                        Log.d("Firebase: ", token);
                        Toast.makeText(MainActivity.this, "Your device registration token is: " + token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new Callback());
        webView.loadUrl("130.89.143.216:8088");
    }

    private void setAdapter() {
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(cameraList);
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

    public void onRecordingClick(View v){
        audioRecorder =  new AudioRecorder("130.89.143.216", 8089, getApplicationContext());
        new Thread(audioRecorder).start();
    }

    public void onStopRecordingClick(View v){
        audioRecorder.stopRecording();
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event){
            return false;
        }
    }
}