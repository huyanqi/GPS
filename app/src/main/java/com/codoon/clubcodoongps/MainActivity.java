package com.codoon.clubcodoongps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ToggleButton;

import com.codoon.clubgps.bean.PaceChatViewPojo;
import com.codoon.clubgps.core.GPSManager;
import com.codoon.clubgps.widget.PaceChatView;

import java.util.ArrayList;
import java.util.List;

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

        PaceChatView mPaceChatView = (PaceChatView) findViewById(R.id.pace_chat_view);
        List<PaceChatViewPojo> datas = new ArrayList<>();
        datas.add(new PaceChatViewPojo("1公里", 150));
        datas.add(new PaceChatViewPojo("3公里", 170));
        datas.add(new PaceChatViewPojo("5公里", 85 ));
        datas.add(new PaceChatViewPojo("7公里", 32 ));
        datas.add(new PaceChatViewPojo("16公里", 187 ));
        datas.add(new PaceChatViewPojo("9公里", 1 ));
        datas.add(new PaceChatViewPojo("20公里", 170 ));
        mPaceChatView.setDatas(datas);


    }
}
