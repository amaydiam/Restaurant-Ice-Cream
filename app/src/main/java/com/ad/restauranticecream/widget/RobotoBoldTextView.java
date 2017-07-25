package com.ad.restauranticecream.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ad.restauranticecream.utils.FontCache;

public class RobotoBoldTextView extends TextView {

    public RobotoBoldTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(FontCache.ROBOTO_BOLD, context);
        setTypeface(customFont);
    }
}