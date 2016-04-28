package com.example.machao10.mp3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machao10.Lrc.Lrc;
import com.example.machao10.Lrc.LrcView;

public class FragmentLRC extends Fragment {
    Activity activity;

    LrcView lrcView;

    public FragmentLRC() {
        // Required empty public constructor
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
        lrcView = (LrcView) v.findViewById(R.id.lrc);
        return v;
    }

    public void update(int position) {
        if (null == getView()) {
            Toast.makeText(activity, "歌词控件未设置", Toast.LENGTH_SHORT).show();
        } else {
            lrcView.update(position);
        }
    }


    public void searchLrc(String url) {
        if (null == getView()) {
            Toast.makeText(activity, "歌词控件未设置", Toast.LENGTH_SHORT).show();
        } else {
            lrcView.searchLrc(url);
        }
    }

    public void setLrcColor(int colorNum) {
        switch (colorNum) {
            case LrcView.TEXT_RED:
                lrcView.setLrcColor(Color.argb(210, 235, 38, 70));
                break;
            case LrcView.TEXT_VIOLET:
                lrcView.setLrcColor(Color.argb(210, 136, 72, 152));
                break;
            default:
                lrcView.setLrcColor(Color.argb(210, 250, 250, 30));
        }
    }

    public void setLrcSize(int size) {
        lrcView.setLrcSize(size);
    }
}
