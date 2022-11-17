package com.example.groceries.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.groceries.helper.ItemBindingInterface;

import java.util.ArrayList;
import java.util.List;

public class BaseSpinnerAdapter<T,VB extends ViewDataBinding,DB extends ViewDataBinding> extends BaseAdapter {
    private List<T> items = new ArrayList<>();
    private final int layoutId;
    private final int dropdownLayoutId;
    private final ItemBindingInterface<VB,T> bindingInterface;
    private final ItemBindingInterface<DB,T> ddBindingInterface;

    public BaseSpinnerAdapter(int layoutId, int dropdownLayoutId, ItemBindingInterface<VB, T> bindingInterface, ItemBindingInterface<DB, T> ddBindingInterface) {
        this.layoutId = layoutId;
        this.dropdownLayoutId = dropdownLayoutId;
        this.bindingInterface = bindingInterface;
        this.ddBindingInterface = ddBindingInterface;
    }

    public void setItems(List<T> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public List<T> getItems(){
        return items;
    }

    @Override
    public int getCount() {
        if(items == null)
            return 0;
        return items.size();
    }

    @Override
    public T getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutId, viewGroup, false);

        VB binding = DataBindingUtil.bind(v);

        bindingInterface.bindData(binding,items.get(i),i);

        return binding.getRoot();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View v = LayoutInflater.from(parent.getContext())
                .inflate(dropdownLayoutId, parent, false);

        DB binding = DataBindingUtil.bind(v);

        ddBindingInterface.bindData(binding,items.get(position),position);

        return binding.getRoot();
    }
}
