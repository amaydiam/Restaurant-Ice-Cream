package com.ad.restauranticecream.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.fragment.PesananBaruDetailFragment;
import com.ad.restauranticecream.fragment.PesananDetailFragment;
import com.ad.restauranticecream.model.Pesanan;
import com.ad.restauranticecream.utils.Prefs;

public class PesananBaruDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
            loadPesananBaruDetailsOf();

    }

    private void loadPesananBaruDetailsOf() {
        PesananBaruDetailFragment fragment = new PesananBaruDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_detail_container, fragment).commit();
    }



}
