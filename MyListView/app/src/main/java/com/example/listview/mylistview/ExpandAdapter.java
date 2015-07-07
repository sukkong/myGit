package com.example.listview.mylistview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hui.zhang on 2015/4/7.
 */
public class ExpandAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private String[] mGroupStrings = null;
    private List<List<Item>> mData = null;
    private int mLcdWidth = 0;
    private float mDensity = 0;
    private final int itemWidth;

    public ExpandAdapter(Context context, List<List<Item>> mData) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupStrings = mContext.getResources().getStringArray(R.array.group);
        this.mData = mData;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        mLcdWidth = dm.widthPixels;
        mDensity = dm.density;
        //这里我每个列表项高度是59dp。
        itemWidth = (int) (59 * mDensity);
    }

    public void setData(List<List<Item>> list) {
        mData = list;
    }

    @Override
    public int getGroupCount() {
        return mData.size() == 0 ? 0 : mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(groupPosition).size() == 0 ? 0 : mData.get(groupPosition).size();
    }

    @Override
    public List<Item> getGroup(int groupPosition) {
        return mData.get(groupPosition) == null ? null : mData.get(groupPosition);
    }

    @Override
    public Item getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).get(childPosition) == null ? null : mData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item, null);
        }
        viewHolder = new ViewHolder();
        viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
        viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
        viewHolder.imgJiantou = (ImageView) convertView.findViewById(R.id.img_jiantou);
        viewHolder.tv.setText(mGroupStrings[groupPosition]);
        if (isExpanded) {
            viewHolder.imgJiantou.setImageResource(R.drawable.xiangshangyuanjiaobutton);
            viewHolder.img.setVisibility(View.GONE);
        } else {
            viewHolder.img.setVisibility(View.VISIBLE);
//            AnimatorSet setCloud = new AnimatorSet();
//            setCloud.playTogether(
//                    ObjectAnimator.ofFloat(viewHolder.img, "translationX", 0, -100)
//            );
//            setCloud.setDuration(1000).start();
            viewHolder.imgJiantou.setImageResource(R.drawable.xialayuanjianjiantou);
        }
//        if (groupPosition == MainActivity.mSelectItem) {
//            AnimatorSet setCloud = new AnimatorSet();
//            setCloud.playTogether(
//                    ObjectAnimator.ofFloat(viewHolder.img, "translationX", 0, -100)
//            );
//            setCloud.setDuration(1000).start();
//        } else {
////            viewHolder.img.setVisibility(View.VISIBLE);
//        }
        RelativeLayout footer = (RelativeLayout) convertView.findViewById(R.id.footer);
        //不明白为什么宽度被设成：屏宽减去10dp(mLcdWidth - 10 * mDensity)，不过不去深究这个，因为我们关心的是高度。
        int widthSpec = View.MeasureSpec.makeMeasureSpec((int) (mLcdWidth - 10 * mDensity), View.MeasureSpec.EXACTLY);
        //然后，调用measure()方法，宽度被设成上面的widthSpec，而高度传了个0，不过没有关系因为高度下面才会设置
        footer.measure(widthSpec, 0);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) footer.getLayoutParams();
        //在此设置高度为：该组(Group)的项目数 * 每一项的高度。
        //本来我参看的那篇博文用的是params.bottomMargin = -footer.getMeasuredHeight();
        //但我使用时取footer.getMeasuredHeight(); 总出问题，第一次取只有listView一项的高度，后面高度也不匹配
        //不知道是listView缓存机制带来的问题还是什么，这里如果知道没一个列表项的高度，照现在的写法也没有问题。
        params.height = (mData.get(groupPosition).size() * itemWidth);
//        if (mData.get(groupPosition).state == 0) {
//            params.bottomMargin = -params.height;
//            footer.setVisibility(View.GONE);
//        } else {
//            params.bottomMargin = 0;
//            footer.setVisibility(View.VISIBLE);
//        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_item, null);
        }
        viewHolder = new ViewHolder();
        viewHolder.tvText = (TextView) convertView.findViewById(R.id.tv_text);
        viewHolder.tvText.setText(getChild(groupPosition, childPosition).getName());
//        viewHolder.gridView = (GridView) convertView.findViewById(R.id.gridView);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        /**
         * 很重要：实现ChildView点击事件，必须返回true
         */
        return true;
    }

    static class ViewHolder {
        TextView tv, tvText;
        ImageView img, imgJiantou;
        GridView gridView;
    }
}
