package com.example.machao10.Lrc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.machao10.mp3.R;

import java.util.List;

/**
 * Created by machao10 on 2016/4/28.
 */
public class LrcAdapter extends BaseAdapter {

    public static final int TEXT_BIG = 0;
    public static final int TEXT_NORMAL = 1;
    public static final int TEXT_SMALL = 2;

    public static final int TEXT_RED = 10;
    public static final int TEXT_YELLOW = 20;
    public static final int TEXT_VIOLET = 30;

    public static final String TEXT_SIZE_DEFAULT = TEXT_NORMAL + "";
    public static final String TEXT_COLOR_DEFAULT = TEXT_RED + "";


    private int text_size_current;
    private int text_size_other;
    int textColor;

    int index;
    private List<Lrc> lrcList;
    private LayoutInflater inflate;


    public LrcAdapter(Context context, List<Lrc> lrcList) {
        inflate = LayoutInflater.from(context);
        this.lrcList = lrcList;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String currentTextSize = sp.getString(context.getString(R.string.key_lrc_size), TEXT_SIZE_DEFAULT);
        if(currentTextSize.length() > 1)
        {
            currentTextSize = currentTextSize.substring(1);
        }

        setLrcSize(Integer.parseInt(currentTextSize));

        String textColor = sp.getString(context.getString(R.string.key_lrc_color), TEXT_COLOR_DEFAULT).substring(1);
        setLrcColor(Integer.parseInt(textColor));
    }

    public void setLrcSize(int size) {
        switch (size) {
            case TEXT_BIG:
                text_size_current = 35;
                text_size_other = 25;
                break;

            case TEXT_SMALL:
                text_size_current = 25;
                text_size_other = 15;
                break;
            default:
                text_size_current = 30;
                text_size_other = 20;
        }
    }

    public void setLrcColor(int colorNum) {
        switch (colorNum) {
            case LrcView.TEXT_RED:
                textColor = Color.argb(210, 235, 38, 70);
                break;
            case LrcView.TEXT_VIOLET:
                textColor = Color.argb(210, 136, 72, 152);
                break;
            default:
                textColor = Color.argb(210, 250, 250, 30);
        }
    }

    public int update(int position) {
        for (int i = 0; i < lrcList.size() - 1; i++) {
            if (position >= lrcList.get(i).getLrcTime() && position < lrcList.get(i + 1).getLrcTime() || position < lrcList.get(0).getLrcTime()) {
                index = i;
                break;
            } else if (position > lrcList.get(lrcList.size() - 1).getLrcTime()) {
                index = lrcList.size() - 1;
            }
        }
        return index;
    }

    public void setLrcList(List<Lrc> lrcList) {
        this.lrcList = lrcList;
        lrcList.add(new Lrc());
        lrcList.add(new Lrc());
        lrcList.add(new Lrc());
        lrcList.add(new Lrc());
        lrcList.add(new Lrc());
        lrcList.add(new Lrc());

        lrcList.add(0, new Lrc());
        lrcList.add(0, new Lrc());
        lrcList.add(0, new Lrc());
        lrcList.add(0, new Lrc());
        lrcList.add(0, new Lrc());
        lrcList.add(0, new Lrc());
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
//        return 0;
        if (position == index) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        return lrcList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holderCurrent = null, holderOther = null;
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case 0:
                    holderCurrent = new ViewHolder();
                    convertView = inflate.inflate(R.layout.lv_lrc, null);
                    holderCurrent.lrc = (TextView) convertView.findViewById(R.id.tv_lrc_current);

                    convertView.setTag(holderCurrent);
                    break;
                case 1:
                    holderOther = new ViewHolder();
                    convertView = inflate.inflate(R.layout.lv_lrc, null);
                    holderOther.lrc = (TextView) convertView.findViewById(R.id.tv_lrc_ohter);

                    convertView.setTag(holderOther);
            }
        } else {
            if (getItemViewType(position) == 0) {
                holderCurrent = (ViewHolder) convertView.getTag();
            } else {
                holderOther = (ViewHolder) convertView.getTag();
            }
        }
        if (getItemViewType(position) == 0) {
            holderCurrent.lrc.setTextSize(text_size_current);
            holderCurrent.lrc.setTextColor(textColor);
            holderCurrent.lrc.setText(lrcList.get(position).getLrcContent());
        } else {
            holderOther.lrc.setTextSize(text_size_other);
            holderOther.lrc.setText(lrcList.get(position).getLrcContent());
        }

        return convertView;
    }

    class ViewHolder {
        public TextView lrc;
    }
}
