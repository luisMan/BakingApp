package tech.niocoders.com.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by luism on 6/29/2018.
 */

public class BakingDataBaseHelper extends SQLiteOpenHelper {
    public static final String TAG = BakingDataBaseHelper.class.getSimpleName();
    public static final String BAKING_HELPER = "BakingDb.db";
    public static int VERSION = 1;
    public Context context;

    public BakingDataBaseHelper(Context context)
    {
        super(context,BAKING_HELPER,null,VERSION); this.context = context;
    }

    //CREATE FOOD TABLE
    public void createFoodTable(SQLiteDatabase database)
    {
        try{

            database.execSQL(BakingTableLiteralsConstant.FOOD_TABLE);
            Log.v(TAG, "SUCESS CREATING DATABASE FOOD TABLE ");



        }catch(SQLException e)
        {
            Log.v(TAG, "ERROR CREATING DATABASE FOOD TABLE "+e.getMessage());
        }
    }

    //CREATE STEPS TABLE
    public void createStepsTable(SQLiteDatabase database)
    {
        try{

            database.execSQL(BakingTableLiteralsConstant.STEPS_TABLE);
            Log.v(TAG, "SUCCESS CREATING DATABASE STEPS TABLE ");

        }catch(SQLException e)
        {
            Log.v(TAG, "ERROR CREATING DATABASE STEPS TABLE "+e.getMessage());
        }
    }

    //CREATE INGREDIENTS TABLE
    public void createIngredientsable(SQLiteDatabase database)
    {
        try{

            database.execSQL(BakingTableLiteralsConstant.INGREDIENTS_TABLE);
            Log.v(TAG, "SUCCESS CREATING DATABASE INGREDIENTS TABLE ");

        }catch(SQLException e)
        {
            Log.v(TAG, "ERROR CREATING DATABASE INGREDIENTS TABLES "+e.getMessage());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       try{

           createFoodTable(sqLiteDatabase);
           createIngredientsable(sqLiteDatabase);
           createStepsTable(sqLiteDatabase);

       }catch(SQLException e)
       {
           Log.v(TAG, "ERROR CREATING DATABASE TABLES "+e.getMessage());
       }
    }



    //FOR NOW i AM UPGRADING AND LOSING THE DATA SAVED INTO THE DATA BASE
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.PATH_FOOD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.PATH_STEPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.PATH_INGREDIENTS);
        onCreate(sqLiteDatabase);
    }
}
