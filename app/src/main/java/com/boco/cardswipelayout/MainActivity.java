package com.boco.cardswipelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        final ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.img_avatar_01);
        list.add(R.drawable.img_avatar_02);
        list.add(R.drawable.img_avatar_03);
        list.add(R.drawable.img_avatar_04);
        list.add(R.drawable.img_avatar_05);
        list.add(R.drawable.img_avatar_06);
        list.add(R.drawable.img_avatar_07);
        final BaseQuickAdapter adapter = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout
                .card_item, list) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                helper.setImageResource(R.id.iv_avatar, item);
            }
        };
        mRecyclerView.setAdapter(adapter);

        CardItemTouchHelperCallback callback = new CardItemTouchHelperCallback();
        callback.setListener(new OnSwipeListener() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder holder, float ratio, int direction) {

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder holder, int position, int direction) {
                list.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, direction == CardConfig.SWIPED_LEFT ? "swiped " +
                        "left" : "swiped right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(MainActivity.this, "滑动完毕", Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(callback);

        CardLayoutManager manager = new CardLayoutManager(mRecyclerView, helper);

        mRecyclerView.setLayoutManager(manager);

        helper.attachToRecyclerView(mRecyclerView);

    }
}
