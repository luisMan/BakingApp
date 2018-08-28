package tech.niocoders.com.fooddatabase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import tech.niocoders.com.bakingapp.R;

public class StepPager extends RecyclerView.ViewHolder implements View.OnClickListener {
    private BakingAppStepsAdapter adapter;
    private Context mContext;
    public TextView tutorialNumber;

    public StepPager(BakingAppStepsAdapter adapter,Context context, View itemView)
    {
        super(itemView);
        this.adapter = adapter;
        this.mContext =  context;
        tutorialNumber =  itemView.findViewById(R.id.number_index);

        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        //get the adapter position
        adapter.listener.PagerClickListener(view,getAdapterPosition());
    }
}
