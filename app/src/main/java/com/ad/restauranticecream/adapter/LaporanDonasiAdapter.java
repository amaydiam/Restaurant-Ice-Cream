package com.ad.restauranticecream.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.model.LaporanDonasi;
import com.ad.restauranticecream.utils.TextUtils;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.ad.restauranticecream.widget.RobotoLightTextView;
import com.joanzapata.iconify.widget.IconButton;

import java.util.ArrayList;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LaporanDonasiAdapter extends RecyclerView.Adapter<LaporanDonasiAdapter.ViewHolder> implements View.OnTouchListener, View.OnClickListener {

    public final ArrayList<LaporanDonasi> data;
    private final GestureDetector gestureDetector;
    private final PicassoLoader imageLoader;
    GradientDrawable bgShape = new GradientDrawable();
    private boolean isTablet = false;
    private String keyword_alamat;
    private Activity activity;
    private SparseBooleanArray mSelectedItemsIds;
    private int selected = -1;
    private OnLaporanDonasiItemClickListener OnLaporanDonasiItemClickListener;


    public LaporanDonasiAdapter(Activity activity, ArrayList<LaporanDonasi> mustahiqList, boolean isTable) {
        this.activity = activity;
        this.data = mustahiqList;
        mSelectedItemsIds = new SparseBooleanArray();
        gestureDetector = new GestureDetector(activity, new SingleTapConfirm());
        this.isTablet = isTable;
        imageLoader = new PicassoLoader();

    }

    public void setOnLaporanDonasiItemClickListener(OnLaporanDonasiItemClickListener onLaporanDonasiItemClickListener) {
        this.OnLaporanDonasiItemClickListener = onLaporanDonasiItemClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int viewId = v.getId();
        if (viewId == R.id.btn_action) {
            if (gestureDetector.onTouchEvent(event)) {
                if (OnLaporanDonasiItemClickListener != null) {
                    AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.playSoundEffect(SoundEffectConstants.CLICK);
                    OnLaporanDonasiItemClickListener.onActionClick(v, (Integer) v.getTag());
                }
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (OnLaporanDonasiItemClickListener != null) {
            OnLaporanDonasiItemClickListener.onRootClick(v, (Integer) v.getTag());
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
                .inflate(R.layout.item_donasi_list, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.rootParent.setOnClickListener(this);
        holder.btnAction.setOnTouchListener(this);
        return holder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.btnAction.setVisibility(View.GONE);
        LaporanDonasi laporanDonasi = data.get(position);

        imageLoader.loadImage(holder.fotoProfilMuzaki, laporanDonasi.nama_muzaki, laporanDonasi.nama_muzaki);
        holder.namaMuzaki.setText("Nama : " + laporanDonasi.nama_muzaki);
        holder.alamatMuzaki.setText("Alamat : " + (TextUtils.isNullOrEmpty(laporanDonasi.alamat_muzaki) ? "-" : laporanDonasi.alamat_muzaki));
        holder.noIdentitasMuzaki.setText("No Identitas : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_identitas_muzaki) ? "-" : laporanDonasi.no_identitas_muzaki));
        holder.noTelpMuzaki.setText("No Telp : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_telp_muzaki) ? "-" : laporanDonasi.no_telp_muzaki));
        holder.statusMuzaki.setText(Html.fromHtml("Status Aktif : " + (laporanDonasi.status_muzaki.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));


        holder.jumlahDonasi.setText(Html.fromHtml("Jumlah Donasi : Rp. " + Utils.Rupiah(laporanDonasi.jumlah_donasi)));


        imageLoader.loadImage(holder.fotoProfilMustahiq, laporanDonasi.nama_calon_mustahiq, laporanDonasi.nama_calon_mustahiq);
        holder.namaMustahiq.setText("Nama : " + laporanDonasi.nama_calon_mustahiq);
        holder.alamatMustahiq.setText("Alamat : " + (TextUtils.isNullOrEmpty(laporanDonasi.alamat_calon_mustahiq) ? "-" : laporanDonasi.alamat_calon_mustahiq));
        holder.noIdentitasMustahiq.setText("No Identitas : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_identitas_calon_mustahiq) ? "-" : laporanDonasi.no_identitas_calon_mustahiq));
        holder.noTelpMustahiq.setText("No Telp : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_telp_calon_mustahiq) ? "-" : laporanDonasi.no_telp_calon_mustahiq));
        holder.statusMustahiq.setText(Html.fromHtml("Status Aktif : " + (laporanDonasi.status_calon_mustahiq.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));
        holder.namaAmilRestaurantIceCream.setText("Nama Amil Restaurant Ice Cream: " + laporanDonasi.nama_amil_zakat);

        if (isTablet) {
            if (selected == position)
                holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_selected_background));
            else
                holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_background));
        } else {
            holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_background));

        }

        holder.btnAction.setTag(position);
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

    public interface OnLaporanDonasiItemClickListener {
        void onActionClick(View v, int position);

        void onRootClick(View v, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.btn_action)
        IconButton btnAction;
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
