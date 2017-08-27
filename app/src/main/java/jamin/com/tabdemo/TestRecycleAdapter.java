package jamin.com.tabdemo;

import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class TestRecycleAdapter extends BaseRecyclePagerAdapter<String> {

    public TestRecycleAdapter(Context context) {
        super(context);
    }

    @Override
    public int setLayout() {
        return R.layout.test_item;
    }

    @Override
    public void onBindYourViewHolder(BaseHolder holder, int position) {
        TextView tv = (TextView) holder.findViewById(R.id.textview_item);
        tv.setText(itemList.get(position));
        if (holder.getLayoutPosition() == selectedItem) {
            tv.setTextColor(Color.BLUE);
        } else {
            tv.setTextColor(Color.BLACK);
        }
    }
}
