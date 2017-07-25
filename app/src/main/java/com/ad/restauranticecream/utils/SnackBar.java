package com.ad.restauranticecream.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mia on 11/07/2015.
 */
public class SnackBar {

    private final Context _c;
    private final CoordinatorLayout _coordinatorLayout;

    public SnackBar(Context c, CoordinatorLayout coordinatorLayout) {
        _c = c;
        _coordinatorLayout = coordinatorLayout;
    }

    public void show(String error) {
        Snackbar snackbar = Snackbar
                .make(_coordinatorLayout, error, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.DKGRAY);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }
}
