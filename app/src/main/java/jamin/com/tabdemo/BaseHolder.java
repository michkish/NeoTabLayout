package jamin.com.tabdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/7/18.
 */

public class BaseHolder extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public View findViewById(int resId) {
        return itemView.findViewById(resId);
    }
}
