package tech.niocoders.com.bakingapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.niocoders.com.fooddatabase.BakingContract;
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

    public static URL getFoodUrl(String id,String farm_id,String server,String secret_key)
    {
        String link = "https://farm"+farm_id+".staticflickr.com/"+server
                +"/"+id+"_"+secret_key+"_m.jpg";

        Uri builtUri = Uri.parse(link).buildUpon()
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
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

                       InsertDataToDataBaseIfNotExists(context,foods);


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
    public static void InsertDataToDataBaseIfNotExists(Context context, List<Food> dataToInsert)
    {

        for(Food food : dataToInsert)
        {
            //am going to compare two columns relation to see if there is such item on my database
            //am thinking on scaling the application and allowing any body to create cooking tutorials therefore, there may be tutorials with same titles
            //but with different authors
            String food_name =  food.getName();
            String author_name = " ";
            Uri BakingUri = BakingContract.FoodEntry.CONTENT_URI;
            String [] SelectionArgs = {food_name};

            Cursor  retCursor  =  context.getContentResolver().query(BakingUri,null,
                    BakingContract.FoodEntry.COLUMN_FOOD_NAME+"=?",SelectionArgs,null);
            if((retCursor!=null && retCursor.moveToFirst())
                    && food_name.equals(retCursor.getString(retCursor.getColumnIndex(BakingContract.FoodEntry.COLUMN_FOOD_NAME)).toString()))
            {
                //Toast.makeText(context,food_name+" is already on DataBase",Toast.LENGTH_LONG).show();

            }else{
                //lets now Add the values to our DataBase
                ContentValues food_values =  new ContentValues();
                food_values.put(BakingContract.FoodEntry.COLUMN_AUTHOR ,author_name);
                food_values.put(BakingContract.FoodEntry.COLUMN_FOOD_NAME,food_name);
                food_values.put(BakingContract.FoodEntry.COLUMN_IMAGE,food.getImage());
                food_values.put(BakingContract.FoodEntry.COLUMN_SERVINGS, food.getServings());

                //add the food to database
                Uri food_added =  context.getContentResolver().insert(BakingContract.FoodEntry.CONTENT_URI,food_values);
                if(food_added!=null)
                {
                    //we just added a new food tutorial to data base lets add the ingredients and steps
                    //since this tables are Foreign key we need to insert also the id of this food item
                    Cursor  justAdd  =  context.getContentResolver().query(BakingUri,null,
                            BakingContract.FoodEntry.COLUMN_FOOD_NAME+"=?",SelectionArgs,null);
                    if(justAdd!=null && justAdd.moveToFirst())
                    {
                        int id =  justAdd.getInt(justAdd.getColumnIndex(BakingContract.FoodEntry.COLUMN_ID));
                        Log.d("Food"," for food "+food.getName());
                        for(Ingredients ing : food.getIngredients())
                        {
                            ContentValues ingredients =  new ContentValues();
                            ingredients.put(BakingContract.IngredientsEntry.COLUMN_FOOD_ID, id);
                            ingredients.put(BakingContract.IngredientsEntry.COLUMN_FOOD_INGREDIENT, ing.getIngredient());
                            ingredients.put(BakingContract.IngredientsEntry.COLUMN_FOOD_MEASURE, ing.getMeasure());
                            ingredients.put(BakingContract.IngredientsEntry.COLUMN_FOOD_QUANTITY, ing.getQuantity());

                            Uri ingredientUri  =  context.getContentResolver().insert(BakingContract.IngredientsEntry.CONTENT_URI,ingredients);
                            if(ingredientUri!=null)
                            {
                                Log.d("FoodIngredient"," just Added ingredient "+ing.getIngredient());
                            }
                        }

                        Log.d("Food"," for food "+food.getName());
                        for(Steps st : food.getSteps())
                        {
                            ContentValues steps = new ContentValues();
                            steps.put(BakingContract.StepsEntry.COLUMN_STEP_FOOD_ID, id);
                            steps.put(BakingContract.StepsEntry.COLUMN_STEP_NUMBER, st.getId());
                            steps.put(BakingContract.StepsEntry.COLUMN_STEP_SHORTDESC, st.getShortDescription());
                            steps.put(BakingContract.StepsEntry.COLUMN_STEP_DESCRIPTION, st.getDescription());
                            steps.put(BakingContract.StepsEntry.COLUMN_STEP_THUMBNAIL,st.getThumbnailURL());
                            steps.put(BakingContract.StepsEntry.COLUMN_STEP_VIDEO_URL, st.getVideoURL());
                           // Log.d("contentValue ",steps.toString());

                            Uri stepsUri  =  context.getContentResolver().insert(BakingContract.StepsEntry.CONTENT_URI,steps);
                            if(stepsUri!=null)
                            {
                                Log.d("FoodSteps"," just Added step "+st.getDescription());
                            }
                        }


                    }//close the food item check


                }else{
                    Log.d("ExeptionFromDb", "The food insertion has problems");
                }



            }




        }


    }


    public static int calculateBestSpanCount(Context context, int posterWidth) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }





}
