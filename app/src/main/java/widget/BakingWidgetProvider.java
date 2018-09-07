package widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import tech.niocoders.com.bakingapp.BakingActivity;
import tech.niocoders.com.bakingapp.FoodDescription;
import tech.niocoders.com.bakingapp.Preference;
import tech.niocoders.com.bakingapp.R;

public class BakingWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,String food_name, String food_id, int appWidgetId) {

        Intent launcher;
        if(!TextUtils.isEmpty(food_id) && !TextUtils.isEmpty(food_name)) {
            Log.d(BakingWidgetProvider.class.getSimpleName(), "food_id=" + food_id+" food_name = "+food_name);
            launcher = new Intent(context, FoodDescription.class);
            launcher.putExtra(BakingActivity.FOOD_NAME, food_name);
            launcher.putExtra(BakingActivity.FOOD_ID,food_id);

        }else {
            launcher = new Intent(context, BakingActivity.class);
        }

        PendingIntent pendingIntent  = PendingIntent.getActivity(context, 0, launcher, 0);

        RemoteViews myView = new RemoteViews(context.getPackageName(),R.layout.food_widget);
        myView.setTextViewText(R.id.widget_food_name,food_name);
        myView.setOnClickPendingIntent(R.id.widget_food_name,pendingIntent);
        //initialize the gridview as well
        Intent AllIngredients = new Intent(context, BakingRemoteView.class);
        // AllIngredients.setAction(BakingWidgetService.ACTION_START_FOOD_INGREDIENTS);
        //lets pass the current widget id on this intent
        AllIngredients.putExtra(BakingWidgetService.ID_EXTRA, food_id);
        AllIngredients.putExtra(BakingWidgetService.NAME_EXTRA,food_name);
        // Bind the remote adapter
        myView.setRemoteAdapter(R.id.ingredients_list, AllIngredients);

        PendingIntent IngridientsIntent = PendingIntent.getService(context, 0, AllIngredients, PendingIntent.FLAG_UPDATE_CURRENT);
        myView.setOnClickPendingIntent(R.id.ingredients_list, IngridientsIntent);


        appWidgetManager.updateAppWidget(appWidgetId, myView);



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

    /**
     * Creates and returns the RemoteViews to be displayed in the GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
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
