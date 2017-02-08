package com.codoon.clubcodoongps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ToggleButton;

import com.codoon.clubcodoongps.widget.CalendarGroupView;
import com.codoon.clubcodoongps.widget.CalendarPojo;
import com.codoon.clubgps.core.GPSManager;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private ToggleButton fakeBtn;
    private boolean fakeChecked;
    private GPSManager mGPSManager;
    private String user_id = "tmp_user_id_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGPSManager = new GPSManager(MainActivity.this)
                        .userId(user_id);

        findViewById(R.id.run_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGPSManager.fake(fakeBtn.isChecked()).run();
            }
        });

        fakeBtn = (ToggleButton) findViewById(R.id.fake_btn);
        fakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeChecked = !fakeChecked;
                fakeBtn.setChecked(fakeChecked);
            }
        });

        findViewById(R.id.record_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGPSManager.getUserRecord(user_id);
            }
        });

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });

        Intent intent = new Intent(this, TestService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        CalendarGroupView calendarGroupView = (CalendarGroupView) findViewById(R.id.calendar_gv);

        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(1);

        int count = 7;
        String[] texts = new String[]{"一","二","三","四","五","六","日"};
        CalendarPojo[] datas = new CalendarPojo[count];
        CalendarPojo pojo;
        for(int i=0;i<count;i++){
            float ratio = Float.parseFloat(n.format(Math.random()));
            pojo = new CalendarPojo(ratio, texts[i], i == count-1 ? 1:0);
            datas[i] = pojo;
        }
        calendarGroupView.setDatas(datas);

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("main connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("main disconnected");
        }
    };

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

}
