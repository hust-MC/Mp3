package com.example.machao10.Lrc;

/**
 * Created by machao10 on 2016/4/27.
 */
public class Lrc {
    private String lrcContent;
    private int lrcTime;

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }

    /**
     * 获取当前歌词的播放时间
     * @return 返回当前词条的播放时间，单位是Ms
     */
    public int getLrcTime() {
        return lrcTime;
    }

    public void setLrcTime(int lrcTime) {
        this.lrcTime = lrcTime;
    }
}
