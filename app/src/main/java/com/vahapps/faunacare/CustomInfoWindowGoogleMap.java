package com.vahapps.faunacare;

/**
 * Created by Vaibhavi on 07-Apr-18.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        TextView vetname = view.findViewById(R.id.infovet);
        TextView add = view.findViewById(R.id.infoadd);
        //ImageView img = view.findViewById(R.id.pic);

        TextView hosp = view.findViewById(R.id.infohosp);
        TextView far = view.findViewById(R.id.infofar);
        //TextView transport_tv = view.findViewById(R.id.transport);
        if (marker.getTag().equals("Fauna")){
            vetname.setText("Your Fauna");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, 0);
            hosp.setLayoutParams(param);
            hosp.setVisibility(View.INVISIBLE);
            far.setLayoutParams(param);
            far.setVisibility(View.INVISIBLE);
            add.setLayoutParams(param);
            add.setVisibility(View.INVISIBLE);
        }else if (marker.getTag().equals("FaunaLoc")){
            vetname.setText(marker.getTitle());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, 0);
            hosp.setLayoutParams(param);
            hosp.setVisibility(View.INVISIBLE);
            far.setLayoutParams(param);
            far.setVisibility(View.INVISIBLE);
            add.setLayoutParams(param);
            add.setVisibility(View.INVISIBLE);
        }else {
            Vet vetObj = (Vet) marker.getTag();
            //vetname.setText(marker.getTitle());
            //add.setText(marker.getSnippet());
            //int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),"drawable", context.getPackageName());
            //img.setImageResource(imageId);
            vetname.setText("Dr. "+vetObj.getName());
            add.setText(vetObj.getHospitaladd());
            Double distance= Double.valueOf(marker.getSnippet());

            if (distance<1){
                distance=Math.floor(distance*100)/100;
                far.setText("at only "+distance*1000+" Meters from Fauna");
            }
            else {
                distance=Math.floor(distance*10)/10;
                far.setText("at "+distance+" KMs from Fauna");
            }
            hosp.setText(vetObj.getHospital());

            //food_tv.setText(infoWindowData.getFood());
            //transport_tv.setText(infoWindowData.getTransport());


        }
        return view;
    }
}
