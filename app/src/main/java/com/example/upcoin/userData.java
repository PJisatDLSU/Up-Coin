package com.example.upcoin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class userData extends SQLiteOpenHelper {
    public userData(Context context) {
        super(context, "userData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("userData","onCreate called");
        db.execSQL("CREATE TABLE UserData(id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS UserData");
        onCreate(db);
    }
}
