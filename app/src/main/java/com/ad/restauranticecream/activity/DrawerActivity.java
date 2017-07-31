package com.ad.restauranticecream.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.fragment.DrawerFragment;
import com.ad.restauranticecream.fragment.HomeFrament;
import com.ad.restauranticecream.fragment.PesananDetailFragment;
import com.ad.restauranticecream.model.ImageFile;
import com.ad.restauranticecream.model.Pesanan;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindBool;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class DrawerActivity extends AppCompatActivity {

    @BindBool(R.bool.is_tablet)
    boolean isTablet;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule());
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        if (isTablet && savedInstanceState == null) {
            loadDetailMustahiqFragmentWith();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        hideSoftKeyboard();
    }

    public void loadDetailMustahiqFragmentWith() {
        HomeFrament fragment = new HomeFrament();
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();
    }


    public void loadDetailMejaFragmentWith() {
/*
        MejaDetailFragment fragment = new MejaDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.MEJA_ID, id_calon_mustahiq);

        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();*/

    }


    public void loadDetailPesananFragmentWith(Pesanan pesanan) {
        PesananDetailFragment fragment = new PesananDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(RestaurantIceCream.PESANAN_OBJECT, pesanan);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Iconify
                .with(new MaterialModule())
                .with(new MaterialCommunityModule());
    }

    @Override
    public void onBackPressed() {

        DrawerFragment fragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (fragment != null) {

            if (fragment.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                fragment.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Tekan sekali lagi untuk keluar.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            super.onBackPressed();
            return;
        }
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(DrawerActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });


    }//onActivityResult

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


}
