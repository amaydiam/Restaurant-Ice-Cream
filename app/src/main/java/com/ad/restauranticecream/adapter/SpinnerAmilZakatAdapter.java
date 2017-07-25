package com.ad.restauranticecream.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ad.restauranticecream.model.AmilZakat;

import java.util.ArrayList;

/**
 * Created by Amay on 12/30/2016.
 */

public class SpinnerAmilZakatAdapter extends ArrayAdapter<AmilZakat> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (AmilZakat)
    private ArrayList<AmilZakat> values;

    public SpinnerAmilZakatAdapter(Context context, int textViewResourceId,
                                   ArrayList<AmilZakat> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.size();
    }

    public AmilRestaurant Ice CreamgetItem(int position){
        return values.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (AmilZakats array) and the current position
        // You can NOW reference each method you has created in your bean object (AmilRestaurant Ice Creamclass)
        label.setText(values.get(position).nama_amil_zakat);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).nama_amil_zakat);

        return label;
    }
}