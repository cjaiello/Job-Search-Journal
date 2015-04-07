package com.example.christinaaiello.general;

import android.app.Activity;
import android.database.Cursor;
import android.provider.CallLog;

/**
 * Created by Christina Aiello on 4/6/2015.
 */
public class PhoneCallCounter {

    public static Integer getNumberOfPhoneCalls (Activity activity, String phoneNumber) {
        String phoneNumberWithoutDashes = phoneNumber.replaceAll("[\\s\\-().]", "");
        String[] selectionArgs = {String.valueOf(phoneNumberWithoutDashes)};
        Cursor cursor = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.NUMBER + " = ? ", selectionArgs, null);
        return cursor.getCount(); // Returning the number of matches we get with this number
    }
}
