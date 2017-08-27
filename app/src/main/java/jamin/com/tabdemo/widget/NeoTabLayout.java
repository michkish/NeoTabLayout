package jamin.com.tabdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.List;

import jamin.com.tabdemo.R;

/**
 * Created by Zheming.xin on 2017/8/25.
 */

public class NeoTabLayout extends HorizontalScrollView {

    protected LinearLayout layoutContent;
    protected Context context;

    protected int arrayMode;

    public final static int FIXED = 0x01;
    public final static int SCROLL = 0x02;

    private int indicatorSelectBackgroundRes = -1;

    public NeoTabLayout(Context context) {
        super(context);
    }

    public NeoTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NeoTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NeoTabLayout);
        int indexCount = array.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.NeoTabLayout_arrayMode:
                    arrayMode = array.getInteger(index, FIXED);
                    break;
                case R.styleable.NeoTabLayout_indicatorSelectBackground:
                    indicatorSelectBackgroundRes = array.getResourceId(index, android.R.color.holo_blue_light);
                    break;
                default:
                    break;
            }
        }
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.neo_tab_layout, this, true);
        layoutContent = (LinearLayout) findViewById(R.id.tablayout_content);
        layoutContent.setOrientation(LinearLayout.HORIZONTAL);
    }
}
