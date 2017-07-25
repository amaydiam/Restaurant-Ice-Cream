package com.ad.restauranticecream.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumberTextWatcher implements TextWatcher {


    private EditText et;

    public NumberTextWatcher(EditText et) {
        this.et = et;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {
            String x;
            x = et.getText().toString();
            if(x.length()==0){
                et.setText("0");
                et.setSelection(1);
            }
            x = et.getText().toString();
            if (x.substring(0,1).equals("0") && x.length() > 1) {
                et.setText(x.substring(1, x.length()));
                et.setSelection(x.length()-1);
            }

        } catch (NumberFormatException nfe) {
            // do nothing?
        }

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

}