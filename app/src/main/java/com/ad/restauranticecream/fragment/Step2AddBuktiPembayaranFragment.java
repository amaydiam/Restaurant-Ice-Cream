package com.ad.restauranticecream.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.activity.ActionDonasiBaruActivity;
import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.aprilapps.easyphotopicker.EasyImage;


public class Step2AddBuktiPembayaranFragment extends Fragment {


    public  String s_foto_bukti_pembayaran;
    @BindView(R.id.foto_bukti_pembayaran)
    ImageView fotoBuktiPembayaran;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.btn_save)
    Button btnSave;

    @OnClick(R.id.btn_save)
    void Save() {
        if(s_foto_bukti_pembayaran==null)
            Toast.makeText(activity, "Harap lampirkan foto pembayaran", Toast.LENGTH_SHORT).show();
        else
        activity.simpanData();
    }

    @OnClick(R.id.btn_upload)
    void Upload() {

        new TedPermission(getActivity())
                .setPermissionListener(permissionGetFotoListener)
                .setDeniedMessage("Jika kamu menolak permission,kamu tidak dapat mengambil foto \nHarap hidupkan permission WRITE EXTERNAL STORAGE dan CAMERA pada [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }


    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;


    private ActionDonasiBaruActivity activity;
    private Unbinder butterknife;

    public Step2AddBuktiPembayaranFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActionDonasiBaruActivity) {
            activity = (ActionDonasiBaruActivity) context;
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public Step2AddBuktiPembayaranFragment newInstance() {
        return new Step2AddBuktiPembayaranFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.form_step_2_donasi, container, false);
        butterknife = ButterKnife.bind(this, rootView);
        setInitView();
        return rootView;
    }

    private void setInitView() {


        SetData();
    }

    private void SetData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterknife.unbind();

    }


    PermissionListener permissionGetFotoListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            EasyImage.openChooserWithGallery(getActivity(), "Pick source", 0);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            String message = String.format(Locale.getDefault(), getString(R.string.message_denied), "Ambil Foto");
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }


    };

    public void setFotoBuktiPembayaran(File imageFile) {
        Glide.with(this).load(imageFile).asBitmap().placeholder(R.drawable.default_placeholder).into(fotoBuktiPembayaran);
        s_foto_bukti_pembayaran = imageFile.getAbsolutePath();
    }
}
