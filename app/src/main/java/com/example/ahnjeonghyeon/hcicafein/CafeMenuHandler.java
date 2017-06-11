package com.example.ahnjeonghyeon.hcicafein;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hojingong on 2017. 6. 9..
 */

public class CafeMenuHandler {
    private CafeMenuDB cafeMenuDB;
    private SQLiteDatabase db;

    public CafeMenuHandler(Context context){
        this.cafeMenuDB = new CafeMenuDB(context);
    }

    public void close(){
        db.close();
    }

    public long insert(String name, String menu,String kind){
        db = cafeMenuDB.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("cafename",name);
        val.put("menu",menu);
        val.put("kind",kind);
        if(db.insert("CafeMenu",null,val)<0){
            db.close();
            return 0;//실패
        }else{
            db.close();
            return 1;//성공
        }
    }


    public String check(String name,String kind) {
        int check = 0;
        String find = null;
        //String
        db = cafeMenuDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CafeMenu WHERE  cafename = '"+name+"' and kind = '"+kind+"'", null);

        while (cursor.moveToNext()) {
            String gets = cursor.getString(0);
            find = cursor.getString(1);
            String g = cursor.getString(2);
            if(name.equals(gets)&& kind.equals(g)){
                check=1;
            }
        }
        if(check == 0) {
            //없다
            return null;
        }else {
            //있다
            return find;
        }
    }
}
