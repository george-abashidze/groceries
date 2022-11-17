package com.example.groceries.helper;

public interface ItemBindingInterface<ViewDataBinding,T> {
    void bindData(ViewDataBinding binding,T model, int position);
}
