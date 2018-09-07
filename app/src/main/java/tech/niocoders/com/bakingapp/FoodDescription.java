package tech.niocoders.com.bakingapp;

/*Author Luis Manon*/

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.niocoders.com.fooddatabase.BakingAppStepsAdapter;
import tech.niocoders.com.fooddatabase.BakingContract;
import tech.niocoders.com.task.ImageLinkTask;
import tech.niocoders.com.task.Ingredients;
import videoplayer.videoPlayer;

public class FoodDescription extends AppCompatActivity implements BakingAppStepsAdapter.PagerClickListener{

    public Context context;
    @BindView(R.id.thumbnail)
    public ImageView thumbnail;
    @BindView(R.id.Author)
    public TextView author;
    @BindView(R.id.TutorialStep)
    public TextView tutorialSteps;
    public Cursor steps;
    public Cursor ingredients;
    public int currentTutorialStepIndex;
    public String Database_food_key_id ;
    public String PAGE_INDEX = "PAGE_INDEX";
    @BindView(R.id.no_video)
    public ImageView noVideoImageView;
    @BindView(R.id.videoFrameLayout)
    public FrameLayout videoFrameLayout;
    @BindView(R.id.steps_list)
    public FrameLayout DescriptionFrameLayout;
    @BindView(R.id.mainScroll)
    public ScrollView xyPosition;
    //lets declare the layouts for the StepRecyclerView that will hold steps
    private BakingAppStepsAdapter mStepAdapter;
    private GridLayoutManager stepsGridLayout;
    private RecyclerView tutorialPagerRecyclerView;
    private String PLAYER_FRAGMENT = "PLAYER_FRAGMENT";
    private String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    public Bundle savedBundle;
    public videoPlayer fragmentPlayer;
    public DescriptionFragment descriptionFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context =  getApplicationContext();
        this.savedBundle =  savedInstanceState;
        setContentView(R.layout.food_description);
        //bind the views
        ButterKnife.bind(this);
        //lets create the adapter and also the layout for our view pager changer
        tutorialPagerRecyclerView =  findViewById(R.id.tutorial_pages);
        stepsGridLayout = new GridLayoutManager(context,
                1, GridLayoutManager.HORIZONTAL, false);
        tutorialPagerRecyclerView.setLayoutManager(stepsGridLayout);
        mStepAdapter =  new BakingAppStepsAdapter(this,this);
        tutorialPagerRecyclerView.setAdapter(mStepAdapter);


        if(getIntent()!=null) {

            String food_name = getIntent().getExtras().getString(BakingActivity.FOOD_NAME);
            String aut = getIntent().getExtras().getString(BakingActivity.FOOD_AUTHOR);
            Database_food_key_id =  ""+getIntent().getExtras().getInt(BakingActivity.FOOD_ID);
            Log.d("activity",food_name+"  "+Database_food_key_id);
            this.getSupportActionBar().setTitle(food_name);
            author.setText("Author : " +aut);
            tutorialSteps.setText("Current Step : "+currentTutorialStepIndex);
        }

        //int cursors to populate steps and ingredients from data base

