package com.ad.restauranticecream.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.Mustahiq;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.utils.SnackBar;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ManageDonasiFragment extends DialogFragment implements CustomVolley.OnCallbackResponse {
    private static final String TAG_ADD = "TAG_ADD";
    private static final String TAG_EDIT = "TAG_EDIT";
    private static final String TAG_DELETE = "TAG_DELETE";
    String title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nama_calon_mustahiq)
    RobotoRegularEditText namaMustahiq;
    @BindView(R.id.alamat_calon_mustahiq)
    RobotoRegularEditText alamatMustahiq;
    @BindView(R.id.no_identitas_calon_mustahiq)
    RobotoRegularEditText noIdentitasMustahiq;
    @BindView(R.id.no_telp_calon_mustahiq)
    RobotoRegularEditText noTelpMustahiq;
    @BindView(R.id.status_calon_mustahiq)
    RadioGroup statusMustahiq;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    @BindView(R.id.aktif)
    RadioButton aktif;
    @BindView(R.id.tidak_aktif)
    RadioButton tidakAktif;

    private SnackBar snackbar;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private ProgressDialog dialogProgress;
    private Unbinder butterKnife;

    private String val_id_mustahiq;
    private String val_nama_calon_mustahiq = "";
    private String val_alamat_calon_mustahiq = "";
    private String val_no_identitas_calon_mustahiq = "";
    private String val_no_telp_calon_mustahiq = "";

    private String val_status_calon_mustahiq = "";

    private Mustahiq mustahiq;
    private Dialog alertDialog;
    private AddEditDonasiListener callback;
    private String action;

    public ManageDonasiFragment() {

    }

    void Action(int id) {
        Utils.HideKeyboard(getActivity(), namaMustahiq);
        Utils.HideKeyboard(getActivity(), alamatMustahiq);
        Utils.HideKeyboard(getActivity(), noIdentitasMustahiq);
        Utils.HideKeyboard(getActivity(), noTelpMustahiq);
        switch (id) {

            case Menus.SEND:
                getData();

                if (val_nama_calon_mustahiq.length() == 0
                        || val_alamat_calon_mustahiq.length() == 0
                        || val_no_identitas_calon_mustahiq.length() == 0
                        || val_no_telp_calon_mustahiq.length() == 0) {
                    snackbar.show("Harap isi semua form...");
                    return;
                }

                Map<String, String> jsonParams = new HashMap<>();

                jsonParams.put(RestaurantIceCream.nama_calon_mustahiq,
                        val_nama_calon_mustahiq);
                jsonParams.put(RestaurantIceCream.alamat_calon_mustahiq,
                        val_alamat_calon_mustahiq);
                jsonParams.put(RestaurantIceCream.no_identitas_calon_mustahiq,
                        val_no_identitas_calon_mustahiq);
                jsonParams.put(RestaurantIceCream.no_telp_calon_mustahiq,
                        val_no_telp_calon_mustahiq);
                jsonParams.put(RestaurantIceCream.status_calon_mustahiq,
                        val_status_calon_mustahiq);

                String TAG = null;
                if (action.equals("add")) {
                    TAG = TAG_ADD;
                } else if (action.equals("edit")) {
                    TAG = TAG_EDIT;
                    jsonParams.put(RestaurantIceCream.id_calon_mustahiq,
                            val_id_mustahiq);

                }

                queue = customVolley.Rest(Request.Method.POST, ApiHelper.getCalonMustahiqAddEditLink(getActivity()), jsonParams, TAG);

                //  dismiss();
                break;
            case Menus.DELETE:
                ConfirmDelete();
                break;
        }
    }

    private void getData() {

        val_nama_calon_mustahiq = namaMustahiq.getText().toString().trim();
        val_alamat_calon_mustahiq = alamatMustahiq.getText().toString().trim();
        val_no_identitas_calon_mustahiq = noIdentitasMustahiq.getText().toString().trim();
        val_no_telp_calon_mustahiq = noTelpMustahiq.getText().toString().trim();


        if (statusMustahiq.getCheckedRadioButtonId() == R.id.aktif)
            val_status_calon_mustahiq = "Aktif";
        else
            val_status_calon_mustahiq = "Tidak Aktif";

    }

    private void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Anda yakin akan menghapus calon_mustahiq ini?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                queue = customVolley.Rest(Request.Method.GET, ApiHelper.getCalonMustahiqDeleteLink(getActivity(), val_id_mustahiq), null, TAG_DELETE);

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
        if (queue != null) {
            queue.cancelAll(TAG_ADD);
            queue.cancelAll(TAG_DELETE);
            queue.cancelAll(TAG_EDIT);
        }

    }

    @Override
    public void onVolleyStart(String TAG) {
        progress(true);
    }

    @Override
    public void onVolleyEnd(String TAG) {
        progress(false);
    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {

        try {

            JSONObject json = new JSONObject(response);
            String res = json.getString(RestaurantIceCream.isSuccess);
            String message = json.getString(RestaurantIceCream.message);
            String s_mustahiq = json.getString(RestaurantIceCream.calon_mustahiq);
            if (Boolean.valueOf(res)) {
                if (!TAG.equals(TAG_DELETE)) {

                    JSONObject obj = json.getJSONObject(s_mustahiq);
                    String id_mustahiq = obj.getString(RestaurantIceCream.id_mustahiq);
                    String id_calon_mustahiq = obj.getString(RestaurantIceCream.id_calon_mustahiq);
                    String nama_calon_mustahiq = obj.getString(RestaurantIceCream.nama_calon_mustahiq);
                    String alamat_calon_mustahiq = obj.getString(RestaurantIceCream.alamat_calon_mustahiq);
                    String latitude_calon_mustahiq = obj.getString(RestaurantIceCream.latitude_calon_mustahiq);
                    String longitude_calon_mustahiq = obj.getString(RestaurantIceCream.longitude_calon_mustahiq);
                    String no_identitas_calon_mustahiq = obj.getString(RestaurantIceCream.no_identitas_calon_mustahiq);
                    String no_telp_calon_mustahiq = obj.getString(RestaurantIceCream.no_telp_calon_mustahiq);
                    String nama_perekomendasi_calon_mustahiq = obj.getString(RestaurantIceCream.nama_perekomendasi_calon_mustahiq);
                    String alasan_perekomendasi_calon_mustahiq = obj.getString(RestaurantIceCream.alasan_perekomendasi_calon_mustahiq);
                    String status_mustahiq = obj.getString(RestaurantIceCream.status_mustahiq);
                    String jumlah_rating = obj.getString(RestaurantIceCream.jumlah_rating);
                    String id_amil_zakat = obj.getString(RestaurantIceCream.id_amil_zakat);
                    String nama_amil_zakat = obj.getString(RestaurantIceCream.nama_amil_zakat);
                    String waktu_terakhir_donasi = obj.getString(RestaurantIceCream.waktu_terakhir_donasi);

                    mustahiq = new Mustahiq(id_mustahiq, id_calon_mustahiq, nama_calon_mustahiq, alamat_calon_mustahiq,
                            latitude_calon_mustahiq,
                            longitude_calon_mustahiq,
                            no_identitas_calon_mustahiq,
                            no_telp_calon_mustahiq,
                            nama_perekomendasi_calon_mustahiq,
                            alasan_perekomendasi_calon_mustahiq,
                            status_mustahiq,jumlah_rating, id_amil_zakat, nama_amil_zakat, waktu_terakhir_donasi);
                    if (TAG.equals(TAG_ADD)) {
                        callback.onFinishAddDonasi(mustahiq);
                    } else if (TAG.equals(TAG_EDIT)) {
                        callback.onFinishEditDonasi(mustahiq);
                    }
                } else {
                    callback.onFinishDeleteDonasi(mustahiq);
                }
                dismiss();
                snackbar.show(message);
            } else {
                snackbar.show(message);
            }
        } catch (Exception e) {
            snackbar.show("error parsing data");
            Log.v("error", e.getMessage());
        }
    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {
        snackbar.show(response);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (AddEditDonasiListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement KonfirmasiPendaftaranPesertaListener");
        }
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setData(Mustahiq mustahiq) {
        this.mustahiq = mustahiq;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_manage_mustahiq, container);

        butterKnife = ButterKnife.bind(this, view);
        customVolley = new CustomVolley(getActivity());
        customVolley.setOnCallbackResponse(this);
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
                new IconDrawable(getActivity(), MaterialIcons.md_send)
                        .colorRes(R.color.white)
                        .actionBarSize());
        _delete.setIcon(
                new IconDrawable(getActivity(), MaterialIcons.md_delete)
                        .colorRes(R.color.white)
                        .actionBarSize());

        if (action.equals("edit")) {
            toolbar.setSubtitle("Ubah");
            _delete.setVisible(true);


            val_id_mustahiq = mustahiq.id_mustahiq;
            val_nama_calon_mustahiq = mustahiq.nama_calon_mustahiq;
            val_alamat_calon_mustahiq = mustahiq.alamat_calon_mustahiq;
            val_no_identitas_calon_mustahiq = mustahiq.no_identitas_calon_mustahiq;
            val_no_telp_calon_mustahiq = mustahiq.no_telp_calon_mustahiq;
            val_status_calon_mustahiq = mustahiq.status_mustahiq;

            namaMustahiq.setText(val_nama_calon_mustahiq);
            alamatMustahiq.setText(val_alamat_calon_mustahiq);
            noIdentitasMustahiq.setText(val_no_identitas_calon_mustahiq);
            noTelpMustahiq.setText(val_no_telp_calon_mustahiq);

            if (val_status_calon_mustahiq.equalsIgnoreCase("Aktif"))
                aktif.setChecked(true);
            else
                tidakAktif.setChecked(true);

        } else {
            toolbar.setSubtitle("Tambah");
            _delete.setVisible(false);
        }

        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    private void progress(boolean status) {
        if (status) {
            dialogProgress = ProgressDialog.show(getActivity(), "Submit",
                    "Harap menunggu...", true);
        } else {

            if (dialogProgress.isShowing())
                dialogProgress.dismiss();
        }
    }


    public interface AddEditDonasiListener {
        void onFinishEditDonasi(Mustahiq mustahiq);

        void onFinishAddDonasi(Mustahiq mustahiq);

        void onFinishDeleteDonasi(Mustahiq mustahiq);
    }


}