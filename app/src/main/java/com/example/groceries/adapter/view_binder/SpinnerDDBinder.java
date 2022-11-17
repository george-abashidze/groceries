package com.example.groceries.adapter.view_binder;

import com.example.groceries.data.local.entity.Category;
import com.example.groceries.databinding.SpinnerDropDownItemBinding;
import com.example.groceries.helper.ItemBindingInterface;

public class SpinnerDDBinder implements ItemBindingInterface<SpinnerDropDownItemBinding, Category> {
    @Override
    public void bindData(SpinnerDropDownItemBinding binding, Category model, int position) {
        binding.setValue(model.getName());
    }
}
