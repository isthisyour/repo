package com.example.a_juan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    private EditText card,name,price,show;
    Button add,cx;
    Integer sum;

    SQLite sqLite;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sqLite = new SQLite(MainActivity2.this);
        db = sqLite.getWritableDatabase();

        card = findViewById(R.id.card);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        show = findViewById(R.id.show);
        add = findViewById(R.id.add);
        cx = findViewById(R.id.cx);

        card.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Toast.makeText(MainActivity2.this,"输入的为:"+card.getText().toString(),Toast.LENGTH_LONG).show();

                return false;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Card = card.getText().toString();
                String Name = name.getText().toString();
                String Price = price.getText().toString();
                add(Card,Name,Price);
                Log.i("tag", "添加成功");

            }
        });

        cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] s= cx();
                card.setText(s[0]);
                name.setText(s[1]);
                price.setText(s[2]);
                Log.i("tag", "查询成功");
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        db.close();
    }


    public void add(String card, String name, String price) {

        ContentValues cv = new ContentValues();
        cv.put("card", card);
        cv.put("name", name);
        cv.put("price", price);
        db.insert("commodity", null, cv);
    }

    public String[] cx() {
        Cursor cur = db.rawQuery("select * from commodity", null);



        sum = cur.getCount();
        String card1 = "";
        String name1 = "";
        String price1= "";
        String sUser = String.format("共有记录数量：%d:\n", sum);
        for (int i = 0; i < sum; i++) {
            cur.moveToPosition(i);
            //获得第一列和第二列的值
            sUser += String.format("第%d条\n卡号：%s\n名称：%s\n价格：%s\n\n", i + 1, cur.getString(0), cur.getString(1), cur.getString(2));
            card1= cur.getString(0);
            name1= cur.getString(1);
            price1= cur.getString(2);

        }
        String[] array = new String[]{card1,name1,price1};
        show.setText(sUser);
        return array;

    }
}