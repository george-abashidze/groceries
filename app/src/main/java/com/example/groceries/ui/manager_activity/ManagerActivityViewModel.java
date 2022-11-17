package com.example.groceries.ui.manager_activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;
import com.example.groceries.data.local.repository.contract.ICartRepository;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.data.local.repository.contract.ICategoryRepository;
import com.example.groceries.data.local.repository.contract.IProductRepository;
import com.example.groceries.helper.Filter;
import com.example.groceries.helper.enums.SortEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManagerActivityViewModel extends ViewModel {

    IProductRepository productRepo;
    ICategoryRepository categoryRepo;
    ICartRepository cartRepo;

    private Filter filter = new Filter(0,SortEnum.NONE);

    MutableLiveData<List<JoinProductCart>> products = new MutableLiveData<>();
    MutableLiveData<String> daoResultMessage = new MutableLiveData<>();

    @Inject
    public ManagerActivityViewModel(IProductRepository productRepo,
                                    ICategoryRepository categoryRepo,
                                    ICartRepository cartRepo){
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.cartRepo = cartRepo;
    }


    public void getProducts(){
        String query = "SELECT product.id, product.name, product.categoryId, product.unitPrice, product.quantity, category.name as categoryName, cart.id as cartId " +
                "FROM product " +
                "LEFT JOIN cart " +
                "ON product.id = cart.productId "+
                "LEFT JOIN category " +
                "ON product.categoryId = category.id";

        productRepo.getJoinedData(query, result -> {

            if (result.isSuccess() && result.getData() != null){
                result.getData().add(0,new JoinProductCart());
                products.setValue(result.getData());
            }
        });
    }

    public void insertProduct(Product product){
        productRepo.insertProducts(result -> {

            if(result.isSuccess()){
                getProducts();
            }
            else{
                daoResultMessage.setValue(result.getMessage());
            }

        }, Collections.singletonList(product));
    }

    public void updateCartItem(long id,int quantity){

        cartRepo.getData("SELECT * FROM cart WHERE id = " + id, result -> {

            if(result.isSuccess() && !result.getData().isEmpty()){
                Cart item = result.getData().get(0);
                if(item.getQuantity() > quantity){
                    item.setQuantity(quantity);
                    cartRepo.updateItems(null,Collections.singletonList(item));
                }

            }
        });
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

    public LiveData<List<Category>> getCategories(){
        return categoryRepo.getLiveData();
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void deleteProduct(Product product){
        productRepo.deleteProduct(result -> {
            if(result.isSuccess()){
                getProducts();
            }
            else{
                daoResultMessage.setValue(result.getMessage());
            }

        }, Collections.singletonList(product));
    }

    public void updateProduct(Product product){
        productRepo.updateProduct(result -> {
            if(result.isSuccess()){
                getProducts();
            }
            else{
                daoResultMessage.setValue(result.getMessage());
            }

        }, Collections.singletonList(product));
    }
}
