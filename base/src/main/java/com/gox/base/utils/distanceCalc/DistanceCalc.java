package com.gox.base.utils.distanceCalc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DistanceCalc {

    String downloadData(String strUrl) throws IOException {
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
