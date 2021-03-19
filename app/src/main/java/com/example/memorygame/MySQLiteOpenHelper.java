package com.example.memorygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static Integer Version = 1;

    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteOpenHelper(Context context, String name, int version){
        this(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlRequest = "CREATE table user(" +
                "id integer primary key autoincrement, " +
                "name varchar(100), " +
                "email varchar(100), " +
                "password varchar(100), " +
                "birthday datetime, " +
                "sex int)";
        db.execSQL(sqlRequest);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("The version is updated from "+oldVersion+" to "+newVersion+".");
    }

    boolean addUser(String name, String email, String password, int sex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("name",name);
        data.put("email",email);
        // ...

        return false;
    }
}
