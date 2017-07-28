package com.ad.restauranticecream.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.utils.SnackBar;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.joanzapata.iconify.fonts.MaterialModule;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NamaPemesanBaruFragment extends DialogFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nama_pemesan_baru)
    RobotoRegularEditText namaPemesanBaru;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private SnackBar snackbar;
    private Unbinder butterKnife;

    private String val_nama_pemesan_baru = "";


    private NamaPemesanBaruListener callback;

    public NamaPemesanBaruFragment() {

    }


    @OnClick(R.id.btn_selanjutnya)
    void NamaPemesanBaru() {
        Utils.HideKeyboard(getActivity(), namaPemesanBaru);
        getData();

        if (val_nama_pemesan_baru.length() == 0) {
            snackbar.show("Harap masukan nama...");
            return;
        }

        callback.onFinishNamaPemesanBaru(val_nama_pemesan_baru);
        dismiss();

    }

    private void getData() {
        val_nama_pemesan_baru = namaPemesanBaru.getText().toString().trim();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule());
        super.onCreate(savedInstanceState);

        try {
            callback = (NamaPemesanBaruListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement KonfirmasiPendaftaranPesertaListener");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_nama_pemesan_baru, container);

        butterKnife = ButterKnife.bind(this, view);
        snackbar = new SnackBar(getActivity(), coordinatorLayout);
        toolbar.setTitle("Master Password");
        toolbar.setNavigationIcon(
                new IconDrawable(getActivity(), MaterialIcons.md_close)
                        .colorRes(R.color.white)
                        .actionBarSize());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }


    public interface NamaPemesanBaruListener {

        void onFinishNamaPemesanBaru(String nama_pemesan_baru);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}