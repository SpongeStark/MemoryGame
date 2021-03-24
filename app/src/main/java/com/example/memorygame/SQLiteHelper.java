package com.example.memorygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static String DB_NAME="Gamer.db", TABLE_NAME="Gamer", ID="id";
    public static String NAME="name", LOGIN="email", PASSWORD="password";
    public static String BIRTHDAY="birthday", SEX="sex", SCORE="score";

    public SQLiteHelper(@Nullable Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                LOGIN + " TEXT," +
                PASSWORD + " TEXT, " +
                BIRTHDAY + " TEXT, " +
                SEX + " INTEGER, " +
                SCORE + " INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    boolean addGamer(String name, String email, String password, String birthday, boolean sex, int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(NAME, name);
        data.put(LOGIN, email);
        data.put(PASSWORD, password);
        data.put(BIRTHDAY, birthday);
        data.put(SEX, sex?1:0);
        data.put(SCORE, score);

        long result = db.insert(TABLE_NAME, null, data);
        return (-1 != result);
    }

    boolean connect(String login, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s' AND %S = '%s'",
                                TABLE_NAME, LOGIN, login, PASSWORD, password);
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst();
    }

}
