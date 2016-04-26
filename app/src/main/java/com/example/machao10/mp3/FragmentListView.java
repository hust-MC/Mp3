package com.example.machao10.mp3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentListView extends Fragment {
    Activity activity;
    List<MusicInfo> musicInfos;
    MusicItemClickeListener l;


    public FragmentListView() {
        // Required empty public constructor
    }

    public void setOnMusicItemClickListener(MusicItemClickeListener l) {
        this.l = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        musicInfos = MusicListUtils.getMusicInfos(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MC", "fragment pause");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lv_music_list);

        listView.setAdapter(new ListViewAdapter(activity));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != l) {
                    l.onMusicItemClicked(position);
                }
            }
        });
        return view;
    }

    /**
     * Created by machao10 on 2016/4/25.
     */
    public class ListViewAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;

        public ListViewAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return musicInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            public TextView title;
            public TextView artist;
            public TextView time;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.music_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.item_name);
                holder.artist = (TextView) convertView.findViewById(R.id.item_artist);
                holder.time = (TextView) convertView.findViewById(R.id.item_duration);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MusicInfo info = musicInfos.get(position);
            holder.title.setText(info.title);
            holder.artist.setText(info.artist);
            holder.time.setText(info.duration);

            return convertView;
        }
    }
}

interface MusicItemClickeListener {
    public void onMusicItemClicked(int position);
}
