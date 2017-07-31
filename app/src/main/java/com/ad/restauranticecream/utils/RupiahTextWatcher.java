package com.ad.restauranticecream.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class RupiahTextWatcher implements TextWatcher {

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";
    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;
    private EditText et;

    public RupiahTextWatcher(EditText et) {
        df = new DecimalFormat("#,###");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###");
        this.et = et;
        hasFractionalPart = false;
    }

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {

            String x;
            x = et.getText().toString();
            if (x.length() == 0) {
                et.setText("0");
                et.setSelection(1);
            }

            int inilen, endlen;
            inilen = et.getText().length();

            x = s.toString().replace(".", "");
            //  return formatter.format(Long.parseLong(bd));
            x = String.format("%,d", BigInteger.valueOf(Long.parseLong(x)));

            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                et.setText(x.replace(",", "."));
            } else {
                et.setText(x.replace(",", "."));
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
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
        hasFractionalPart = s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
    }

}