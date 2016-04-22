package com.example.machao10.mp3;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    ViewPager viewPager;
    ListView listView;
    TextView tvMusicName, tvMuiscTime;
    ImageView ivPicture;

    private void initViewPager() {
        final List<View> lvViewPager = new ArrayList<>();
        FrameLayout layoutListView = (FrameLayout) getLayoutInflater().inflate(R.layout.music_list, null);
        listView = (ListView) layoutListView.findViewById(R.id.lv_music_list);

        lvViewPager.add(layoutListView);
        lvViewPager.add(getLayoutInflater().inflate(R.layout.music_lrc, null));
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return lvViewPager.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(lvViewPager.get(position));
                return lvViewPager.get(position);
            }
        });

        listView.setAdapter(new SimpleAdapter(MainActivity.this, getMusicData(), R.layout.music_item, new String[]{"title", "artist", "duration"}, new int[]{R.id.item_name, R.id.item_artist, R.id.item_duration}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<MusicInfo> musicInfos = MusicInfoUtils.getMusicInfos(MainActivity.this);
                ivPicture.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate));

            }
        });
    }

    private void initWidget() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tvMusicName = (TextView) findViewById(R.id.music_name);
        tvMuiscTime = (TextView) findViewById(R.id.music_time);
        ivPicture = (ImageView) findViewById(R.id.pic);

        initViewPager();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            int readSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (readSDPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        123);
                return;
            }
        }

        setContentView(R.layout.activity_main);
        initWidget();

    }

    public List<Map<String, String>> getMusicData() {
        List<MusicInfo> musicInfos = MusicInfoUtils.getMusicInfos(this);
        List<Map<String, String>> musicData = new ArrayList<>();

        for (MusicInfo info : musicInfos) {
            Map<String, String> musicMap = new HashMap<>();
            musicMap.put("title", info.title);
            musicMap.put("artist", info.artist);
            if (info.duration % 60 < 10) {
                musicMap.put("duration", info.duration / 60 + ":0" + info.duration % 60);
            } else {
                musicMap.put("duration", info.duration / 60 + ":" + info.duration % 60);
            }


            musicData.add(musicMap);
        }
        return musicData;
    }
}
