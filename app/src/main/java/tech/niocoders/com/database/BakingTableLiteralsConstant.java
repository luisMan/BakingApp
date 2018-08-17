package tech.niocoders.com.database;

public class BakingTableLiteralsConstant {
    //LETS CREATE THE QUERY STRING TO EXECUTE THE FOOD

    public static final String FOOD_TABLE = "CREATE TABLE "+BakingContract.PATH_FOOD+" ("
            +BakingContract.FoodEntry.COLUMN_ID+" INTEGER NOT NULL PRIMARY KEY AUTO INCREMENT,"
            +BakingContract.FoodEntry.COLUMN_FOOD_NAME+ " TEXT NOT NULL,"
            +BakingContract.FoodEntry.COLUMN_IMAGE+" TEXT NOT NULL,"
            +BakingContract.FoodEntry.COLUMN_SERVINGS+" INTEGER);";

    //LETS CREATE THE QUERY STRING TO EXECUTE THE INGREDIENTS TABLE WITH FOREIGN KEY TO OUR FOOD TABLE
    public static final String INGREDIENTS_TABLE ="CREATE TABLE "+BakingContract.PATH_INGREDIENTS+ " ("
            +BakingContract.IngredientsEntry.COLUMN_FOOD_ID+" INTEGER NOT NULL,"
            +BakingContract.IngredientsEntry.COLUMN_FOOD_INGREDIENT+" TEXT NOT NULL,"
            +BakingContract.IngredientsEntry.COLUMN_FOOD_MEASURE+" TEXT NOT NULL,"
            +BakingContract.IngredientsEntry.COLUMN_FOOD_QUANTITY+" INTEGER NOT NULL,"
            +"FOREIGN KEY  ("+BakingContract.IngredientsEntry.COLUMN_FOOD_ID+") REFERENCES "
            +BakingContract.PATH_FOOD+"("+BakingContract.FoodEntry.COLUMN_ID+") ON DELETE CASCADE);";


    //LETS CREATE THE QUERY STRING TO EXECUTE THE STEPS TABLE WITH FOREIGN KEY REFERENCING OUR FOOD TABLE
    public static final String STEPS_TABLE ="CREATE TABLE "+BakingContract.PATH_STEPS+" ("
            +BakingContract.StepsEntry.COLUMN_STEP_FOOD_ID+" INTEGER NOT NULL,"
            +BakingContract.StepsEntry.COLUMN_STEP_NUMBER+" INTEGER NOT NULL,"
            +BakingContract.StepsEntry.COLUMN_STEP_SHORTDESC+" TEXT NOT NULL,"
            +BakingContract.StepsEntry.COLUMN_STEP_DESCRIPTION+" TEXT NOT NULL,"
            +BakingContract.StepsEntry.COLUMN_STEP_THUMBNAIL+" TEXT NOT NULL,"
            +BakingContract.StepsEntry.COLUMN_STEP_VIDEO_URL+" TEXT NOT NULL,"
            +"FOREIGN KEY ("+BakingContract.StepsEntry.COLUMN_STEP_FOOD_ID+") REFERENCES "
            +BakingContract.PATH_FOOD+"("+BakingContract.FoodEntry.COLUMN_ID+") ON DELETE CASCADE);";

}
