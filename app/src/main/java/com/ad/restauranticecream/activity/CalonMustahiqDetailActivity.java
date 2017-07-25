package com.ad.restauranticecream.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.fragment.CalonMustahiqDetailFragment;
import com.ad.restauranticecream.fragment.DialogDetailDonasiFragment;
import com.ad.restauranticecream.model.ImageFile;
import com.ad.restauranticecream.model.LaporanDonasi;
import com.ad.restauranticecream.model.PickLocation;
import com.ad.restauranticecream.utils.Prefs;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class CalonMustahiqDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        if (savedInstanceState == null) {
            String mustahiqId;
            mustahiqId = getIntent().getStringExtra(RestaurantIceCream.CALON_MUSTAHIQ_ID);
            loadMustahiqDetailsOf(mustahiqId);
        } else {
            loadMustahiqOfType(RestaurantIceCream.VIEW_TYPE_CALON_MUSTAHIQ);
        }
    }

    private void loadMustahiqDetailsOf(String mustahiqId) {
        CalonMustahiqDetailFragment fragment = new CalonMustahiqDetailFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantIceCream.CALON_MUSTAHIQ_ID, mustahiqId);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_detail_container, fragment).commit();
    }

    @SuppressLint("CommitPrefEdits")
    private void loadMustahiqOfType(int viewType) {
        Prefs.putLastSelected(this, RestaurantIceCream.VIEW_TYPE_CALON_MUSTAHIQ);
        startActivity(new Intent(this, DrawerActivity.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(CalonMustahiqDetailActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });


    }//onActivityResult




}
