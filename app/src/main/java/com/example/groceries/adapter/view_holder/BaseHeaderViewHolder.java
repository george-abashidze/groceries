package com.example.groceries.adapter.view_holder;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceries.helper.Filter;
import com.example.groceries.helper.HeaderBindingInterface;


public class BaseHeaderViewHolder<VM extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private final VM binding;
    private final HeaderBindingInterface<VM> bindingInterface;

    public BaseHeaderViewHolder(View view, HeaderBindingInterface<VM> bindingInterface) {
        super(view);
        binding = DataBindingUtil.bind(view);
        this.bindingInterface = bindingInterface;
    }

    public void bind(Filter filter) {
        bindingInterface.bind(binding,filter);
    }
}
