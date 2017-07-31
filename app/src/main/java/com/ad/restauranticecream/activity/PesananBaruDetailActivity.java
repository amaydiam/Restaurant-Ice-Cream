package com.ad.restauranticecream.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.fragment.PesananBaruDetailFragment;

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
