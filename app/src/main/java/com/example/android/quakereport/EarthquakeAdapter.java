package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(Context context, ArrayList<Earthquake> items) {
        super(context, 0, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        Earthquake cureentDataItem = getItem(position);



        TextView magTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        double mag = cureentDataItem.getMagnitude();
        DecimalFormat formater = new DecimalFormat("0.0");
        String magFormated = formater.format(mag);
        magTextView.setText(magFormated);

        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();
        magnitudeCircle.setColor(getMagnitudeColor(cureentDataItem.getMagnitude()));


        String place = cureentDataItem.getLocation();
        String[] placeSplited = place.split(", ");
        String distance, location;
        if(placeSplited.length == 1){
            distance = "Near the";
            location = placeSplited[0];
        }
        else{
            distance = placeSplited[0];
            location = placeSplited[1];
        }

        TextView distanceTextView = listItemView.findViewById(R.id.location_offset);
        distanceTextView.setText(distance);


        TextView locationextView = (TextView) listItemView.findViewById(R.id.primary_location);
        locationextView.setText(location);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(cureentDataItem.getDate());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);

        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);


        TextView timeView = (TextView) listItemView.findViewById(R.id.time);

        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);

        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        return listItemView;
    }


    private int getMagnitudeColor(double magnitude) {
        int maginudeColorResouseId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                maginudeColorResouseId = R.color.magnitude1;
                break;
            case 2:
                maginudeColorResouseId = R.color.magnitude2;
                break;
            case 3:
                maginudeColorResouseId = R.color.magnitude3;
                break;
            case 4:
                maginudeColorResouseId = R.color.magnitude4;
                break;
            case 5:
                maginudeColorResouseId = R.color.magnitude5;
                break;
            case 6:
                maginudeColorResouseId = R.color.magnitude6;
                break;
            case 7:
                maginudeColorResouseId = R.color.magnitude7;
                break;
            case 8:
                maginudeColorResouseId = R.color.magnitude8;
                break;
            case 9:
                maginudeColorResouseId = R.color.magnitude9;
                break;
            default: maginudeColorResouseId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(),maginudeColorResouseId);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
