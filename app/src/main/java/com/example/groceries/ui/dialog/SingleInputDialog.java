package com.example.groceries.ui.dialog;

import static com.example.groceries.helper.Constants.KEY_IS_EDIT;
import static com.example.groceries.helper.Constants.KEY_PRODUCT_FORM_DATA;
import static com.example.groceries.helper.Constants.KEY_SINGLE_INPUT_DIALOG_DATA;
import static com.example.groceries.helper.Constants.KEY_TITLE;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.groceries.databinding.DialogSingleInputBinding;

public class SingleInputDialog extends DialogFragment {
    public static String TAG = "single_input_dialog";

    public interface DialogCallback{
        void onFinish(String data);
    }

    String title;
    String initData;
    DialogSingleInputBinding binding = null;
    DialogCallback callback;

     public static SingleInputDialog newInstance(String title,String data,DialogCallback callback) {

        Bundle args = new Bundle();
        args.putString(KEY_SINGLE_INPUT_DIALOG_DATA,data);
        args.putString(KEY_TITLE,title);
        SingleInputDialog fragment = new SingleInputDialog();
        fragment.setArguments(args);
        fragment.callback = callback;
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if(args != null){
            title = args.getString(KEY_TITLE);
            initData = args.getString(KEY_SINGLE_INPUT_DIALOG_DATA);
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

        binding = DialogSingleInputBinding.inflate(inflater);
        binding.setTitle(title);

        if(initData != null && !initData.isEmpty()){
            binding.setData(initData);
        }

        //restore data if there is a savedInstanceState
        if (savedInstanceState != null) {
            binding.setData(savedInstanceState.getString(KEY_SINGLE_INPUT_DIALOG_DATA));
        }

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save data state
        outState.putString(KEY_SINGLE_INPUT_DIALOG_DATA,binding.getData());
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
                    String data = binding.getData();

                    if(data == null || data.isEmpty()){
                       binding.input.setError("This field is required");
                        return;
                    }
                    else{
                        binding.input.setError(null);
                    }


                    if(callback != null){
                        callback.onFinish(binding.getData());
                    }

                    dismiss();
                }
            });
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }
}
