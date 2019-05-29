package com.gox.base.utils.polyline;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DirectionUtils {

    public String getDirectionsUrl(LatLng origin, LatLng dest, String key) {

        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String parameters = strOrigin + "&" + strDest + "&" + sensor + "&" + mode;
        String output = "json";

        String  url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters + "&key=" + key;
        System.out.println("RRR Google PolyLine URL = " + url);

        return url;
    }

    String downloadUrl(String strUrl) throws IOException {
        String data = "";
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(strUrl).openConnection();
        urlConnection.connect();
        try (InputStream iStream = urlConnection.getInputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return data;
    }
}
