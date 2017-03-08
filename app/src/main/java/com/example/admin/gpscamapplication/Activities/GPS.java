package com.example.admin.gpscamapplication.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.admin.gpscamapplication.R;


public class GPS extends AppCompatActivity {

    private SensorManager sensorManager;
    LocationManager locationManager;
    LocationListener locationListener;
    TextView dataGPS;
    TextView previousDataGPS;

    private double latitude;
    private double longitude;
    private double altitude;
    private static final String LKL = "LAST_KNOWN_LOCATION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ONCREATE ","BEING INVOKED");

        setContentView(R.layout.activity_gps);
        dataGPS = (TextView) findViewById(R.id.dataGPS);
        previousDataGPS = (TextView) findViewById(R.id.previousDataGPS);

        //concatenation problem?
        previousDataGPS.setText("LAST KNOWN LOCATION:\n"
                + "Latitude: "    + String.format("%.5f",latitude )
                + "\nLongitude: " + String.format("%.5f",longitude)
                + "\nAltitude: "  + String.format("%.5f",altitude ));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latitude  = location.getLatitude();
                longitude = location.getLongitude();
                altitude = location.getAltitude();
                //concatenation problem?
                previousDataGPS.setText("LAST KNOWN LOCATION:\n" + dataGPS.getText());
                dataGPS.setText("Latitude: "    + String.format("%.5f",latitude )
                        + "\nLongitude: " + String.format("%.5f",longitude)
                        + "\nAltitude: "  + String.format("%.5f",altitude ) );
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences lastKnownLoc = this.getSharedPreferences("key",0);
        latitude  = lastKnownLoc.getFloat("latitude" ,0);
        longitude = lastKnownLoc.getFloat("longitude",0);
        altitude  = lastKnownLoc.getFloat("altitude" ,0);
        previousDataGPS.setText("LAST KNOWN LOCATION:\n"
                + "Latitude: "    + String.format("%.5f",latitude )
                + "\nLongitude: " + String.format("%.5f",longitude)
                + "\nAltitude: "  + String.format("%.5f",altitude ) );
        startLocUpdates();
    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences lastKnownLoc = this.getSharedPreferences("key",0);
        SharedPreferences.Editor lastKnownLocEditor = lastKnownLoc.edit();
        lastKnownLocEditor.putString("last_known_location",dataGPS.getText().toString());
        lastKnownLocEditor.putFloat("latitude", (float)  latitude);
        lastKnownLocEditor.putFloat("longitude", (float) longitude);
        lastKnownLocEditor.putFloat("altitude", (float)  altitude);
        lastKnownLocEditor.commit();
        stopLocUpdates();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SharedPreferences lastKnownLoc = this.getSharedPreferences("key",0);
        SharedPreferences.Editor lastKnownLocEditor = lastKnownLoc.edit();
        lastKnownLocEditor.putString("last_known_location",dataGPS.getText().toString());
        lastKnownLocEditor.putFloat("latitude", (float)  latitude);
        lastKnownLocEditor.putFloat("longitude", (float) longitude);
        lastKnownLocEditor.putFloat("altitude", (float)  altitude);
        lastKnownLocEditor.commit();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }
    private void startLocUpdates(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET},2);
            }
            return;
        }
        else
        {
            locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
        }
    }

    private void stopLocUpdates(){
        locationManager.removeUpdates(locationListener);
    }

}
