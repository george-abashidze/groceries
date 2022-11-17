package com.example.groceries.adapter.view_binder;

import android.view.View;
import android.widget.AdapterView;

import com.example.groceries.R;
import com.example.groceries.adapter.BaseSpinnerAdapter;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.databinding.ProductListHeaderBinding;
import com.example.groceries.databinding.SpinnerDropDownItemBinding;
import com.example.groceries.databinding.SpinnerItemBinding;
import com.example.groceries.helper.Filter;
import com.example.groceries.helper.HeaderBindingInterface;
import com.example.groceries.helper.enums.SortEnum;

import java.util.List;

public class UserListHeaderBinder implements HeaderBindingInterface<ProductListHeaderBinding> {

    public interface HeaderEvents {
        void onCategoryItemSelected(long categoryId,int index);
        void onSortSelected(SortEnum sort);
    }

    private final HeaderEvents events;
    private BaseSpinnerAdapter<Category, SpinnerItemBinding, SpinnerDropDownItemBinding> categoryAdapter;

    public UserListHeaderBinder(HeaderEvents events){

        this.events = events;
        this.categoryAdapter = new BaseSpinnerAdapter<>(R.layout.spinner_item, R.layout.spinner_drop_down_item, new SpinnerItemBinder(), new SpinnerDDBinder());
    }

    @Override
    public void bind(ProductListHeaderBinding binding, Filter filter) {

        binding.itemName.setOnClickListener(view -> {
            if(events != null){
                events.onSortSelected(SortEnum.BY_NAME);
            }
        });

        binding.itemPrice.setOnClickListener(view -> {
            if(events != null){
                events.onSortSelected(SortEnum.BY_PRICE);
            }
        });

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(events != null){
                    Category item = categoryAdapter.getItem(i);
                    events.onCategoryItemSelected(item.getId(),i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.categorySpinner.setAdapter(categoryAdapter);
        binding.categorySpinner.setSelection(filter.categoryIndex,false);
    }

    public void setCategories(List<Category> items){
        categoryAdapter.setItems(items);
    }
}
