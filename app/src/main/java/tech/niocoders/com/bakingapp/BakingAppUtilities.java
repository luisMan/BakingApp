package tech.niocoders.com.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.niocoders.com.task.Food;
import tech.niocoders.com.task.FoodClient;
import tech.niocoders.com.task.FoodEndPoint;
import tech.niocoders.com.task.Ingredients;
import tech.niocoders.com.task.Steps;

public class BakingAppUtilities {
    public static final String TAG =  BakingAppUtilities.class.getSimpleName();
    public static final String JSON_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static List<Food> foods =  new ArrayList<>();

    //The method is intended to check connection
    //it returns true if phone has active connection else false
    public static boolean IsNetworkAvailable(Context context)
    {
        ConnectivityManager cn =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni =  cn.getActiveNetworkInfo();
       return  ni != null && ni.isConnectedOrConnecting();
    }


    //construct a Network Url with the static link
    public static URL getNetworkUri(String link)
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

    //read all text method
    public static String readAllText(Reader rd) throws IOException
    {
        StringBuilder string = new StringBuilder();
        int index;
        while((index = rd.read())!=-1)
        {
            string.append((char)index);
        }
        return string.toString();
    }

    //read from json and return json
    public static String readJSonFromUrl(String url) throws IOException, JSONException
    {
        InputStream is =  new URL(url).openStream();
        try{
            BufferedReader rd  =  new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String JsonText =  readAllText(rd);
           // JSONObject json =  new JSONObject(JsonText);
            return JsonText;

        }finally {
            is.close();
        }

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


    //lets make an http call with retorsfit to get the user json data
    public static void ExecuteRetrosfitCall(final Context context)
    {
        FoodEndPoint api = FoodClient.getFoodEndPointService();

        /**
         * Calling JSON
         */
        Call<List<Food>> call = api.getMyJSON();

        /**
         * Enqueue Callback will be call when get response...
         */
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {


                if(response.isSuccessful()) {
                    /**
                     * Got Successfully and try to store item to our data base
                     */
                      foods = response.body();
                      for(Food f : foods)
                    {
                       Log.v("food", f.getName()+" "+f.getId()+" "+f.getImage()+" "+f.getServings());
                        for(Ingredients d : f.getIngredients())
                        {

                               Log.v("foodIngre", d.getQuantity()+" "+d.getIngredient()+" "+d.getMeasure());

                        }
                        for(Steps d : f.getSteps())
                        {

                            Log.v("foodSteps", d.getDescription()+" "+d.getShortDescription()
                                    +" "+d.getThumbnailURL()+d.getVideoURL()+" "+d.getId());

                        }
                    }

                } else {
                    Toast.makeText(context,"There was something wrong with the String",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                //Toast.makeText(context,"There was something wrong with the String"+call.toString()+" "+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.v("ErrorResponse", call.toString()+" "+t.getMessage());

            }
        });

    }

    // this great method will be in charge of saving content to data base.
    public static void InsertDataToDataBaseIfNotExists(List<Food> dataToInsert)
    {
        
    }



    public static int calculateBestSpanCount(Context context, int posterWidth) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }
}
