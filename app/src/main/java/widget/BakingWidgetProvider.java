package widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RemoteViews;

import tech.niocoders.com.bakingapp.BakingActivity;
import tech.niocoders.com.bakingapp.FoodDescription;
import tech.niocoders.com.bakingapp.Preference;
import tech.niocoders.com.bakingapp.R;
import tech.niocoders.com.fooddatabase.BakingContract;

public class BakingWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,String food_name, String food_id, int appWidgetId) {

        Intent launcher;
        if(!TextUtils.isEmpty(food_id) && !TextUtils.isEmpty(food_name)) {
            launcher = new Intent(context, FoodDescription.class);
            launcher.putExtra(BakingActivity.FOOD_NAME, food_name);
            launcher.putExtra(BakingActivity.FOOD_ID,food_id);

        }else {
            launcher = new Intent(context, BakingActivity.class);
        }

        PendingIntent pendingIntent  = PendingIntent.getActivity(context, 0, launcher, 0);

        RemoteViews myView = new RemoteViews(context.getPackageName(),R.layout.food_widget);
        myView.setTextViewText(R.id.widget_food_name,food_name);
        myView.setTextViewText(R.id.ingredients,getIngredientsBaseOnFoodSelected(context));

        myView.setOnClickPendingIntent(R.id.widget_food_name,pendingIntent);
        myView.setOnClickPendingIntent(R.id.ingredients,pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, myView);



    }

    public static  String getIngredientsBaseOnFoodSelected(Context context)
    {
        StringBuilder textIngredients = new StringBuilder();
        String id = Preference.getPreferenceFoodId(context);
        String [] selectionArgs =  {id};
        Cursor mCursor = context.getContentResolver().query(
                BakingContract.IngredientsEntry.CONTENT_URI, null,
                BakingContract.IngredientsEntry.COLUMN_FOOD_ID+"=?",
                selectionArgs,
                null
        );

        for(int i=0; i<mCursor.getCount(); i++)
        {
            mCursor.moveToPosition(i);
            String description =  mCursor.getString(mCursor.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_INGREDIENT));
            String measure =  mCursor.getString(mCursor.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_MEASURE));
            String quantity =  mCursor.getString(mCursor.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_QUANTITY));
            textIngredients.append(description+" "+measure+" "+quantity+"\n");
        }

        mCursor.close();
        return textIngredients.toString();
    }

    public static void updateFoodWidgets(Context context, AppWidgetManager appWidgetManager,String food_name,String food_id, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, food_name,food_id, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,Preference.getPreferenceFoodName(context),Preference.getPreferenceFoodId(context), appWidgetId);
        }
    }


   /* private static RemoteViews getSingleBakingRemoteView(Context context, String food_id) {
        // Set the click handler to open the DetailActivity for plant ID,
        // or the MainActivity if plant ID is invalid
        Intent intent;
        if (TextUtils.isEmpty(food_id)) {
            intent = new Intent(context, BakingActivity.class);
        } else { // Set on click to open the corresponding detail activity
            Log.d(BakingWidgetProvider.class.getSimpleName(), "food_id=" + food_id);
            intent = new Intent(context, FoodDescription.class);
            intent.putExtra(BakingActivity.FOOD_ID, food_id);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.food_widget);
        // Update image and text
        views.setImageViewResource(R.id.widget_ingredient_button, R.drawable.ic_launcher_background);
        views.setTextViewText(R.id.food_name, "food_id "+food_id);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_food_item, pendingIntent);

        return views;
    }*/


   /* private static RemoteViews getFoodListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_view);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context,BakingWidgetService.class);
        views.setRemoteAdapter(R.id.widget_ingredients_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, FoodDescription.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_ingredients_view, appPendingIntent);
        // Handle empty gardens
       // views.setEmptyView(R.id.widget_ingredients_view, R.id.empty_view);
        return views;
    }*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        String food_id =  Preference.getPreferenceFoodId(context);
        String food_name = Preference.getPreferenceFoodName(context);
        BakingWidgetService.startBakingIngredients(context, food_id, food_name);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}
