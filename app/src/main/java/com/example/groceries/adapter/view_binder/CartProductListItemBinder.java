package com.example.groceries.adapter.view_binder;

import android.view.View;

import com.example.groceries.data.model.JoinCartProduct;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.databinding.CartListItemBinding;
import com.example.groceries.helper.ItemBindingInterface;

public class CartProductListItemBinder implements ItemBindingInterface<CartListItemBinding, JoinCartProduct> {

    public interface CartProductListEvents{
        void onIncrement(long itemId,int index,double price);
        void onDecrement(long itemId,int index,double price);
    }

    private final CartProductListEvents events;

    public CartProductListItemBinder(CartProductListEvents events){
        this.events = events;
    }

    @Override
    public void bindData(CartListItemBinding binding, JoinCartProduct model, int position) {
        binding.setProduct(model);

        binding.btnIncrement.setOnClickListener(view -> {
            JoinCartProduct item = binding.getProduct();
            if(item.getQuantity() >= item.getAvailable())
                return;
            item.setQuantity(item.getQuantity()+1);
            item.setStateChanged(true);
            binding.setProduct(item);
            if(events != null){
                events.onIncrement(item.getId(),position,item.getUnitPrice());
            }
        });

        binding.btnDecrement.setOnClickListener(view -> {
            JoinCartProduct item = binding.getProduct();
            if(item.getQuantity() <= 0)
                return;
            item.setQuantity(item.getQuantity()-1);
            item.setStateChanged(true);
            binding.setProduct(item);
            if(events != null){
                events.onDecrement(item.getId(),position,item.getUnitPrice());
            }
        });
    }
}
