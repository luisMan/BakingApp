package tech.niocoders.com.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;
/*created by luis manon*/
public class Preference {
    public static final String PREFS_ID_NAME = "PREFERENCE_ID";
    public static final String FOOD_ITEM_ID_KEY = "FOOD_ITEM_ID_KEY";
    public static final String FOOD_ITEM_NAME = "FOOD_ITEM_NAME";



    public static void saveFoodIdAndKey(Context context, String  food_id, String name) {
        SharedPreferences.Editor preference = context.getSharedPreferences(PREFS_ID_NAME, Context.MODE_PRIVATE).edit();
        preference.putString(FOOD_ITEM_ID_KEY,food_id);
        preference.putString(FOOD_ITEM_NAME, name);

        preference.apply();
    }

    public static String getPreferenceFoodId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_ID_NAME, Context.MODE_PRIVATE);
        String food_id = preferences.getString(FOOD_ITEM_ID_KEY,"");
        if(food_id.equals(""))
            return null;

        return food_id;
    }

    public static String getPreferenceFoodName(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_ID_NAME, Context.MODE_PRIVATE);
        String food_name = preferences.getString(FOOD_ITEM_NAME,"");
        if(food_name.equals(""))
            return null;

        return food_name;

    }
}
