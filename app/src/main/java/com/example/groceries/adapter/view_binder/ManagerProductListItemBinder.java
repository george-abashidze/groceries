package com.example.groceries.adapter.view_binder;

import android.view.View;

import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.databinding.ManagerProductListItemBinding;
import com.example.groceries.helper.ItemBindingInterface;

public class ManagerProductListItemBinder implements ItemBindingInterface<ManagerProductListItemBinding, JoinProductCart> {

    public interface ManagerProductEvents{
        void onEditClick(JoinProductCart item);
        void onDeleteClick(JoinProductCart item);
    }

    private ManagerProductEvents events;

    public ManagerProductListItemBinder(ManagerProductEvents events){
        this.events = events;
    }

    @Override
    public void bindData(ManagerProductListItemBinding binding, JoinProductCart model, int position) {
        binding.setProduct(model);

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
