package com.example.groceries.adapter.view_binder;

import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.databinding.UserProductListItemBinding;
import com.example.groceries.helper.ItemBindingInterface;

public class UserProductListItemBinder implements ItemBindingInterface<UserProductListItemBinding, JoinProductCart> {


    public interface UserProductListEvents{
        void onStateChanged(long itemId,int index);
    }

    private final UserProductListEvents events;

    public UserProductListItemBinder(UserProductListEvents events) {
        this.events = events;
    }

    @Override
    public void bindData(UserProductListItemBinding binding, JoinProductCart model,int position) {
        binding.setProduct(model);

        binding.btnIncrement.setOnClickListener(view -> {

            JoinProductCart item = binding.getProduct();
            if(item.getInCartCount() >= item.getQuantity())
                return;
            item.setInCartCount(item.getInCartCount()+1);
            item.setStateChanged(true);
            binding.setProduct(item);
            if(events != null){
                events.onStateChanged(item.getId(),position);
            }
        });

        binding.btnDecrement.setOnClickListener(view -> {
            JoinProductCart item = binding.getProduct();
            if(item.getInCartCount() <= 0)
                return;
            item.setInCartCount(item.getInCartCount()-1);
            item.setStateChanged(true);
            binding.setProduct(item);
            if(events != null){
                events.onStateChanged(item.getId(),position);
            }
        });

        binding.btnAddToCart.setOnClickListener(view -> {
            JoinProductCart item = binding.getProduct();
            item.setInCartCount(item.getInCartCount()+1);
            item.setStateChanged(true);
            binding.setProduct(item);
            if(events != null){
                events.onStateChanged(item.getId(),position);
            }
        });
    }
}
