package com.example.groceries.adapter.view_holder;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceries.helper.ItemBindingInterface;

public class BaseItemViewHolder<T,VM extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private final VM binding;
    private final ItemBindingInterface<VM,T> bindingInterface;

    public BaseItemViewHolder(View view, ItemBindingInterface<VM, T> bindingInterface) {
        super(view);
        binding = DataBindingUtil.bind(view);
        this.bindingInterface = bindingInterface;
    }

    public void bindData(T model,int position) {
        bindingInterface.bindData(binding, model,position);
    }
}
