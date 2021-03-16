package com.example.memorygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static String DB_NAME="GamerDB", TABLE_NAME="Gamer", ID="id";
    public static String NAME="name", LOGIN="login", PASSWORD="password", SCORE="score";

    public SQLiteHelper(@Nullable Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOGIN + " TEXT," +
                PASSWORD + " TEXT), " +
                SCORE + "INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    boolean addGamer(String name, String login, String password, int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(NAME, name);
        data.put(LOGIN, login);
        data.put(PASSWORD, password);
        data.put(NAME, name);
        data.put(SCORE, score);

        long result = db.insert(TABLE_NAME, null, data);
        return (-1 != result);
    }

    boolean connect(String login, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s = %S AND %S = %S;",
                                TABLE_NAME, LOGIN, login, PASSWORD, password);
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst();
    }
}