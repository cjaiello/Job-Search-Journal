package com.example.christinaaiello.employerinformation;

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
    public static abstract class CompanyDataTable implements BaseColumns {
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

    /* Inner class that defines the table contents */
    public static abstract class InitialContactTable implements BaseColumns {
        public static final String TABLE_NAME = "initial_contact";
        public static final String COLUMN_NAME_COMPANYID = "company_id";
        public static final String COLUMN_NAME_DATE = "date_of_interview";
        public static final String COLUMN_NAME_CONTACT = "contact_name";
        public static final String COLUMN_NAME_EMAIL = "contact_email_address";
        public static final String COLUMN_NAME_PHONE = "contact_phone_number";
        public static final String COLUMN_NAME_METHOD = "method_of_interaction";
        public static final String COLUMN_NAME_DISCUSSION = "what_was_discussed_with_contact";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_INITIAL_CONTACT_TABLE =
            "CREATE TABLE " + InitialContactTable.TABLE_NAME + " (" +
                    InitialContactTable._ID + " INTEGER PRIMARY KEY," +
                    InitialContactTable.COLUMN_NAME_COMPANYID + TEXT_TYPE + COMMA_SEP +
                    InitialContactTable.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    InitialContactTable.COLUMN_NAME_CONTACT + TEXT_TYPE + COMMA_SEP +
                    InitialContactTable.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    InitialContactTable.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                    InitialContactTable.COLUMN_NAME_METHOD + TEXT_TYPE + COMMA_SEP +
                    InitialContactTable.COLUMN_NAME_DISCUSSION +
                    " )";
    private static final String SQL_DELETE_INITIAL_CONTACT_TABLE =
            "DROP TABLE IF EXISTS " + InitialContactTable.TABLE_NAME;

    private static final String CREATE_COMPANY_DATA_TABLE =
            "CREATE TABLE " + CompanyDataTable.TABLE_NAME + " (" +
                    CompanyDataTable._ID + " INTEGER PRIMARY KEY," +
                    CompanyDataTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_LOGO + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_POSITION + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_SIZE + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_WEBSITE + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_INDUSTRY + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_OVERALL_RATING + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_CULTURE + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_GOAL + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_LEADERSHIP + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_COMPENSATION + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_OPPORTUNITIES + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_WORKLIFE + TEXT_TYPE + COMMA_SEP +
                    CompanyDataTable.COLUMN_NAME_MISCELLANEOUS +
                    " )";
    private static final String SQL_DELETE_COMPANY_DATA_TABLE =
            "DROP TABLE IF EXISTS " + CompanyDataTable.TABLE_NAME;

    public static class DatabaseHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Database.db";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_COMPANY_DATA_TABLE);
            db.execSQL(CREATE_INITIAL_CONTACT_TABLE);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_COMPANY_DATA_TABLE);
            db.execSQL(SQL_DELETE_INITIAL_CONTACT_TABLE);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}
