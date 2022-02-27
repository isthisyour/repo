package com.example.a_juan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.forUse.zigbee.ZigbeeControlListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    private Button button,but2;
    private EditText editText1,editText2,editText3,editText4;
    private ImageView imageView,fs;
    AnimationDrawable fs_dh;

    String TAG = "tag";

    private Zigbee zigbee;
    private Modbus4150 modbus4150;

    private double light,wd,sd;
    double[] four;

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 1000);

                        editText1.setText(""+light);
                        editText2.setText(""+wd);

                        Log.i(TAG, "光照"+light);
                        Log.i(TAG, "温度"+wd);

                        //光照传感器
                        try {
                            light = zigbee.getLight();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //四输入温度
                        try {
                            four = zigbee.getFourEnter();
                            wd = FourChannelValConvert.getTemperature(four[1]);
                            sd = FourChannelValConvert.getHumidity(four[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        String y=editText3.getText().toString();
                        String y1=editText4.getText().toString();

                        if(y.equals("") || y1.equals(""))
                            Toast.makeText(MainActivity.this, "请输入阀值", Toast.LENGTH_SHORT).show();
                        else {
                            int j = Integer.parseInt(y);
                            double i =  light;
                            double i1 = wd;
                            int j1 = Integer.parseInt(y1);
                            if(i<j){
                                imageView.setImageResource(R.drawable.light);
                                openled();
                                Toast.makeText(MainActivity.this, "点亮", Toast.LENGTH_SHORT).show();


                            }else {
                                imageView.setImageResource(R.drawable.dark);
                                closeled();
//                                Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
                            }


                            if (j1 < i1) {
                                fs_dh.start();
                                openfs();
                                Toast.makeText(MainActivity.this, "启动", Toast.LENGTH_SHORT).show();
                            }else {
                                fs_dh.stop();
                                closefs();
//                                Toast.makeText(MainActivity.this, "停止", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                };
                handler.postDelayed(runnable1, 1000);


            }
        });

    }

    private void init() {
        button = findViewById(R.id.but1);
        but2 = findViewById(R.id.but2);
        editText1=findViewById(R.id.edittext_1);
        editText2=findViewById(R.id.edittext_2);
        editText3=findViewById(R.id.edittext_3);
        editText4=findViewById(R.id.edittext_4);
        imageView=findViewById(R.id.buld);
        fs = findViewById(R.id.fs);
        fs_dh = (AnimationDrawable) fs.getBackground();

        handler = new Handler();

        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2, 9600));
        modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(1, 38400));

//        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.16.16.15", 6002));
//        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.16.16.15", 6001));

    }

    public void getzigbee() {
        //光照传感器
        try {
            light = zigbee.getLight();

            Log.e(TAG, "getzigbee: "+light );
            editText1.setText(light+"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //四输入温度
        try {
            four = zigbee.getFourEnter();
            wd = FourChannelValConvert.getTemperature(four[1]);
            sd = FourChannelValConvert.getHumidity(four[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class mythread extends Thread {
        @Override
        public void run() {
            super.run();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 1000);

                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    public void openled() {
        try {
            zigbee.ctrlDoubleRelay(0xffff, 2, true, new ZigbeeControlListener() {
                @Override
                public void onCtrl(boolean b) {
                    Log.i("tag", "灯打开成功");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeled() {
        try {
            zigbee.ctrlDoubleRelay(0xffff, 2, false, new ZigbeeControlListener() {
                @Override
                public void onCtrl(boolean b) {
                    Log.i("tag", "灯关闭成功");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openfs() {
        try {
            zigbee.ctrlDoubleRelay(0xffff, 1, true, new ZigbeeControlListener() {
                @Override
                public void onCtrl(boolean b) {
                    Log.i("tag", "风扇打开成功");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closefs() {
        try {
            zigbee.ctrlDoubleRelay(0xffff, 1, false, new ZigbeeControlListener() {
                @Override
                public void onCtrl(boolean b) {
                    Log.i("tag", "风扇关闭成功");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void finish() {
        super.finish();
        Log.e(TAG, "finish: 结束" );
    }
}