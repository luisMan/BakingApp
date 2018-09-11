package tech.niocoders.com.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import tech.niocoders.com.bakingapp.Preference;
import tech.niocoders.com.bakingapp.R;

public class BakingWidgetService extends IntentService {
    public static final String ACTION_UPDATE_FOOD_INGREDIENTS = "widget.action.update_ingredient";
    public static final String ACTION_START_FOOD_INGREDIENTS = "tech.niocoders.com.bakingapp.widget.action.water_plant";
    public static String ID_EXTRA = "ID_EXTRA";
    public static String NAME_EXTRA = "FOOD_NAME";

    public BakingWidgetService(){super("BakingWidgetService"); }

    public  static void startBakingIngredients(Context context, String food_id, String name) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_START_FOOD_INGREDIENTS);
        intent.putExtra(ID_EXTRA, food_id);
        intent.putExtra(NAME_EXTRA,name);
        context.startService(intent);

    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_FOOD_INGREDIENTS.equals(action)) {
                String food_id = intent.getStringExtra(ID_EXTRA);
                String food_name =  intent.getStringExtra(NAME_EXTRA);
                if(TextUtils.isEmpty(food_id)&& TextUtils.isEmpty(food_name))
                {
                    food_id = Preference.getPreferenceFoodId(getApplicationContext());
                    food_name = Preference.getPreferenceFoodName(getApplicationContext());
                }
                 handleActionUpdateBakingWidgets(food_id,food_name);
            }
        }
    }





    //handle action start baking ingredients
    public void handleActionUpdateBakingWidgets(String food_id, String name)
    {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_remoteView);
        //Now update all widgets
        BakingWidgetProvider.updateFoodWidgets(this, appWidgetManager,name,food_id,appWidgetIds);

    }

}
