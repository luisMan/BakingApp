package tech.niocoders.com.fooddatabase;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import tech.niocoders.com.bakingapp.BakingActivity;
import tech.niocoders.com.bakingapp.R;
import tech.niocoders.com.task.ImageLinkTask;

public class FoodDatabaseAdapter extends RecyclerView.Adapter<FoodView> {

    //our Cursor and viewHolder counter
    public Cursor mCursor;
    public int ViewHolderCounter;
    private ImageLinkTask linkTask;

    private Context context;
    public  FoodItemClickListener listener;
    public interface FoodItemClickListener
    {
        void ItemClick(View view,int position);
    }


    public FoodDatabaseAdapter(Context context, FoodItemClickListener listener)
    {
        this.context =  context;
        this.listener =  listener;
        this.ViewHolderCounter = 0;

    }

    @Override
    public FoodView onCreateViewHolder(ViewGroup parent, int viewType) {
        //here we will be creating our view holder and increment our counter
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.food_item, parent, shouldAttachToParentImmediately);
        FoodView food = new FoodView(FoodDatabaseAdapter.this,context, view);

        ViewHolderCounter++;

        return food;
    }

    @Override
    public void onBindViewHolder(FoodView holder, int position) {
         mCursor.moveToPosition(position);
        //lets bind the data
        int id = mCursor.getInt(mCursor.getColumnIndex(BakingContract.FoodEntry.COLUMN_ID));
        String name = mCursor.getString(mCursor.getColumnIndex(BakingContract.FoodEntry.COLUMN_FOOD_NAME));
        String image = mCursor.getString(mCursor.getColumnIndex(BakingContract.FoodEntry.COLUMN_IMAGE));

        BakingActivity activity = (BakingActivity) context;
        holder.food_id.setTag(id);
        holder.food_name.setText(name);
        holder.ImageLink = image;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        holder.food_poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        new ImageLinkTask(context,holder).execute(name);


    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor c)
    {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
