package com.example.machao10.Lrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.List;

/**
 * Created by machao10 on 2016/4/27.
 */
public class LrcView extends TextView {

    Paint currentPaint, otherPaint;
    int index;
    private final int TEXT_INTERVAL = 78;
    private final int TEXT_SIZE_CURRENT = 95;
    private final int TEXT_SIZE_OTHER = 65;
    List<Lrc> lrcList;

    public LrcView(Context context) {
        super(context);

        init();
    }

    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        currentPaint = new Paint();
        currentPaint.setAntiAlias(true);
        currentPaint.setTextAlign(Paint.Align.CENTER);
        currentPaint.setColor(Color.argb(210, 250, 250, 30));
        currentPaint.setTextSize(TEXT_SIZE_CURRENT);
        currentPaint.setTypeface(Typeface.DEFAULT);

        otherPaint = new Paint();
        otherPaint.setAntiAlias(true);
        otherPaint.setTextAlign(Paint.Align.CENTER);
        otherPaint.setColor(Color.argb(140, 0, 0, 0));
        otherPaint.setTextSize(TEXT_SIZE_OTHER);
        otherPaint.setTypeface(Typeface.DEFAULT);

    }

    /**
     * 设置歌词信息列表
     *
     * @param url 当前歌词路径
     */
    public void searchLrc(String url) {
        lrcList = new LrcReader().getLrc(url);
    }

    public void update(int position) {
        int i;

        for (i = 0; i < lrcList.size() - 1; i++) {
            if (position >= lrcList.get(i).getLrcTime() && position < lrcList.get(i + 1).getLrcTime() || position < lrcList.get(0).getLrcTime()) {
                index = i;
                invalidate();
                return;
            } else if (position > lrcList.get(lrcList.size() - 1).getLrcTime()) {
                index = lrcList.size() - 1;
                invalidate();
            }
        }
        index = i;
        invalidate();

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
