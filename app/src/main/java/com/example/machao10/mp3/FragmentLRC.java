package com.example.machao10.mp3;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.machao10.Lrc.Lrc;
import com.example.machao10.Lrc.LrcAdapter;
import com.example.machao10.Lrc.LrcReader;

import java.util.ArrayList;
import java.util.List;

public class FragmentLRC extends Fragment {
    Activity activity;

    ListView lvLrc;
    List<Lrc> lrcList = new ArrayList<>();
    LrcAdapter adapter;

    private int lrcColor;
    private int lrcSize;

    /**
     * 记录点击时的X坐标
     */
    private float x_down;

    /**
     * 记录点击时的Y坐标
     */
    private float y_down;

    /**
     * 记录移动时的X坐标
     */
    private float x_move;

    /**
     * 记录移动时的Y坐标
     */
    private float y_move;


    /**
     * 标志当前是否是自动滑动状态
     */
    private boolean isAutoScroll;
    /**
     * 标志当前listview是否正在处理触摸事件
     */
    private boolean isTouching;
    private boolean isFling;
    private boolean isVerticalScroll;


    public FragmentLRC() {
        // Required empty public constructor
    }


    private void initWidget(View v) {
        lvLrc = (ListView) v.findViewById(R.id.lv_lrc);
        adapter = new LrcAdapter(activity, lrcList);
        lvLrc.setAdapter(adapter);
        adapter.setLrcColor(lrcColor);
        adapter.setLrcSize(lrcSize);

        lvLrc.setOnTouchListener(new View.OnTouchListener() {
                                     @Override
                                     public boolean onTouch(View v, MotionEvent event) {

                                         switch (event.getAction()) {
                                             case MotionEvent.ACTION_DOWN:
                                                 isTouching = true;
                                                 break;
                                             case MotionEvent.ACTION_UP:
                                                 int time = lrcList.get(lvLrc.getFirstVisiblePosition() + 5).getLrcTime();
                                                 ((MainActivity) activity).resume(time / 1000);
                                                 isTouching = false;
                                                 break;
                                             case MotionEvent.ACTION_CANCEL:
                                                 isTouching = false;
                                                 break;
                                         }
                                         return false;
                                     }
                                 }
        );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music_lrc, container, false);

        initWidget(v);

        return v;
    }

    /**
     * 更新歌词内容
     *
     * @param position 当前演唱哪一句
     */
    public void update(int position) {
        if (!isTouching) {
            adapter.notifyDataSetChanged();
            isAutoScroll = true;
            lvLrc.smoothScrollToPositionFromTop(adapter.update(position) - 4, 0, 1000);
        }
    }

    public void searchLrc(String url) {
        lrcList = new LrcReader().getLrcByUrl(url);
        if (lrcList.size() <= 0) {
            Lrc lrc = new Lrc();
            lrc.setLrcContent("<未找到歌词文件>");
            lrcList.add(lrc);
        }
        adapter.setLrcList(lrcList);
        adapter.notifyDataSetChanged();
    }

    public void setLrcColor(int colorNum) {
        lrcColor = colorNum;
        /*如果还没生成adapter，则延时到createView中设置*/
        if (adapter != null) {
            adapter.setLrcColor(lrcColor);
        }
    }

    public void setLrcSize(int size) {
        lrcSize = size;
        /*如果还没生成adapter，则延时到createView中设置*/
        if (adapter != null) {
            adapter.setLrcSize(lrcSize);
        }
    }
}
