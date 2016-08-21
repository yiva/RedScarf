package com.redscarf.weidou.customwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by yeahwang on 2016/7/16.
 */
public class ScrollGridView extends GridView{
    public ScrollGridView(Context context) {
        super(context);
    }

    public ScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//    /**
//     * 对于动态item不能正确计算高度，先自己算一下，之后再改
//     * @param gridView
//     * @param height
//     */
//    public static void getTotalHeightofGridView(GridView gridView, int height) {
//
//        ListAdapter mAdapter = gridView.getAdapter();
//        if (mAdapter == null) {
//            return;
//        }
//        int totalHeight = 0;
//        for (int i = 0; i < mAdapter.getCount(); i++) {
//            View mView = mAdapter.getView(i, null, gridView);
//            mView.measure(
//                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            totalHeight += height;
//            Log.w("HEIGHT" + i, String.valueOf(totalHeight));
//        }
//        ViewGroup.LayoutParams params = gridView.getLayoutParams();
//        params.height = totalHeight + (gridView.getDividerHeight() * (mAdapter.getCount() - 1));
//        gridView.setLayoutParams(params);
//        gridView.requestLayout();
//    }
}
