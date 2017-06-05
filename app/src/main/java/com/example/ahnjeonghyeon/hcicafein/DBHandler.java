package com.example.ahnjeonghyeon.hcicafein;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Scanner;

/**
 * Created by AhnJeongHyeon on 2017. 6. 4..
 */

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="CafeIn";
    public static final String DATABASE_TABLE="cafes";

    Context context;
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
        this.context=context;
    }
    public Cursor getCafeList(){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="select * from "+DATABASE_TABLE;       //query문을 바꿔주면 될듯
        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE="create table if not exists "+DATABASE_TABLE
                +"('key' integer primary key autoincrement, 'name' text," +
                " 'address' text, 'phone' text)";
        db.execSQL(CREATE_TABLE);

        Scanner scanner=new Scanner(context.getResources().openRawResource(R.raw.cafein));
        String json="";
        while(scanner.hasNextLine()){
            json=scanner.nextLine();
            try {
                JSONObject jsonObject=new JSONObject(json);
                String name=jsonObject.getString("name");
                String address=jsonObject.getString("address");
                String phone=jsonObject.getString("phone");

                ContentValues values=new ContentValues();
                values.put("name",name);
                values.put("address",address);
                values.put("phone",phone);
                db.insert(DATABASE_TABLE,null,values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public Cafe findCafe(String cafeName) {
        String query = "select * from " + DATABASE_TABLE + " where " + "name =\'" + cafeName + "\';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Cafe cafe = new Cafe();
        if (cursor.moveToFirst()) {
            cafe.setName(cursor.getString(0));
            cafe.setAddress(cursor.getString(1));
            cafe.setPhone(cursor.getString(2));
            cursor.close();
        }else{
            cafe=null;
        }
        db.close();

        return cafe;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
