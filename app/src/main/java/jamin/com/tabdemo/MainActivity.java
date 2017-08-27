package jamin.com.tabdemo;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jamin.com.tabdemo.widget.TabRecyclerView;

public class MainActivity extends AppCompatActivity {
    TabRecyclerView mrv;
    ViewPager vp;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(String.valueOf(i));
        }

        mrv = (TabRecyclerView) this.findViewById(R.id.mRecycleview);
        mrv.setArrayMode(TabRecyclerView.FIXED);

        vp = (ViewPager) this.findViewById(R.id.mViewpager);
        List<View> listview = new ArrayList<>();
        for (int i=0;i< list.size();i++) {
            View v = getLayoutInflater().inflate(R.layout.pager_item, null);
            TextView tv = (TextView) v.findViewById(R.id.pager_textview);
            tv.setText(list.get(i));
            listview.add(v);
        }

        TestPagerAdapter tpadapter = new TestPagerAdapter(listview);

        vp.setAdapter(tpadapter);

        TestRecycleAdapter adapter = new TestRecycleAdapter(this);
        adapter.addItemList(list);
        adapter.bindViewPager(vp);
        adapter.setOnPageChangeListener(new BaseRecyclePagerAdapter.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mrv.smoothScrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mrv.setAdapter(adapter);
        mrv.setItemAnimator(new DefaultItemAnimator());

        findViewById(R.id.btn_changetab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mrv.getArrayMode() == TabRecyclerView.FIXED) {
                    mrv.setArrayMode(TabRecyclerView.SCROLL);
                } else {
                    mrv.setArrayMode(TabRecyclerView.FIXED);
                }
            }
        });
    }
}