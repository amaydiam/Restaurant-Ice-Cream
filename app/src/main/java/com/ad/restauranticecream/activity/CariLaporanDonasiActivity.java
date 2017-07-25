package com.ad.restauranticecream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.Zakat;
import com.ad.restauranticecream.fragment.LaporanDonasiDetailFragment;
import com.ad.restauranticecream.fragment.LaporanDonasiListFragment;
import com.ad.restauranticecream.fragment.LaporanDonasiDetailFragment;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CariLaporanDonasiActivity extends AppCompatActivity {

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
            String keyword;
            Intent intent = getIntent();
            keyword = intent.getStringExtra(RestaurantIceCream.KEYWORD);
            if (actionbar != null) {
                actionbar.setTitle(keyword);
            }
            loadListLaporanDonasi(keyword);
        }

    }

    private void loadListLaporanDonasi(String keyword) {
        LaporanDonasiListFragment fragment = new LaporanDonasiListFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.KEYWORD, keyword);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }

    public void loadDetailLaporanDonasiFragmentWith(String id_mustahiq) {
        LaporanDonasiDetailFragment fragment = new LaporanDonasiDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.DONASI_ID, id_mustahiq);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
