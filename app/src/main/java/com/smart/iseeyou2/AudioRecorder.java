package com.smart.iseeyou2;


import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class AudioRecorder implements Runnable {

    private final Context context;
    private Socket socket;
    private final String IP;
    private final int port;

    private static final int RECORDER_SAMPLERATE = 44000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    private final int BytesPerElement = 2; // 2 bytes in 16bit format

    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    boolean recording = true;
    private File recordingFile;
    private File convertedFile;

    public AudioRecorder(String IP, int port, Context context) {
        this.IP = IP;
        this.port = port;
        this.context = context;
    }

    @Override
    public void run() {
        try{
            socket = new Socket(IP, port);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        if(recording){
            startRecording();
        }
    }

    public void startRecording() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);
        recorder.startRecording();
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();

    }

    private void writeAudioDataToFile() {
        short[] sData = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(getRecordingFilePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (recording) {
            recorder.read(sData, 0, BufferElements2Rec);
            try {
                byte[] bData = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        convertedFile = new File(new ContextWrapper(context).getExternalFilesDir(Environment.DIRECTORY_MUSIC),"temp"+".wav" ); // The location where you want your WAV file
        try {
            new rawToWaveConverter().rawToWave(recordingFile, convertedFile, RECORDER_SAMPLERATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendAudio();
    }

    private byte[] short2byte(short[] sData) {
        int shortArrSize = sData.length;
        byte[] bytes = new byte[shortArrSize * 2];
        for (int i = 0; i < shortArrSize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    public void sendAudio(){
        System.out.println("Sending audio to camera");
        try {
            InputStream fileInputStream = new FileInputStream(convertedFile.getPath());
            OutputStream outputStream = socket.getOutputStream();
            byte[] buffer = new byte[4096];
            for (int bytesRead = fileInputStream.read(buffer); bytesRead != -1; bytesRead = fileInputStream.read(buffer)) {
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Audio sent 100%");
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        recordingFile = new File(musicDirectory, "temp"+".pcm");
        return recordingFile.getPath();
    }

    public void stopRecording() {
        if (null != recorder) {
            recording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }
    }

}
