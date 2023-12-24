package com.example.rubricapersonale.database;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB = "contattiPersonali";
    public static final int SCHEMA_VERSION = 1;
    public static final String DATABASE_NAME = "contattiPersonali.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    /**
     * onCreate() is only run when the database file did not exist and was just created.
     * If onCreate() returns successfully (doesn't throw an exception), the database is assumed
     * to be created with the requested version number.
     * As an implication, you should not catch SQLExceptions in onCreate() yourself.
     */
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(DatabaseHelper.DATABASE_NAME, "OnCreate invoked!");
        // tabella contatti
        String sql = "create table if not exists contatti(id integer primary key autoincrement, nome text, indirizzo text, telefono text, email text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    /**
     * onUpgrade() is only called when the database file exists
     * but the stored version number is lower than requested in the constructor.
     * The onUpgrade() should update the table schema to the requested version.
     *
     * When changing the table schema in code (onCreate()), you should make sure the database is updated.
     * Increment the database version so that onUpgrade() is invoked.
     * This is slightly more complicated as more code is needed.
     * For released versions, you should implement data migration in onUpgrade() so your users don't lose their data.
     *
     */
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(DatabaseHelper.DATABASE_NAME, "OnUpgrade invoked!");
    }
}
