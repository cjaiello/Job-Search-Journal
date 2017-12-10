package com.example.jobsearchjournal.general;

import android.app.Activity;
import android.database.Cursor;
import android.provider.CallLog;

/**
 * Created by Christina Aiello on 4/6/2015.
 */
public class PhoneCallCounter {

    public static Integer getNumberOfPhoneCalls (Activity activity, String phoneNumber) {
        // This will replace any dashes, slashes, periods, and/or parentheses with empty space:
        String phoneNumberWithoutDashes = phoneNumber.replaceAll("[\\s\\-().]", "");
        // This is what we want to search for, the current phone number
        String[] selectionArgs = {String.valueOf(phoneNumberWithoutDashes)};
        // This is our query
        Cursor cursor = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.NUMBER + " = ? ", selectionArgs, null);
        // Returning the number of matches we get with this number
        return cursor.getCount();
    }
}
