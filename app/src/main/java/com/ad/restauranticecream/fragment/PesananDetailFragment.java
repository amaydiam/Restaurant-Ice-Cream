package com.ad.restauranticecream.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.activity.DrawerActivity;
import com.ad.restauranticecream.model.DetailPesanan;
import com.ad.restauranticecream.model.Pesanan;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.utils.TextUtils;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.ad.restauranticecream.widget.RobotoLightTextView;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.ad.restauranticecream.widget.RobotoRegularTextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import agency.tango.android.avatarview.loader.PicassoLoader;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PesananDetailFragment extends Fragment implements CustomVolley.OnCallbackResponse {

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
    @BindView(R.id.nama_meja)
    RobotoBoldTextView namaMeja;
    @BindView(R.id.nama_pemesan)
    RobotoLightTextView namaPemesan;
    @BindView(R.id.kode_pesanan)
    RobotoLightTextView kodePesanan;
    @BindView(R.id.status_pesanan)
    RobotoLightTextView statusPesanan;
    @BindView(R.id.jumlah_harga)
    RobotoLightTextView jumlahHarga;

    @BindView(R.id.tabel)
    TableLayout tabel;

    @BindView(R.id.movie_detail_holder)
    NestedScrollView movieDetailHolder;


    @BindView(R.id.catatan)
    RobotoRegularEditText catatan;

    @BindView(R.id.catatan_kedua)
    RobotoRegularTextView catatanKedua;


    private Unbinder unbinder;
    private Pesanan pesanan;
    private PicassoLoader imageLoader;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private String detail_pesanan;

    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pesanan_detail, container, false);
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


        catatan.setVisibility(View.GONE);
        catatanKedua.setVisibility(View.VISIBLE);

        // Download pesanan details if new instance, else restore from saved instance
        if (savedInstanceState == null || !(savedInstanceState.containsKey(RestaurantIceCream.DETAIL_PESANAN)
                && savedInstanceState.containsKey(RestaurantIceCream.PESANAN_OBJECT))) {
            pesanan = getArguments().getParcelable(RestaurantIceCream.PESANAN_OBJECT);
            if (TextUtils.isNullOrEmpty(pesanan.id_pesanan)) {
                progressCircle.setVisibility(View.GONE);
                toolbarTextHolder.setVisibility(View.GONE);
                toolbar.setTitle("");
            } else {
                downloadLokasiDetails(pesanan.id_pesanan);
            }
        } else {
            pesanan = savedInstanceState.getParcelable(RestaurantIceCream.PESANAN_OBJECT);
            detail_pesanan = savedInstanceState.getString(RestaurantIceCream.DETAIL_PESANAN);
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
        if (pesanan != null && detail_pesanan != null) {
            outState.putString(RestaurantIceCream.DETAIL_PESANAN, detail_pesanan);
            outState.putParcelable(RestaurantIceCream.PESANAN_OBJECT, pesanan);
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
        String urlToDownload = ApiHelper.getPesananDetailLink(getActivity(), id);
        queue = customVolley.Rest(Request.Method.GET, urlToDownload, null, TAG_DETAIL);
    }

    private void onDownloadSuccessful() {

        // Toggle visibility
        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        movieDetailHolder.setVisibility(View.VISIBLE);

        toolbar.setTitle(pesanan.nama_meja);
        toolbarTextHolder.setVisibility(View.GONE);

        namaMeja.setText(pesanan.nama_meja);
        namaPemesan.setText("Nama Pemesan : " + (TextUtils.isNullOrEmpty(pesanan.nama_pemesan) ? "-" : pesanan.nama_pemesan));
        kodePesanan.setText("Kode Pesanan : " + (TextUtils.isNullOrEmpty(pesanan.kode_pesanan) ? "-" : pesanan.kode_pesanan));
        statusPesanan.setText("Status : " + (TextUtils.isNullOrEmpty(pesanan.status_pesanan) ? "-" : pesanan.status_pesanan));
        jumlahHarga.setText("Total : "+Utils.Rupiah(pesanan.total_harga));

        catatanKedua.setText(pesanan.catatan);


        try {
            JSONObject json = new JSONObject(detail_pesanan);
            JSONArray items_obj = json.getJSONArray(RestaurantIceCream.detail_pesanan);
            int jumlah_list_data = items_obj.length();
            for (int i = 0; i < jumlah_list_data; i++) {
                JSONObject obj = items_obj.getJSONObject(i);
                String id_detail_pesanan = obj.getString(RestaurantIceCream.id_detail_pesanan);
                String id_pesanan = obj.getString(RestaurantIceCream.id_pesanan);
                String id_menu = obj.getString(RestaurantIceCream.id_menu);
                String jumlah_pesanan = obj.getString(RestaurantIceCream.jumlah_pesanan);
                String harga_pesanan = obj.getString(RestaurantIceCream.harga_pesanan);
                String nama_menu = obj.getString(RestaurantIceCream.nama_menu);
                String total_harga_pesanan = obj.getString(RestaurantIceCream.total_harga_pesanan);
                //set map object
                DetailPesanan detailPesanan = new DetailPesanan(
                        id_detail_pesanan,
                        id_pesanan,
                        id_menu,
                        jumlah_pesanan,
                        harga_pesanan,
                        nama_menu,
                        total_harga_pesanan

                );

                View tableRow = LayoutInflater.from(getActivity()).inflate(R.layout.item_detail_pesanan_list, null, false);

                RobotoLightTextView r_no = (RobotoLightTextView) tableRow.findViewById(R.id.no);
                RobotoLightTextView r_nama_menu = (RobotoLightTextView) tableRow.findViewById(R.id.nama_menu);
                RobotoLightTextView r_jumlah_pesanan = (RobotoLightTextView) tableRow.findViewById(R.id.jumlah_pesanan);
                RobotoLightTextView r_harga_pesanan = (RobotoLightTextView) tableRow.findViewById(R.id.harga_pesanan);
                RobotoLightTextView r_total_harga_pesanan = (RobotoLightTextView) tableRow.findViewById(R.id.total_harga_pesanan);

                r_no.setText((i + 1) + ".");
                r_nama_menu.setText(detailPesanan.nama_menu);
                r_jumlah_pesanan.setText(detailPesanan.jumlah_pesanan);
                r_harga_pesanan.setText(Utils.Rupiah(detailPesanan.harga_pesanan));
                r_total_harga_pesanan.setText(Utils.Rupiah(detailPesanan.total_harga_pesanan));

                tabel.addView(tableRow);

            }
        } catch (JSONException ex) {
            Log.d("Parse Error", ex.getMessage(), ex);
        }


    }

    private void onDownloadFailed() {
        errorMessage.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
        movieDetailHolder.setVisibility(View.GONE);
        toolbarTextHolder.setVisibility(View.GONE);
        toolbar.setTitle("");
    }

    private void onNotAvailable(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        textError.setText(message);
        tryAgain.setVisibility(View.GONE);
        progressCircle.setVisibility(View.GONE);
        movieDetailHolder.setVisibility(View.GONE);
        toolbarTextHolder.setVisibility(View.GONE);
        toolbar.setTitle("");
    }

    @OnClick(R.id.try_again)
    public void onTryAgainClicked() {
        errorMessage.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        downloadLokasiDetails(pesanan.id_pesanan);
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
            JSONObject json = new JSONObject(response);
            Boolean isSuccess = Boolean.parseBoolean(json.getString(RestaurantIceCream.isSuccess));
            String message = json.getString(RestaurantIceCream.message);
            if (isSuccess) {
                detail_pesanan = response;

                onDownloadSuccessful();


            } else {
                onNotAvailable(message);
            }


        } catch (JSONException ex) {
            onDownloadFailed();
            Log.d("Parse Error", ex.getMessage(), ex);
        }


    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {

    }


}