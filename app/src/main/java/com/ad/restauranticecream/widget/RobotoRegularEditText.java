package com.ad.restauranticecream.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.ad.restauranticecream.utils.FontCache;


public class RobotoRegularEditText extends EditText {

    public RobotoRegularEditText(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(FontCache.ROBOTO_REGULAR, context);
        setTypeface(customFont);
    }
}