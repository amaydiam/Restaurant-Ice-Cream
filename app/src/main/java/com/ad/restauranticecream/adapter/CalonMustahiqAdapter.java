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
import com.ad.restauranticecream.model.CalonMustahiq;
import com.ad.restauranticecream.utils.TextUtils;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.ad.restauranticecream.widget.RobotoLightTextView;
import com.joanzapata.iconify.widget.IconButton;

import java.util.ArrayList;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CalonMustahiqAdapter extends RecyclerView.Adapter<CalonMustahiqAdapter.ViewHolder> implements View.OnTouchListener, View.OnClickListener {

    public final ArrayList<CalonMustahiq> data;
    private final GestureDetector gestureDetector;
    private final PicassoLoader imageLoader;
    GradientDrawable bgShape = new GradientDrawable();
    private boolean isTablet = false;
    private String keyword_alamat;
    private Activity activity;
    private SparseBooleanArray mSelectedItemsIds;
    private int selected = -1;
    private OnCalonMustahiqItemClickListener OnCalonMustahiqItemClickListener;


    public CalonMustahiqAdapter(Activity activity, ArrayList<CalonMustahiq> calon_mustahiqList, boolean isTable) {
        this.activity = activity;
        this.data = calon_mustahiqList;
        mSelectedItemsIds = new SparseBooleanArray();
        gestureDetector = new GestureDetector(activity, new SingleTapConfirm());
        this.isTablet = isTable;
        imageLoader = new PicassoLoader();

    }

    public void setOnCalonMustahiqItemClickListener(OnCalonMustahiqItemClickListener onCalonMustahiqItemClickListener) {
        this.OnCalonMustahiqItemClickListener = onCalonMustahiqItemClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int viewId = v.getId();
        if (viewId == R.id.btn_action) {
            if (gestureDetector.onTouchEvent(event)) {
                if (OnCalonMustahiqItemClickListener != null) {
                    AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.playSoundEffect(SoundEffectConstants.CLICK);
                    OnCalonMustahiqItemClickListener.onActionClick(v, (Integer) v.getTag());
                }
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (OnCalonMustahiqItemClickListener != null) {
            OnCalonMustahiqItemClickListener.onRootClick(v, (Integer) v.getTag());
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
                .inflate(R.layout.item_mustahiq_list, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.rootParent.setOnClickListener(this);
        holder.btnAction.setOnTouchListener(this);
        return holder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.btnAction.setVisibility(View.GONE);
        CalonMustahiq calon_mustahiq = data.get(position);
        imageLoader.loadImage(holder.fotoProfil, calon_mustahiq.nama_calon_mustahiq, calon_mustahiq.nama_calon_mustahiq);
        holder.namaCalonMustahiq.setText("Nama : " + calon_mustahiq.nama_calon_mustahiq);
        holder.alamatCalonMustahiq.setText(Html.fromHtml("Alamat : " + (!TextUtils.isNullOrEmpty(keyword_alamat) && calon_mustahiq.alamat_calon_mustahiq.contains(keyword_alamat) ?
                calon_mustahiq.alamat_calon_mustahiq.replaceAll("(?i)" + keyword_alamat, "<font color='" + ContextCompat.getColor(activity, R.color.accent) + "'>" + keyword_alamat + "</font>") : calon_mustahiq.alamat_calon_mustahiq)));
        holder.noIdentitasCalonMustahiq.setText("No Identitas : " + (TextUtils.isNullOrEmpty(calon_mustahiq.no_identitas_calon_mustahiq) ? "-" : calon_mustahiq.no_identitas_calon_mustahiq));
        holder.noTelpCalonMustahiq.setText("No Telp : " + (TextUtils.isNullOrEmpty(calon_mustahiq.no_telp_calon_mustahiq) ? "-" : calon_mustahiq.no_telp_calon_mustahiq));
        holder.namaPerekomendasiCalonMustahiq.setText("Nama Perekomendasi: " + (TextUtils.isNullOrEmpty(calon_mustahiq.nama_perekomendasi_calon_mustahiq) ? "-" : calon_mustahiq.nama_perekomendasi_calon_mustahiq));
        holder.alasanPerekomendasiCalonMustahiq.setText("Alasan Perekomendasian: " + (TextUtils.isNullOrEmpty(calon_mustahiq.alasan_perekomendasi_calon_mustahiq) ? "-" : calon_mustahiq.alasan_perekomendasi_calon_mustahiq));
        holder.statusCalonMustahiq.setText(Html.fromHtml("Status Validasi : " + (calon_mustahiq.status_calon_mustahiq.equalsIgnoreCase("ya") ? "<font color='#002800'>Valid</font>" : "<font color='red'>Belum/Tidak Valid</font>")));
        holder.statusCalonMustahiq.setText(Html.fromHtml("Status Aktif : " + (calon_mustahiq.status_calon_mustahiq.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));
        holder.statusMustahiq.setVisibility(View.GONE);
        holder.namaAmilRestaurantIceCream.setVisibility(View.GONE);
        holder.waktuTerakhirDonasi.setVisibility(View.GONE);
        holder.statusCalonMustahiq.setVisibility(View.GONE);

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

    public interface OnCalonMustahiqItemClickListener {
        void onActionClick(View v, int position);

        void onRootClick(View v, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.alasan_perekomendasi_calon_mustahiq)
        RobotoLightTextView alasanPerekomendasiCalonMustahiq;
        @BindView(R.id.status_calon_mustahiq)
        RobotoLightTextView statusCalonMustahiq;
        @BindView(R.id.status_mustahiq)
        RobotoLightTextView statusMustahiq;
        @BindView(R.id.nama_amil_zakat)
        RobotoLightTextView namaAmilZakat;
        @BindView(R.id.waktu_terakhir_donasi)
        RobotoLightTextView waktuTerakhirDonasi;
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
