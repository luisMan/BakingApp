package widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import tech.niocoders.com.bakingapp.R;

public class BakingWidgetService extends IntentService {
    public static final String ACTION_UPDATE_FOOD_INGREDIENTS = "widget.action.update_ingredient";
    public static final String ACTION_START_FOOD_INGREDIENTS = "widget.action.start_ingredient";
    public static String ID_EXTRA = "ID_EXTRA";
    public static String NAME_EXTRA = "FOOD_NAME";

    public BakingWidgetService(){super("BakingWidgetService"); }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_FOOD_INGREDIENTS.equals(action)) {
                final String food_id = intent.getStringExtra(ID_EXTRA);
                final String food_name =  intent.getStringExtra(NAME_EXTRA);
                 handleActionUpdateBakingWidgets(food_id,food_name);
            }
        }
    }

    public  static void startBakingIngredients(Context context, String food_id, String name) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_START_FOOD_INGREDIENTS);
        intent.putExtra(ID_EXTRA, food_id);
        intent.putExtra(NAME_EXTRA,name);
        context.startService(intent);
    }



    //handle action start baking ingredients
    public void handleActionUpdateBakingWidgets(String food_id, String name)
    {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_food_name);
        //Now update all widgets
        BakingWidgetProvider.updateFoodWidgets(this, appWidgetManager,name,food_id,appWidgetIds);
    }

}
