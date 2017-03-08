package com.example.admin.gpscamapplication.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.gpscamapplication.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GPSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GPSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GPSFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LocationManager  locationManager;
    private LocationListener locationListener;
    private TextView locationData;
    private OnFragmentInteractionListener mListener;

    public GPSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GPSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GPSFragment newInstance(String param1, String param2) {
        GPSFragment fragment = new GPSFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private double latitude;
    private double longitude;
    private double altitude;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        locationManager  = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationData.setText("PHOTO DATA"
                        + "\nLatitude: "    + String.format("%.5f",location.getLatitude() )
                        + "\nLongitude: "   + String.format("%.5f",location.getLongitude())
                        + "\nAltitude: "    + String.format("%.5f",location.getAltitude() ) );
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gps, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        locationData     = (TextView) getActivity().findViewById(R.id.locationData);
        SharedPreferences lastKnownLoc = getActivity().getSharedPreferences("key",0);
        latitude  = lastKnownLoc.getFloat("latitude" ,0);
        longitude = lastKnownLoc.getFloat("longitude",0);
        altitude  = lastKnownLoc.getFloat("altitude" ,0);
        locationData.setText("LAST KNOWN LOCATION:\n"
                + "Latitude: "    + String.format("%.5f",latitude )
                + "\nLongitude: " + String.format("%.5f",longitude)
                + "\nAltitude: "  + String.format("%.5f",altitude ) );
        startLocUpdates();
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences lastKnownLoc = getActivity().getSharedPreferences("key",0);
        SharedPreferences.Editor lastKnownLocEditor = lastKnownLoc.edit();
        lastKnownLocEditor.putString("last_known_location",locationData.getText().toString());
        lastKnownLocEditor.putFloat("latitude", (float)  latitude);
        lastKnownLocEditor.putFloat("longitude", (float) longitude);
        lastKnownLocEditor.putFloat("altitude", (float)  altitude);
        lastKnownLocEditor.commit();
        stopLocUpdates();
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
 /*   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void startLocUpdates(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
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
