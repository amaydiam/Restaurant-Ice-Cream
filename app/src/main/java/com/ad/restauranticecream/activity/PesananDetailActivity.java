package com.ad.restauranticecream.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.fragment.PesananDetailFragment;
import com.ad.restauranticecream.model.Pesanan;
import com.ad.restauranticecream.utils.Prefs;

public class PesananDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        if (savedInstanceState == null) {
            Pesanan pesanan;
            Intent intent = getIntent();
            pesanan = getIntent().getParcelableExtra(RestaurantIceCream.PESANAN_OBJECT);
            loadPesananDetailsOf(pesanan);

        }
    }

    private void loadPesananDetailsOf(Pesanan pesanan) {
        PesananDetailFragment fragment = new PesananDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(RestaurantIceCream.PESANAN_OBJECT, pesanan);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_detail_container, fragment).commit();
    }

    @SuppressLint("CommitPrefEdits")
    private void loadPesananOfType(int viewType) {
        if (Prefs.getModeApp(this) == RestaurantIceCream.MODE_PELAYAN) {
            Prefs.putLastSelected(this, RestaurantIceCream.VIEW_TYPE_HOME_PELAYAN);
        } else if (Prefs.getModeApp(this) == RestaurantIceCream.MODE_KASIR) {
            Prefs.putLastSelected(this, RestaurantIceCream.VIEW_TYPE_HOME_KASIR);
        }
        startActivity(new Intent(this, DrawerActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

     /*   if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                LaporanPesanan laporanPesanan = data.getParcelableExtra(RestaurantIceCream.PESANAN_OBJECT);
                if (laporanPesanan != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    DialogDetailPesananFragment dialogDetailPesananFragment = new DialogDetailPesananFragment();
                    dialogDetailPesananFragment.setCancelable(false);
                    dialogDetailPesananFragment.setData(laporanPesanan);
                    ft.add(dialogDetailPesananFragment, null);
                    ft.commitAllowingStateLoss();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }*/
    }//onActivityResult

}
