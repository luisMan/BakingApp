package tech.niocoders.com.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class BakingAppUtilities {
    public static final String JSON_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    //The method is intended to check connection
    //it returns true if phone has active connection else false
    public static boolean IsNetworkAvailable(Context context)
    {
        ConnectivityManager cn =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni =  cn.getActiveNetworkInfo();
       return  ni != null && ni.isConnectedOrConnecting();
    }


    //construct a Network Url with the static link
    public URL getNetworkUri(String link)
    {
        Uri buildUri =  Uri.parse(JSON_URL)
                .buildUpon().build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseJsonStringFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection urlConnection =  (HttpURLConnection) url.openConnection();
        try{
            InputStream in =  urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }

        }finally {
            urlConnection.disconnect();
        }
    }
}
