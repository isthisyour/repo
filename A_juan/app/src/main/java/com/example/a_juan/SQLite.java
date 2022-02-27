package com.example.a_juan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SQLite extends SQLiteOpenHelper {
    public SQLite(@Nullable Context context) {
        super(context, "shop5.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table shop(card String, name String, price String)";
        db.execSQL(sql);

        ContentValues contentValues = new ContentValues();
        contentValues.put("card", "E2 00 00 1D 33 0E 00 87 25 00 3A 8F");
        contentValues.put("name", "华为mate20");
        contentValues.put("price", "5999");
        db.insert("shop", null, contentValues);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("card", "E2 00 00 1D 33 0E 00 86 25 00 33 A3");
        contentValues1.put("name", "IPhoneXS");
        contentValues1.put("price", "2299");
        db.insert("shop", null, contentValues1);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("card", "E2 00 00 1D 33 0E 00 90 25 00 3B 89");
        contentValues2.put("name", "小米 Mix3");
        contentValues2.put("price", "7699");
        db.insert("shop", null, contentValues2);

        Cursor cursor = db.query("shop", null, null, null, null, null, null);
        int count1 = cursor.getCount();
        if (count1 != 0) {
            cursor.moveToFirst();
            do {
                Log.e("tag", "onClick: " + cursor.getString(1));
                Log.e("tag", "onClick: " + cursor.getString(2));

            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
