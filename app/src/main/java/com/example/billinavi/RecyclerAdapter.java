package com.example.billinavi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;
    private int a;

    public RecyclerAdapter(int count) {
        a = count;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem, parent, false);
        context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView1;
        private TextView textView;
        private ImageView imageView2;
        public LinearLayout linearLayout;
        public LinearLayout linearLayout1;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.image1);
            imageView2 = itemView.findViewById(R.id.image2);
            textView = itemView.findViewById(R.id.tex1);
            linearLayout = itemView.findViewById(R.id.linear);
        }

        void onBind(Data data) {
            imageView1.setImageResource(data.getResId());
            textView.setText(data.getTitle());
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (a == 1) {
                switch (view.getId()) {
                    case R.id.linear:
                        if (getAdapterPosition() == 0) {
                            context.startActivity(new Intent(context, SettingSub2.class));
                            return;
                        }
                        if (getAdapterPosition() == 1) {
                            context.startActivity(new Intent(context, SettingSub3.class));
                            return;
                        }
                        if (getAdapterPosition() == 2) {
                            context.startActivity(new Intent(context, SettingSub4.class));
                            return;
                        }
                        if (getAdapterPosition() == 3) {
                            context.startActivity(new Intent(context, SettingSub5.class));
                            return;
                        }
                        if (getAdapterPosition() == 4) {
                            context.startActivity(new Intent(context, SettingSub6.class));
                            return;
                        }
                        if (getAdapterPosition() == 5) {
                            context.startActivity(new Intent(context, SignSub7.class));
                            return;
                        } else
                            context.startActivity(new Intent(context, MainActivity.class));
                        return;
                }
            }
            //이용안내
            else if (a == 2) {
                switch (view.getId()) {
                    case R.id.linear:
                        if (getAdapterPosition() == 0) {
                            context.startActivity(new Intent(context, SettingSub6_1.class));
                            return;
                        }
                        if (getAdapterPosition() == 1) {
                            context.startActivity(new Intent(context, SettingSub6_2.class));
                            return;
                        } else
                            context.startActivity(new Intent(context, MainActivity.class));
                        return;
                }
            }

        }
    }
}