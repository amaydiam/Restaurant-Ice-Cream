package com.ad.restauranticecream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.fragment.PesananDetailFragment;
import com.ad.restauranticecream.fragment.PesananListFragment;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PesananListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(
                new IconDrawable(this, MaterialIcons.md_arrow_back)
                        .colorRes(R.color.white)
                        .actionBarSize());
        ActionBar actionbar = getSupportActionBar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (actionbar != null) {
                actionbar.setTitle("Pesanan");
            }
            loadListPesanan();
        }

    }

    private void loadListPesanan() {
        PesananListFragment fragment = new PesananListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
