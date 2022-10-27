package com.smart.iseeyou2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;

public class CameraActivity extends Activity {

    private WebView webView;
    private AudioRecorder audioRecorder;
    private ImageButton recordButton;
    private String target;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);
        webView = findViewById(R.id.webView);
        recordButton = findViewById(R.id.recordButton);

        setWebView();

        target = getIntent().getExtras().getString("target");
        webView.loadUrl(target+":"+NavigationActivity.GLOBAL_PORT);

        recordButton.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    MediaPlayer.create(this, R.raw.recording1).start();
                    onRecording(v);
                    break;
                case MotionEvent.ACTION_UP:
                    onStopRecording(v);
                    MediaPlayer.create(this, R.raw.recording2).start();
                    break;
            }
            return false;
        });

    }

    private void setWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new Callback());
    }

    public void onRecording(View v){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO }, 1);
        }
        audioRecorder =  new AudioRecorder(target, NavigationActivity.GLOBAL_PORT+1, getApplicationContext());
        new Thread(audioRecorder).start();
    }

    public void onStopRecording(View v){
        if(audioRecorder != null){
            audioRecorder.stopRecording();
        }audioRecorder = null;
    }

    @Override
    public void onBackPressed(){
        webView.destroy();
        startActivity(new Intent(this, NavigationActivity.class));
    }

    private static class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event){
            return false;
        }
    }

}
