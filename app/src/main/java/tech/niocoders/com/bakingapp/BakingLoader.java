package tech.niocoders.com.bakingapp;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;

public class BakingLoader implements LoaderManager.LoaderCallbacks<String> {

    private static final int MAIN_FOOD_LOADER_ID=20;
    private String JSON_URL_PATH =  "JSON_URL_PATH";
    private String JSON_URL;
    private final Context context;
    private Bundle SavedInstance;
    private BakingAppUtilities utilitiesSingleton;
    private BakingActivity mainActivity;

    public BakingLoader(Context context)
    {
        this.context=context;
        mainActivity = (BakingActivity)context;
        SavedInstance = ((Activity)context).getIntent().getExtras();
    }

    //execute method to instantiate the loader
    public void ExecuteLoader()
    {
        if(SavedInstance!=null)
        {
            JSON_URL =  SavedInstance.getString(JSON_URL_PATH);
            BakingActivity mainClass = (BakingActivity)context;
            if(!utilitiesSingleton.IsNetworkAvailable(context))
            {
                 mainClass.HideLoader();
                 mainClass.ShowNetworkError();

            }else{
                mainClass.RemoveNetworkError();
                mainClass.ShowLoaderBar();
                mainActivity.getSupportLoaderManager().initLoader(MAIN_FOOD_LOADER_ID, SavedInstance, this);

            }//close else for network
        }else{
            //lets put the url path to our loader
            Bundle movieBundle = new Bundle();
            movieBundle.putString(JSON_URL_PATH, BakingAppUtilities.JSON_URL);


            LoaderManager loaderManager =   mainActivity.getSupportLoaderManager();
            Loader<String> foodSearch = loaderManager.getLoader(MAIN_FOOD_LOADER_ID);


            if(foodSearch==null)
            {
                // Log.v("url_create_loader","created loader");
                loaderManager.initLoader(MAIN_FOOD_LOADER_ID, movieBundle, this);

            }else{
                // Log.v("url_restart_loader","restarted loader");
                loaderManager.restartLoader(MAIN_FOOD_LOADER_ID,movieBundle,this);
            }
        }
    }


    @Override
    public Loader<String> onCreateLoader(final int id,final Bundle bundle) {
        return new AsyncTaskLoader<String>(context){
            String json;
            @Override
            protected void onStartLoading() {
                BakingActivity mainActivity =  (BakingActivity)context;
                //Log.v("url_onStartLoading","onStartLoadingCalled");
                if(bundle==null && !utilitiesSingleton.IsNetworkAvailable(context) )
                {
                    mainActivity.ShowNetworkError();
                    return;
                }else{
                    mainActivity.RemoveNetworkError();

                }



                if (null!= json ) {
                    // Log.v("url_results",json);
                    mainActivity.HideLoader();
                    deliverResult(json);
                } else {
                    mainActivity.ShowLoaderBar();
                    forceLoad();
                }
            }


            @Override
            public String loadInBackground() {
                String searchQueryUrlString = bundle.getString(JSON_URL_PATH);
                //Log.v("url_back",searchQueryUrlString);
                /* If the user didn't enter anything, there's nothing to search for */
                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }

                /* Parse the URL from the passed in String and perform the search */
                try {

                    BakingAppUtilities.ExecuteRetrosfitCall(context);
                    return "Success";

                }catch (Exception d)
                {
                    d.printStackTrace();
                    return null;
                }
            }



            @Override
            public void deliverResult(String data) {
                json = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        BakingActivity mainActivity =  (BakingActivity)context;
        mainActivity.HideLoader();
        /*
         * If the results are null, we assume an error has occurred. There are much more robust
         * methods for checking errors, but we wanted to keep this particular example simple.
         */
        if (null == data) {
            Log.d("jsonError","no data has return host!");
        } else {
            Log.v("url_onloadFinished",data);

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
