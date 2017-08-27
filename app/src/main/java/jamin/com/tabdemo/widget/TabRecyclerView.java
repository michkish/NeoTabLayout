package jamin.com.tabdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import jamin.com.tabdemo.BaseRecyclePagerAdapter;
import jamin.com.tabdemo.R;

/**
 * Created by Zheming.xin on 2017/8/25.
 */

public class TabRecyclerView extends RecyclerView {

    protected Context context;
    protected int arrayMode = FIXED;

    protected BaseRecyclePagerAdapter.OnItemCountChangedListener onItemCountChangedListener;

    protected LinearLayoutManager linearLayoutManager;
    protected GridLayoutManager gridLayoutManager;

    //当item数量超过这个值得时候模式切换为SCROLL
    protected int thresholdValue = Integer.MAX_VALUE;

    public final static int FIXED = 0x01;
    public final static int SCROLL = 0x02;

    public TabRecyclerView(Context context) {
        super(context);
    }

    public TabRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabRecyclerView);
        int indexCount = array.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.TabRecyclerView_arrayMode:
                    setArrayMode(array.getInteger(index, FIXED));
                    break;
                case R.styleable.TabRecyclerView_thresholdValue:
                    setThresholdValue(array.getInteger(index, Integer.MAX_VALUE));
                    break;
                default:
                    break;
            }
        }
        setOverScrollMode(OVER_SCROLL_NEVER);
        onItemCountChangedListener = new BaseRecyclePagerAdapter.OnItemCountChangedListener() {
            @Override
            public void onItemCountChanged(int count) {
                setCorrectArrayMode(count);
            }
        };
    }

    @Override
    @VisibleForTesting
    public void setOverScrollMode(int overScrollMode) {
        super.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public int getArrayMode() {
        return arrayMode;
    }

    public void setArrayMode(@IntRange(from = FIXED, to = SCROLL) int arrayMode) {
        if (getAdapter() != null && thresholdValue < Integer.MAX_VALUE) {
            if (getAdapter().getItemCount() > thresholdValue) {
                this.arrayMode = SCROLL;
                if (linearLayoutManager == null) {
                    linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                }
                setLayoutManager(linearLayoutManager);
            } else {
                this.arrayMode = FIXED;
                int itemCount = getAdapter() == null ? 1 : getAdapter().getItemCount();
                if (gridLayoutManager == null) {
                    gridLayoutManager = new GridLayoutManager(context, itemCount, GridLayoutManager.VERTICAL, false);
                } else {
                    gridLayoutManager.setSpanCount(itemCount);
                }
                setLayoutManager(gridLayoutManager);
            }
        } else {
            this.arrayMode = arrayMode;
            if (arrayMode == SCROLL) {
                if (linearLayoutManager == null) {
                    linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                }
                setLayoutManager(linearLayoutManager);
            } else {
                int itemCount = getAdapter() == null ? 1 : getAdapter().getItemCount();
                if (gridLayoutManager == null) {
                    gridLayoutManager = new GridLayoutManager(context, itemCount, GridLayoutManager.VERTICAL, false);
                } else {
                    gridLayoutManager.setSpanCount(itemCount);
                }
                setLayoutManager(gridLayoutManager);
            }
        }
        if (getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
        requestLayout();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (arrayMode == SCROLL) {
            if (!(layout instanceof LinearLayoutManager)) {
                throw new ClassCastException("the LayoutManager of TabRecyclerView must set LinearLayoutManager in SCROLL mode");
            }
        } else {
            if (!(layout instanceof GridLayoutManager)) {
                throw new ClassCastException("the LayoutManager of TabRecyclerView must set GridLayoutManager in SCROLL mode");
            }
        }
        super.setLayoutManager(layout);
        if (arrayMode == SCROLL) {
            int first = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            int last = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (last - first + 1 < linearLayoutManager.getChildCount()) {
            } else {

            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof BaseRecyclePagerAdapter)) {
            throw new ClassCastException("the Adapter of TabRecyclerView must extend from BaseRecyclePagerAdapter");
        }
        super.setAdapter(adapter);
        ((BaseRecyclePagerAdapter) adapter).setOnItemCountChangedListener(onItemCountChangedListener);
        if (adapter != null) {
            setCorrectArrayMode(adapter.getItemCount());
        }
    }

    @Override
    public BaseRecyclePagerAdapter getAdapter() {
        return (BaseRecyclePagerAdapter) super.getAdapter();
    }

    public void setThresholdValue(int thresholdValue) {
        this.thresholdValue = thresholdValue;
        if (getAdapter() != null) {
            if(getAdapter().getItemCount() > thresholdValue && arrayMode == FIXED) {
                setArrayMode(SCROLL);
            } else if (getAdapter().getItemCount() <= thresholdValue && arrayMode == SCROLL) {
                setArrayMode(FIXED);
            }
        }
    }

    private void setCorrectArrayMode(int itemCount) {
        if (itemCount > thresholdValue && arrayMode == FIXED) {
            setArrayMode(SCROLL);
        } else if (itemCount <= thresholdValue && arrayMode == SCROLL) {
            setArrayMode(FIXED);
        } else {
            if (arrayMode == FIXED && getLayoutManager() != null) {
                gridLayoutManager.setSpanCount(itemCount > 0 ? itemCount : 1);
            }
        }
    }
}
