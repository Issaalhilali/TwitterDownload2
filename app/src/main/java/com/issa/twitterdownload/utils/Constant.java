package com.issa.twitterdownload.utils;

import android.Manifest;

public class Constant {

    public static final String TWITTER_KEY = "K0w8rlDCB6zBB739TGt1BLY2n";
    public static final String TWITTER_SECRET = "3dk9oqc7CQoI90fCyk9JcZEvS88bvkP1YHxI3ylyorl1cNaD5H";

    // Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static final String FIRSTRUN = "firstrun";
    public static final int NOTI_IDENTIFIER = 3100;
    public static final int REQUEST_CODE = 20;
    public static final int AUTO_REQUEST_CODE = 30;


}
