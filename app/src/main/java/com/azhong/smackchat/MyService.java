package com.azhong.smackchat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by mangowangwang on 2018/1/18.
 */

public class MyService extends Service {
    public static final String TAG = "MyService";

   // private MyBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyService thread id is " + Thread.currentThread().getId());
        Log.d(TAG, "onCreate() executed");

    }

    /**
     * 前台service的方法
     */
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        //获取系统通知的管理类，负责发通知、清除通知等操作。
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//        mBuilder.setContentTitle("测试标题")//设置通知栏标题
//                .setContentText("测试内容") //设置通知栏显示内容</span>
//                //.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
//                //  .setNumber(number) //设置通知集合的数量
//                .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
//                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//                //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
//                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
//        Notification notification = mBuilder.build();
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        mNotificationManager.notify(1, mBuilder.build());
//
//
//


    private MyAIDLService.Stub  mBinder = new MyAIDLService.Stub() {

        @Override
        public String toUpperCase(String str) throws RemoteException {
            if (str != null) {
                return str.toUpperCase();
            }
            return null;
        }

        @Override
        public int plus(int a, int b) throws RemoteException {
            return a + b;
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class MyBinder extends Binder {

        public void startDownload() {
            Log.d("TAG", "startDownload() executed");
            // 执行具体的下载任务
        }

    }
}
