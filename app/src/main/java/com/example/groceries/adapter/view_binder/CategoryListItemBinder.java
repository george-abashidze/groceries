package com.example.groceries.adapter.view_binder;

import android.view.View;

import com.example.groceries.data.local.entity.Category;
import com.example.groceries.databinding.CategoryListItemBinding;
import com.example.groceries.helper.ItemBindingInterface;

public class CategoryListItemBinder implements ItemBindingInterface<CategoryListItemBinding, Category> {



    public interface CategoryEvents{
        void onEditClick(Category item);
        void onDeleteClick(Category item);
    }

    private final CategoryEvents events;

    public CategoryListItemBinder(CategoryEvents events) {
        this.events = events;
    }

    @Override
    public void bindData(CategoryListItemBinding binding, Category model, int position) {
        binding.setCategory(model);

        binding.btnEdit.setOnClickListener(view -> {
            if(events != null){
                events.onEditClick(model);
            }
        });

        binding.btnDelete.setOnClickListener(view -> {
            if(events != null){
                events.onDeleteClick(model);
            }
        });
    }
}
