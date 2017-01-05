package com.codoon.clubcodoongps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ToggleButton;

import com.codoon.clubgps.bean.RecordDetail;
import com.codoon.clubgps.core.GPSManager;

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
                List<RecordDetail> recordDetailList = mGPSManager.getUserRecord(user_id);
                for(RecordDetail recordDetail : recordDetailList){
                    System.out.println(recordDetail);
                }
            }
        });

    }
}
