package com.ad.restauranticecream.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.activity.ActionDonasiBaruActivity;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.utils.RupiahTextWatcher;
import com.ad.restauranticecream.utils.TextUtils;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.ad.restauranticecream.widget.RobotoLightTextView;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.google.android.gms.maps.SupportMapFragment;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Step1AddInfoMuzakiFragment extends Fragment {


    public String s_nama_muzaki;
    public String s_alamat_muzaki;
    public String s_no_identitas_muzaki;
    public String s_no_telp_muzaki;
    public String s_jumlah_donasi;
    public boolean errorForm;
    @BindView(R.id.nama_muzaki)
    RobotoRegularEditText namaMuzaki;
    @BindView(R.id.alamat_muzaki)
    RobotoRegularEditText alamatMuzaki;
    @BindView(R.id.no_identitas_muzaki)
    RobotoRegularEditText noIdentitasMuzaki;
    @BindView(R.id.no_telp_muzaki)
    RobotoRegularEditText noTelpMuzaki;
    @BindView(R.id.id_mustahiq)
    RobotoRegularEditText idMustahiq;
    @BindView(R.id.foto_profil)
    AvatarView fotoProfil;
    @BindView(R.id.nama_calon_mustahiq)
    RobotoBoldTextView namaCalonMustahiq;
    @BindView(R.id.alamat_calon_mustahiq)
    RobotoLightTextView alamatCalonMustahiq;
    @BindView(R.id.no_identitas_calon_mustahiq)
    RobotoLightTextView noIdentitasCalonMustahiq;
    @BindView(R.id.no_telp_calon_mustahiq)
    RobotoLightTextView noTelpCalonMustahiq;
    @BindView(R.id.nama_perekomendasi_calon_mustahiq)
    RobotoLightTextView namaPerekomendasiCalonMustahiq;
    @BindView(R.id.nama_amil_zakat)
    RobotoLightTextView namaAmilZakat;
    @BindView(R.id.status_mustahiq)
    RobotoLightTextView statusMustahiq;
    @BindView(R.id.waktu_terakhir_donasi)
    RobotoLightTextView waktuTerakhirDonasi;
    @BindView(R.id.jumlah_donasi)
    RobotoRegularEditText jumlahDonasi;
    @BindView(R.id.btn_step_2)
    Button btnStep2;
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;
    boolean after_launch;
    private ActionDonasiBaruActivity activity;
    private Unbinder unbinder;
    private PicassoLoader imageLoader;

    @OnClick(R.id.btn_step_2)
    void Step2() {
        SubmitData1(true, 1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.form_step_1_donasi, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        imageLoader = new PicassoLoader();
        activity = (ActionDonasiBaruActivity) getActivity();
        displayView();
        return rootView;

    }


    void displayView() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        View mapView = mapFragment.getView();
        mapView.setVisibility(View.GONE);

        setupUI(parentLayout);

        errorForm = false;
        after_launch = false;


        jumlahDonasi.addTextChangedListener(new RupiahTextWatcher(jumlahDonasi));

        jumlahDonasi.setText("0");


        SetData();

    }

    private void SetData() {
        if (activity.mustahiqPrepareDonasi != null) {

            if (Prefs.getLogin(getActivity())) {
                namaMuzaki.setEnabled(false);
                alamatMuzaki.setEnabled(false);
                noIdentitasMuzaki.setEnabled(false);
                noTelpMuzaki.setEnabled(false);

                namaMuzaki.setText(Prefs.getNamaUser(getActivity()));
                alamatMuzaki.setText(Prefs.getAlamatUser(getActivity()));
                noIdentitasMuzaki.setText(Prefs.getNomorIdentitasUser(getActivity()));
                noTelpMuzaki.setText(Prefs.getNomorTelpUser(getActivity()));
            }

            idMustahiq.setText(activity.mustahiqPrepareDonasi.id_mustahiq);

            imageLoader.loadImage(fotoProfil, activity.mustahiqPrepareDonasi.nama_calon_mustahiq, activity.mustahiqPrepareDonasi.nama_calon_mustahiq);
            namaCalonMustahiq.setText("Nama : " + activity.mustahiqPrepareDonasi.nama_calon_mustahiq);
            alamatCalonMustahiq.setText("Alamat : " + (TextUtils.isNullOrEmpty(activity.mustahiqPrepareDonasi.alamat_calon_mustahiq) ? "-" : activity.mustahiqPrepareDonasi.alamat_calon_mustahiq));
            noIdentitasCalonMustahiq.setText("No Identitas : " + (TextUtils.isNullOrEmpty(activity.mustahiqPrepareDonasi.no_identitas_calon_mustahiq) ? "-" : activity.mustahiqPrepareDonasi.no_identitas_calon_mustahiq));
            noTelpCalonMustahiq.setText("No Telp : " + (TextUtils.isNullOrEmpty(activity.mustahiqPrepareDonasi.no_telp_calon_mustahiq) ? "-" : activity.mustahiqPrepareDonasi.no_telp_calon_mustahiq));
            namaPerekomendasiCalonMustahiq.setText("Nama Perekomendasi : " + (TextUtils.isNullOrEmpty(activity.mustahiqPrepareDonasi.nama_perekomendasi_calon_mustahiq) ? "-" : activity.mustahiqPrepareDonasi.nama_perekomendasi_calon_mustahiq));
            statusMustahiq.setText(Html.fromHtml("Status Aktif : " + (activity.mustahiqPrepareDonasi.status_mustahiq.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));
            namaAmilRestaurantIceCream.setText("Nama Amil Restaurant Ice Cream: " + activity.mustahiqPrepareDonasi.nama_amil_zakat);
            waktuTerakhirDonasi.setText("Waktu Terakhir Menerima Donasi : " + (TextUtils.isNullOrEmpty(activity.mustahiqPrepareDonasi.waktu_terakhir_donasi) ? "-" : activity.mustahiqPrepareDonasi.waktu_terakhir_donasi));
            waktuTerakhirDonasi.setVisibility(View.GONE);
            statusMustahiq.setVisibility(View.GONE);

        }
    }

    public void SubmitData1(boolean btn, int index) {
        getData();
        getValidasiForm();

        if (!after_launch && !errorForm) {
            hitungWarisan();
        }
        after_launch = true;

        if (!errorForm) {
            {
                if (btn) {
                    namaMuzaki.clearFocus();
                    alamatMuzaki.clearFocus();
                    noIdentitasMuzaki.clearFocus();
                    noTelpMuzaki.clearFocus();
                    jumlahDonasi.clearFocus();
                }
                activity.default_pager = false;
                activity.mPager.setSwipeable(true);

                if (index != 0) {
                    activity.mPager.setCurrentItem(index);
                }

            }
        } else {
            activity.default_pager = true;
            activity.mPager.setSwipeable(false);
        }
    }


    void hitungWarisan() {
        getData();


    }

    public void getData() {
        s_nama_muzaki = namaMuzaki.getText().toString().trim();
        s_alamat_muzaki = alamatMuzaki.getText().toString().trim();
        s_no_identitas_muzaki = noIdentitasMuzaki.getText().toString().trim();
        s_no_telp_muzaki = noTelpMuzaki.getText().toString().trim();
        s_jumlah_donasi = jumlahDonasi.getText().toString().trim().replace(".", "");
    }

    void getValidasiForm() {


        boolean error_nama_muzaki;
        boolean error_alamat_muzaki;
        boolean error_no_identitas_muzaki;
        boolean error_no_telp_muzaki;
        boolean error_jumlah_donasi;

        if (s_jumlah_donasi.length() == 0 || s_jumlah_donasi.equals("0")) {
            noIdentitasMuzaki.setError("Tidak boleh kosong");
            noIdentitasMuzaki.requestFocus();
            error_jumlah_donasi = true;
        } else {
            noIdentitasMuzaki.setError(null);
            error_jumlah_donasi = false;
        }


        if (s_no_telp_muzaki.length() == 0) {
            noTelpMuzaki.setError("Tidak boleh kosong");
            noTelpMuzaki.requestFocus();
            error_no_telp_muzaki = true;
        } else {
            noTelpMuzaki.setError(null);
            error_no_telp_muzaki = false;
        }

        if (s_no_identitas_muzaki.length() == 0) {
            noIdentitasMuzaki.setError("Tidak boleh kosong");
            noIdentitasMuzaki.requestFocus();
            error_no_identitas_muzaki = true;
        } else {
            noIdentitasMuzaki.setError(null);
            error_no_identitas_muzaki = false;
        }

        if (s_alamat_muzaki.length() == 0) {
            alamatMuzaki.setError("Tidak boleh kosong");
            alamatMuzaki.requestFocus();
            error_alamat_muzaki = true;
        } else {
            alamatMuzaki.setError(null);
            error_alamat_muzaki = false;
        }

        if (s_nama_muzaki.length() == 0) {
            namaMuzaki.setError("Tidak boleh kosong");
            namaMuzaki.requestFocus();
            error_nama_muzaki = true;
        } else {
            namaMuzaki.setError(null);
            error_nama_muzaki = false;
        }


        errorForm = error_nama_muzaki
                || error_alamat_muzaki
                || error_no_identitas_muzaki
                || error_no_telp_muzaki
                || error_jumlah_donasi;

    }


    public void setupUI(final View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    //	view.requestFocus();
                    hideSoftKeyboard();
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    protected void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
