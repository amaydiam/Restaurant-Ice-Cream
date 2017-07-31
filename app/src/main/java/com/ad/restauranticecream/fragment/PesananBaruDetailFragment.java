package com.ad.restauranticecream.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.activity.KategoriMenuListActivity;
import com.ad.restauranticecream.model.DetailPesanan;
import com.ad.restauranticecream.model.PesanBaru;
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
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialCommunityIcons;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PesananBaruDetailFragment extends Fragment implements CustomVolley.OnCallbackResponse {

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

    @BindView(R.id.catatan)
    RobotoRegularEditText catatan;

    @BindView(R.id.catatan_kedua)
    RobotoRegularTextView catatanKedua;

    @BindView(R.id.btn_kirim)
    Button btnKirim;
    @BindView(R.id.btn_pesan_lagi)
    Button btnPesanLagi;
    @BindView(R.id.btn_batal)
    Button btnBatal;

    @BindView(R.id.movie_detail_holder)
    NestedScrollView movieDetailHolder;


    private Unbinder unbinder;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private ProgressDialog dialogProgress;

    @OnClick({R.id.btn_kirim, R.id.btn_pesan_lagi, R.id.btn_batal})
    void ClickMode(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_kirim:
                new AlertDialog.Builder(getActivity())
                        .setIcon(
                                new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_alert_octagon)
                                        .colorRes(R.color.primary)
                                        .actionBarSize())
                        .setTitle("Pengiriman Pesanan")
                        .setMessage("Apa anda akan melakukan pesanan ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Map<String, String> jsonParams = new HashMap<>();
                                jsonParams.put(RestaurantIceCream.id_meja,
                                        Prefs.getIdMeja(getActivity()));
                                jsonParams.put(RestaurantIceCream.kode_pesanan,
                                        Prefs.getKodePesanan(getActivity()));
                                jsonParams.put(RestaurantIceCream.nama_pemesan,
                                        Prefs.getNamaPelanggan(getActivity()));

                                List<PesanBaru> pb = PesanBaru.listAll(PesanBaru.class);
                                int jumlah_list_data = pb.size();
                                String pesan_baru = "";
                                for (int ii = 0; ii < jumlah_list_data; ii++) {
                                    String id_menu = pb.get(ii).id_menu;
                                    String jumlah_pesanan = pb.get(ii).jumlah_pesanan;
                                    String harga_pesanan = pb.get(ii).harga_menu;
                                    pesan_baru = pesan_baru + "#|" + id_menu + "|" + jumlah_pesanan + "|" + harga_pesanan;
                                }

                                jsonParams.put("pesanan_baru", pesan_baru);

                                if (!TextUtils.isNullOrEmpty(Prefs.getNamaPelanggan(getActivity()))) {
                                    jsonParams.put(RestaurantIceCream.catatan,
                                            Prefs.getCatatanPesanan(getActivity()));
                                }

                                queue = customVolley.Rest(Request.Method.POST, ApiHelper.getPesananBaruLink(getActivity()), jsonParams, TAG_DETAIL);


                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
                break;
            case R.id.btn_pesan_lagi:

                Intent pesanan = new Intent(getActivity(), KategoriMenuListActivity.class);
                startActivity(pesanan);
                break;
            case R.id.btn_batal:
                new AlertDialog.Builder(getActivity())
                        .setIcon(
                                new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_alert_octagon)
                                        .colorRes(R.color.primary)
                                        .actionBarSize())
                        .setTitle("Batal Pesanan")
                        .setMessage("Apa anda akan membatalkan pesanan ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Prefs.putNamaPelanggan(getActivity(), null);
                                Prefs.putKodePesanan(getActivity(), null);
                                Prefs.putCatatanPesanan(getActivity(), "");
                                PesanBaru.deleteAll(PesanBaru.class);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
                break;
            default:
                break;

        }

    }

    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pesanan_detail, container, false);
        unbinder = ButterKnife.bind(this, v);
        customVolley = new CustomVolley(getActivity());
        customVolley.setOnCallbackResponse(this);

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

        btnKirim.setVisibility(View.VISIBLE);
        btnPesanLagi.setVisibility(View.VISIBLE);
        btnBatal.setVisibility(View.VISIBLE);

        catatan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Prefs.putCatatanPesanan(getActivity(), catatan.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        onDownloadSuccessful();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        onDownloadSuccessful();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_DETAIL);
        }

    }

    private void onDownloadSuccessful() {

        // Toggle visibility
        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        movieDetailHolder.setVisibility(View.VISIBLE);

        toolbar.setTitle(Prefs.getNamaMeja(getActivity()));
        toolbarTextHolder.setVisibility(View.GONE);

        namaMeja.setText(Prefs.getNamaMeja(getActivity()));
        namaPemesan.setText("Nama Pemesan : " + (TextUtils.isNullOrEmpty(Prefs.getNamaPelanggan(getActivity())) ? "-" : Prefs.getNamaPelanggan(getActivity())));
        kodePesanan.setText("Kode Pesanan : " + (TextUtils.isNullOrEmpty(Prefs.getKodePesanan(getActivity())) ? "-" : Prefs.getKodePesanan(getActivity())));
        statusPesanan.setText("Status : -");

        catatan.setText(Prefs.getCatatanPesanan(getActivity()));
        catatanKedua.setText(Prefs.getCatatanPesanan(getActivity()));

        if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_PELANGGAN) {
            catatan.setVisibility(View.VISIBLE);
            catatanKedua.setVisibility(View.GONE);
        } else {
            catatan.setVisibility(View.GONE);
            catatanKedua.setVisibility(View.VISIBLE);
        }

        tabel.removeAllViews();
        int totaljumlahHarga = 0;
        List<PesanBaru> pb = PesanBaru.listAll(PesanBaru.class);
        int jumlah_list_data = pb.size();
        for (int i = 0; i < jumlah_list_data; i++) {
            String id_menu = pb.get(i).id_menu;
            String jumlah_pesanan = pb.get(i).jumlah_pesanan;
            String harga_pesanan = pb.get(i).harga_menu;
            String nama_menu = pb.get(i).nama_menu;
            Integer total_harga_pesanan = Integer.parseInt(pb.get(i).harga_menu) * Integer.parseInt(pb.get(i).jumlah_pesanan);
            //set map object
            DetailPesanan detailPesanan = new DetailPesanan(
                    "",
                    "",
                    id_menu,
                    jumlah_pesanan,
                    harga_pesanan,
                    nama_menu,
                    String.valueOf(total_harga_pesanan)

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
            totaljumlahHarga = totaljumlahHarga + total_harga_pesanan;
        }

        jumlahHarga.setText("Total : " + Utils.Rupiah(totaljumlahHarga));

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
    }

    private void startProgress() {

        dialogProgress = ProgressDialog.show(getActivity(), "Kirim ...",
                "Please wait...", true);
    }

    private void stopProgress() {
        if (dialogProgress != null)
            dialogProgress.dismiss();
    }


    @Override
    public void onVolleyStart(String TAG) {
        startProgress();
    }

    @Override
    public void onVolleyEnd(String TAG) {
        stopProgress();
    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {

        try {
            JSONObject json = new JSONObject(response);
            Boolean isSuccess = Boolean.parseBoolean(json.getString(RestaurantIceCream.isSuccess));
            String message = json.getString(RestaurantIceCream.message);
            if (isSuccess) {
                Prefs.putNamaPelanggan(getActivity(), null);
                Prefs.putKodePesanan(getActivity(), null);
                Prefs.putCatatanPesanan(getActivity(), "");
                PesanBaru.deleteAll(PesanBaru.class);
                getActivity().finish();

            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException ex) {
            Log.d("Parse Error", ex.getMessage(), ex);
        }


    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {

    }


}