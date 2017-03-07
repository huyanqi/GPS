package com.codoon.clubcodoongps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ToggleButton;

import com.codoon.clubcodoongps.widget.CalendarGroupView;
import com.codoon.clubcodoongps.widget.CalendarPojo;
import com.codoon.clubcodoongps.widget.ChatPojo;
import com.codoon.clubcodoongps.widget.NewElevationView;
import com.codoon.clubcodoongps.widget.NewHistoryCountView;
import com.codoon.clubcodoongps.widget.NewPaceView;
import com.codoon.clubcodoongps.widget.NewStepFreqView;
import com.codoon.clubgps.core.GPSManager;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ToggleButton fakeBtn;
    private boolean fakeChecked;
    private GPSManager mGPSManager;
    private String user_id = "tmp_user_id_1";
    private MediaPlayer mPlayer;
    private NewHistoryCountView mNewHistoryCountView;
    private NewPaceView mNewPaceView;
    private NewElevationView mNewElevationView;
    private NewStepFreqView mNewStepFreqView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewHistoryCountView = (NewHistoryCountView) findViewById(R.id.new_history_view);
        mNewPaceView = (NewPaceView) findViewById(R.id.new_pace_view);
        mNewElevationView = (NewElevationView) findViewById(R.id.new_elevation_view);
        mNewStepFreqView = (NewStepFreqView) findViewById(R.id.new_stepfreq_view);

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

        findViewById(R.id.play_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*EditText voiceEt = ((EditText)findViewById(R.id.voice_num));
                String voiceText = voiceEt.getText().toString();
                if(TextUtils.isEmpty(voiceText)) voiceText = "0";
                VoicePlayer voicePlayer = VoicePlayer.getInstance();
                voicePlayer.runFinish();*/
                /*MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.exercise_to_relax);
                mediaPlayer.start();*/
                String path = getFilesDir() + File.separator + "sound";
                try {
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(path);
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer arg0) {
                            Logger.e("tag", "player.setOnCompletionListener");
                            if (mPlayer != null) {
                                mPlayer.release();
                                mPlayer = null;
                            }
                        }
                    });
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

        initNewStepFreqView();
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

    private void initNewStepFreqView(){
        List<ChatPojo> datas = new ArrayList<>();
        /*datas.add(new ChatPojo(234,"234", ""));
        datas.add(new ChatPojo(23,"23", ""));
        datas.add(new ChatPojo(321,"321", ""));
        datas.add(new ChatPojo(78,"78", ""));
        datas.add(new ChatPojo(76,"76", ""));
        datas.add(new ChatPojo(234,"234", "第一个"));
        datas.add(new ChatPojo(67,"67", "第二个"));
        datas.add(new ChatPojo(23,"23", ""));
        datas.add(new ChatPojo(43,"43", ""));
        datas.add(new ChatPojo(87,"87", ""));
        datas.add(new ChatPojo(13,"13", "第三个"));
        datas.add(new ChatPojo(45,"45", ""));
        datas.add(new ChatPojo(58,"58", "第四个"));
        datas.add(new ChatPojo(567,"567", "第五个"));
        datas.add(new ChatPojo(65,"65", ""));
        datas.add(new ChatPojo(55,"55", ""));
        datas.add(new ChatPojo(56,"56", ""));
        datas.add(new ChatPojo(33,"33", ""));
        datas.add(new ChatPojo(23,"23", "第六个"));
        datas.add(new ChatPojo(67,"67", "第七个"));
        datas.add(new ChatPojo(23,"23", ""));
        datas.add(new ChatPojo(43,"43", ""));
        datas.add(new ChatPojo(87,"87", ""));
        datas.add(new ChatPojo(13,"13", "第八个"));
        datas.add(new ChatPojo(45,"45", ""));
        datas.add(new ChatPojo(58,"58", "第九个"));
        datas.add(new ChatPojo(567,"567", "第十个"));
        datas.add(new ChatPojo(65,"65", ""));
        datas.add(new ChatPojo(33,"33", ""));
        datas.add(new ChatPojo(23,"23", "第十一个"));
        datas.add(new ChatPojo(58,"58", "第十二个"));
        datas.add(new ChatPojo(55,"55", ""));
        datas.add(new ChatPojo(56,"56", ""));
        datas.add(new ChatPojo(567,"567", "第十三个"));*/


        datas.add(new ChatPojo(1,"1公里", "1"));
        datas.add(new ChatPojo(23,"23公里", "•"));
        datas.add(new ChatPojo(23,"23公里", "•"));
        datas.add(new ChatPojo(23,"23公里", "•"));
        datas.add(new ChatPojo(45,"45公里", "5"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "10"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(23,"23公里", "15"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(45,"45公里", "20"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(23,"23公里", "25"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(65.8f,"65.8公里", "•"));
        datas.add(new ChatPojo(23,"23公里", "30"));
        datas.add(new ChatPojo(23,"23公里", "•"));
        mNewStepFreqView.setDatas(datas);
        mNewElevationView.setDatas(datas);
        mNewPaceView.setDatas(datas);
        mNewHistoryCountView.setDatas(datas);

    }

    /**
     * 为避免每段数据不平均,补全数据
     */
    private List<ChatPojo> makeUpData(List<ChatPojo> datas){
        List<ChatPojo> result = new ArrayList<>();
        //存每段数据
        List<List<ChatPojo>> _cache = new ArrayList<>();
        //1.取最长一段的数据数量
        List<ChatPojo> tmp = new ArrayList<>();
        int count = 0;
        int max_count = 0;
        for(ChatPojo chatPojo : datas){
            count++;
            tmp.add(chatPojo);
            if(!TextUtils.isEmpty(chatPojo.getBottomText())){
                //每一段的最后一个点
                _cache.add(tmp);
                tmp = new ArrayList<>();
                max_count = Math.max(max_count, count);
                count = 0;
            }
        }

        //如果datas没有底部标签或者只有一段，直接返回原始数据，不需要补数据
        if(max_count == 0 || _cache.size() == 1) return datas;

        //2.获取每段数据与最长数据差的数据个数
        int size;//每段的大小
        int offset_count;//每段与最大段的个数差
        double offset_value;//每一段应该补的值
        float startValue;//应该补数据的开始值
        float endValue;//补数据的结束值
        List<ChatPojo> list;
        ChatPojo newPojo;
        String bottomText = "";
        for(int i = 0;i < _cache.size();i++){
            list = _cache.get(i);
            if(list.size() / max_count == 1) continue;
            size = list.size();
            offset_count = max_count - size;//差数据的个数
            startValue = list.get(size-1).getValue();

            //获取endValue
            if(i < _cache.size()-1){
                //取下一段的第一个值
                endValue = _cache.get(i+1).get(0).getValue();
            }else{
                //取当前值
                endValue = startValue;
            }

            //取这一段的bottomText
            for(ChatPojo chatPojo : list){
                if(!TextUtils.isEmpty(chatPojo.getBottomText())){
                    bottomText = chatPojo.getBottomText();
                    chatPojo.setBottomText("");
                    break;
                }
            }

            offset_value = (endValue - startValue) / offset_count;

            for(int j=0;j<offset_count;j++){
                newPojo = new ChatPojo((int) (startValue + offset_value), "", j == offset_count - 1 ? bottomText : "");
                list.add(newPojo);
                startValue = newPojo.getValue();
            }

        }

        for(List<ChatPojo> mlist : _cache){
            result.addAll(mlist);
        }

        return result;
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

}
