package jamin.com.tabdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public abstract class BaseRecyclePagerAdapter<T> extends RecyclerView.Adapter<BaseHolder> {
    protected ViewPager viewPager;
    protected int selectedItem;
    protected OnPageChangeListener onPageChangeListener;
    protected OnItemClick onItemClick;
    protected OnItemCountChangedListener onItemCountChangedListener;
    protected Context context;
    protected List<T> itemList;
    protected LayoutInflater inflater;

    public BaseRecyclePagerAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void setOnItemCountChangedListener(OnItemCountChangedListener onItemCountChangedListener) {
        this.onItemCountChangedListener = onItemCountChangedListener;
    }

    public final void setSeletedItem(int pos) {
        this.selectedItem = pos;
    }

    public final int getSelectedItem() {
        return this.selectedItem;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItemList(List<T> list){
        if (list == null) {
            return;
        }

        itemList.addAll(list);
        notifyDataSetChanged();
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(getItemCount());
        }
    }

    public void addItemList(List<T> list, int position) {
        if (list == null || itemList.size() < position + 1) {
            return;
        }

        itemList.addAll(position, list);
        notifyDataSetChanged();
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(getItemCount());
        }
    }

    public void addItem(T item) {
        if(item == null) {
            return;
        }

        itemList.add(item);
        notifyDataSetChanged();
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(getItemCount());
        }
    }

    public void addItem(T item, int position) {
        if(item == null || itemList.size() < position + 1) {
            return;
        }

        itemList.add(position, item);
        notifyDataSetChanged();
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(getItemCount());
        }
    }

    public void updateItem(T item, int position) {
        if(item == null || itemList.size() < position + 1) {
            return;
        }

        itemList.set(position, item);
        notifyItemChanged(position);
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(getItemCount());
        }
    }

    public boolean removeItem(T item) {
        if(item == null) {
            return false;
        }

        if (!itemList.contains(item)) {
            return false;
        }

        int position = itemList.indexOf(item);

        if (!itemList.remove(item)) {
            return false;
        }
        notifyItemRemoved(position);
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(getItemCount());
        }
        return true;
    }

    public boolean removeItem(int position) {
        if(itemList.size() < position + 1) {
            return false;
        }

        itemList.remove(position);
        notifyItemRemoved(position);
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(getItemCount());
        }
        return true;
    }

    public void bindViewPager(final ViewPager vp) {
        this.viewPager = vp;
        this.selectedItem = vp.getCurrentItem();
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if( position > itemList.size() - 1) {
                    throw new ArrayIndexOutOfBoundsException("viewpage position is larger than recycle title.");
                }
                selectedItem = position;
                notifyDataSetChanged();
                if(onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    public abstract int setLayout();

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(setLayout(), parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, int position) {
        if (holder != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.getLayoutPosition() < viewPager.getAdapter().getCount()) {
                        viewPager.setCurrentItem(holder.getLayoutPosition(), true);
                        selectedItem = holder.getLayoutPosition();
                        notifyDataSetChanged();
                        Toast.makeText(context, String.valueOf(holder.getLayoutPosition()), Toast.LENGTH_SHORT).show();
                    }
                    if (onItemClick != null) {
                        onItemClick.onClick(holder.getLayoutPosition());
                    }
                }
            });
            onBindYourViewHolder(holder, position);
        }
    }

    public abstract void onBindYourViewHolder(final BaseHolder holder, int position);

    public interface OnItemClick {
        void onClick(int pos);
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

    public interface OnItemCountChangedListener {
        void onItemCountChanged(int count);
    }
}
