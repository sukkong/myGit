package com.example.listview.mylistview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private ExpandableListView mListView = null;
    private ExpandAdapter mAdapter = null;
    private List<List<Item>> mData = new ArrayList<List<Item>>();
    public  int mSelectItem = 0;

    private int[] mGroupArrays = new int[]{
            R.array.title1,
            R.array.title2,
            R.array.title3};

    private int[] mDetailIds = new int[]{
            R.array.detail1,
            R.array.detail2,
            R.array.detail3};

    private int[][] mImageIds = new int[][]{
            {R.drawable.img_00,
                    R.drawable.img_01,
                    R.drawable.img_02},
            {R.drawable.img_10,
                    R.drawable.img_11,
                    R.drawable.img_12
            },
            {R.drawable.img_20,
                    R.drawable.img_21,
                    R.drawable.img_22}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        initData();
        mListView = new ExpandableListView(this);
        mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        setContentView(mListView);

        mListView.setGroupIndicator(null);
        mAdapter = new ExpandAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectItem = position;
                View footer = view.findViewById(R.id.footer);
                footer.startAnimation(new ViewExpandAnimation(footer));
            }
        });
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    //第二个参数false表示展开时是否触发默认滚动动画
                    parent.expandGroup(groupPosition, false);
                }
                //telling the listView we have handled the group click, and don't want the default actions.
                return true;
            }
        });
//        mListView.setOnChildClickListener(this);
    }

    private void initData() {
        for (int i = 0; i < mGroupArrays.length; i++) {
            List<Item> list = new ArrayList<Item>();
            String[] childs = getStringArray(mGroupArrays[i]);
            String[] details = getStringArray(mDetailIds[i]);
            for (int j = 0; j < childs.length; j++) {
                Item item = new Item(mImageIds[i][j], childs[j], details[j]);
                list.add(item);
            }
            mData.add(list);
        }
    }

    private String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }
}
