package com.jldev.hueapp.DetailedLight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jldev.hueapp.Light;
import com.jldev.hueapp.R;

import java.util.ArrayList;

/**
 * Created by Jeffrey on 8-12-2017.
 */

public class DetailedAdapter extends ArrayAdapter<Light> {

    public DetailedAdapter(@NonNull Context context, ArrayList<Light> lights) {
        super(context,0,lights);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Light current = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_lights, parent, false);
        }


        TextView ID = (TextView) convertView.findViewById(R.id.id_tv);
        ID.setText(current.id);

        TextView status = (TextView) convertView.findViewById(R.id.status_tv);
        String on = null;
        if(current.On){
            on = "On";
        } else {
            on = "Off";
        }
        status.setText(on);

        TextView tHue = (TextView) convertView.findViewById(R.id.valueHue_tv);
        String hue = current.Hue + "";
        tHue.setText(hue);

        TextView tBri = (TextView) convertView.findViewById(R.id.valueBri_tv);
        String bri = current.Bri + "";
        tBri.setText(bri);

        TextView tSat = (TextView) convertView.findViewById(R.id.valueSat_tv);
        String sat = current.Sat + "";
        tSat.setText(sat);

        return convertView;
    }
}