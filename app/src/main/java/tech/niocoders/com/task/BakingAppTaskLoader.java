package tech.niocoders.com.task;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import tech.niocoders.com.bakingapp.BakingActivity;
import tech.niocoders.com.bakingapp.BakingAppUtilities;
import tech.niocoders.com.fooddatabase.BakingContract;
//Author Luis Manon
//Lets execute a Loader to extract Item from the Data Base

public class BakingAppTaskLoader implements LoaderManager.LoaderCallbacks<Cursor>{

    private static int FOOD_LOADER_ID = 22;
    private Context context;
    private BakingActivity  mainActivity;
    private Bundle prioSavedInstanceState;
    private  static String TAG  =  BakingAppTaskLoader.class.getSimpleName();
    private Cursor mCursor;

    public BakingAppTaskLoader(Context context, Bundle InstanceState){

        this.context =  context;
        mainActivity =  (BakingActivity)context;
        this.prioSavedInstanceState =  InstanceState;
        //lets execute the loader and check for network connectivity incase
         executeLoader();
    }

    //getters
    public Cursor getmCursor() {
        return mCursor;
    }

    //our execution Loader
    public void executeLoader()
    {
        if(!BakingAppUtilities.IsNetworkAvailable(context))
        {
            mainActivity.ShowNetworkError();
        }else{
            mainActivity.RemoveNetworkError();
            android.support.v4.app.LoaderManager loaderManager = mainActivity.getSupportLoaderManager();
            if (loaderManager == null) {

                mainActivity.getSupportLoaderManager().initLoader(FOOD_LOADER_ID, null, this);
            } else {
                onResume();
            }

        }

    }


    public void onResume() {

        mainActivity.getSupportLoaderManager().restartLoader(FOOD_LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new AsyncTaskLoader<Cursor>(context) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if(mTaskData != null)
                {
                    mainActivity.HideLoader();
                    deliverResult(mTaskData);
                }else{
                    mainActivity.ShowLoaderBar();
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try{
                    return context.getContentResolver().query(BakingContract.FoodEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            BakingContract.FoodEntry.COLUMN_ID);
                }catch (Exception e)
                {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mTaskData = data;
                Log.v("cursor", data.toString());
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(data!=null)
        {
            mCursor =  data;
        }
        mainActivity.HideLoader();
        mainActivity.getmAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
     //will not be implementing loader reset at this moment
    }



}
