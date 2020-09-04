package com.example.billinavi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LoungeSub5RecyclerAdapter extends RecyclerView.Adapter<LoungeSub5RecyclerAdapter.ItemViewHolder> {
    private ArrayList<LoungeSub5Data> listData = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public LoungeSub5RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lounge_sub5_item, parent, false);
        mContext = parent.getContext();
        return new LoungeSub5RecyclerAdapter.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LoungeSub5RecyclerAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(LoungeSub5Data data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, start, finish, text;

        ItemViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.lounge_sub5_contest_title);
            start = v.findViewById(R.id.lounge_sub5_startDay);
            finish = v.findViewById(R.id.lounge_sub5_finishDay);
            text = v.findViewById(R.id.lounge_sub5_text);
        }

        void onBind(LoungeSub5Data data) {
            title.setText(data.getTitle());
            start.setText(data.getStartDate());
            finish.setText(data.getFinishDate());
            start.setTextColor(CalendarAdapter.color[data.getIndex()]);
            finish.setTextColor(CalendarAdapter.color[data.getIndex()]);
            text.setTextColor(CalendarAdapter.color[data.getIndex()]);
        }

        @Override
        public void onClick(View view) {

        }
    }
}