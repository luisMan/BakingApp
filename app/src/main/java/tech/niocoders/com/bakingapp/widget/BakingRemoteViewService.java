package tech.niocoders.com.bakingapp.widget;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import tech.niocoders.com.bakingapp.Preference;
import tech.niocoders.com.bakingapp.R;
import tech.niocoders.com.fooddatabase.BakingContract;
/*I was trying to add the remote view object but my widget was giving me a lot of problems thus I decide to
show all the ingredients as text 
 */
public class BakingRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingViewFactory(this.getApplicationContext());
    }
}

class BakingViewFactory implements RemoteViewsService.RemoteViewsFactory {

    String ID_EXTRA = "ID_EXTRA";
    Context mContext;
    Cursor mCursor;
    String id;

    public BakingViewFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDataSetChanged() {

        if (mCursor != null) mCursor.close();
        id = Preference.getPreferenceFoodId(mContext);
        String [] selectionArgs =  {id};
        mCursor = mContext.getContentResolver().query(
                BakingContract.IngredientsEntry.CONTENT_URI, null,
                BakingContract.IngredientsEntry.COLUMN_FOOD_ID+"=?",
                selectionArgs,
                null
        );
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;

        mCursor.moveToPosition(position);
        String description =  mCursor.getString(mCursor.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_INGREDIENT));
        String measure =  mCursor.getString(mCursor.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_MEASURE));
        String quantity =  mCursor.getString(mCursor.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_QUANTITY));

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient);
        views.setTextViewText(R.id.tutorial_step,"Step("+position+")");
        views.setTextViewText(R.id.Ingredient_text,description);
        views.setTextViewText(R.id.measure, " "+measure);
        views.setTextViewText(R.id.quantity, quantity);

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putString(BakingWidgetService.ID_EXTRA, Preference.getPreferenceFoodId(mContext));
        extras.putString(BakingWidgetService.NAME_EXTRA, Preference.getPreferenceFoodName(mContext));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.Ingredient_text, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