            initCursorLoaders();



    }

    //THIS METHOD IS CREATED TO INITIALIZE THE CURSORS AND LOAD ALL THE DATA NEEDED FOR OUR STEPS AND INGREDIENTS DATA
    public void initCursorLoaders() {
        Uri BakingStepUri = BakingContract.StepsEntry.CONTENT_URI;
        Uri BakingIngredientsUri =  BakingContract.IngredientsEntry.CONTENT_URI;
        String[] SelectionArgs = {Database_food_key_id};
        steps = context.getContentResolver().query(BakingStepUri, null, BakingContract.StepsEntry.COLUMN_STEP_FOOD_ID+"=?", SelectionArgs, null);
        ingredients = context.getContentResolver().query(BakingIngredientsUri, null, BakingContract.IngredientsEntry.COLUMN_FOOD_ID+"=?",SelectionArgs,null);
        Log.d("activity",steps.getCount()+"  "+ingredients.getCount());
        populateStepsUI();
        populateIngredientUI();

    }

    public void GetImageIfVideoIsEmpty(String food_name)
    {
        if(TextUtils.isEmpty(""))
        {
            new ImageLinkTask(context,thumbnail).execute(food_name);
        }else{
            String url =  BakingAppUtilities.getNetworkUri(" ").toString();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            PicassoSingleton.populateImageView(url,thumbnail, width,height);
        }

    }

    //populate description fragment method
    public void populateDescriptionFragment(String shortDesc, String fullDesc)
    {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        if(savedBundle==null) {
            descriptionFragment = new DescriptionFragment();
            descriptionFragment.setShortAndFullDescription(shortDesc,fullDesc);
            fragmentManager.beginTransaction()
                    .add(R.id.steps_list, descriptionFragment)
                    .commit();

        }else{

            descriptionFragment = (DescriptionFragment) getSupportFragmentManager().getFragment(savedBundle,DETAIL_FRAGMENT);
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_list, descriptionFragment)
                    .commit();

        }
    }

    //populateVideoFragment method
    public void populateVideoFragment(String video_url, String food_name)
    {
        if(video_url.length()==0||video_url.equals(" "))
        {
            //there is no video then lets show no video available
            videoFrameLayout.removeAllViews();
            videoFrameLayout.addView(noVideoImageView);
            noVideoImageView.setVisibility(View.VISIBLE);
            new ImageLinkTask(context,noVideoImageView).execute(food_name);
            Toast.makeText(context,"The video url is empty ", Toast.LENGTH_LONG).show();
        }else{
            noVideoImageView.setVisibility(View.INVISIBLE);


            FragmentManager fragmentManager =  getSupportFragmentManager();
            if(savedBundle==null) {
                fragmentPlayer = new videoPlayer();
                fragmentPlayer.setContext(this);
                fragmentPlayer.setUrl(video_url);
                fragmentManager.beginTransaction()
                        .add(R.id.videoFrameLayout, fragmentPlayer)
                        .commit();

            }else{
                fragmentPlayer = (videoPlayer)getSupportFragmentManager().getFragment(savedBundle,PLAYER_FRAGMENT);
                fragmentManager.beginTransaction()
                        .replace(R.id.videoFrameLayout, fragmentPlayer)
                        .commit();

            }
        }//close else
    }



    //populateStep ui method
    public void populateStepsUI()
    {
        //move step cursor to first position  and Instantiate an loader for steps process
        if(steps!=null && steps.getCount()>0)
        {
            //steps.moveToFirst();
            steps.moveToPosition(currentTutorialStepIndex);
            //load the pages
            mStepAdapter.swapCursor(steps);
            //since we are on the step data we are going to follow each steps base on item clicks
            //if the step has a thumbnail image we will place it to our container if not then we use flicker to do that for us
            String food_name = getIntent().getExtras().getString(BakingActivity.FOOD_NAME);
            String desc = steps.getString(steps.getColumnIndex(BakingContract.StepsEntry.COLUMN_STEP_DESCRIPTION));
            String sdesc =  steps.getString(steps.getColumnIndex(BakingContract.StepsEntry.COLUMN_STEP_SHORTDESC));
            String number =  steps.getString(steps.getColumnIndex(BakingContract.StepsEntry.COLUMN_STEP_NUMBER));
            //String thumb =  steps.getString(steps.getColumnIndex(BakingContract.StepsEntry.COLUMN_STEP_THUMBNAIL));
            String video_url = steps.getString(steps.getColumnIndex(BakingContract.StepsEntry.COLUMN_STEP_VIDEO_URL));
            tutorialSteps.setText("Page : "+number);


            //get image to display if video is empty
            GetImageIfVideoIsEmpty(food_name);

            //here we will start a new fragment with different views to input
            populateDescriptionFragment(sdesc,desc);
            //lets add the video to our fragment view
            populateVideoFragment(video_url, food_name);

        }


    }

    //populateIngredientUI
    public void populateIngredientUI()
    {
        if(ingredients!=null &&ingredients.getCount()>0)
        {
            ingredients.moveToFirst();
            ArrayList<Ingredients> parceableIngredients = new ArrayList<>();
            for(int i=0; i<ingredients.getCount(); i++)
            {
                ingredients.moveToPosition(i);
                String description =  ingredients.getString(ingredients.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_INGREDIENT));
                String measure =  ingredients.getString(ingredients.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_MEASURE));
                String quantity =  ingredients.getString(ingredients.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_FOOD_QUANTITY));

                parceableIngredients.add(new Ingredients(measure,description,Double.parseDouble(quantity)));
            }


            //lets create the fragment object
            IngredientFragment fragment =  new IngredientFragment();
            fragment.setIngridients(parceableIngredients);

            FragmentManager fragmentManager =  getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.ingredients_fragments,fragment)
                    .commit();


        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_INDEX, currentTutorialStepIndex);
        outState.putIntArray("MOVIE_SCROLL_POSITION",
                new int[]{ xyPosition.getScrollX(), xyPosition.getScrollY()});

        //lets store the fragment to the bundle
        getSupportFragmentManager().putFragment(outState,PLAYER_FRAGMENT,fragmentPlayer);
        getSupportFragmentManager().putFragment(outState,DETAIL_FRAGMENT,descriptionFragment);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null) {
            currentTutorialStepIndex = savedInstanceState.getInt(PAGE_INDEX);
             final int[] position = savedInstanceState.getIntArray("MOVIE_SCROLL_POSITION");
            if (position != null)
                xyPosition.post(new Runnable() {
                    public void run() {
                        xyPosition.scrollTo(position[0], position[1]);
                    }
                });
        }

        initCursorLoaders();

    }



    @Override
    public void PagerClickListener(View v, int position) {
        //this is the listener that will make our page change to populate the fragments
        currentTutorialStepIndex = position;
        Toast.makeText(context,"The pager clicked is  = "+currentTutorialStepIndex,Toast.LENGTH_LONG).show();
        populateStepsUI();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the parent activity
        if (id == android.R.id.home) {
            //supportFinishAfterTransition();
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
