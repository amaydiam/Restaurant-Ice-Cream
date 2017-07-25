package com.ad.restauranticecream.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.Zakat;
import com.ad.restauranticecream.model.CalonMustahiq;
import com.ad.restauranticecream.model.ImageFile;
import com.ad.restauranticecream.model.PickLocation;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.utils.SnackBar;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ManageCalonMustahiqFragment extends DialogFragment implements CustomVolley.OnCallbackResponse {
    private static final String TAG_ADD = "TAG_ADD";
    private static final String TAG_EDIT = "TAG_EDIT";
    private static final String TAG_DELETE = "TAG_DELETE";
    String title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nama_calon_mustahiq)
    RobotoRegularEditText namaCalonMustahiq;
    @BindView(R.id.alamat_calon_mustahiq)
    RobotoRegularEditText alamatCalonMustahiq;
    @BindView(R.id.no_identitas_calon_mustahiq)
    RobotoRegularEditText noIdentitasCalonMustahiq;
    @BindView(R.id.no_telp_calon_mustahiq)
    RobotoRegularEditText noTelpCalonMustahiq;
    @BindView(R.id.alasan_perekomendasi_calon_mustahiq)
    RobotoRegularEditText alasanPerekomendasiCalonMustahiq;


    @BindView(R.id.caption_img_foto_1)
    RobotoRegularEditText captionImgFoto1;
    @BindView(R.id.caption_img_foto_2)
    RobotoRegularEditText captionImgFoto2;
    @BindView(R.id.caption_img_foto_3)
    RobotoRegularEditText captionImgFoto3;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.img_foto_1)
    ImageView imgFoto1;
    @BindView(R.id.img_foto_2)
    ImageView imgFoto2;
    @BindView(R.id.img_foto_3)
    ImageView imgFoto3;

    private int TYPE_IMG;
    private int PHOTO_1 = 1;
    private int PHOTO_2 = 2;
    private int PHOTO_3 = 3;
    private String val_server_photo_1;
    private String val_server_photo_2;
    private String val_server_photo_3;

    @OnClick(R.id.pick_location)
    void pickLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            getActivity().startActivityForResult(builder.build(getActivity()), RestaurantIceCream.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private File filePhoto1;
    private File filePhoto2;
    private File filePhoto3;

    @OnClick(R.id.foto_1)
    void PHOTO_1() {
        if (filePhoto1 != null) {
            OpenDialog(PHOTO_1);
        } else {
            getFoto(PHOTO_1);
        }
    }

    @OnClick(R.id.foto_2)
    void PHOTO_2() {
        if (filePhoto2 != null) {
            OpenDialog(PHOTO_2);
        } else {
            getFoto(PHOTO_2);
        }
    }

    @OnClick(R.id.foto_3)
    void PHOTO_3() {
        if (filePhoto3 != null) {
            OpenDialog(PHOTO_3);
        } else {
            getFoto(PHOTO_3);
        }
    }

    void OpenDialog(final int type) {
        final String[] option = new String[]{"View", "Change"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { // TODO Auto-generated method stub
                if (which == 0)
                    viewFoto(type);
                else if (which == 1)
                    getFoto(type);
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void viewFoto(int type) {
        FragmentManager ft = getChildFragmentManager();
        DialogViewSinggleImageFragment newFragment = DialogViewSinggleImageFragment.newInstance(type == PHOTO_1 ? filePhoto1.getAbsolutePath() : (type == PHOTO_2 ? filePhoto2.getAbsolutePath() : filePhoto3.getAbsolutePath()));
        newFragment.setTargetFragment(this, 0);
        newFragment.show(ft, "slideshow");
    }

    private void getFoto(int type) {

        TYPE_IMG = type;
        new TedPermission(getActivity())
                .setPermissionListener(permissionGetFotoListener)
                .setDeniedMessage(String.format(getString(R.string.upload_document_permission), type == PHOTO_1 ? "PHOTO 1" : (type == PHOTO_2 ? "PHOTO 2" : "PHOTO 3")))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }


    PermissionListener permissionGetFotoListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            EasyImage.openChooserWithGallery(getActivity(), getString(R.string.pick_source), 0);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            String message = String.format(Locale.getDefault(), getString(R.string.message_denied), "WRITE STORAGE EKSTERNAL");
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }


    };


    private SnackBar snackbar;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private ProgressDialog dialogProgress;
    private Unbinder butterKnife;

    private String val_id_calon_mustahiq;
    private String val_nama_calon_mustahiq = "";
    private String val_alamat_calon_mustahiq = "";
    private String val_no_identitas_calon_mustahiq = "";
    private String val_no_telp_calon_mustahiq = "";
    private String val_alasan_perekomendasi_calon_mustahiq = "";
    private String val_caption_img_foto_1 = "";
    private String val_caption_img_foto_2 = "";
    private String val_caption_img_foto_3 = "";

    private Double val_latitude_calon_mustahiq;
    private Double val_longitude_calon_mustahiq;


    private CalonMustahiq calonMustahiq;
    private Dialog alertDialog;
    private AddEditCalonMustahiqListener callback;
    private String action;

    public ManageCalonMustahiqFragment() {

    }

    void Action(int id) {
        Utils.HideKeyboard(getActivity(), namaCalonMustahiq);
        Utils.HideKeyboard(getActivity(), alamatCalonMustahiq);
        Utils.HideKeyboard(getActivity(), noIdentitasCalonMustahiq);
        Utils.HideKeyboard(getActivity(), noTelpCalonMustahiq);
        Utils.HideKeyboard(getActivity(), alasanPerekomendasiCalonMustahiq);
        Utils.HideKeyboard(getActivity(), captionImgFoto1);
        Utils.HideKeyboard(getActivity(), captionImgFoto2);
        Utils.HideKeyboard(getActivity(), captionImgFoto3);
        switch (id) {

            case Menus.SEND:
                getData();

                if (val_nama_calon_mustahiq.length() == 0
                        || val_alamat_calon_mustahiq.length() == 0
                        || val_no_identitas_calon_mustahiq.length() == 0
                        || val_no_telp_calon_mustahiq.length() == 0
                        || val_alasan_perekomendasi_calon_mustahiq.length() == 0) {
                    snackbar.show("Harap isi semua form...");
                    return;
                }
                if (val_latitude_calon_mustahiq == null
                        || val_longitude_calon_mustahiq == null) {
                    snackbar.show("Harap masukan kordinat lokasi...");
                    return;
                }

                boolean eror = false;
                if (val_server_photo_1 == null) {
                    eror = filePhoto1 == null;
                }
                if (val_server_photo_2 == null) {
                    eror = filePhoto2 == null;
                }
                if (val_server_photo_3 == null) {
                    eror = filePhoto3 == null;
                }

                if (eror) {
                    snackbar.show("Harap lampirkan 3 foto calon mustahiq...");
                    return;
                }

                if (val_caption_img_foto_1.length() == 0
                        || val_caption_img_foto_2.length() == 0
                        || val_caption_img_foto_3.length() == 0) {
                    snackbar.show("Harap lengkapi caption foto...");
                    return;
                }


                Map<String, String> jsonParams = new HashMap<>();

                jsonParams.put(RestaurantIceCream.nama_calon_mustahiq,
                        val_nama_calon_mustahiq);
                jsonParams.put(RestaurantIceCream.alamat_calon_mustahiq,
                        val_alamat_calon_mustahiq);
                jsonParams.put(RestaurantIceCream.latitude_calon_mustahiq,
                        String.valueOf(val_latitude_calon_mustahiq));
                jsonParams.put(RestaurantIceCream.longitude_calon_mustahiq,
                        String.valueOf(val_longitude_calon_mustahiq));
                jsonParams.put(RestaurantIceCream.no_identitas_calon_mustahiq,
                        val_no_identitas_calon_mustahiq);
                jsonParams.put(RestaurantIceCream.no_telp_calon_mustahiq,
                        val_no_telp_calon_mustahiq);
                jsonParams.put(RestaurantIceCream.alasan_perekomendasi_calon_mustahiq,
                        val_alasan_perekomendasi_calon_mustahiq);

                jsonParams.put(RestaurantIceCream.caption_photo_1,
                        val_caption_img_foto_1);

                jsonParams.put(RestaurantIceCream.caption_photo_2,
                        val_caption_img_foto_2);

                jsonParams.put(RestaurantIceCream.caption_photo_3,
                        val_caption_img_foto_3);

                jsonParams.put(RestaurantIceCream.id_user_perekomendasi,
                        Prefs.getIdUser(getActivity()));

                if (filePhoto1 != null) {
                    // process only post has valid image
                    Bitmap newsBitmap = BitmapFactory.decodeFile(filePhoto1.getAbsolutePath());
                    ByteArrayOutputStream imageBaOs = new ByteArrayOutputStream();
                    newsBitmap.compress(Bitmap.CompressFormat.JPEG, RestaurantIceCream.JPEG_OUTPUT_QUALITY, imageBaOs);
                    byte[] imageByteArrayNews = imageBaOs.toByteArray();

                    // process to transfrom from byteArray to base64
                    String fotoBase64 = Base64.encodeToString(imageByteArrayNews, Base64.DEFAULT);
                    jsonParams.put(RestaurantIceCream.photo_1, "data:image/jpg;base64," + fotoBase64);
                    Log.v("filePhoto1", "ada");
                }

                if (filePhoto2 != null) {
                    // process only post has valid image
                    Bitmap newsBitmap = BitmapFactory.decodeFile(filePhoto2.getAbsolutePath());
                    ByteArrayOutputStream imageBaOs = new ByteArrayOutputStream();
                    newsBitmap.compress(Bitmap.CompressFormat.JPEG, RestaurantIceCream.JPEG_OUTPUT_QUALITY, imageBaOs);
                    byte[] imageByteArrayNews = imageBaOs.toByteArray();

                    // process to transfrom from byteArray to base64
                    String fotoBase64 = Base64.encodeToString(imageByteArrayNews, Base64.DEFAULT);
                    jsonParams.put(RestaurantIceCream.photo_2, "data:image/jpg;base64," + fotoBase64);
                    Log.v("filePhoto2", "ada");
                }

                if (filePhoto3 != null) {
                    // process only post has valid image
                    Bitmap newsBitmap = BitmapFactory.decodeFile(filePhoto3.getAbsolutePath());
                    ByteArrayOutputStream imageBaOs = new ByteArrayOutputStream();
                    newsBitmap.compress(Bitmap.CompressFormat.JPEG, RestaurantIceCream.JPEG_OUTPUT_QUALITY, imageBaOs);
                    byte[] imageByteArrayNews = imageBaOs.toByteArray();

                    // process to transfrom from byteArray to base64
                    String fotoBase64 = Base64.encodeToString(imageByteArrayNews, Base64.DEFAULT);
                    jsonParams.put(RestaurantIceCream.photo_3, "data:image/jpg;base64," + fotoBase64);
                    Log.v("filePhoto3", "ada");
                }

                String TAG = null;
                if (action.equals("add")) {
                    TAG = TAG_ADD;
                } else if (action.equals("edit")) {
                    TAG = TAG_EDIT;
                    jsonParams.put(RestaurantIceCream.id_calon_mustahiq,
                            val_id_calon_mustahiq);

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

        val_nama_calon_mustahiq = namaCalonMustahiq.getText().toString().trim();
        val_alamat_calon_mustahiq = alamatCalonMustahiq.getText().toString().trim();
        val_no_identitas_calon_mustahiq = noIdentitasCalonMustahiq.getText().toString().trim();
        val_no_telp_calon_mustahiq = noTelpCalonMustahiq.getText().toString().trim();
        val_alasan_perekomendasi_calon_mustahiq = alasanPerekomendasiCalonMustahiq.getText().toString().trim();
        val_caption_img_foto_1 = captionImgFoto1.getText().toString().trim();
        val_caption_img_foto_2 = captionImgFoto2.getText().toString().trim();
        val_caption_img_foto_3 = captionImgFoto3.getText().toString().trim();


    }

    private void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Anda yakin akan menghapus calon mustahiq ini?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                queue = customVolley.Rest(Request.Method.GET, ApiHelper.getCalonMustahiqDeleteLink(getActivity(), val_id_calon_mustahiq), null, TAG_DELETE);

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
            if (Boolean.valueOf(res)) {
                if (!TAG.equals(TAG_DELETE)) {
                    JSONObject obj = new JSONObject(json.getString(RestaurantIceCream.calon_mustahiq));
                    String id_calon_mustahiq = obj.getString(RestaurantIceCream.id_calon_mustahiq);
                    String nama_calon_mustahiq = obj.getString(RestaurantIceCream.nama_calon_mustahiq);
                    String alamat_calon_mustahiq = obj.getString(RestaurantIceCream.alamat_calon_mustahiq);
                    String latitude_calon_mustahiq = obj.getString(RestaurantIceCream.latitude_calon_mustahiq);
                    String longitude_calon_mustahiq = obj.getString(RestaurantIceCream.longitude_calon_mustahiq);
                    String no_identitas_calon_mustahiq = obj.getString(RestaurantIceCream.no_identitas_calon_mustahiq);
                    String no_telp_calon_mustahiq = obj.getString(RestaurantIceCream.no_telp_calon_mustahiq);
                    String id_user_perekomendasi = obj
                            .getString(RestaurantIceCream.id_user_perekomendasi);
                    String alasan_perekomendasi_calon_mustahiq = obj
                            .getString(RestaurantIceCream.alasan_perekomendasi_calon_mustahiq);
                    String photo_1 = obj
                            .getString(RestaurantIceCream.photo_1);
                    String photo_2 = obj
                            .getString(RestaurantIceCream.photo_2);
                    String photo_3 = obj
                            .getString(RestaurantIceCream.photo_3);
                    String caption_photo_1 = obj
                            .getString(RestaurantIceCream.caption_photo_1);
                    String caption_photo_2 = obj
                            .getString(RestaurantIceCream.caption_photo_2);
                    String caption_photo_3 = obj
                            .getString(RestaurantIceCream.caption_photo_3);
                    String nama_perekomendasi_calon_mustahiq = obj
                            .getString(RestaurantIceCream.nama_perekomendasi_calon_mustahiq);
                    String status_calon_mustahiq = obj.getString(RestaurantIceCream.status_calon_mustahiq);

                    calonMustahiq = new CalonMustahiq(id_calon_mustahiq, nama_calon_mustahiq, alamat_calon_mustahiq, latitude_calon_mustahiq, longitude_calon_mustahiq, no_identitas_calon_mustahiq, no_telp_calon_mustahiq, id_user_perekomendasi, alasan_perekomendasi_calon_mustahiq, photo_1, photo_2, photo_3, caption_photo_1, caption_photo_2, caption_photo_3, nama_perekomendasi_calon_mustahiq, status_calon_mustahiq);
                    if (TAG.equals(TAG_ADD)) {
                        callback.onFinishAddCalonMustahiq(calonMustahiq);
                    } else if (TAG.equals(TAG_EDIT)) {
                        callback.onFinishEditCalonMustahiq(calonMustahiq);
                    }
                } else {
                    callback.onFinishDeleteCalonMustahiq(calonMustahiq);
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
        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule());
        super.onCreate(savedInstanceState);

        try {
            callback = (AddEditCalonMustahiqListener) getTargetFragment();
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

    public void setData(CalonMustahiq calon_mustahiq) {
        this.calonMustahiq = calon_mustahiq;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_manage_calon_mustahiq, container);

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

        // Spinner on item click listener

        if (action.equals("edit")) {
            toolbar.setSubtitle("Ubah");
            _delete.setVisible(true);


            val_id_calon_mustahiq = calonMustahiq.id_calon_mustahiq;
            val_nama_calon_mustahiq = calonMustahiq.nama_calon_mustahiq;
            val_alamat_calon_mustahiq = calonMustahiq.alamat_calon_mustahiq;
            val_no_identitas_calon_mustahiq = calonMustahiq.no_identitas_calon_mustahiq;
            val_no_telp_calon_mustahiq = calonMustahiq.no_telp_calon_mustahiq;
            val_alasan_perekomendasi_calon_mustahiq = calonMustahiq.alasan_perekomendasi_calon_mustahiq;

            val_caption_img_foto_1 = calonMustahiq.caption_photo_1;
            val_caption_img_foto_2 = calonMustahiq.caption_photo_2;
            val_caption_img_foto_3 = calonMustahiq.caption_photo_3;

            val_latitude_calon_mustahiq = Double.valueOf(calonMustahiq.latitude_calon_mustahiq);
            val_longitude_calon_mustahiq = Double.valueOf(calonMustahiq.longitude_calon_mustahiq);

            val_server_photo_1 = calonMustahiq.photo_1;
            val_server_photo_2 = calonMustahiq.photo_2;
            val_server_photo_3 = calonMustahiq.photo_3;

            namaCalonMustahiq.setText(val_nama_calon_mustahiq);
            alamatCalonMustahiq.setText(val_alamat_calon_mustahiq);
            noIdentitasCalonMustahiq.setText(val_no_identitas_calon_mustahiq);
            noTelpCalonMustahiq.setText(val_no_telp_calon_mustahiq);
            alasanPerekomendasiCalonMustahiq.setText(val_alasan_perekomendasi_calon_mustahiq);
            captionImgFoto1.setText(val_caption_img_foto_1);
            captionImgFoto2.setText(val_caption_img_foto_2);
            captionImgFoto3.setText(val_caption_img_foto_3);


        } else {
            toolbar.setSubtitle("Tambah");
            _delete.setVisible(false);
        }

        checkPhoto1();
        checkPhoto2();
        checkPhoto3();

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


    public interface AddEditCalonMustahiqListener {
        void onFinishEditCalonMustahiq(CalonMustahiq calon_mustahiq);

        void onFinishAddCalonMustahiq(CalonMustahiq calon_mustahiq);

        void onFinishDeleteCalonMustahiq(CalonMustahiq calon_mustahiq);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPickLocation(PickLocation cp) {
        alamatCalonMustahiq.setText(cp.getAddress());
        val_latitude_calon_mustahiq = cp.getLatitude();
        val_longitude_calon_mustahiq = cp.getLongitude();

        PickLocation stickyEvent = EventBus.getDefault().getStickyEvent(PickLocation.class);
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


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onImageFile(ImageFile cp) {
        setImage(cp.getImageFile());

        ImageFile stickyEvent = EventBus.getDefault().getStickyEvent(ImageFile.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    private void setImage(File imageFile) {
        if (TYPE_IMG == PHOTO_1) {
            filePhoto1 = imageFile;
            checkPhoto1();
        } else if (TYPE_IMG == PHOTO_2) {
            filePhoto2 = imageFile;
            checkPhoto2();
        } else {
            filePhoto3 = imageFile;
            checkPhoto3();
        }

    }


    private void checkPhoto1() {

        if (val_server_photo_1 != null) {
            Glide.with(this)
                    .load(ApiHelper.getBaseUrl(getActivity()) + "/" + val_server_photo_1)
                    .asBitmap()
                    .override(200, 200)
                    .centerCrop()
                    .into(imgFoto1);

        } else {
            Picasso.with(getActivity())
                    .load(R.drawable.default_placeholder)
                    .resize(200, 200)
                    .centerCrop()
                    .into(imgFoto1);
        }

        if (filePhoto1 != null) {
            Glide.with(this)
                    .load(filePhoto1)
                    .asBitmap()
                    .override(200, 200)
                    .centerCrop()
                    .into(imgFoto1);
        }

    }

    private void checkPhoto2() {
        if (val_server_photo_2 != null) {
            Glide.with(this)
                    .load(ApiHelper.getBaseUrl(getActivity()) + "/" + val_server_photo_2)
                    .asBitmap()
                    .override(200, 200)
                    .centerCrop()
                    .into(imgFoto2);
        } else {

            Picasso.with(getActivity())
                    .load(R.drawable.default_placeholder)
                    .resize(200, 200)
                    .centerCrop()
                    .into(imgFoto2);
        }

        if (filePhoto2 != null) {

            Picasso.with(getActivity())
                    .load(filePhoto2)
                    .resize(200, 200)
                    .centerCrop()
                    .into(imgFoto2);
        }

    }

    private void checkPhoto3() {
        if (val_server_photo_3 != null) {
            Glide.with(this)
                    .load(ApiHelper.getBaseUrl(getActivity()) + "/" + val_server_photo_3)
                    .asBitmap()
                    .override(200, 200)
                    .centerCrop()
                    .into(imgFoto3);
        } else {

            Picasso.with(getActivity())
                    .load(R.drawable.default_placeholder)
                    .resize(200, 200)
                    .centerCrop()
                    .into(imgFoto3);
        }
        if (filePhoto3 != null) {

            Picasso.with(getActivity())
                    .load(filePhoto3)
                    .resize(200, 200)
                    .centerCrop()
                    .into(imgFoto3);
        }

    }


}