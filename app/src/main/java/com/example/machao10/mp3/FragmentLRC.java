package com.example.machao10.mp3;

import android.app.Activity;
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
        return inflater.inflate(R.layout.fragment_music_lrc, container, false);
    }

    public void update(int position) {
        if (null == getView()) {
            Toast.makeText(activity, "歌词控件未设置", Toast.LENGTH_SHORT).show();
        } else {
            ((LrcView) ((FrameLayout) getView()).getChildAt(0)).update(position);
        }
    }


    public void searchLrc(String url) {
        if (null == getView()) {
            Toast.makeText(activity, "歌词控件未设置", Toast.LENGTH_SHORT).show();
        } else {
            ((LrcView) ((FrameLayout) getView()).getChildAt(0)).searchLrc(url);
        }
    }
}
