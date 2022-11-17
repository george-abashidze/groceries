package com.example.groceries.ui.user_activity;

import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.repository.contract.ICartRepository;
import com.example.groceries.data.local.repository.contract.ICategoryRepository;
import com.example.groceries.data.local.repository.contract.IProductRepository;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.helper.Filter;
import com.example.groceries.helper.SimpleResultCallback;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;
import com.example.groceries.helper.enums.SortEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserActivityViewModel extends ViewModel {

    IProductRepository productRepo;
    ICategoryRepository categoryRepo;
    ICartRepository cartRepo;

    private Filter filter = new Filter(0,SortEnum.NONE);

    MutableLiveData<List<JoinProductCart>> products = new MutableLiveData<>();
    MutableLiveData<String> daoResultMessage = new MutableLiveData<>();

    @Inject
    public UserActivityViewModel(
            IProductRepository productRepo,
            ICategoryRepository categoryRepo,
            ICartRepository cartRepo){

        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.cartRepo = cartRepo;
    }

    public void getProducts(){
        String query = "SELECT product.id, product.name, product.categoryId, product.unitPrice, product.quantity, cart.quantity as inCartCount, cart.id as cartId, category.name as categoryName " +
                    "FROM product " +
                    "LEFT JOIN cart " +
                    "ON product.id = cart.productId " +
                    "LEFT JOIN category " +
                    "ON product.categoryId = category.id";


        productRepo.getJoinedData(query, result -> {
            if (result.isSuccess()){
                result.getData().add(0,new JoinProductCart());
                products.setValue(result.getData());
            }
        });
    }

    public void addToCart(List<Cart> items, SimpleResultCallback callback){

        cartRepo.insertItems(result -> {
            if(!result.isSuccess()){
                callback.onResult(false);
            }
        },items);
    }

    public void removeFromCart(List<Cart> items,SimpleResultCallback callback){


        cartRepo.deleteItems(result -> {
            if (!result.isSuccess()) {
                callback.onResult(false);
            }

        }, items);
    }

    public void updateCartItems(List<Cart> items,SimpleResultCallback callback){

        cartRepo.updateItems(result -> {
            if(!result.isSuccess()) {
                callback.onResult(false);
            }

        },items);
    }

    public List<JoinProductCart> sortAndFilter(long categoryId,SortEnum sort,List<JoinProductCart> items){

        if(items == null)
            return new ArrayList<>();

        List<JoinProductCart> itemsCopy = new ArrayList<>(items);

        itemsCopy.remove(0);
        if(categoryId != 0){

                ListIterator<JoinProductCart> iterator = itemsCopy.listIterator();

                while(iterator.hasNext()){
                    if(iterator.next().getCategoryId() != categoryId){
                        iterator.remove();
                    }
                }

        }

        if(sort != SortEnum.NONE){

            Collections.sort(itemsCopy, (joinProductCart, t1) -> {
                if(sort == SortEnum.BY_NAME){
                    return joinProductCart.getName().compareTo(t1.getName());
                }
                else if(sort == SortEnum.BY_PRICE){
                    return Float.compare(joinProductCart.getUnitPrice(), t1.getUnitPrice());
                }
                return 0;
            });
        }

        itemsCopy.add(0,new JoinProductCart());
        return itemsCopy;
    }

    public void saveChanges(List<Cart> deletedItems,List<Cart> insertedItems,List<Cart> updatedItems){
        AtomicReference<Integer> failedTasks = new AtomicReference<>(0);

        if(deletedItems.size() > 0){
            removeFromCart(deletedItems, (success) -> {
                if(!success)
                    failedTasks.getAndSet(failedTasks.get() + 1);
            });
        }

        if(insertedItems.size() > 0){
            addToCart(insertedItems, (success) -> {
                if(!success)
                    failedTasks.getAndSet(failedTasks.get() + 1);

            });
        }

        if(updatedItems.size() > 0){
            updateCartItems(updatedItems, (success) -> {
                if(!success)
                    failedTasks.getAndSet(failedTasks.get() + 1);
            });
        }


        if(failedTasks.get() > 0){
            daoResultMessage.setValue(failedTasks.get() +" Tasks Failed.");
        }

        getProducts();

    }

    public LiveData<List<Category>> getCategories(){
        return categoryRepo.getLiveData();
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
