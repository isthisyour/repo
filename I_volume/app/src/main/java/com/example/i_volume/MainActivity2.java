package com.example.i_volume;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150RelayListener;
import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity2 extends AppCompatActivity {

    private ImageView iv_left,iv_right;

    private Modbus4150 modbus4150;

    boolean x = false;

    Thread thread;
    Handler handler;

    int time = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);

                try {
                    modbus4150.getVal(1, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            Log.i("tag", "获取数据 " + i);
                            if (i == 1) {
                                time = 0;
                                x = true;
                            } else {
                                x = false;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (x == true) {
                    time = 0;
                    dohua_open();

                    try {
                        out();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (time == 60) {
                        try {
                            come();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dohua_close();
                    }
                }
            }
        };
        handler.postDelayed(runnable, 500);

        Runnable gettime = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                time++;
                Log.i("tag", "time:"+time);

            }
        };

        handler.postDelayed(gettime, 1000);


    }





    private void dohua_close() {
        iv_left.animate().translationX(0);
        iv_right.animate().translationX(0);
    }

    private void dohua_open() {
        iv_left.animate().translationX(-50);
        iv_right.animate().translationX(50);

    }

    private void init() {
        iv_left = findViewById(R.id.iv_left);
        iv_right = findViewById(R.id.iv_right);

        handler = new Handler();

//        modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(1, 9600));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("192.168.5.200",6002));
    }

    private void out() throws Exception {
        modbus4150.closeRelay(5, new MdBus4150RelayListener() {
            @Override
            public void onCtrl(boolean b) {

            }
        });

        modbus4150.openRelay(6, new MdBus4150RelayListener() {
            @Override
            public void onCtrl(boolean b) {

            }
        });

    }

    private void come() throws Exception {
        modbus4150.closeRelay(6, new MdBus4150RelayListener() {
            @Override
            public void onCtrl(boolean b) {

            }
        });

        modbus4150.openRelay(5, new MdBus4150RelayListener() {
            @Override
            public void onCtrl(boolean b) {

            }
        });
    }
}