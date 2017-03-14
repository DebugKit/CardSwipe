package com.boco.cardswipelayout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CardLayoutManager extends RecyclerView.LayoutManager {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;

    public CardLayoutManager(@NonNull RecyclerView recyclerView, @NonNull ItemTouchHelper
            itemTouchHelper) {
        mRecyclerView = recyclerView;
        mItemTouchHelper = itemTouchHelper;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        //先移除所有view
        removeAllViews();
        //布局之前，将所有的子View先Detach掉，放入到Scrap中
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        //在这里，我们默认配置 CardConfig.DEFAULT_SHOW_ITEM = 3。即在屏幕上显示的卡片数为3
        //当数据源大于最大显示数时
        if (itemCount > CardConfig.DEFAULT_SHOW_ITEM) {
            for (int position = CardConfig.DEFAULT_SHOW_ITEM; position >= 0; position--) {
                View view = recycler.getViewForPosition(position);
                //将view添加到RecyclerView中
                addView(view);
                //测量子View
                measureChild(view, 0, 0);
                //getDecoratedMeasuredWidth(view)可以得到view的宽度
                //所以widthSpace就是除去itemView后剩余的值
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                //heightSpace 同上
                int heightSpace = getWidth() - getDecoratedMeasuredHeight(view);
                //将itemView放入RecyclerView中，默认将其放入RecyclerView中心
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));
                //第四张卡片
                //将第四张卡片与第三张重叠在一起，保持动画的连贯性
                if (position == CardConfig.DEFAULT_SHOW_ITEM) {
                    //按照第三张的缩放规则进行缩放，所以设置为1-(position-1)*CardConfig.DEFAULT_SCALE
                    view.setScaleX(1 - (position - 1) * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - (position - 1) * CardConfig.DEFAULT_SCALE);
                    //设置位移
                    view.setTranslationY((position-1) * view.getMeasuredHeight() / CardConfig
                            .DEFAULT_TRANSLATE_Y);
                } else if (position > 0) {
                    view.setScaleX(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY(position * view.getMeasuredHeight() / CardConfig
                            .DEFAULT_TRANSLATE_Y);
                } else {//position==0
                    //设置mOnTouchListener的意义就是让顶层的view可以随意滑动
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        } else {//当数据源小于或等于最大显示数时
            for (int position = itemCount - 1; position >= 0; position--) {
                View view = recycler.getViewForPosition(position);
                addView(view);
                measureChild(view, 0, 0);
                int withSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getWidth() - getDecoratedMeasuredHeight(view);
                layoutDecoratedWithMargins(view, withSpace / 2, heightSpace / 2,
                        withSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));
                if (position > 0) {
                    view.setScaleX(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY(position * view.getMeasuredHeight() / CardConfig
                            .DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        }
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
            //把触摸事件交给mItemTouchHelper，让其处理卡片滑动事件
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mItemTouchHelper.startSwipe(holder);
            }
            return false;
        }
    };
}
