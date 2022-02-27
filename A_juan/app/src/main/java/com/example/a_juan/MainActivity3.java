package com.example.a_juan;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity3 extends AppCompatActivity {

    private EditText card,name,price,show;

    String card1;

    Button add,cx;
    Integer sum;

    private RFID rfid;

    SQLite sqLite;
    SQLiteDatabase db;
    Cursor cursor;

    TextToSpeech textToSpeech;

    Handler handler;
    Runnable runnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 6000);

                try {
                    rfid.readSingleEpc(new SingleEpcListener() {
                        @Override
                        public void onVal(String s) {
                            card1 = s;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("tag", "卡号   "+card1 );

                if(card1 == null){

                }
                else {
                    cursor = db.query("shop", null, "card=?", new String[]{card1}, null, null, null);
                    int count = cursor.getCount();
                    if (count != 0) {
                        cursor.moveToFirst();
                        do {
                            card.setText(cursor.getString(0));
                            name.setText(cursor.getString(1));
                            price.setText(cursor.getString(2));

                        } while (cursor.moveToNext());
                    }
                    textToSpeech.speak("商品名称" + name.getText().toString() + "价格" + price.getText().toString(), 0, null);

                }
                card1 = null;
            }
        };
        handler.postDelayed(runnable, 1000);




    }

    private void init() {
        sqLite = new SQLite(MainActivity3.this);
        db = sqLite.getWritableDatabase();

        rfid = new RFID(DataBusFactory.newSerialDataBus(3, 115200));

//        rfid = new RFID(DataBusFactory.newSocketDataBus("172.16.16.15", 6003));

        card = findViewById(R.id.card);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        show = findViewById(R.id.show);
        add = findViewById(R.id.add);
        cx = findViewById(R.id.cx);

        add.setOnClickListener(new onclic());
        cx.setOnClickListener(new onclic());

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setSpeechRate(1.0f);
                textToSpeech.setPitch(1.0f);
            }
        });

        handler = new Handler();

    }


    private class onclic implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add:

                    cursor = db.query("shop", null, null, null, null, null, null);
                    int count1 = cursor.getCount();
                    if (count1 != 0) {
                        cursor.moveToFirst();
                        do {

                            Log.e("tag", "卡号：" + cursor.getString(0));
                            Log.e("tag", "名字: " + cursor.getString(1));
                            Log.e("tag", "价格: " + cursor.getString(2));

                        } while (cursor.moveToNext());

                        break;
                    }
                case R.id.cx:
                    try {
                        rfid.readSingleEpc(new SingleEpcListener() {
                            @Override
                            public void onVal(String s) {
                                Log.i("tag", "卡号"+s);
                                card1 = s;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("tag", "卡号"+card1 );
                    cursor = db.query("shop", null,"card=?", new String[] {card1}, null, null, null);
                    int count = cursor.getCount();
                    if (count != 0) {
                        cursor.moveToFirst();
                        do {
                            name.setText(cursor.getString(1));
                            price.setText(cursor.getString(2));

                        }while (cursor.moveToNext());
                    }
                    textToSpeech.speak("商品名称" + name.getText().toString() + "价格" + price.getText().toString(),0,null);
                    break;
            }
        }
    }

    public void getcard(){
        try {
            rfid.readSingleEpc(new SingleEpcListener() {
                @Override
                public void onVal(String s) {
                    Log.i("tag", "卡号"+s);
                    card1 = s;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
