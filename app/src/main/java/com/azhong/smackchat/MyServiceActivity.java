package com.azhong.smackchat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MyServiceActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TGA = "MyServiceActivity";

    private Button startService;

    private Button stopService;

    private Button bindService;

    private Button unbindService;

    private MyService.MyBinder myBinder;

    private MyAIDLService myAIDLService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            myBinder = (MyService.MyBinder) iBinder;
            myAIDLService = MyAIDLService.Stub.asInterface(iBinder);
            try {
                int result = myAIDLService.plus(3, 5);
                String upperStr = myAIDLService.toUpperCase("hello world");
                Log.d(TGA, "result is " + result);
                Log.d(TGA, "upperStr is " + upperStr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
           // myBinder.startDownload();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button) findViewById(R.id.stop_service);
        bindService = (Button) findViewById(R.id.bind_service);
        unbindService = (Button) findViewById(R.id.unbind_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);

        Log.d("MyService", "MainActivity thread id is " + Thread.currentThread().getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startIntent.setPackage("com.azhong.smackchat");
                startService(startIntent);
                break;
            case R.id.stop_service:
                Log.d("MyService", "click Stop Service button");
                Intent stopIntent = new Intent(this, MyService.class);
                stopIntent.setPackage("com.azhong.smackchat");
                stopService(stopIntent);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                bindIntent.setPackage("com.azhong.smackchat");
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                Log.d("MyService", "click Unbind Service button");
                unbindService(connection);
                break;
            default:
                break;
        }
    }
}
