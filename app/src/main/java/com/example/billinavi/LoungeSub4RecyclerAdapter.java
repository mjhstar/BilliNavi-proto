package com.example.billinavi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LoungeSub4RecyclerAdapter extends RecyclerView.Adapter<LoungeSub4RecyclerAdapter.ItemViewHolder> {
    private ArrayList<LoungeSub4Data> listData = new ArrayList<>();
    private Context mContext;
    private LinearLayout item;

    @NonNull
    @Override
    public LoungeSub4RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lounge_sub4_item, parent, false);
        mContext = parent.getContext();
        return new LoungeSub4RecyclerAdapter.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LoungeSub4RecyclerAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(LoungeSub4Data data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, user, date;

        ItemViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.lounge_sub4_title);
            user = v.findViewById(R.id.lounge_sub4_user);
            date = v.findViewById(R.id.lounge_sub4_date);
            item = v.findViewById(R.id.lounge_sub4_item);
        }

        void onBind(LoungeSub4Data data) {
            title.setText(data.getTitle());
            user.setText(data.getUser());
            date.setText(data.getDate());
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position;
            if(MainActivity.userIndex<0){
                Toast.makeText(view.getContext(),"로그인을 해주세요",Toast.LENGTH_SHORT).show();
            }else {
                switch (view.getId()) {
                    case R.id.lounge_sub4_item:
                        position = getAdapterPosition();
                        Intent intent = new Intent(mContext, LoungeSub4Read.class);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.putExtra("title", LoungeSub4.title.get(position));
                        intent.putExtra("writer", LoungeSub4.writer.get(position));
                        intent.putExtra("context", LoungeSub4.context.get(position));
                        intent.putExtra("day", LoungeSub4.day.get(position));
                        intent.putExtra("time", LoungeSub4.time.get(position));
                        intent.putExtra("index", LoungeSub4.index.get(position));
                        intent.putExtra("userIndex",LoungeSub4.userIndex.get(position));
                        Log.i("asdf", "타이틀 : " + LoungeSub4.title.get(position));
                        Log.i("asdf", "index : " + LoungeSub4.index.get(position));
                        mContext.startActivity(intent);
                        return;
                }
            }
        }
    }
}