package com.example.machao10.Lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by machao10 on 2016/4/27.
 */
public class LrcReader {
    private static List<Lrc> lrcList;

    public LrcReader() {
        lrcList = new ArrayList<>();
    }

    public static List<Lrc> getCurrentLrc() {
        return lrcList;
    }

    public List<Lrc> getLrcByUrl(String path) {
        try {
            FileInputStream fis = new FileInputStream(new File(path.replace(".mp3", ".lrc")));
            InputStreamReader isReader = new InputStreamReader(fis, "GB2312");
            BufferedReader bReader = new BufferedReader(isReader);
            String s;
            while ((s = bReader.readLine()) != null) {
                Lrc lrc = new Lrc();
                //替换字符
                s = s.replace("[", "");

                //分离“@”字符
                String lrcValue[] = s.split("]");
                if (lrcValue.length > 1) {
                    //处理歌词取得歌曲的时间
                    int time = getTime(lrcValue[0]);
                    if (time < 0) {
                        continue;
                    }
                    lrc.setLrcTime(time);
                    lrc.setLrcContent(lrcValue[1]);
                    lrcList.add(lrc);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcList;
    }

    private int getTime(String str) {

        str = str.replace(":", ".");
        String[] strs = str.split("\\.");
        if (strs.length != 3) {
            return -1;
        }
        int m, s, ms;
        try {
            m = Integer.parseInt(strs[0]);
            s = Integer.parseInt(strs[1]);
            ms = Integer.parseInt(strs[2]);
        } catch (Exception e) {
            return -1;
        }

        return (m * 60 + s) * 1000 + ms;
    }

}
