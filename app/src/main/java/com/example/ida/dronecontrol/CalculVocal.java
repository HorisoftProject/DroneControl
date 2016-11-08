package com.example.ida.dronecontrol;

/**
 * Created by mathieukostiuk on 08/11/2016.
 */

public class CalculVocal {

    public native float dtw();

    static {
        System.loadLibrary("lol");
    }

}
