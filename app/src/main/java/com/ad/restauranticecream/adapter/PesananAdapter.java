package com.ad.restauranticecream.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.Pesanan;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.utils.TextUtils;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.ad.restauranticecream.widget.RobotoLightTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder> implements View.OnTouchListener, View.OnClickListener {

    public final ArrayList<Pesanan> data;
    private final GestureDetector gestureDetector;

    private boolean isTablet = false;
    private String keyword_alamat;
    private Activity activity;
    private SparseBooleanArray mSelectedItemsIds;
    private int selected = -1;
    private OnPesananItemClickListener OnPesananItemClickListener;


    public PesananAdapter(Activity activity, ArrayList<Pesanan> mustahiqList, boolean isTable) {
        this.activity = activity;
        this.data = mustahiqList;
        mSelectedItemsIds = new SparseBooleanArray();
        gestureDetector = new GestureDetector(activity, new SingleTapConfirm());
        this.isTablet = isTable;
    }

    public void setOnPesananItemClickListener(OnPesananItemClickListener onPesananItemClickListener) {
        this.OnPesananItemClickListener = onPesananItemClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int viewId = v.getId();
        if (viewId == R.id.btn_siap || viewId == R.id.btn_nikmati || viewId == R.id.btn_batal || viewId == R.id.btn_bayar) {
            if (gestureDetector.onTouchEvent(event)) {
                if (OnPesananItemClickListener != null) {
                    AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.playSoundEffect(SoundEffectConstants.CLICK);
                    OnPesananItemClickListener.onActionClick(v, (Integer) v.getTag());
                }
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (OnPesananItemClickListener != null) {
            OnPesananItemClickListener.onRootClick(v, (Integer) v.getTag());
        }
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public void delete_all() {
        int count = getItemCount();
        if (count > 0) {
            data.clear();
            notifyDataSetChanged();
        }

    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pesanan_list, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.rootParent.setOnClickListener(this);
        holder.btnSiap.setOnTouchListener(this);
        holder.btnNikmati.setOnTouchListener(this);
        holder.btnBatal.setOnTouchListener(this);
        holder.btnBayar.setOnTouchListener(this);
        return holder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        Pesanan pesanan = data.get(position);

        holder.namaMeja.setText(pesanan.nama_meja);
        holder.namaPemesan.setText("Nama Pemesan : " + (TextUtils.isNullOrEmpty(pesanan.nama_pemesan) ? "-" : pesanan.nama_pemesan));
        holder.kodePesanan.setText("Kode Pesanan : " + (TextUtils.isNullOrEmpty(pesanan.kode_pesanan) ? "-" : pesanan.kode_pesanan));
        holder.statusPesanan.setText("Status : " + (TextUtils.isNullOrEmpty(pesanan.status_pesanan) ? "-" : pesanan.status_pesanan));
        holder.jumlahHarga.setText("Total : " + Utils.Rupiah(pesanan.total_harga));

        if (isTablet) {
            if (selected == position)
                holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_selected_background));
            else
                holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_background));
        } else {
            holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_background));

        }

        if (Prefs.getModeApp(activity) == RestaurantIceCream.MODE_PELAYAN) {
            if (pesanan.status_pesanan.toLowerCase().contains("menunggu")) {
                holder.btnSiap.setVisibility(View.VISIBLE);
                holder.btnNikmati.setVisibility(View.GONE);
            } else if (pesanan.status_pesanan.toLowerCase().contains("siap")) {
                holder.btnSiap.setVisibility(View.GONE);
                holder.btnNikmati.setVisibility(View.VISIBLE);
            } else {
                holder.btnSiap.setVisibility(View.GONE);
                holder.btnNikmati.setVisibility(View.GONE);
            }
            holder.btnBatal.setVisibility(View.VISIBLE);
            holder.btnBayar.setVisibility(View.GONE);
        } else if (Prefs.getModeApp(activity) == RestaurantIceCream.MODE_KASIR) {
            holder.btnSiap.setVisibility(View.GONE);
            holder.btnNikmati.setVisibility(View.GONE);
            holder.btnBatal.setVisibility(View.GONE);
            holder.btnBayar.setVisibility(View.VISIBLE);
        } else {
            holder.btnSiap.setVisibility(View.GONE);
            holder.btnNikmati.setVisibility(View.GONE);
            holder.btnBatal.setVisibility(View.GONE);
            holder.btnBayar.setVisibility(View.GONE);
        }

        holder.btnSiap.setTag(position);
        holder.btnNikmati.setTag(position);
        holder.btnBatal.setTag(position);
        holder.btnBayar.setTag(position);
        holder.rootParent.setTag(position);

    }

    public int getItemCount() {
        return data.size();
    }

    /**
     * Here is the key method to apply the animation
     */

    public void remove(int position) {
        data.remove(data.get(position));
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void setValSearchAlamat(String keyword_alamat) {
        this.keyword_alamat = keyword_alamat;
        notifyDataSetChanged();
    }

    public void changeDisiapkan(int position) {
        data.get(position).status_pesanan = "Pesanan Sedang Disiapkan";
        notifyDataSetChanged();
    }

    public interface OnPesananItemClickListener {
        void onActionClick(View v, int position);

        void onRootClick(View v, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nama_meja)
        RobotoBoldTextView namaMeja;
        @BindView(R.id.nama_pemesan)
        RobotoLightTextView namaPemesan;
        @BindView(R.id.jumlah_harga)
        RobotoLightTextView jumlahHarga;
        @BindView(R.id.kode_pesanan)
        RobotoLightTextView kodePesanan;
        @BindView(R.id.status_pesanan)
        RobotoLightTextView statusPesanan;
        @BindView(R.id.btn_siap)
        Button btnSiap;
        @BindView(R.id.btn_nikmati)
        Button btnNikmati;
        @BindView(R.id.btn_batal)
        Button btnBatal;
        @BindView(R.id.btn_bayar)
        Button btnBayar;
        @BindView(R.id.root_parent)
        CardView rootParent;

        public ViewHolder(View vi) {
            super(vi);
            ButterKnife.bind(this, vi);

        }

    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }


    }

}
