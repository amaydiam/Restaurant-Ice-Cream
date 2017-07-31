package com.ad.restauranticecream.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.activity.KategoriMenuListActivity;
import com.ad.restauranticecream.activity.PesananBaruDetailActivity;
import com.ad.restauranticecream.model.DataPesananMenunggu;
import com.ad.restauranticecream.model.PesanBaru;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.utils.TextUtils;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.ad.restauranticecream.widget.RobotoLightTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePelangganFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePelangganFragment extends Fragment implements NamaPemesanBaruFragment.NamaPemesanBaruListener {


    @BindView(R.id.nama_meja)
    RobotoBoldTextView namaMeja;
    @BindView(R.id.nama_pemesan)
    RobotoLightTextView namaPemesan;
    @BindView(R.id.kode_pesanan)
    RobotoLightTextView kodePesanan;
    @BindView(R.id.jumlah_harga)
    RobotoLightTextView jumlahHarga;
    @BindView(R.id.status_pesanan)
    RobotoLightTextView statusPesanan;
    @BindView(R.id.ket_pesanan_menunggu)
    RobotoLightTextView ketPesananMenunggu;
    @BindView(R.id.layout_pesanan_belum_dikirim)
    LinearLayout layoutPesananBelumDikirim;
    @BindView(R.id.btn_pesan_baru)
    Button btnPesanBaru;
    Unbinder unbinder;

    public HomePelangganFragment() {
        // Required empty public constructor
    }

    public static HomePelangganFragment newInstance() {
        HomePelangganFragment fragment = new HomePelangganFragment();
        return fragment;
    }

    @OnClick({R.id.root_parent, R.id.btn_pesan_baru})
    void ClickMode(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.root_parent:
                Intent pesanan = new Intent(getActivity(), PesananBaruDetailActivity.class);
                startActivity(pesanan);
                break;

            case R.id.btn_pesan_baru:

                FragmentManager fragmentManager = getChildFragmentManager();
                NamaPemesanBaruFragment mp = new NamaPemesanBaruFragment();
                mp.setTargetFragment(this, 0);
                mp.show(fragmentManager, "mp");
                break;
            default:
                break;

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_pelanggan, container, false);
        unbinder = ButterKnife.bind(this, view);
        namaMeja.setText(Prefs.getNamaMeja(getActivity()));
        ketPesananMenunggu.setVisibility(View.GONE);
        cekPesananBaru();
        loadListLaporanDonasi();
        return view;
    }

    private void cekPesananBaru() {
        if (!TextUtils.isNullOrEmpty(Prefs.getNamaPelanggan(getActivity()))) {
            layoutPesananBelumDikirim.setVisibility(View.VISIBLE);

            namaPemesan.setText("Nama Pemesan : " + Prefs.getNamaPelanggan(getActivity()));
            kodePesanan.setText("Kode Pesanan : " + Prefs.getKodePesanan(getActivity()));

            int totaljumlahHarga = 0;
            List<PesanBaru> pb = PesanBaru.listAll(PesanBaru.class);
            int jumlah_list_data = pb.size();
            for (int i = 0; i < jumlah_list_data; i++) {
                Integer total_harga_pesanan = Integer.parseInt(pb.get(i).harga_menu) * Integer.parseInt(pb.get(i).jumlah_pesanan);
                totaljumlahHarga = totaljumlahHarga + total_harga_pesanan;
            }

            jumlahHarga.setText("Total : " + Utils.Rupiah(totaljumlahHarga));

            btnPesanBaru.setVisibility(View.GONE);
        } else {
            layoutPesananBelumDikirim.setVisibility(View.GONE);
            btnPesanBaru.setVisibility(View.VISIBLE);
        }

    }

    private void loadListLaporanDonasi() {
        PesananListFragment fragment = new PesananListFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pelanggan_fragment, fragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        cekPesananBaru();
    }

    @Override
    public void onFinishNamaPemesanBaru(String nama_pemesan_baru) {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String nm = Prefs.getNamaMeja(getActivity()).replace(" ", "");
        Prefs.putNamaPelanggan(getActivity(), nama_pemesan_baru);
        Prefs.putKodePesanan(getActivity(), nm + "_" + ts);
        cekPesananBaru();
        Intent pesanan = new Intent(getActivity(), KategoriMenuListActivity.class);
        startActivity(pesanan);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onRefreshDrawer(DataPesananMenunggu cp) {
        if (cp.Jumlah() == 0) {
            ketPesananMenunggu.setVisibility(View.GONE);
        } else
            ketPesananMenunggu.setVisibility(View.VISIBLE);
        DataPesananMenunggu stickyEvent = EventBus.getDefault().getStickyEvent(DataPesananMenunggu.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
