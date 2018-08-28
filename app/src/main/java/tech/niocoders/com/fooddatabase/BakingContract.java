package tech.niocoders.com.fooddatabase;

import android.net.Uri;
import android.provider.BaseColumns;

public class BakingContract  {

    /*
    lets construct a database to hold
    the food database and maintain UI
    populated even if there is no Network connectivity
     */
 public static final String AUTHORITY = "tech.niocoders.com.fooddatabase";
 //our base content uri that will point to our database schema
 public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
 //this is our path to food table
 public static final String PATH_FOOD = "food";
 //this is our path to ingredients table
 public static final String PATH_INGREDIENTS =  "ingredients";
 //this is our path to steps table
 public static final String PATH_STEPS  = "cooking_steps";


 //this food entry columns attributes will be used to reference the food entry data base table
 public static final class FoodEntry implements BaseColumns
 {
     public static final Uri CONTENT_URI =
             BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD).build();
     public static final String COLUMN_IMAGE = "image";
     public static final String COLUMN_AUTHOR = "author";
     public static final String COLUMN_SERVINGS = "servings";
     public static final String COLUMN_ID="id";
     public static final String COLUMN_FOOD_NAME = "name";

 }

 //our ingredient entry class to reference our ingredients data base
 public static final class IngredientsEntry implements  BaseColumns
 {
     public static final Uri CONTENT_URI =
             BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

     public static final String COLUMN_FOOD_ID = "food_id";
     public static final String COLUMN_FOOD_QUANTITY = "quantity";
     public static final String COLUMN_FOOD_MEASURE = "measure";
     public static final String COLUMN_FOOD_INGREDIENT  ="ingredient";

 }

 //our step table for food
    public static final class StepsEntry implements BaseColumns{

     public static final Uri CONTENT_URI =
             BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

     //we will provide an step id so that we can sort our database on ascending order
     public static final String COLUMN_STEP_NUMBER = "step";
     public static final String COLUMN_STEP_SHORTDESC  = "shortDesc";
     public static final String COLUMN_STEP_DESCRIPTION = "description";
     public static final String COLUMN_STEP_VIDEO_URL  = "video_url";
     public static final String COLUMN_STEP_THUMBNAIL  =" thumbNail";
     public static final String COLUMN_STEP_FOOD_ID = "food_id";
 }



}
