package com.example.groceries.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceries.adapter.view_holder.BaseHeaderViewHolder;
import com.example.groceries.adapter.view_holder.BaseItemViewHolder;
import com.example.groceries.helper.Filter;
import com.example.groceries.helper.HeaderBindingInterface;
import com.example.groceries.helper.ItemBindingInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseAdapter<T, VB extends ViewDataBinding,HB extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final int layoutId;
    private final int headerLayoutId;
    private List<T> items = new ArrayList<>();
    private final ItemBindingInterface<VB,T> bindingInterface;
    private final HeaderBindingInterface<HB> headerBindingInterface;
    private final boolean showHeader;
    private Filter filter;
    private HashMap<Long,T> changedItems = new HashMap<>();

    public BaseAdapter(int layoutId, int headerLayoutId, ItemBindingInterface<VB, T> bindingInterface, HeaderBindingInterface<HB> headerBindingInterface,Filter filter, boolean showHeader){
        this.layoutId = layoutId;
        this.bindingInterface = bindingInterface;
        this.headerLayoutId = headerLayoutId;
        this.headerBindingInterface = headerBindingInterface;
        this.showHeader = showHeader;
        this.filter = filter;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<T> getItems(){
        return items;
    }

    public T getItemAt(int index){

        return items.get(index);
    }

    public HashMap<Long,T> getChangedItems(){
        return changedItems;
    }

    public void setItemAsChanged(long id,int index){
        changedItems.put(id,items.get(index));
    }

    public void clearChangedItems(){
        changedItems.clear();
    }

    public void removeItemAt(int index){
        items.remove(index);
        notifyItemRemoved(index);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(showHeader){
            if (viewType == TYPE_ITEM) {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(layoutId, parent, false);

                return new BaseItemViewHolder<T, VB>(v,bindingInterface);

            } else if (viewType == TYPE_HEADER) {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(headerLayoutId, parent, false);
                return new BaseHeaderViewHolder<HB>(v,headerBindingInterface);
            }

            return null;
        }
        else{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(layoutId, parent, false);

            return new BaseItemViewHolder<T, VB>(v,bindingInterface);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseItemViewHolder) {
            BaseItemViewHolder<T, VB> vhItem = (BaseItemViewHolder) holder;
            vhItem.bindData(items.get(position),position);
        }
        else if(holder instanceof BaseHeaderViewHolder){
            BaseHeaderViewHolder<HB> vhItem = (BaseHeaderViewHolder) holder;
            vhItem.bind(filter);
        }
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public int getItemCount() {
        if(items == null)
            return 0;
        else
            return items.size();
    }
}
