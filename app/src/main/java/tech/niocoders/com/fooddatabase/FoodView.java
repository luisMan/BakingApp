package tech.niocoders.com.fooddatabase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tech.niocoders.com.bakingapp.R;

public class FoodView extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Context  context;
    public  ImageView food_poster;
    public  TextView food_name;
    public  TextView food_id;
    public  FoodDatabaseAdapter adapter;
    public  String ImageLink;
    public FoodView(FoodDatabaseAdapter adapter,Context context, View itemView)
    {
        super(itemView);
        //cast the adapter
        this.context  =  context;
        this.adapter = adapter;
        this.food_poster = itemView.findViewById(R.id.food_item);
        this.food_name =  itemView.findViewById(R.id.food_name);
        this.food_id =  itemView.findViewById(R.id.food_id);
        //set click listener to our View
        itemView.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
      int position =  getAdapterPosition();
      //send back the click position to our Item Click listener method
      adapter.listener.ItemClick(position);
    }
}
