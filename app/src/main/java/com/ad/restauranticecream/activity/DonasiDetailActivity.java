package com.ad.restauranticecream.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.Zakat;
import com.ad.restauranticecream.fragment.DialogDetailDonasiFragment;
import com.ad.restauranticecream.fragment.DonasiDetailFragment;
import com.ad.restauranticecream.model.LaporanDonasi;
import com.ad.restauranticecream.utils.Prefs;

public class DonasiDetailActivity extends AppCompatActivity {

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
                mustahiqId = getIntent().getStringExtra(RestaurantIceCream.CALON_MUSTAHIQ_ID);
                loadDonasiDetailsOf(mustahiqId);
            } else {
                // Loading from deep link
                String[] parts = data.toString().split("/");
                mustahiqId = parts[parts.length - 1];
                switch (mustahiqId) {
                    // Load Donasi Lists
                    case "movie":
                        loadDonasiOfType(0);
                        break;
                    case "top-rated":
                        loadDonasiOfType(1);
                        break;
                    case "faq":
                        loadDonasiOfType(2);
                        break;
                    case "now-playing":
                        loadDonasiOfType(3);
                        break;
                    // Load details of a particular movie
                    default:
                        int dashPosition = mustahiqId.indexOf("-");
                        if (dashPosition != -1) {
                            mustahiqId = mustahiqId.substring(0, dashPosition);
                        }
                        loadDonasiDetailsOf(mustahiqId);
                        break;
                }
            }
        }
    }

    private void loadDonasiDetailsOf(String mustahiqId) {
        DonasiDetailFragment fragment = new DonasiDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.CALON_MUSTAHIQ_ID, mustahiqId);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_detail_container, fragment).commit();
    }

    @SuppressLint("CommitPrefEdits")
    private void loadDonasiOfType(int viewType) {
        Prefs.putLastSelected(this, RestaurantIceCream.VIEW_TYPE_DONASI);
        startActivity(new Intent(this, DrawerActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                LaporanDonasi laporanDonasi = data.getParcelableExtra(RestaurantIceCream.LAPORAN_DONASI_OBJECT);
                if (laporanDonasi != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    DialogDetailDonasiFragment dialogDetailDonasiFragment = new DialogDetailDonasiFragment();
                    dialogDetailDonasiFragment.setCancelable(false);
                    dialogDetailDonasiFragment.setData(laporanDonasi);
                    ft.add(dialogDetailDonasiFragment, null);
                    ft.commitAllowingStateLoss();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

}
