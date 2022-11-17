package com.example.groceries.adapter.view_binder;

import com.example.groceries.data.local.entity.Category;
import com.example.groceries.databinding.SpinnerItemBinding;
import com.example.groceries.helper.ItemBindingInterface;

public class SpinnerItemBinder implements ItemBindingInterface<SpinnerItemBinding, Category> {
    @Override
    public void bindData(SpinnerItemBinding binding, Category model, int position) {
        binding.setValue(model.getName());
    }
}
