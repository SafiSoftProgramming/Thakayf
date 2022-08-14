package com.thakaif.root.thaqayif;

import android.content.Context;
import java.net.InetAddress;


public class DetectInternet {


        public static boolean isConnected(Context context) {


            try {
                InetAddress ipAddr = InetAddress.getByName("google.com");
                return !ipAddr.equals("");
            } catch (Exception e) {
                return false;
            }
        }



}
