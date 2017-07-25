package com.ad.restauranticecream.activity;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.fragment.Step1AddInfoMuzakiFragment;
import com.ad.restauranticecream.fragment.Step2AddBuktiPembayaranFragment;
import com.ad.restauranticecream.model.LaporanDonasi;
import com.ad.restauranticecream.model.Mustahiq;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.widget.JazzyViewPager;
import com.ad.restauranticecream.widget.JazzyViewPager.TransitionEffect;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


@SuppressLint("NewApi")
public class ActionDonasiBaruActivity extends AppCompatActivity implements CustomVolley.OnCallbackResponse {

    @BindView(R.id.pager)
    public JazzyViewPager mPager;
    public boolean default_pager;
    public Mustahiq mustahiqPrepareDonasi;
    protected String tipe_jadwal_pewaris;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    private TabFragmentAdapter mAdapter;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();
    private CustomVolley customVolley;
    private String TAG_SUBMIT = "TAG_SUBMIT";
    private RequestQueue queue;
    private ProgressDialog dialogProgress;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule());

        mustahiqPrepareDonasi = getIntent().getParcelableExtra(RestaurantIceCream.calon_mustahiq);


        customVolley = new CustomVolley(this);
        customVolley.setOnCallbackResponse(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jadwal);
        ButterKnife.bind(this);
        if (toolbar != null) {
            // toolbar.setTitle(title);
            setSupportActionBar(toolbar);
        }
        tipe_jadwal_pewaris = "";
        default_pager = true;
        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeButtonEnabled(true);
            actionbar.setTitle("Donasi Baru");
        }

        mPager.setTransitionEffect(TransitionEffect.Stack);
        mPager.setPageMargin(30);
        mPager.setOffscreenPageLimit(2);
        mPager.setSwipeable(false);

        mAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);

        tabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (position == 1) {
                    SubmitData1(position);
                    if (default_pager) {
                        mPager.setCurrentItem(0);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void SubmitData1(int position) {
        Fragment fragment = mAdapter.getFragment(0);
        if (fragment instanceof Step1AddInfoMuzakiFragment) {
            Step1AddInfoMuzakiFragment step1Fragment = (Step1AddInfoMuzakiFragment) fragment;
            step1Fragment.SubmitData1(false, position);
        }
    }

    public void SubmitDataDanSave() {
        Fragment fragment = mAdapter.getFragment(0);
        if (fragment instanceof Step1AddInfoMuzakiFragment) {
            Step1AddInfoMuzakiFragment step1Fragment = (Step1AddInfoMuzakiFragment) fragment;
            step1Fragment.SubmitData1(false, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == Menus.HOME) {
            //  simpanData();
            backActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void simpanData() {
        SubmitDataDanSave();
        if (default_pager) {
            mPager.setCurrentItem(0);
        } else {
            Map<String, String> jsonParams = new HashMap<>();
            Fragment fragment0 = mAdapter.getFragment(0);
            Fragment fragment1 = mAdapter.getFragment(1);

            if (fragment0 instanceof Step1AddInfoMuzakiFragment) {
                Step1AddInfoMuzakiFragment step1Fragment = (Step1AddInfoMuzakiFragment) fragment0;
                step1Fragment.getData();
                jsonParams.put(RestaurantIceCream.nama_muzaki,
                        step1Fragment.s_nama_muzaki);
                jsonParams.put(RestaurantIceCream.alamat_muzaki,
                        step1Fragment.s_alamat_muzaki);
                jsonParams.put(RestaurantIceCream.no_identitas_muzaki,
                        step1Fragment.s_no_identitas_muzaki);
                jsonParams.put(RestaurantIceCream.no_telp_muzaki,
                        step1Fragment.s_no_telp_muzaki);
                jsonParams.put(RestaurantIceCream.jumlah_donasi,
                        step1Fragment.s_jumlah_donasi);
            }

            if (fragment1 instanceof Step2AddBuktiPembayaranFragment) {
                Step2AddBuktiPembayaranFragment step2Fragment = (Step2AddBuktiPembayaranFragment) fragment1;
                if (step2Fragment.s_foto_bukti_pembayaran != null) {
                    // process only post has valid image
                    Bitmap newsBitmap = BitmapFactory.decodeFile(step2Fragment.s_foto_bukti_pembayaran);
                    ByteArrayOutputStream imageBaOs = new ByteArrayOutputStream();
                    newsBitmap.compress(Bitmap.CompressFormat.JPEG, RestaurantIceCream.JPEG_OUTPUT_QUALITY, imageBaOs);
                    byte[] imageByteArrayNews = imageBaOs.toByteArray();

                    // process to transfrom from byteArray to base64
                    String fotoBase64 = Base64.encodeToString(imageByteArrayNews, Base64.DEFAULT);
                    jsonParams.put(RestaurantIceCream.foto_bukti_pembayaran, "data:image/jpg;base64," + fotoBase64);
                }

            }


            if (mustahiqPrepareDonasi != null)
                jsonParams.put(RestaurantIceCream.id_mustahiq, mustahiqPrepareDonasi.id_mustahiq);


            queue = customVolley.Rest(Request.Method.POST,
                    ApiHelper.getDonasiBaruLink(this), jsonParams, TAG_SUBMIT);

        }
    }

    @Override
    public void onVolleyStart(String TAG) {
        if (TAG.equals(TAG_SUBMIT)) {
            dialogProgress = ProgressDialog.show(this, "Donasi Baru",
                    "Harap menunggu...", true);
        }
    }

    @Override
    public void onVolleyEnd(String TAG) {
        if (TAG.equals(TAG_SUBMIT)) {
            ProgressLoadLoginFinish();
        }
    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {
        if (TAG.equals(TAG_SUBMIT)) {
            try {
                JSONObject json = new JSONObject(response);
                Toast.makeText(this, json.getString(RestaurantIceCream.message), Toast.LENGTH_SHORT).show();

                if (json.getBoolean(RestaurantIceCream.isSuccess)) {

                    JSONObject jsDetail = new JSONObject(json.getString(RestaurantIceCream.donasi));

                    String id_donasi = jsDetail.getString(RestaurantIceCream.id_donasi);
                    String jumlah_donasi = jsDetail.getString(RestaurantIceCream.jumlah_donasi);
                    String id_muzaki = jsDetail.getString(RestaurantIceCream.id_muzaki);
                    String nama_muzaki = jsDetail.getString(RestaurantIceCream.nama_muzaki);
                    String alamat_muzaki = jsDetail.getString(RestaurantIceCream.alamat_muzaki);
                    String no_identitas_muzaki = jsDetail.getString(RestaurantIceCream.no_identitas_muzaki);
                    String no_telp_muzaki = jsDetail.getString(RestaurantIceCream.no_telp_muzaki);
                    String status_muzaki = jsDetail.getString(RestaurantIceCream.status_muzaki);
                    String id_mustahiq = jsDetail.getString(RestaurantIceCream.id_mustahiq);
                    String id_calon_mustahiq = jsDetail.getString(RestaurantIceCream.id_calon_mustahiq);
                    String nama_calon_mustahiq = jsDetail.getString(RestaurantIceCream.nama_calon_mustahiq);
                    String alamat_calon_mustahiq = jsDetail.getString(RestaurantIceCream.alamat_calon_mustahiq);
                    String no_identitas_calon_mustahiq = jsDetail.getString(RestaurantIceCream.no_identitas_calon_mustahiq);
                    String no_telp_calon_mustahiq = jsDetail.getString(RestaurantIceCream.no_telp_calon_mustahiq);
                    String status_calon_mustahiq = jsDetail.getString(RestaurantIceCream.status_calon_mustahiq);
                    String id_amil_zakat = jsDetail.getString(RestaurantIceCream.id_amil_zakat);
                    String nama_amil_zakat = jsDetail.getString(RestaurantIceCream.nama_amil_zakat);

                    LaporanDonasi laporanDonasi = new LaporanDonasi(
                            id_donasi,
                            jumlah_donasi,
                            id_muzaki,
                            nama_muzaki,
                            alamat_muzaki,
                            no_identitas_muzaki,
                            no_telp_muzaki,
                            status_muzaki,
                            id_mustahiq,
                            id_calon_mustahiq,
                            nama_calon_mustahiq,
                            alamat_calon_mustahiq,
                            no_identitas_calon_mustahiq,
                            no_telp_calon_mustahiq,
                            status_calon_mustahiq,
                            id_amil_zakat,
                            nama_amil_zakat);

                    Intent intent = new Intent();
                    intent.putExtra(RestaurantIceCream.LAPORAN_DONASI_OBJECT, laporanDonasi);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } catch (JSONException e) {
                Toast.makeText(this, "Maaf, terdapat kesalahan parsing data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {
        if (TAG.equals(TAG_SUBMIT)) {
            Toast.makeText(this, "Ada kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    public void ProgressLoadLoginFinish() {
        if (dialogProgress.isShowing())
            dialogProgress.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Log.v("error", e.toString());
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                Log.v("foto", "ada foto");

                Fragment fragment1 = mAdapter.getFragment(1);
                if (fragment1 instanceof Step2AddBuktiPembayaranFragment) {
                    Step2AddBuktiPembayaranFragment step2Fragment = (Step2AddBuktiPembayaranFragment) fragment1;
                    step2Fragment.setFotoBuktiPembayaran(imageFile);
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(ActionDonasiBaruActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backActivity();
    }

    private void backActivity() {
        {

            Fragment fragment = mAdapter.getFragment(0);
            if (fragment instanceof Step1AddInfoMuzakiFragment) {
                Step1AddInfoMuzakiFragment step1Fragment = (Step1AddInfoMuzakiFragment) fragment;
                step1Fragment.getData();

                if (!step1Fragment.s_nama_muzaki.equals("")
                        || !step1Fragment.s_alamat_muzaki.equals("")
                        || !step1Fragment.s_no_identitas_muzaki.equals("")
                        || !step1Fragment.s_no_telp_muzaki.equals("0")
                        || !step1Fragment.s_jumlah_donasi.equals("0")
                        || step1Fragment.errorForm
                        ) {
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                    alt_bld.setMessage("Apakah anda akan membatalkan donasi?")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_CANCELED, returnIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alert.show();
                } else {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public class TabFragmentAdapter extends FragmentPagerAdapter {

        TabFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Step1AddInfoMuzakiFragment Step1 = new Step1AddInfoMuzakiFragment();
                    mPager.setObjectForPosition(Step1, position);
                    mPageReferenceMap.put(position, Step1);
                    return Step1;
                case 1:
                    Step2AddBuktiPembayaranFragment Step2 = new Step2AddBuktiPembayaranFragment();
                    mPager.setObjectForPosition(Step2, position);
                    mPageReferenceMap.put(position, Step2);
                    return Step2;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "INFO";
                case 1:
                    return "BUKTI PEMBAYARAN";
            }
            return null;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        Fragment getFragment(int key) {
            return mPageReferenceMap.get(key);
        }

    }
}