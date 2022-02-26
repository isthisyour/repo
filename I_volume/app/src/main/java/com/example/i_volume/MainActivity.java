package com.example.i_volume;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.forUse.mdbus4150.MdBus4150RelayListener;
import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    private String TAG = "tag";

    private String ip = "192.168.5.200";

    private TextView tv_hy;

    private ImageView iv_bjd;

    private AnimationDrawable ma;

    private Modbus4150 modbus4150;

    private LedScreen led;

    private Button bt_open,bt_close;

    private ImageView imageView;

    private Handler handler;
    private Runnable runnable;

    private boolean x = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        //p1无火焰
        imageView.setImageResource(R.drawable.p1);



    }

    private void init() {
        handler = new Handler();

        tv_hy = findViewById(R.id.tv_hy);

        iv_bjd = findViewById(R.id.iv_bjd);

        ma = (AnimationDrawable) iv_bjd.getBackground();

        led = new LedScreen(DataBusFactory.newSocketDataBus(ip,6002));

//        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus(ip,6001));

        modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(1, 9600));

        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);

        imageView = findViewById(R.id.imageview);


        bt_open.setOnClickListener(new onclick());
        bt_close.setOnClickListener(new onclick());

    }


    private class onclick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_open:
                    Log.i("TAG", "onClick: 开启监控");

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(this, 1000);


                            try {
                                Log.e(TAG, "获取数据" );
                                modbus4150.getVal(2, new MdBus4150SensorListener() {
                                    @Override
                                    public void onVal(int i) {
                                        if (i == 1) {
                                            Log.i(TAG, "onVal: 、有火焰");
                                            x = true;
                                        }else {
                                            Log.i(TAG, "onVal: 、无火焰");
                                            x = false;
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Log.e(TAG, "获取错误: "+e);
                            }

                            if (x==true){
                                try {
                                    led.switchLed(true);
                                    tv_hy.setText("有火焰");
                                    led.sendTxt("发现烟雾报警，小心火灾", PlayType.LEFT, ShowSpeed.SPEED5,1,100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                imageView.setImageResource(R.drawable.p2);
                                ma.start();

                                try {
                                    modbus4150.openRelay(0, new MdBus4150RelayListener() {
                                        @Override
                                        public void onCtrl(boolean b) {

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            else {
                                try {
                                    led.switchLed(false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                imageView.setImageResource(R.drawable.p1);
                                ma.stop();

                                try {
                                    modbus4150.closeRelay(0, new MdBus4150RelayListener() {
                                        @Override
                                        public void onCtrl(boolean b) {

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }



                        }
                    };
                    handler.postDelayed(runnable, 500);

                    break;


                case R.id.bt_close:
                    handler.removeCallbacks(runnable);

                    x=false;

                    try {
//                        led.switchLed(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imageView.setImageResource(R.drawable.p1);
                    ma.stop();

                    try {
                        modbus4150.closeRelay(0, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean b) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                    break;
            }

        }
    }
}