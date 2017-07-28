package com.ad.restauranticecream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.fragment.KategoriMenuListFragment;
import com.ad.restauranticecream.model.ImageFile;
import com.ad.restauranticecream.model.KategoriMenu;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class KategoriMenuListActivity extends AppCompatActivity {

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
                actionbar.setTitle("Kategori Menu");
            }
            loadListKategoriMenu();
        }

    }

    private void loadListKategoriMenu() {
        KategoriMenuListFragment fragment = new KategoriMenuListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void loadDetailKategoriMenuFragmentWith(KategoriMenu kategoriMenu) {/*
        PesananDetailFragment fragment = new PesananDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.KATEGORI_MENU_ID, kategoriMenu.id_mustahiq);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                EventBus.getDefault().postSticky(new ImageFile(imageFile));

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(KategoriMenuListActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });


    }//onActivityResult
}
