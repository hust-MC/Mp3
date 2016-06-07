package com.example.machao10.Lrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.util.List;

/**
 * Created by machao10 on 2016/4/27.
 */
public class LrcView extends TextView {

    Paint currentPaint, otherPaint;

    private final int TEXT_INTERVAL = 78;

    public static final int TEXT_BIG = 0;
    public static final int TEXT_NORMAL = 1;
    public static final int TEXT_SMALL = 2;

    public static final int TEXT_RED = 10;
    public static final int TEXT_YELLOW = 20;
    public static final int TEXT_VIOLET = 30;

    int index;

    private int text_size_current = 95;
    private int text_size_other = 65;

    private int textColor = Color.argb(210, 250, 250, 30);

    List<Lrc> lrcList;

    public LrcView(Context context) {
        super(context);

        init();
    }

    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setLrcSize(int size) {
        switch (size) {
            case TEXT_BIG:
                text_size_current = 115;
                text_size_other = 85;
                break;

            case TEXT_SMALL:
                text_size_current = 75;
                text_size_other = 55;
                break;
            default:
                text_size_current = 95;
                text_size_other = 65;
        }
        currentPaint.setTextSize(text_size_current);
        otherPaint.setTextSize(text_size_other);
    }

    /**
     * 设置歌词高亮字体颜色
     *
     * @param color 字体颜色
     */
    public void setLrcColor(int color) {
        textColor = color;
        currentPaint.setColor(textColor);

    }

    private void init() {

        currentPaint = new Paint();
        currentPaint.setAntiAlias(true);
        currentPaint.setTextAlign(Paint.Align.CENTER);
        currentPaint.setColor(textColor);
        currentPaint.setTextSize(text_size_current);
        currentPaint.setTypeface(Typeface.DEFAULT);

        otherPaint = new Paint();
        otherPaint.setAntiAlias(true);
        otherPaint.setTextAlign(Paint.Align.CENTER);
        otherPaint.setColor(Color.argb(140, 0, 0, 0));
        otherPaint.setTextSize(text_size_other);
        otherPaint.setTypeface(Typeface.DEFAULT);

    }

    /**
     * 设置歌词信息列表
     *
     * @param url 当前歌词路径
     */
    public void searchLrc(String url) {
        lrcList = new LrcReader().getLrcByUrl(url);
    }

    public void update(int position) {
        int i;

        for (i = 0; i < lrcList.size() - 1; i++) {
            if (position >= lrcList.get(i).getLrcTime() && position < lrcList.get(i + 1).getLrcTime() || position < lrcList.get(0).getLrcTime()) {
                index = i;
                break;
            } else if (position > lrcList.get(lrcList.size() - 1).getLrcTime()) {
                index = lrcList.size() - 1;
                TranslateAnimation animation = new TranslateAnimation(getX(), getX(), getY(), getY() - TEXT_INTERVAL);
                animation.setDuration(2000);
                animation.setRepeatCount(1);
                setAnimation(animation);
                animation.startNow();
            }
        }
//        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lrcList.size() == 0) {
            canvas.drawText("没有找到歌词", getWidth() / 2, getHeight() / 2, currentPaint);
        } else {
            if (null != canvas) {
                int drawX = getWidth() / 2;
                int drawY = getHeight() / 2;

                canvas.drawText(lrcList.get(index).getLrcContent(), drawX, drawY, currentPaint);
                for (int i = index - 1; i >= 0; i--) {
                    drawY -= TEXT_INTERVAL;
                    canvas.drawText(lrcList.get(i).getLrcContent(), drawX, drawY, otherPaint);
                }
                drawY = getHeight() / 2;
                for (int i = index + 1; i < lrcList.size(); i++) {
                    drawY += TEXT_INTERVAL;
                    canvas.drawText(lrcList.get(i).getLrcContent(), drawX, drawY, otherPaint);
                }
            }
        }
    }
}
