package com.example.machao10.playservice;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.example.machao10.mp3.MainActivity;

import java.io.IOException;

public class PlayService extends Service {
    Mp3Binder mp3Binder;
    MediaPlayer mediaPlayer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (null != mediaPlayer) {
                    Intent intent = new Intent();
                    intent.setAction(MainActivity.Mp3Receiver.ACTION_UPDATE);
                    intent.putExtra("currentPosition", mediaPlayer.getCurrentPosition());
                    sendBroadcast(intent);
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mp3Binder = new Mp3Binder();
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                sendBroadcast(new Intent(MainActivity.Mp3Receiver.ACTION_NEW));
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                sendBroadcast(new Intent(MainActivity.Mp3Receiver.ACTION_END));
            }
        });


    }

    @Override
    public void onDestroy() {
        if (null != mediaPlayer) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public IBinder onBind(Intent intent) {
        Log.d("MC", "bind");
        return mp3Binder;
    }

    public class Mp3Binder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    public void play(String path) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            handler.sendEmptyMessage(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume(int position) {
        mediaPlayer.start();
        if (position >= 0) {
            mediaPlayer.seekTo(position * 1000);
        }
    }
}
