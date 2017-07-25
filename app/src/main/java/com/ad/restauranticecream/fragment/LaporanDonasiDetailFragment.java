package com.ad.restauranticecream.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.LaporanDonasi;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.TextUtils;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.ad.restauranticecream.widget.RobotoLightTextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.json.JSONObject;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LaporanDonasiDetailFragment extends Fragment implements CustomVolley.OnCallbackResponse {

    private static final String TAG_DETAIL = "TAG_DETAIL";
    @BindBool(R.bool.is_tablet)
    boolean isTablet;
    // Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_text_holder)
    View toolbarTextHolder;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbarSubtitle;
    // Main views
    @BindView(R.id.progress_circle)
    View progressCircle;
    @BindView(R.id.error_message)
    View errorMessage;
    @BindView(R.id.text_error)
    RobotoLightTextView textError;
    @BindView(R.id.try_again)
    RobotoBoldTextView tryAgain;

    // Basic info
    @BindView(R.id.movie_detail_holder)
    NestedScrollView movieHolder;
    @BindView(R.id.foto_profil_muzaki)
    AvatarView fotoProfilMuzaki;
    @BindView(R.id.nama_muzaki)
    RobotoBoldTextView namaMuzaki;
    @BindView(R.id.alamat_muzaki)
    RobotoLightTextView alamatMuzaki;
    @BindView(R.id.no_identitas_muzaki)
    RobotoLightTextView noIdentitasMuzaki;
    @BindView(R.id.no_telp_muzaki)
    RobotoLightTextView noTelpMuzaki;
    @BindView(R.id.status_muzaki)
    RobotoLightTextView statusMuzaki;
    @BindView(R.id.foto_profil_mustahiq)
    AvatarView fotoProfilMustahiq;
    @BindView(R.id.nama_calon_mustahiq)
    RobotoBoldTextView namaMustahiq;
    @BindView(R.id.alamat_calon_mustahiq)
    RobotoLightTextView alamatMustahiq;
    @BindView(R.id.no_identitas_calon_mustahiq)
    RobotoLightTextView noIdentitasMustahiq;
    @BindView(R.id.no_telp_calon_mustahiq)
    RobotoLightTextView noTelpMustahiq;
    @BindView(R.id.status_calon_mustahiq)
    RobotoLightTextView statusMustahiq;
    @BindView(R.id.nama_amil_zakat)
    RobotoLightTextView namaAmilZakat;
    @BindView(R.id.jumlah_donasi)
    RobotoLightTextView jumlahDonasi;
    private Unbinder unbinder;
    private String id;
    private LaporanDonasi laporanDonasi;
    private PicassoLoader imageLoader;
    private CustomVolley customVolley;
    private RequestQueue queue;

    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_laporan_donasi_detail, container, false);
        unbinder = ButterKnife.bind(this, v);
        customVolley = new CustomVolley(getActivity());
        customVolley.setOnCallbackResponse(this);
        imageLoader = new PicassoLoader();

        // Setup toolbar
        toolbar.setTitle(R.string.loading);
        if (!isTablet) {
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.action_home));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }


        // Download laporanDonasi details if new instance, else restore from saved instance
        if (savedInstanceState == null || !(savedInstanceState.containsKey(RestaurantIceCream.DONASI_ID)
                && savedInstanceState.containsKey(RestaurantIceCream.LAPORAN_DONASI_OBJECT))) {
            id = getArguments().getString(RestaurantIceCream.DONASI_ID);
            if (TextUtils.isNullOrEmpty(id)) {
                progressCircle.setVisibility(View.GONE);
                toolbarTextHolder.setVisibility(View.GONE);
                toolbar.setTitle("");
            } else {
                downloadLokasiDetails(id);
            }
        } else {
            id = savedInstanceState.getString(RestaurantIceCream.DONASI_ID);
            laporanDonasi = savedInstanceState.getParcelable(RestaurantIceCream.LAPORAN_DONASI_OBJECT);
            onDownloadSuccessful();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Send screen id_user_kru to analytics
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (laporanDonasi != null && id != null) {
            outState.putString(RestaurantIceCream.DONASI_ID, id);
            outState.putParcelable(RestaurantIceCream.LAPORAN_DONASI_OBJECT, laporanDonasi);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_DETAIL);
        }

    }


    // JSON parsing and display
    private void downloadLokasiDetails(String id) {
        String urlToDownload = ApiHelper.getLaporanDonasiDetailLink(getActivity(), id);
        queue = customVolley.Rest(Request.Method.GET, urlToDownload, null, TAG_DETAIL);
    }

    private void onDownloadSuccessful() {

        // Toggle visibility
        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        movieHolder.setVisibility(View.VISIBLE);

        toolbar.setTitle(laporanDonasi.nama_calon_mustahiq);
        toolbarTextHolder.setVisibility(View.GONE);

        imageLoader.loadImage(fotoProfilMuzaki, laporanDonasi.nama_muzaki, laporanDonasi.nama_muzaki);
        namaMuzaki.setText("Nama : " + laporanDonasi.nama_muzaki);
        alamatMuzaki.setText("Alamat : " + (TextUtils.isNullOrEmpty(laporanDonasi.alamat_muzaki) ? "-" : laporanDonasi.alamat_muzaki));
        noIdentitasMuzaki.setText("No Identitas : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_identitas_muzaki) ? "-" : laporanDonasi.no_identitas_muzaki));
        noTelpMuzaki.setText("No Telp : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_telp_muzaki) ? "-" : laporanDonasi.no_telp_muzaki));
        statusMuzaki.setText(Html.fromHtml("Status Aktif : " + (laporanDonasi.status_muzaki.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));


        jumlahDonasi.setText(Html.fromHtml("Jumlah Donasi : Rp. " + Utils.Rupiah(laporanDonasi.jumlah_donasi)));


        imageLoader.loadImage(fotoProfilMustahiq, laporanDonasi.nama_calon_mustahiq, laporanDonasi.nama_calon_mustahiq);
        namaMustahiq.setText("Nama : " + laporanDonasi.nama_calon_mustahiq);
        alamatMustahiq.setText("Alamat : " + (TextUtils.isNullOrEmpty(laporanDonasi.alamat_calon_mustahiq) ? "-" : laporanDonasi.alamat_calon_mustahiq));
        noIdentitasMustahiq.setText("No Identitas : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_identitas_calon_mustahiq) ? "-" : laporanDonasi.no_identitas_calon_mustahiq));
        noTelpMustahiq.setText("No Telp : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_telp_calon_mustahiq) ? "-" : laporanDonasi.no_telp_calon_mustahiq));
        statusMustahiq.setText(Html.fromHtml("Status Aktif : " + (laporanDonasi.status_calon_mustahiq.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));
        namaAmilRestaurantIceCream.setText("Nama Amil Restaurant Ice Cream: " + laporanDonasi.nama_amil_zakat);

    }

    private void onDownloadFailed() {
        errorMessage.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
        movieHolder.setVisibility(View.GONE);
        toolbarTextHolder.setVisibility(View.GONE);
        toolbar.setTitle("");
    }

    private void onNotAvailable(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        textError.setText(message);
        tryAgain.setVisibility(View.GONE);
        progressCircle.setVisibility(View.GONE);
        movieHolder.setVisibility(View.GONE);
        toolbarTextHolder.setVisibility(View.GONE);
        toolbar.setTitle("");
    }

    @OnClick(R.id.try_again)
    public void onTryAgainClicked() {
        errorMessage.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        downloadLokasiDetails(id);
    }


    @Override
    public void onVolleyStart(String TAG) {

    }

    @Override
    public void onVolleyEnd(String TAG) {

    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String isSuccess = jsonObject.getString(RestaurantIceCream.isSuccess);
            String message = jsonObject.getString(RestaurantIceCream.message);

            JSONObject jsDetail = new JSONObject(jsonObject.getString(RestaurantIceCream.donasi));

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

            laporanDonasi = new LaporanDonasi(
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

            if (Boolean.parseBoolean(isSuccess))
                onDownloadSuccessful();
            else
                onNotAvailable(message);

        } catch (Exception ex) {
            // Parsing error
            onDownloadFailed();
            Log.d("Parse Error", ex.getMessage(), ex);
        }
    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {

    }


}