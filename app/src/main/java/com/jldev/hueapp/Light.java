package com.jldev.hueapp;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by Jeffrey on 8-12-2017.
 */

public class Light implements Serializable {
    public String id;
    public float Sat,Hue,Bri;
    public boolean On;
}
