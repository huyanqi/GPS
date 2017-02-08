package com.codoon.clubcodoongps;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Frankie on 2017/1/17.
 */

public class TestService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("TestService--->onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("TestService--->onStart");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("TestService--->onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("TestService--->onBind");
        return new TestServiceBinder();
    }

    @Override
    public void onDestroy() {
        System.out.println("TestService--->onDestroy");
        super.onDestroy();
    }

    public class TestServiceBinder extends Binder {

        public TestService getService (){
            return TestService.this;
        }

    }

    private int num = 0;
    private Timer mTimer;
    private TimerTask mTimerTask;

    public void daojishi(int number){
        num = number;
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("进入run");
                if(num == 0){
                    mTimer.cancel();
                    System.out.println("取消timer");
                    return;
                }
                System.out.println("num:"+num);
                num--;
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

}
