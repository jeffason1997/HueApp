package com.jldev.hueapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Jeffrey on 8-12-2017.
 */

public class DetailedAdapter extends ArrayAdapter<Light> {

    public DetailedAdapter(@NonNull Context context, ArrayList<Light> lights) {
        super(context,0,lights);
    }


    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_lights, parent, false);
        }



        return convertView;
    }
}