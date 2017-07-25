package com.ad.restauranticecream.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.Zakat;
import com.ad.restauranticecream.fragment.MustahiqDetailFragment;
import com.ad.restauranticecream.utils.Prefs;

public class MustahiqDetailActivity extends AppCompatActivity {

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
                mustahiqId = getIntent().getStringExtra(RestaurantIceCream.MUSTAHIQ_ID);
                loadMustahiqDetailsOf(mustahiqId);
            } else {
                // Loading from deep link
                String[] parts = data.toString().split("/");
                mustahiqId = parts[parts.length - 1];
                switch (mustahiqId) {
                    // Load Mustahiq Lists
                    case "movie":
                        loadMustahiqOfType(0);
                        break;
                    case "top-rated":
                        loadMustahiqOfType(1);
                        break;
                    case "faq":
                        loadMustahiqOfType(2);
                        break;
                    case "now-playing":
                        loadMustahiqOfType(3);
                        break;
                    // Load details of a particular movie
                    default:
                        int dashPosition = mustahiqId.indexOf("-");
                        if (dashPosition != -1) {
                            mustahiqId = mustahiqId.substring(0, dashPosition);
                        }
                        loadMustahiqDetailsOf(mustahiqId);
                        break;
                }
            }
        }
    }

    private void loadMustahiqDetailsOf(String mustahiqId) {
        MustahiqDetailFragment fragment = new MustahiqDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.MUSTAHIQ_ID, mustahiqId);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_detail_container, fragment).commit();
    }

    @SuppressLint("CommitPrefEdits")
    private void loadMustahiqOfType(int viewType) {
        Prefs.putLastSelected(this, RestaurantIceCream.VIEW_TYPE_MUSTAHIQ);
        startActivity(new Intent(this, DrawerActivity.class));
        finish();
    }

}
