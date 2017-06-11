package com.example.ahnjeonghyeon.hcicafein;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hojingong on 2017. 6. 9..
 */

public class LikeHandler {
    private LikeDB mLikedb;
    private SQLiteDatabase db;
    String DB_NAME ="LikeCafe";

    public LikeHandler(Context context){
        this.mLikedb = new LikeDB(context);
    }

    public void close(){
        db.close();
    }

    public long insert(String name, String address){
        db = mLikedb.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("cafename",name);
        val.put("cafead",address);

        if(db.insert("LikeCafe",null,val)<0){
            db.close();
            return 0;//실패
        }else{
            db.close();
            return 1;//성공
        }
    }


    public long check(String name) {
        int check = 0;
        String gets = null;
        db = mLikedb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM LikeCafe;", null);

        while (cursor.moveToNext()) {
            gets = cursor.getString(0);
            if(name.equals(gets)){
                check=1;
            }
        }
        if(check == 0) {
            //없다
            return 0;
        }else {
            //있다
            return 1;
        }
    }


    public void delete(String name,String address){
        db = mLikedb.getWritableDatabase();
        //삭제
        db.rawQuery("DELETE FROM LikeCafe WHERE cafename = '"+name+"'and cafead = '"+address+"'", null);
//        db.setTransactionSuccessful();

     //   db.execSQL("DELETE FROM "+DB_NAME+" WHERE "+name+"='"+name+"'");
        db.close();

    }
}
