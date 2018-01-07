package com.moming.jml.bakingtime;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jml on 2018/1/1.
 */

public class NetworkTools {

    public static final String RECIPE_URL =
            "https://s3.cn-north-1.amazonaws.com.cn/static-documents/nd801/ProjectResources/Baking/baking-cn.json";
    public static URL buildUrl(){
        Uri uri = Uri.parse(RECIPE_URL).buildUpon().build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url)throws IOException {
        HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput =scanner.hasNext();
            if (hasInput){
                return  scanner.next();
            }else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }


}
