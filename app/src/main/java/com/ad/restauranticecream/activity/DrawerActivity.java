package com.ad.restauranticecream.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.fragment.CalonMustahiqDetailFragment;
import com.ad.restauranticecream.fragment.DialogDetailDonasiFragment;
import com.ad.restauranticecream.fragment.DonasiDetailFragment;
import com.ad.restauranticecream.fragment.DrawerFragment;
import com.ad.restauranticecream.fragment.LaporanDonasiDetailFragment;
import com.ad.restauranticecream.fragment.MustahiqDetailFragment;
import com.ad.restauranticecream.model.ImageFile;
import com.ad.restauranticecream.model.LaporanDonasi;
import com.ad.restauranticecream.model.PickLocation;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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
            loadDetailMustahiqFragmentWith("null");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        hideSoftKeyboard();
    }

    public void loadDetailMustahiqFragmentWith(String id_mustahiq) {
        MustahiqDetailFragment fragment = new MustahiqDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.MUSTAHIQ_ID, id_mustahiq);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();
    }


    public void loadDetailCalonMustahiqFragmentWith(String id_calon_mustahiq) {

        CalonMustahiqDetailFragment fragment = new CalonMustahiqDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.CALON_MUSTAHIQ_ID, id_calon_mustahiq);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();

    }


    public void loadDetailDonasiFragmentWith(String id_mustahiq) {
        DonasiDetailFragment fragment = new DonasiDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.CALON_MUSTAHIQ_ID, id_mustahiq);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();
    }


    public void loadDetailLaporanDonasiFragmentWith(String id_donasi) {
        LaporanDonasiDetailFragment fragment = new LaporanDonasiDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.DONASI_ID, id_donasi);
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


        if (requestCode == RestaurantIceCream.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                String address = place.getAddress().toString();
                EventBus.getDefault().postSticky(new PickLocation(latitude, longitude, address));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

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
