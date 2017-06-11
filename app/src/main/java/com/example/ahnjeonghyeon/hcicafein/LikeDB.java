package com.example.ahnjeonghyeon.hcicafein;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hojingong on 2017. 6. 9..
 */

public class LikeDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "LikeCafe.db";
    public static final int DB_VERSION=1;

    public LikeDB(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists LikeCafe(cafename TEXT, cafead TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //  db.execSQL("DROP TABLE IF EXISTS");
        //   onCreate(db);
    }
    public void delete(String name, String address){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM LikeCafe WHERE cafename = '"+name+"'and cafead = '"+address+"';");
        //DELETE FROM COSLISTS WHERE cosname='" + cosname + "';"
        db.close();
    }
}
