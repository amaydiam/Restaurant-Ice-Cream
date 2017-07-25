package com.ad.restauranticecream.utils;

import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class SetAlphaView {

    // constructor
    public SetAlphaView() {
    }


    public static void setAlpha(View view, float alpha) {
        if (Build.VERSION.SDK_INT < 11) {
            final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        } else view.setAlpha(alpha);
    }

}
