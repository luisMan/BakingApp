package tech.niocoders.com.bakingapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import tech.niocoders.com.fooddatabase.FoodDatabaseAdapter;

public class BakingActivity extends AppCompatActivity implements FoodDatabaseAdapter.FoodItemClickListener{

    //recycler view
    public static RecyclerView food_recylcerview;
    public static ProgressBar network_progressbar;
    public static TextView error_message;


    //gridlayoutManager
    private GridLayoutManager layoutManager;

    //food adapter
    private FoodDatabaseAdapter mAdapter;


    //our loader to load the text to populate dataBase
    private BakingLoader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking);

        food_recylcerview = findViewById(R.id.food_recyclerview);
        network_progressbar = findViewById(R.id.progressbar);
        error_message =  findViewById(R.id.network_error);

        //lets construct our layout manager object
        layoutManager =  new GridLayoutManager(this,
                BakingAppUtilities.calculateBestSpanCount(this,
                        Integer.parseInt(getResources().getString(R.string.image_width).toString())));

        //lets attach the recyclerView to our layoutManager
        food_recylcerview.setLayoutManager(layoutManager);


        //lets construct the adpter for our recycler view
        mAdapter =  new FoodDatabaseAdapter(this,this);
        food_recylcerview.setAdapter(mAdapter);
        //fetch data from server and populate our recyclerView

        //populate content to the data base
        PopulateDataBase();
    }

    public void PopulateDataBase()
    {
        mLoader =  new BakingLoader(this);
        //lets now execute the loader
        mLoader.ExecuteLoader();
    }
    //method to show network error
    public void ShowNetworkError()
    {
    error_message.setVisibility(View.VISIBLE);
    }
    //remove network error()
    public void RemoveNetworkError()
    {
     error_message.setVisibility(View.INVISIBLE);
    }

    //show loader while fetching data
    public void ShowLoaderBar()
    {
     network_progressbar.setVisibility(View.VISIBLE);
    }

    //hide loader
    public void HideLoader()
    {
     network_progressbar.setVisibility(View.INVISIBLE);
    }


    //override saved instance state

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    //override restoreInstance state
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void ItemClick(int position) {

    }
}
