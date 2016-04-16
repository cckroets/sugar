package com.orm;

import android.database.sqlite.SQLiteDatabase;

public interface SugarDbCallback {

    void onCreate(SQLiteDatabase sqLiteDatabase);

    void onConfigure(SQLiteDatabase db);

    void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion);
}
