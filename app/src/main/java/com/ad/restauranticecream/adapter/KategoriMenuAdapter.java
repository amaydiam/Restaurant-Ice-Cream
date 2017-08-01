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
import android.widget.ImageView;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.KategoriMenu;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.widget.RobotoLightTextView;
import com.bumptech.glide.Glide;
import com.joanzapata.iconify.widget.IconButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KategoriMenuAdapter extends RecyclerView.Adapter<KategoriMenuAdapter.ViewHolder> implements View.OnTouchListener, View.OnClickListener {

    public final ArrayList<KategoriMenu> data;
    private final GestureDetector gestureDetector;

    private boolean isTablet = false;
    private String keyword_alamat;
    private Activity activity;
    private SparseBooleanArray mSelectedItemsIds;
    private int selected = -1;
    private OnKategoriMenuItemClickListener OnKategoriMenuItemClickListener;


    public KategoriMenuAdapter(Activity activity, ArrayList<KategoriMenu> mustahiqList, boolean isTable) {
        this.activity = activity;
        this.data = mustahiqList;
        mSelectedItemsIds = new SparseBooleanArray();
        gestureDetector = new GestureDetector(activity, new SingleTapConfirm());
        this.isTablet = isTable;
    }

    public void setOnKategoriMenuItemClickListener(OnKategoriMenuItemClickListener onKategoriMenuItemClickListener) {
        this.OnKategoriMenuItemClickListener = onKategoriMenuItemClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int viewId = v.getId();
        if (viewId == R.id.btn_action) {
            if (gestureDetector.onTouchEvent(event)) {
                if (OnKategoriMenuItemClickListener != null) {
                    AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.playSoundEffect(SoundEffectConstants.CLICK);
                    OnKategoriMenuItemClickListener.onActionClick(v, (Integer) v.getTag());
                }
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (OnKategoriMenuItemClickListener != null) {
            OnKategoriMenuItemClickListener.onRootClick(v, (Integer) v.getTag());
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
                .inflate(R.layout.item_kategori_menu_list, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.rootParent.setOnClickListener(this);
        holder.btnAction.setOnTouchListener(this);
        return holder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        KategoriMenu kategoriMenu = data.get(position);

        holder.namaKategoriMenu.setText(kategoriMenu.nama_kategori_menu);

        Glide.with(activity)
                .load(ApiHelper.getBaseUrl(activity) + "/" + kategoriMenu.gambar_kategori_menu)
                .asBitmap()
                .placeholder(R.drawable.default_placeholder)
                .centerCrop()
                .into(holder.gambarKategoriMenu);

        if (isTablet) {
            if (selected == position)
                holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_selected_background));
            else
                holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_background));
        } else {
            holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_background));

        }

        if (Prefs.getModeApp(activity) == RestaurantIceCream.MODE_PELAYAN)
            holder.btnAction.setVisibility(View.VISIBLE);
        else
            holder.btnAction.setVisibility(View.GONE);


        holder.rootParent.setTag(position);
        holder.btnAction.setTag(position);

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


    public interface OnKategoriMenuItemClickListener {
        void onActionClick(View v, int position);

        void onRootClick(View v, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.gambar_kategori_menu)
        ImageView gambarKategoriMenu;
        @BindView(R.id.nama_kategori_menu)
        RobotoLightTextView namaKategoriMenu;
        @BindView(R.id.root_parent)
        CardView rootParent;
        @BindView(R.id.btn_action)
        IconButton btnAction;

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
