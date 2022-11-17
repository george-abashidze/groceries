package com.example.groceries.ui.dialog;

import static com.example.groceries.helper.Constants.KEY_CATEGORIES;
import static com.example.groceries.helper.Constants.KEY_PRODUCT_FORM_DATA;
import static com.example.groceries.helper.Constants.KEY_TITLE;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.groceries.R;
import com.example.groceries.adapter.BaseSpinnerAdapter;
import com.example.groceries.adapter.view_binder.SpinnerDDBinder;
import com.example.groceries.adapter.view_binder.SpinnerItemBinder;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;
import com.example.groceries.databinding.DialogProductFormBinding;
import com.example.groceries.databinding.SpinnerDropDownItemBinding;
import com.example.groceries.databinding.SpinnerItemBinding;
import com.example.groceries.helper.ItemBindingInterface;

import java.util.ArrayList;


public class ProductFormDialog extends DialogFragment {
    public static String TAG = "product_form_dialog";

    public interface ProductDialogCallback{
        void onFinish(Product data);
    }

    Product initData;
    ProductDialogCallback callback;
    DialogProductFormBinding binding = null;
    ArrayList<Category> categories;

    BaseSpinnerAdapter<Category, SpinnerItemBinding, SpinnerDropDownItemBinding> categorySpinnerAdapter;
    String title;


    public static ProductFormDialog newInstance(String title,Product data, ArrayList<Category> categoryList, ProductDialogCallback callback) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_PRODUCT_FORM_DATA,data);
        args.putParcelableArrayList(KEY_CATEGORIES,categoryList);
        args.putString(KEY_TITLE,title);
        ProductFormDialog fragment = new ProductFormDialog();
        fragment.setArguments(args);
        fragment.callback = callback;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categorySpinnerAdapter = new BaseSpinnerAdapter<>(R.layout.spinner_item, R.layout.spinner_drop_down_item, new SpinnerItemBinder(), new SpinnerDDBinder());

        Bundle args = getArguments();

        if(args != null){
            initData = args.getParcelable(KEY_PRODUCT_FORM_DATA);
            categories = args.getParcelableArrayList(KEY_CATEGORIES);
            title = args.getString(KEY_TITLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if(dialog != null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         binding = DialogProductFormBinding.inflate(inflater);

         if(initData != null){
             binding.setProduct(initData);
         }
        else{
            binding.setProduct(new Product());

        }

        //restore object if there is a savedInstanceState
        if (savedInstanceState != null) {
            binding.setProduct(savedInstanceState.getParcelable(KEY_PRODUCT_FORM_DATA));
        }

         binding.setTitle(title);
         binding.categorySpinner.setAdapter(categorySpinnerAdapter);

         categorySpinnerAdapter.setItems(categories);

         binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 Product product = binding.getProduct();
                 if(product != null){
                     Category category = categorySpinnerAdapter.getItem(i);
                     product.setCategoryId(category.getId());
                     binding.setProduct(product);
                 }

             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });


        //sync objects categoryId and unitId to spinners
        //when creating view
        if(binding.getProduct() != null){

            Product product = binding.getProduct();
            Category category = null;

            for (Category c : categories) {
                if (c.getId() == product.getCategoryId()) {
                    category = c;
                }
            }

            if(category != null){
                int position = categories.indexOf(category);
                binding.categorySpinner.setSelection(position,true);

            }
        }


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(binding != null){
            binding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product product = binding.getProduct();
                    if(product.isValid()){
                        binding.error.setVisibility(View.GONE);
                        if(callback != null){
                            callback.onFinish(binding.getProduct());
                        }

                        dismiss();
                    }
                    else{
                        binding.error.setVisibility(View.VISIBLE);
                    }

                }
            });
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save object state
        outState.putParcelable(KEY_PRODUCT_FORM_DATA,binding.getProduct());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }
}
