package tech.niocoders.com.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import tech.niocoders.com.fooddatabase.BakingContract;
import tech.niocoders.com.fooddatabase.FoodDatabaseAdapter;
import tech.niocoders.com.task.BakingAppTaskLoader;

public class BakingActivity extends AppCompatActivity implements FoodDatabaseAdapter.FoodItemClickListener{

    //recycler view
    public static RecyclerView food_recylcerview;
    public static ProgressBar network_progressbar;
    public static TextView error_message;
    public static String RECYCLERVIEW_STATE = "RECYCLERVIEW_STATE";
    public static String FOOD_NAME = "FOOD_NAME";
    public static String FOOD_ID = "FOOD_ID";
    public static String FOOD_AUTHOR = "FOOD_AUTHOR";

    //gridlayoutManager
    private GridLayoutManager layoutManager;

    //food adapter
    private FoodDatabaseAdapter mAdapter;

    //food loader from DataBase with Cursor object
    private BakingAppTaskLoader foodLoader;


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
        layoutManager =  new GridLayoutManager(this, GridLayoutManager.VERTICAL);

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
        //get data from the given url server to see if any new items has been added then populate mysql database
        //adding all the missing elements
        mLoader.ExecuteLoader();

        //now lets return the query of all the food and then add it to our RecyclerView
        foodLoader = new BakingAppTaskLoader(this, getIntent().getExtras());

        //lets see if we get any food images
    }

    //getters
    public static RecyclerView getFood_recylcerview() {
        return food_recylcerview;
    }
    public FoodDatabaseAdapter getmAdapter() {
        return mAdapter;
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
        if(!BakingAppUtilities.IsNetworkAvailable(getApplicationContext()))
        {
            ShowNetworkError();
        }else {
            RemoveNetworkError();
            outState.putParcelable(RECYCLERVIEW_STATE, food_recylcerview.getLayoutManager().onSaveInstanceState());
            super.onSaveInstanceState(outState, outPersistentState);
        }
    }


    //override restoreInstance state
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(!BakingAppUtilities.IsNetworkAvailable(getApplicationContext()))
        {
            ShowNetworkError();
        }else {
            RemoveNetworkError();
            if (savedInstanceState != null) {
                Parcelable state = savedInstanceState.getParcelable(RECYCLERVIEW_STATE);
                if (state != null) {
                    HideLoader();
                    food_recylcerview.getLayoutManager().onRestoreInstanceState(state);
                }
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void ItemClick(View view ,int position) {
        //here we have the click even listener for our loader data
        view.findViewById(R.id.food_id);
        Cursor temp =  foodLoader.getmCursor();
        if(temp!=null)
        {
            temp.moveToPosition(position);
            String food_name =  temp.getString(temp.getColumnIndex(BakingContract.FoodEntry.COLUMN_FOOD_NAME)).toString();
            int key =  temp.getInt(temp.getColumnIndex(BakingContract.FoodEntry.COLUMN_ID));
            //lets set the shared preference for our name of the food item and the key
            Preference.saveFoodIdAndKey(this, Integer.toString(key),food_name);
            String author =  temp.getString(temp.getColumnIndex(BakingContract.FoodEntry.COLUMN_AUTHOR));
            author  =  (author.equals(" ") ? "Unknown Author " : author);
            //lets construct some explicit content to send between activities

            Context tempContext =  BakingActivity.this;
            Class childActivity =  FoodDescription.class;
            Intent launcher =  new Intent(tempContext,childActivity);
            launcher.putExtra(FOOD_NAME,food_name);
            launcher.putExtra(FOOD_ID,key);
            launcher.putExtra(FOOD_AUTHOR, author);
            startActivity(launcher);


        }
    }
}
