package com.example.christinaaiello;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Christina Aiello on 3/23/2015.
 * http://developer.android.com/training/basics/data-storage/databases.html *
 */
public class DatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class DatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "company_information";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LOGO = "logo";
        public static final String COLUMN_NAME_POSITION = "position";
        public static final String COLUMN_NAME_SIZE = "size";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_WEBSITE = "website";
        public static final String COLUMN_NAME_INDUSTRY = "industry";
        public static final String COLUMN_NAME_OVERALL_RATING = "overallRating";
        public static final String COLUMN_NAME_CULTURE = "culture";
        public static final String COLUMN_NAME_GOAL = "goal";
        public static final String COLUMN_NAME_LEADERSHIP = "leadership";
        public static final String COLUMN_NAME_COMPENSATION = "compensation";
        public static final String COLUMN_NAME_OPPORTUNITIES = "opportunities";
        public static final String COLUMN_NAME_WORKLIFE = "worklife";
        public static final String COLUMN_NAME_MISCELLANEOUS = "miscellaneous";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseEntry.TABLE_NAME + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_LOGO + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_POSITION + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_SIZE + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_WEBSITE + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_INDUSTRY + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_OVERALL_RATING + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_CULTURE + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_GOAL + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_LEADERSHIP + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_COMPENSATION + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_OPPORTUNITIES + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_WORKLIFE + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_NAME_MISCELLANEOUS +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME;

    public static class DatabaseHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Database.db";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}
