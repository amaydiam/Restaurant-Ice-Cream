package com.ad.restauranticecream.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.model.PesanBaru;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.utils.SnackBar;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialCommunityIcons;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.orm.util.NamingHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ManagePesanBaruFragment extends DialogFragment {
    String title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nama_menu)
    TextView namaMenu;
    @BindView(R.id.jumlah_pesanan)
    RobotoRegularEditText jumlahPesanan;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    private SnackBar snackbar;
    private Unbinder butterKnife;

    private String val_jumlah_pesanan = "";

    private PesanBaru pesan_baru;
    private Dialog alertDialog;
    private AddEditPesanBaruListener callback;
    private String action;

    public ManagePesanBaruFragment() {

    }


    void Action(int id) {

        switch (id) {

            case Menus.SEND:
                getData();

                if (val_jumlah_pesanan.length() == 0) {
                    snackbar.show("Harap isi form...");
                    return;
                }

                if (action.equals("add")) {
                    PesanBaru pb = new PesanBaru(pesan_baru.id_menu, pesan_baru.nama_menu, pesan_baru.harga_menu, val_jumlah_pesanan);
                    pb.save();
                } else if (action.equals("edit")) {

                    List<PesanBaru> p = PesanBaru.find(PesanBaru.class,  NamingHelper.toSQLNameDefault("id_menu")+ "= ?", pesan_baru.id_menu);
                    PesanBaru z = p.get(0);
                    z.jumlah_pesanan = val_jumlah_pesanan; // modify the values
                    z.save();
                }
                dismiss();
                break;
            case Menus.DELETE:
                ConfirmDelete();
                break;
        }
    }

    private void getData() {
        val_jumlah_pesanan = jumlahPesanan.getText().toString();
    }

    private void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Anda yakin akan menghapus pesanan ini?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                List<PesanBaru> p = PesanBaru.find(PesanBaru.class,  NamingHelper.toSQLNameDefault("id_menu")+ "= ?", pesan_baru.id_menu);
                PesanBaru z = p.get(0);
                z.delete();
                dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (AddEditPesanBaruListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement PesanBaruListener");
        }
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setData(PesanBaru pesan_baru) {
        this.pesan_baru = pesan_baru;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_manage_pesan_baru, container);

        butterKnife = ButterKnife.bind(this, view);
        snackbar = new SnackBar(getActivity(), coordinatorLayout);
        toolbar.setTitle(title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Action(item.getItemId());
                return true;
            }
        });
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
        toolbar.inflateMenu(R.menu.menu_manage);
        Menu menu = toolbar.getMenu();
        MenuItem _send = menu.findItem(Menus.SEND);
        MenuItem _delete = menu.findItem(Menus.DELETE);
        _send.setIcon(
                new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_plus)
                        .colorRes(R.color.white)
                        .actionBarSize());
        _delete.setIcon(
                new IconDrawable(getActivity(), MaterialIcons.md_delete)
                        .colorRes(R.color.white)
                        .actionBarSize());


        namaMenu.setText(pesan_baru.nama_menu);
        if (action.equals("edit")) {
            toolbar.setSubtitle("Ubah");
            _delete.setVisible(true);

            val_jumlah_pesanan = pesan_baru.jumlah_pesanan;
            jumlahPesanan.setText(val_jumlah_pesanan);


        } else {
            toolbar.setSubtitle("Tambah");
            _delete.setVisible(false);
        }

        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    public interface AddEditPesanBaruListener {
        void onFinishEditPesanBaru(PesanBaru pesan_baru);

        void onFinishAddPesanBaru(PesanBaru pesan_baru);

        void onFinishDeletePesanBaru(PesanBaru pesan_baru);
    }

}