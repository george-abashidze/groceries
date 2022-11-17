package com.example.groceries.helper;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class CustomBindingAdapters {

    @InverseBindingAdapter(attribute = "floatText")
    public static float getFloatFromEditText(EditText view) {
        try {
            return Float.parseFloat(view.getText().toString());
        } catch (NumberFormatException e) {
            return  0f;
        }

    }

    @InverseBindingAdapter(attribute = "intText")
    public static int getIntFromEditText(EditText view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return  0;
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("floatText")
    public static void setFloatInEditText(EditText view, Float value) {

        try {

            if (value == null && (view.getText().toString().isEmpty() || view.getText().toString().equals("."))) return;

            float val = Float.parseFloat(view.getText().toString());

            if (val == value)
                return;

            view.setText(value.toString());

        } catch (NumberFormatException e) {
            if(value != null && value != 0.0f){
                view.setText(value.toString());
            }
            else {
                view.setText("");
            }

        }

    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("intText")
    public static void setIntInEditText(EditText view, Integer value) {

        try {

            if (value == null && (view.getText().toString().isEmpty())) return;

            float val = Integer.parseInt(view.getText().toString());

            if (val == value)
                return;

            view.setText(value.toString());

        } catch (NumberFormatException e) {
            if(value != null && value != 0){
                view.setText(value.toString());
            }
            else {
                view.setText("");
            }

        }

    }

    @BindingAdapter("floatTextAttrChanged")
    public static void setFloatTextChangeListener(EditText view, InverseBindingListener listener) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onChange();
            }
        });
    }

    @BindingAdapter("intTextAttrChanged")
    public static void setIntTextChangeListener(EditText view, InverseBindingListener listener) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onChange();
            }
        });
    }

}
