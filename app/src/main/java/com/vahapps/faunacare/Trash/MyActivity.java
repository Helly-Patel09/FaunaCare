/*
package com.vahapps.faunacare.Trash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vahapps.faunacare.AppLocationService;
import com.vahapps.faunacare.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyActivity extends Activity {

    Button btnGPSShowLocation;
    Button btnShowAddress;
    TextView tvAddress;
    AppLocationService appLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        tvAddress =  findViewById(R.id.tvAddress);
        appLocationService = new AppLocationService(MyActivity.this);
        btnGPSShowLocation =  findViewById(R.id.btnGPSShowLocation);
        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
                if (gpsLocation != null) {
                    //double latitude = gpsLocation.getLatitude();
                    //double longitude = gpsLocation.getLongitude();
                    String result = "Latitude: " + gpsLocation.getLatitude() + " Longitude: " + gpsLocation.getLongitude();
                    Log.wtf("gps",result);
                    tvAddress.setText(result);
                } else {
                    showSettingsAlert();
                }
            }
        });

        btnShowAddress = findViewById(R.id.btnShowAddress);
        btnShowAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);

                //you can hard-code the lat & long if you have issues with getting it
                //remove the below if-condition and use the following couple of lines
                //double latitude = 37.422005;
                //double longitude = -122.084095

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());
                } else {
                    showSettingsAlert();
                }
            }
        });
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MyActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            tvAddress.setText(locationAddress);
        }
    }
    public static class LocationAddress {

        private static final String TAG = "com.gpslocation.MainActivity.LocationAddress";

        public static void getAddressFromLocation(final double latitude, final double longitude,
                                                  final Context context, final Handler handler) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    String result = null;
                    try {
                        List<Address> addressList = geocoder.getFromLocation(
                                latitude, longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append("\n");
                            }
                            sb.append(address.getLocality()).append("\n");
                            sb.append(address.getPostalCode()).append("\n");
                            sb.append(address.getCountryName());
                            result = sb.toString();
                        }
                    } catch (IOException e) {
                        //Log.e(TAG, "Unable connect to Geocoder", e);
                    } finally {
                        Message message = Message.obtain();
                        message.setTarget(handler);
                        if (result != null) {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            result = "Latitude: " + latitude + " Longitude: " + longitude +
                                    "\n\nAddress:\n" + result;
                            bundle.putString("address", result);
                            message.setData(bundle);
                        } else {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            result = "Latitude: " + latitude + " Longitude: " + longitude +
                                    "\n Unable to get address for this lat-long.";
                            bundle.putString("address", result);
                            message.setData(bundle);
                        }
                        message.sendToTarget();
                    }
                }
            };
            thread.start();
        }
    }
}
*/
