package com.example.billinavi;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpaceHeight;
    private final int dividerHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight, int dividerHeight){
        this.verticalSpaceHeight = verticalSpaceHeight;//공백
        this.dividerHeight = dividerHeight;//구분선
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() -1 ){// 마지막 아이템이 아닌 경우, 공백 추가
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=verticalSpaceHeight;
                outRect.bottom=dividerHeight;
            }
            if(parent.getChildAdapterPosition(view)==2){
                outRect.top=verticalSpaceHeight;
                outRect.bottom=verticalSpaceHeight;
            }
            if (parent.getChildAdapterPosition(view)==4){
                outRect.bottom=verticalSpaceHeight;
                outRect.top=dividerHeight;
            }
        }
    }
}
