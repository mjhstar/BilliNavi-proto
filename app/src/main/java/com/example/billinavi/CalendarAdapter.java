package com.example.billinavi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private List<DayInfo> list;
    private LayoutInflater inflater;
    private ArrayList<Integer> start = new ArrayList<>();
    private ArrayList<Integer> finish = new ArrayList<>();
    public static int[] color;

    public CalendarAdapter(Context context, List<DayInfo> list) {
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        Context mContext = parent.getContext();
        color = parent.getResources().getIntArray(R.array.colorArray);
        int today = 0;
        DayInfo day = list.get(position);

        if (view == null) {
            view = inflater.inflate(R.layout.lounge_sub5_calendar_item, parent, false);
            holder = new ViewHolder();
            holder.dayText = view.findViewById(R.id.dayText);
            holder.dayBackground = view.findViewById(R.id.dayBackground);
            holder.dayBack = view.findViewById(R.id.dayBack);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (day != null) {
            holder.dayText.setText(day.getDay());
            if (day.isInMonth()) {
                if (position % 7 == 0) {
                    holder.dayText.setTextColor(Color.RED);
                } else if (position % 7 == 6) {
                    holder.dayText.setTextColor(Color.BLUE);
                } else {
                    holder.dayText.setTextColor(Color.BLACK);
                }
            }
            holder.dayBack.setBackgroundColor(Color.WHITE);
            //데이터 받아와서 정수로 변환
            try {
                for (int i = 0; i < LoungeSub5.startDay.size(); i++) {
                    if (Integer.parseInt(LoungeSub5.startYear.get(i)) == Integer.parseInt(LoungeSub5.finishYear.get(i))) {
                        if (Integer.parseInt(LoungeSub5.startMonth.get(i)) < LoungeSub5.month) {
                            start.add(1);
                            finish.add(Integer.parseInt(LoungeSub5.finishDay.get(i)));
                        } else if (Integer.parseInt(LoungeSub5.startMonth.get(i)) == Integer.parseInt(LoungeSub5.finishMonth.get(i))) {
                            start.add(Integer.parseInt(LoungeSub5.startDay.get(i)));
                            finish.add(Integer.parseInt(LoungeSub5.finishDay.get(i)));
                        } else if (Integer.parseInt(LoungeSub5.startMonth.get(i)) < Integer.parseInt(LoungeSub5.finishMonth.get(i))) {
                            start.add(Integer.parseInt(LoungeSub5.startDay.get(i)));
                            finish.add(LoungeSub5.thisCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        }
                    } else {
                        if (Integer.parseInt(LoungeSub5.startMonth.get(i)) > LoungeSub5.month) {
                            start.add(1);
                            finish.add(Integer.parseInt(LoungeSub5.finishDay.get(i)));
                        } else if (Integer.parseInt(LoungeSub5.startMonth.get(i)) > Integer.parseInt(LoungeSub5.finishMonth.get(i))) {
                            start.add(Integer.parseInt(LoungeSub5.startDay.get(i)));
                            finish.add(LoungeSub5.thisCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        }
                    }
                }
                today = Integer.parseInt(day.getDay());
            } catch (NumberFormatException e) {
            }
            int colorCount=0;
            //날짜 비교후 배경색 색칠
            for (int i = 0; i < start.size(); i++) {
                if ((start.get(i) <= today) && (today <= finish.get(i))) {
                    holder.dayBackground.setBackgroundColor(color[i]);
                    //holder.dayText.setTextColor(Color.WHITE);
                    break;
                }
            }
        }
        return view;
    }

    private class ViewHolder {
        TextView dayText;
        LinearLayout dayBackground, dayBack;
    }
}