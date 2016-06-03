package com.example.machao10.mp3;

import com.example.machao10.playservice.PlayService;

import java.util.List;

/**
 * Created by machao10 on 2016/4/26.
 */
public class Mp3Player {

    public static final int STATE_PLAY = 0;
    public static final int STATE_PAUSE = 1;
    public static final int STATE_STOP = 2;

    public static final int MODE_SEQ = 100;
    public static final int MODE_LOOP_ALL = 101;
    public static final int MODE_LOOP_SINGLE = 102;
    public static final int MODE_RANDOM = 103;


    private PlayService mp3Binder;
    private List<MusicInfo> musicInfos;

    private int currentMusic;
    private int state = STATE_STOP;
    private int mode = MODE_LOOP_ALL;


    public Mp3Player(PlayService mp3Binder, List<MusicInfo> musicInfos) {
        this.mp3Binder = mp3Binder;
        this.musicInfos = musicInfos;
    }

    public int getState() {
        return state;
    }

    public void setCurrentMusic(int current) {
        currentMusic = current;
    }

    public int getCurrentMusic() {
        return currentMusic;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * 从断点开始播放当前歌曲
     */
    void play(int musicNum) {
        mp3Binder.play(musicInfos.get(musicNum).data);
        setCurrentMusic(musicNum);
        state = STATE_PLAY;
    }

    void play(String path) {
        mp3Binder.play(path);
    }

    void pause() {
        mp3Binder.pause();
        state = STATE_PAUSE;
    }

    /**
     * 恢复播放
     *
     * @param position 为负则为从暂停中恢复，为正则为从断点恢复。单位是S
     */
    void resume(int position) {
        mp3Binder.resume(position);
        state = STATE_PLAY;
    }


    /**
     * 从暂停中恢复
     */
    void resume() {
        resume(-1);
    }

    boolean previous() {
        if (MODE_RANDOM == mode) {
            next();
        } else if (currentMusic <= 0) {
            if (MODE_LOOP_ALL == mode) {
                play(musicInfos.size() - 1);
            } else {
                return false;
            }
        } else {
            play(currentMusic - 1);
        }
        return true;
    }


    /**
     * 返回非0表示顺序播放状态到最后一首歌
     *
     * @return 如果没有下一首，返回-1，否则返回0
     */
    boolean next() {
        if (MODE_SEQ == mode && currentMusic + 1 >= musicInfos.size()) {
            return false;
        }
        play(getNextMusic());
        return true;
    }

    public void stop() {
        mp3Binder.stop();
    }


    private int getNextMusic() {
        switch (mode) {
            case MODE_SEQ:
                if (currentMusic + 1 < musicInfos.size()) {
                    return currentMusic + 1;
                }
            case MODE_LOOP_SINGLE:
                return currentMusic;
            case MODE_LOOP_ALL:
                if (currentMusic + 1 < musicInfos.size()) {
                    return currentMusic + 1;
                } else {
                    return 0;
                }
            case MODE_RANDOM:
                return (int) (Math.random() * musicInfos.size() - 1);
            default:
                return 0;
        }
    }
}
