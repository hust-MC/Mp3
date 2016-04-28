package com.example.machao10.mp3;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machao10.Lrc.LrcView;
import com.example.machao10.playservice.PlayService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    ViewPager viewPager;
    TextView tvMusicName, tvMuiscTime;
    ImageView ivPicture;
    ImageButton btPlay, btPrevious, btNext, btMode, btSettings;
    SeekBar seekBar;

    List<MusicInfo> musicInfos;
    List<Fragment> fragmentList;
    Mp3Player player;
    Mp3Receiver receiver;

    public final int REQUEST_SETTINGS = 100;

    /**
     * android 6的机型必须动态申请权限
     */
    private void android6Permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int readSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (readSDPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        123);
            }
        }
    }

    private void initViewPager() {
        FragmentListView fragmentListView = new FragmentListView();
        fragmentListView.setOnMusicItemClickListener(new MusicItemClickeListener() {
            /**
             * 歌曲列表被点击事件处理
             * @param position  被点击的歌曲位置
             */
            @Override
            public void onMusicItemClicked(int position) {
                play(position);
            }
        });
        FragmentLRC fragmentLRC = new FragmentLRC();
        fragmentList = new ArrayList<>();
        fragmentList.add(fragmentListView);
        fragmentList.add(fragmentLRC);

        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
    }

    private void initWidget() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        tvMusicName = (TextView) findViewById(R.id.music_name);
        tvMuiscTime = (TextView) findViewById(R.id.music_time);
        ivPicture = (ImageView) findViewById(R.id.pic);
        seekBar = (SeekBar) findViewById(R.id.seek);


        btPlay = (ImageButton) findViewById(R.id.bt_play);
        btPrevious = (ImageButton) findViewById(R.id.bt_previous);
        btNext = (ImageButton) findViewById(R.id.bt_next);
        btMode = (ImageButton) findViewById(R.id.bt_mode);
        btSettings = (ImageButton) findViewById(R.id.bt_settings);

        ControlButtonListener listener = new ControlButtonListener();
        btPlay.setOnClickListener(listener);
        btPrevious.setOnClickListener(listener);
        btNext.setOnClickListener(listener);
        btMode.setOnClickListener(listener);
        btSettings.setOnClickListener(listener);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.resume(seekBar.getProgress());
            }
        });

        initViewPager();
    }

    private void registerMp3Receiver() {
        receiver = new MainActivity.Mp3Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Mp3Receiver.ACTION_NEW);
        filter.addAction(Mp3Receiver.ACTION_END);
        filter.addAction(Mp3Receiver.ACTION_UPDATE);
        registerReceiver(receiver, filter);
    }

    private void setDisplay() {
        MusicInfo info = musicInfos.get(player.getCurrentMusic());
        Bitmap albumBitmap = MusicListUtils.getMusicPoster(MainActivity.this, info.albumId, info.albumId);
        if (albumBitmap == null) {
            ivPicture.setImageResource(R.drawable.cd);
        } else {
            ivPicture.setImageBitmap(albumBitmap);
        }
        tvMusicName.setText(info.title);
        tvMuiscTime.setText(info.duration);
        seekBar.setMax(info.durationInSeconds);
        seekBar.setProgress(0);
    }

    private void setMode(int mode) {
        switch (mode) {
            case Mp3Player.MODE_LOOP_ALL:
                btMode.setImageResource(R.drawable.loop_all);
                break;
            case Mp3Player.MODE_LOOP_SINGLE:
                btMode.setImageResource(R.drawable.loop_single);
                break;
            case Mp3Player.MODE_RANDOM:
                btMode.setImageResource(R.drawable.random);
                break;
            case Mp3Player.MODE_SEQ:
                btMode.setImageResource(R.drawable.seq);
                break;
        }
        player.setMode(mode);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.key_play_mode), "a" + mode);
        editor.apply();
    }

    private void play() {
        play(player.getCurrentMusic());
    }

    private void play(int musicNum) {
        player.play(musicNum);
        setDisplay();
    }

    private void play(String path) {
        player.play(path);
        setDisplay();
    }

    private void next() {
        if (player.next()) {
            setDisplay();
        } else {
            Toast.makeText(this, "已经是最后一首", Toast.LENGTH_SHORT).show();
        }
    }

    private void previous() {
        if (player.previous()) {
            setDisplay();
        } else {
            Toast.makeText(this, "已经是第一首", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android6Permission();

        setContentView(R.layout.activity_main);
        initWidget();
        bindService(new Intent(MainActivity.this, PlayService.class), connection, Context.BIND_AUTO_CREATE);
        registerMp3Receiver();

        musicInfos = MusicListUtils.getMusicInfos(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
        unbindService(connection);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.Mp3Binder binder = (PlayService.Mp3Binder) service;
            player = new Mp3Player(binder.getService(), musicInfos);
            ((FragmentLRC) fragmentList.get(1)).searchLrc(MusicListUtils.getMusicInfos(MainActivity.this).get(player.getCurrentMusic()).data);

            /*
         * 判断是否是从文件中启动
         */
            Intent intent = getIntent();
            if (intent.getAction().equals(Intent.ACTION_VIEW)) {
                MusicInfo externMusic = new MusicInfo();
                externMusic.data = Uri.decode(intent.getData().toString());
                play(musicInfos.indexOf(externMusic));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        设置菜单处理~
         */
        if (REQUEST_SETTINGS == requestCode) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            /*处理播放模式*/
            String mode = sp.getString(getString(R.string.key_play_mode), "" + Mp3Player.MODE_LOOP_ALL).substring(1);
            setMode(Integer.parseInt(mode));

            /*处理歌词颜色*/
            String lrcColor = sp.getString(getString(R.string.key_lrc_color), LrcView.TEXT_YELLOW + "").substring(1);
            ((FragmentLRC) fragmentList.get(1)).setLrcColor(Integer.parseInt(lrcColor));

            /*处理歌词大小*/
            String lrcSize = sp.getString(getString(R.string.key_lrc_size), LrcView.TEXT_NORMAL + "").substring(1);
            ((FragmentLRC) fragmentList.get(1)).setLrcSize(Integer.parseInt(lrcSize));
        }
    }

    class ControlButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_settings:
                    startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), REQUEST_SETTINGS);
                    break;
                case R.id.bt_play:
                    int state = player.getState();
                    ((ImageButton) v).setImageResource(Mp3Player.STATE_PLAY == state ? R.drawable.pause : R.drawable.play);
                    if (Mp3Player.STATE_STOP == state) {
                        play();
                    } else if (Mp3Player.STATE_PLAY == state) {
                        player.pause();
                    } else if (Mp3Player.STATE_PAUSE == state) {
                        player.resume();
                    }
                    break;
                case R.id.bt_previous:
                    previous();
                    break;
                case R.id.bt_next:
                    next();
                    break;
                case R.id.bt_mode:
                    switch (player.getMode()) {
                        case Mp3Player.MODE_SEQ:
                            setMode(Mp3Player.MODE_LOOP_ALL);
                            break;
                        case Mp3Player.MODE_LOOP_ALL:
                            setMode(Mp3Player.MODE_RANDOM);
                            break;
                        case Mp3Player.MODE_RANDOM:
                            setMode(Mp3Player.MODE_LOOP_SINGLE);
                            break;
                        default:
                            setMode(Mp3Player.MODE_SEQ);
                    }
                    break;
            }
        }
    }

    public class HomePagerAdapter extends FragmentPagerAdapter {
        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    public class Mp3Receiver extends BroadcastReceiver {

        public static final String ACTION_NEW = "com.example.machao10.ACTION_NEW";
        public static final String ACTION_END = "com.example.machao10.ACTION_END";
        public static final String ACTION_UPDATE = "com.example.machao10.ACTION_UPDATE";

        public Mp3Receiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_NEW:
                    ((FragmentLRC) fragmentList.get(1)).searchLrc(MusicListUtils.getMusicInfos(MainActivity.this).get(player.getCurrentMusic()).data);
                    break;
                case ACTION_END:
                    next();
                    break;
                case ACTION_UPDATE:
                    int currentPosition = intent.getIntExtra("currentPosition", 0);
                    seekBar.setProgress(currentPosition / 1000);
                    ((FragmentLRC) fragmentList.get(1)).update(currentPosition);

                    break;
            }
        }
    }
}
