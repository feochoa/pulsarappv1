package com.dawn.java.ui.activity;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dawn.java.R;
import com.dawn.java.ui.MyApplication;
import com.dawn.java.ui.activity.base.DawnActivity;
import com.dawn.java.ui.homePage.HomeActivity;
import com.dawn.java.ui.widget.TopToolbar;
import com.dawn.java.util.TimeDataUtil;
import com.dawn.java.util.WifiUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuthFuncActivity extends DawnActivity implements View.OnClickListener{
    private Button btnAuth;
    private Button btnIpScan;
    private TextView tvAuth;
    private TopToolbar topBar;
    private RadioGroup radioGroup;
    private int comType = 0;            //通讯类型
    private int threadStartType = 0;    //线程启动类型
    private Context context;
    private String wifiLocaIp="",pcServerIp="";
    private final int AUTH_HANDLE_IP = 0x01;
    private final int AUTH_HANDLE_SCANIP = 0x02;
    public static final String TAG = "AuthTag";
    private int nStartThreadFlag = 0;
    private int totalTime= 0;

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_auth:
               // startAuthThread();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_auth;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData(){
        // setScanCallback();
        // SoftEngine.getInstance().closeAuthThread();
    }

    private void initView() {
        context = getApplicationContext();
        topBar = findViewById(R.id.topBar);
        topBar.setMenuToolBarListener(new TopToolbar.MenuToolBarListener() {
            @Override
            public void onToolBarClickLeft(View v) {
                // SoftEngine.getInstance().closeAuthThread();
                finish();
            }

            @Override
            public void onToolBarClickRight(View v) {

            }
        });

        btnAuth = findViewById(R.id.btn_auth);
        btnAuth.setOnClickListener(this);
        tvAuth = findViewById(R.id.tv_auth);
        tvAuth.setMovementMethod(ScrollingMovementMethod.getInstance());
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.radioBtnAdb:
                        comType = 0;
                        btnIpScan.setVisibility(View.GONE);
                        break;
                    case R.id.radioBtnWifi:
                        comType = 1;
                        btnIpScan.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btnIpScan = findViewById(R.id.btn_ip);
        btnIpScan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(MyApplication.TAG, "App button touch down");
                        // ServiceTools.getInstance().startScan();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        Log.d(MyApplication.TAG, "App button touch up");
                        // ServiceTools.getInstance().stopScan();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }


    void refreshView(String msg)
    {
        String time = TimeDataUtil.timeStamp2Date(TimeDataUtil.timeStamp(), "yyyy-MM-dd HH:mm:ss");
        tvAuth.append(time+" "+msg);
        int offset=tvAuth.getLineCount()*tvAuth.getLineHeight();
        if(offset>tvAuth.getHeight())
        {
            tvAuth.scrollTo(0,offset-tvAuth.getHeight());
        }
    }

}
