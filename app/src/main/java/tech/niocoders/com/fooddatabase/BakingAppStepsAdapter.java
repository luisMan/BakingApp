package tech.niocoders.com.fooddatabase;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.niocoders.com.bakingapp.R;

public class BakingAppStepsAdapter extends RecyclerView.Adapter<StepPager> {

    private Cursor mSteps;
    public  PagerClickListener listener;
    public int viewIndex;
    private Context context;


    public interface PagerClickListener
    {
        void PagerClickListener(View v,int position);
    }
    public BakingAppStepsAdapter(Context context, PagerClickListener listener)
    {
        this.context =  context;
        this.listener =  listener;
        this.viewIndex = 0;

    }

    public Cursor getmStepsCursor() {
        return mSteps;
    }

    @Override
    public StepPager onCreateViewHolder(ViewGroup parent, int viewType) {
       boolean shouldAttachAdaper = false;
       View view  = LayoutInflater.from(context).
               inflate(R.layout.tutorial_box,parent,shouldAttachAdaper);
       StepPager pager = new StepPager(this,context,view);
       viewIndex++;
       return pager;
    }

    @Override
    public void onBindViewHolder(StepPager holder, int position) {
        if(mSteps!=null) {
           mSteps.moveToPosition(position);
           String TutorialNumber  =  mSteps.getString(mSteps.getColumnIndex(BakingContract.StepsEntry.COLUMN_STEP_NUMBER));
           holder.tutorialNumber.setText(TutorialNumber);
        }

    }
    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        }
        return mSteps.getCount();
    }


    public Cursor swapCursor(Cursor c)
    {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mSteps == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mSteps;
        this.mSteps = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
