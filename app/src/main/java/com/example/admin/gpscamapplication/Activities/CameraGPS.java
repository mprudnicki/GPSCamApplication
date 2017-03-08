package com.example.admin.gpscamapplication.Activities;



import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.example.admin.gpscamapplication.Backend.CameraPreview;
import com.example.admin.gpscamapplication.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class CameraGPS extends FragmentActivity {


    private Camera camera;
    private CameraPreview cameraPreview;
    private Camera.PictureCallback pictureCallback;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_gps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }

        cameraPreview = new CameraPreview(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(cameraPreview);

        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if(pictureFile == null) return;
                try{
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
        } catch(Exception e){
            Log.e("CAMERA","FAILED TO OPEN");
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(camera != null)
        {
            camera.stopPreview();
            camera.release();
        }
    }


    public void takePhoto(View view) {
        Log.i("CAMERA", "CAMERA IS f" + camera.toString());
        camera.takePicture(null,null,pictureCallback);
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                                                                                     "CamGPSApplication");
        }
        if(! mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("CamGSPApplication","failed to create directory");
            return null;

        }

        String timeStamp = String.format("yyyyMMdd_HHmmss",new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
        try {
            mediaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaFile;
    }

    protected Camera getCameraInstance(){
        Camera c = null;
        try
        {
            c = Camera.open();
        } catch (Exception e){}
        return c;
    }


}
