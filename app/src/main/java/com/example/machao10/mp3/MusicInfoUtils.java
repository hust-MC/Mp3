package com.example.machao10.mp3;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 管理歌曲信息，类加载时获取一次歌曲信息，对外提供fresh函数，用以刷新歌曲数据。
 */
public class MusicInfoUtils {

    private static List<MusicInfo> musicInfos;

    public static List<MusicInfo> getMusicInfos(Context context) {
        if (null == musicInfos) {
            musicInfos = getMusicInfoList(context);
        }
        return musicInfos;
    }

    public static void refreshMusicInfo(Context context) {
        musicInfos = getMusicInfoList(context);
    }


    private static List<MusicInfo> getMusicInfoList(Context context) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        List<MusicInfo> list = new ArrayList<>();

        int count = cursor.getCount();
        while (count-- > 0) {
            cursor.moveToNext();

            if (0 == cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC))) {
                continue;
            }

            MusicInfo info = new MusicInfo();
            info.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            info.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            info.duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) / 1000;
            info.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            info.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            info.data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            list.add(info);
        }

        return list;
    }
}

class MusicInfo {
    long id;
    String title;
    String artist;
    long duration;
    long size;
    String data;
}