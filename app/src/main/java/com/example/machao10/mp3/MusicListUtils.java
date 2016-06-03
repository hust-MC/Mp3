package com.example.machao10.mp3;


import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理歌曲信息，类加载时获取一次歌曲信息，对外提供fresh函数，用以刷新歌曲数据。
 */
public class MusicListUtils {
    private static final Uri ARTWORK_URI = Uri
            .parse("content://media/external/audio/albumart");

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

            long durationSeconds = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) / 1000;
            info.durationInSeconds = (int) durationSeconds;
            info.duration = durationSeconds % 60 < 10 ? durationSeconds / 60 + ":0" + durationSeconds % 60 : durationSeconds / 60 + ":" + durationSeconds % 60;
            info.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            info.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            info.data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            info.albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            list.add(info);
        }

        return list;
    }


    public static Bitmap getMusicPoster(Context context, long songid,
                                        long albumid) {
        Bitmap bm = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException(
                    "Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                return null;
            } else {
                Uri uri = ContentUris.withAppendedId(ARTWORK_URI, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                } else {
                    return null;
                }
            }
        } catch (
                FileNotFoundException ex
                )

        {
            ex.printStackTrace();
        }

        return bm;
    }


}

class MusicInfo {
    long id;
    String title;
    String artist;
    String duration;
    int durationInSeconds;
    long size;
    String data;
    long albumId;

    @Override
    public boolean equals(Object o) {
        data = data.replace("file://", "");
        return data.equals(((MusicInfo) o).data);
    }
}