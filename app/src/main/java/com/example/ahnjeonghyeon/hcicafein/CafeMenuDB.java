package com.example.ahnjeonghyeon.hcicafein;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Scanner;

/**
 * Created by hojingong on 2017. 6. 9..
 */

public class CafeMenuDB extends SQLiteOpenHelper{
    public static final String DB_NAME = "CafeMenu.db";
    public static final int DB_VERSION=1;
    public static final String DATABASE_TABLE = "CafeMenu";

    Context context;
    public CafeMenuDB(Context context){
        super(context,DB_NAME,null,DB_VERSION); this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists CafeMenu(cafename TEXT, menu TEXT, kind TEXT);");

        //json

        Scanner scanner=new Scanner(context.getResources().openRawResource(R.raw.cafemenu));
        String json="";
        while(scanner.hasNextLine()){
            json=scanner.nextLine();
            try {
                JSONObject jsonObject=new JSONObject(json);
                String name=jsonObject.getString("name");
                String address=jsonObject.getString("menu");
                String phone=jsonObject.getString("kind");

                ContentValues values=new ContentValues();
                values.put("cafename",name);
                values.put("menu",address);
                values.put("kind",phone);
                db.insert("CafeMenu",null,values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  db.execSQL("DROP TABLE IF EXISTS");
     //   onCreate(db);
    }
    public void delete(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CafeMenu;");
        db.close();
    }
}
