package com.example.groceries.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.groceries.data.local.dao.CartDao;
import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.repository.contract.ICartRepository;
import com.example.groceries.data.local.repository.contract.ICategoryRepository;
import com.example.groceries.data.local.repository.implementation.CartRepositoryImpl;
import com.example.groceries.data.model.JoinCartProduct;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.helper.Filter;
import com.example.groceries.helper.SimpleResultCallback;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;
import com.example.groceries.helper.enums.SortEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CartActivityViewModel extends ViewModel {

    ICartRepository cartRepo;
    ICategoryRepository categoryRepo;

    MutableLiveData<List<JoinCartProduct>> cartItems = new MutableLiveData<>();
    MutableLiveData<String> daoResultMessage = new MutableLiveData<>();
    private Filter filter = new Filter(0, SortEnum.NONE);

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Inject
    public CartActivityViewModel(ICartRepository cartRepo, ICategoryRepository categoryRepo){

        this.cartRepo = cartRepo;
        this.categoryRepo = categoryRepo;
    }

    public void getItems(){
        String query = "SELECT cart.id, cart.productId,cart.quantity, product.unitPrice, product.name as productName, product.quantity as available, category.id as categoryId, category.name as categoryName " +
                "FROM cart " +
                "LEFT JOIN product " +
                "ON cart.productId = product.id " +
                "LEFT JOIN category " +
                "ON product.categoryId = category.id";

        cartRepo.getJoinedData(query, result -> {
            if(result.isSuccess() && result.getData() != null){
                result.getData().add(0,new JoinCartProduct());
                cartItems.setValue(result.getData());
            }
            else{
                daoResultMessage.setValue(result.getMessage());
            }
        });
    }

    public List<JoinCartProduct> sortAndFilter(long categoryId,SortEnum sort,List<JoinCartProduct> items){

        if(items == null)
            return new ArrayList<>();

        List<JoinCartProduct> itemsCopy = new ArrayList<>(items);

        itemsCopy.remove(0);
        if(categoryId != 0){

            ListIterator<JoinCartProduct> iterator = itemsCopy.listIterator();

            while(iterator.hasNext()){
                if(iterator.next().getCategoryId() != categoryId){
                    iterator.remove();
                }
            }

        }

        if(sort != SortEnum.NONE){

            Collections.sort(itemsCopy, (joinProductCart, t1) -> {
                if(sort == SortEnum.BY_NAME){
                    return joinProductCart.getProductName().compareTo(t1.getProductName());
                }
                else if(sort == SortEnum.BY_PRICE){
                    return Float.compare(joinProductCart.getUnitPrice(), t1.getUnitPrice());
                }
                return 0;
            });
        }

        itemsCopy.add(0,new JoinCartProduct());
        return itemsCopy;
    }

    public void updateItems(List<Cart> items, SimpleResultCallback callback) {
        cartRepo.updateItems(result -> {
            if (!result.isSuccess()) {
                callback.onResult(false);
            }
        }, items);
    }

    public void deleteItems(List<Cart> items, SimpleResultCallback callback){

        cartRepo.deleteItems(result -> {
            if (!result.isSuccess()) {
                callback.onResult(false);
            }
        },items);

    }

    public void saveChanges(List<Cart> deletedItems, List<Cart> updatedItems){
        AtomicReference<Integer> failedTasks = new AtomicReference<>(0);

        if(deletedItems.size() > 0){
            deleteItems(deletedItems, (success) -> {
                if(!success)
                    failedTasks.getAndSet(failedTasks.get() + 1);
            });
        }

        if(updatedItems.size() > 0){
            updateItems(updatedItems, (success) -> {
                if(!success)
                    failedTasks.getAndSet(failedTasks.get() + 1);
            });
        }


        if(failedTasks.get() > 0){
            daoResultMessage.setValue(failedTasks.get() +" Tasks Failed.");
        }

        getItems();

    }

    public LiveData<List<Category>> getCategories(){
        return categoryRepo.getLiveData();
    }

}
