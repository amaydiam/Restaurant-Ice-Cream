package com.ad.restauranticecream.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.fragment.LaporanDonasiDetailFragment;
import com.ad.restauranticecream.utils.Prefs;

public class LaporanDonasiDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        if (savedInstanceState == null) {
            String mustahiqId;
            Intent intent = getIntent();
            Uri data = intent.getData();
            if (data == null) {
                // Not loading from deep link
                mustahiqId = getIntent().getStringExtra(RestaurantIceCream.DONASI_ID);
                loadLaporanDonasiDetailsOf(mustahiqId);
            } else {
                // Loading from deep link
                String[] parts = data.toString().split("/");
                mustahiqId = parts[parts.length - 1];
                switch (mustahiqId) {
                    // Load LaporanDonasi Lists
                    case "movie":
                        loadLaporanDonasiOfType(0);
                        break;
                    case "top-rated":
                        loadLaporanDonasiOfType(1);
                        break;
                    case "faq":
                        loadLaporanDonasiOfType(2);
                        break;
                    case "now-playing":
                        loadLaporanDonasiOfType(3);
                        break;
                    // Load details of a particular movie
                    default:
                        int dashPosition = mustahiqId.indexOf("-");
                        if (dashPosition != -1) {
                            mustahiqId = mustahiqId.substring(0, dashPosition);
                        }
                        loadLaporanDonasiDetailsOf(mustahiqId);
                        break;
                }
            }
        }
    }

    private void loadLaporanDonasiDetailsOf(String mustahiqId) {
        LaporanDonasiDetailFragment fragment = new LaporanDonasiDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.DONASI_ID, mustahiqId);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_detail_container, fragment).commit();
    }

    @SuppressLint("CommitPrefEdits")
    private void loadLaporanDonasiOfType(int viewType) {
        Prefs.putLastSelected(this, RestaurantIceCream.VIEW_TYPE_LAPORAN_DONASI);
        startActivity(new Intent(this, DrawerActivity.class));
        finish();
    }

}
