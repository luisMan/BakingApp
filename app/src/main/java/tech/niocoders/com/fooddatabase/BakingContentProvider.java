package tech.niocoders.com.fooddatabase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class BakingContentProvider extends ContentProvider {
    public static final int FOOD = 100;
    public static final int STEPS =  101;
    public static final int INGREDIENTS = 102;
    public static final int FOOD_WITH_ID = 103;
    private Context context;
    private BakingDataBaseHelper helper;

    public static final UriMatcher globalMatcher =  buildMatches();

    public static UriMatcher buildMatches()
    {
         UriMatcher matcher  = new UriMatcher(UriMatcher.NO_MATCH);
         matcher.addURI(BakingContract.AUTHORITY,BakingContract.PATH_FOOD,FOOD);
         matcher.addURI(BakingContract.AUTHORITY,BakingContract.PATH_STEPS,STEPS);
         matcher.addURI(BakingContract.AUTHORITY,BakingContract.PATH_INGREDIENTS,INGREDIENTS);
         matcher.addURI(BakingContract.AUTHORITY,BakingContract.PATH_FOOD+"/#",FOOD_WITH_ID);
     return matcher;
    }


    @Override
    public boolean onCreate() {
        context =  getContext();
        helper =  new BakingDataBaseHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = helper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = globalMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case FOOD:
                retCursor =  db.query(BakingContract.PATH_FOOD,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FOOD_WITH_ID:
                retCursor = db.query(BakingContract.PATH_FOOD,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case STEPS:
                retCursor = db.query(BakingContract.PATH_STEPS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENTS:
                retCursor = db.query(BakingContract.PATH_INGREDIENTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db =  helper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = globalMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
             case FOOD:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(BakingContract.PATH_FOOD, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(BakingContract.FoodEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case STEPS:
                // Insert new values into the database
                // Inserting values into tasks table
                id = db.insert(BakingContract.PATH_STEPS, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(BakingContract.StepsEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case INGREDIENTS:
                // Insert new values into the database
                // Inserting values into tasks table
                id = db.insert(BakingContract.PATH_INGREDIENTS, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(BakingContract.IngredientsEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);


        return  returnUri;
    }


    //here we will just need to delete the food item since we using constraint cascade sqlite syntax
    //any other relational data that match our deleted food id will be delete automatically
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = globalMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FOOD_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(BakingContract.PATH_FOOD, "id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
