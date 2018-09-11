package tech.niocoders.com.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import tech.niocoders.com.bakingapp.FoodDescription;
import tech.niocoders.com.bakingapp.Preference;
import tech.niocoders.com.bakingapp.R;

public class BakingWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String food_name,String food_id, int appWidgetId) {

        RemoteViews rv = getFoodRemoteView(context, food_name, food_id);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        BakingWidgetService.startBakingIngredients(context, Preference.getPreferenceFoodId(context),Preference.getPreferenceFoodName(context));
    }


    public static void updateFoodWidgets(Context context, AppWidgetManager appWidgetManager,String food_name,String food_id, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, food_name,food_id, appWidgetId);
        }
    }


    private static RemoteViews getFoodRemoteView(Context context, String food_name, String food_id) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.food_widget);
        views.setTextViewText(R.id.food_name, food_name);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, BakingRemoteViewService.class);
        intent.putExtra(BakingWidgetService.NAME_EXTRA, food_name);
        intent.putExtra(BakingWidgetService.ID_EXTRA,food_id);
        views.setRemoteAdapter(R.id.widget_remoteView, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent foodDescription = new Intent(context, FoodDescription.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, foodDescription, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_remoteView, appPendingIntent);
        views.setOnClickFillInIntent(R.id.food_name,intent);
        // Handle empty gardens
        views.setEmptyView(R.id.widget_remoteView, R.id.empty_view);
        return views;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        BakingWidgetService.startBakingIngredients(context, Preference.getPreferenceFoodId(context),Preference.getPreferenceFoodName(context));
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