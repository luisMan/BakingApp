package tech.niocoders.com.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BakingActivity extends AppCompatActivity {
    //lets create an object reference to our baking app utilities
    public static BakingAppUtilities networkUtilities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking);

        //lets check if there is network connectivity to start the loader
    }

    public static void InitApplicationDataBase()
    {

    }



}
