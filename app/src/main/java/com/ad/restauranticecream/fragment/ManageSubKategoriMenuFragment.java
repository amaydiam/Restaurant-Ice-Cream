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
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.ImageFile;
import com.ad.restauranticecream.model.SubKategoriMenu;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.utils.SnackBar;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
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

public class ManageSubKategoriMenuFragment extends DialogFragment implements CustomVolley.OnCallbackResponse {
    private static final String TAG_ADD = "TAG_ADD";
    private static final String TAG_EDIT = "TAG_EDIT";
    private static final String TAG_DELETE = "TAG_DELETE";
    String title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nama_sub_kategori_menu)
    RobotoRegularEditText namaSubKategoriMenu;
    @BindView(R.id.img_gambar_sub_kategori_menu)
    ImageView imgGambarSubKategoriMenu;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    private String val_server_gambar_sub_kategori_menu;

    private File fileGambarSubKategoriMenu;
    private String idKategoriMenu;

    @OnClick(R.id.gambar_sub_kategori_menu)
    void GAMBAR_SUB_KATEGORI_MENU() {
        if (fileGambarSubKategoriMenu != null) {
            OpenDialog();
        } else {
            getFoto();
        }
    }


    void OpenDialog() {
        final String[] option = new String[]{"View", "Change"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { // TODO Auto-generated method stub
                if (which == 0)
                    viewFoto();
                else if (which == 1)
                    getFoto();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void viewFoto() {
        FragmentManager ft = getChildFragmentManager();
        DialogViewSinggleImageFragment newFragment = DialogViewSinggleImageFragment.newInstance(fileGambarSubKategoriMenu.getAbsolutePath());
        newFragment.setTargetFragment(this, 0);
        newFragment.show(ft, "slideshow");
    }

    private void getFoto() {

        new TedPermission(getActivity())
                .setPermissionListener(permissionGetFotoListener)
                .setDeniedMessage(String.format(getString(R.string.upload_document_permission), "PHOTO SUB_KATEGORI MENU"))
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

    private String val_id_sub_kategori_menu;
    private String val_nama_sub_kategori_menu = "";


    private SubKategoriMenu sub_kategoriMenu;
    private Dialog alertDialog;
    private AddEditSubKategoriMenuListener callback;
    private String action;

    public ManageSubKategoriMenuFragment() {

    }

    void Action(int id) {
        Utils.HideKeyboard(getActivity(), namaSubKategoriMenu);
        switch (id) {

            case Menus.SEND:
                getData();

                if (val_nama_sub_kategori_menu.length() == 00) {
                    snackbar.show("Harap isi semua form...");
                    return;
                }

                boolean eror = false;
                if (val_server_gambar_sub_kategori_menu == null) {
                    eror = fileGambarSubKategoriMenu == null;
                }

                if (eror) {
                    snackbar.show("Harap lampirkan  foto sub_kategori menu...");
                    return;
                }


                Map<String, String> jsonParams = new HashMap<>();

                jsonParams.put(RestaurantIceCream.id_kategori_menu,
                        idKategoriMenu);
                jsonParams.put(RestaurantIceCream.nama_sub_kategori_menu,
                        val_nama_sub_kategori_menu);

                if (fileGambarSubKategoriMenu != null) {
                    // process only post has valid image
                    Bitmap newsBitmap = BitmapFactory.decodeFile(fileGambarSubKategoriMenu.getAbsolutePath());
                    ByteArrayOutputStream imageBaOs = new ByteArrayOutputStream();
                    newsBitmap.compress(Bitmap.CompressFormat.JPEG, RestaurantIceCream.JPEG_OUTPUT_QUALITY, imageBaOs);
                    byte[] imageByteArrayNews = imageBaOs.toByteArray();

                    // process to transfrom from byteArray to base64
                    String fotoBase64 = Base64.encodeToString(imageByteArrayNews, Base64.DEFAULT);
                    jsonParams.put(RestaurantIceCream.gambar_sub_kategori_menu, "data:image/jpg;base64," + fotoBase64);
                    Log.v("fileGambarSubKategoriMenu", "ada");
                }

                String TAG = null;
                if (action.equals("add")) {
                    TAG = TAG_ADD;
                } else if (action.equals("edit")) {
                    TAG = TAG_EDIT;
                    jsonParams.put(RestaurantIceCream.id_sub_kategori_menu,
                            val_id_sub_kategori_menu);

                }

                queue = customVolley.Rest(Request.Method.POST, ApiHelper.getSubKategoriMenuAddEditLink(getActivity()), jsonParams, TAG);

                //  dismiss();
                break;
            case Menus.DELETE:
                ConfirmDelete();
                break;
        }
    }

    private void getData() {

        val_nama_sub_kategori_menu = namaSubKategoriMenu.getText().toString().trim();


    }

    private void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Anda yakin akan menghapus sub kategori ini?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                queue = customVolley.Rest(Request.Method.GET, ApiHelper.getSubKategoriMenuDeleteLink(getActivity(), val_id_sub_kategori_menu), null, TAG_DELETE);

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
                    JSONObject obj = new JSONObject(json.getString(RestaurantIceCream.sub_kategori_menu));
                    String id_sub_kategori_menu = obj.getString(RestaurantIceCream.id_sub_kategori_menu);
                    String id_kategori_menu = obj.getString(RestaurantIceCream.id_kategori_menu);
                    String nama_sub_kategori_menu = obj.getString(RestaurantIceCream.nama_sub_kategori_menu);
                    String gambar_sub_kategori_menu = obj.getString(RestaurantIceCream.gambar_sub_kategori_menu);

                    sub_kategoriMenu = new SubKategoriMenu(id_sub_kategori_menu, id_kategori_menu, nama_sub_kategori_menu, gambar_sub_kategori_menu);
                    if (TAG.equals(TAG_ADD)) {
                        callback.onFinishAddSubKategoriMenu(sub_kategoriMenu);
                    } else if (TAG.equals(TAG_EDIT)) {
                        callback.onFinishEditSubKategoriMenu(sub_kategoriMenu);
                    }
                } else {
                    callback.onFinishDeleteSubKategoriMenu(sub_kategoriMenu);
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
            callback = (AddEditSubKategoriMenuListener) getTargetFragment();
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

    public void setData(SubKategoriMenu sub_kategori_menu) {
        this.sub_kategoriMenu = sub_kategori_menu;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_manage_sub_kategori_menu, container);

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


            val_id_sub_kategori_menu = sub_kategoriMenu.id_sub_kategori_menu;
            val_nama_sub_kategori_menu = sub_kategoriMenu.nama_sub_kategori_menu;
            val_server_gambar_sub_kategori_menu = sub_kategoriMenu.gambar_sub_kategori_menu;

            namaSubKategoriMenu.setText(val_nama_sub_kategori_menu);


        } else {
            toolbar.setSubtitle("Tambah");
            _delete.setVisible(false);
        }

        checkGambarSubKategoriMenu();

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

    public void setIdKategoriMenu(String idKategoriMenu) {
        this.idKategoriMenu = idKategoriMenu;
    }


    public interface AddEditSubKategoriMenuListener {
        void onFinishEditSubKategoriMenu(SubKategoriMenu sub_kategori_menu);

        void onFinishAddSubKategoriMenu(SubKategoriMenu sub_kategori_menu);

        void onFinishDeleteSubKategoriMenu(SubKategoriMenu sub_kategori_menu);
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
        fileGambarSubKategoriMenu = imageFile;
        checkGambarSubKategoriMenu();
    }


    private void checkGambarSubKategoriMenu() {

        if (val_server_gambar_sub_kategori_menu != null) {
            Glide.with(this)
                    .load(ApiHelper.getBaseUrl(getActivity()) + "/" + val_server_gambar_sub_kategori_menu)
                    .asBitmap()
                    .override(200, 200)
                    .centerCrop()
                    .into(imgGambarSubKategoriMenu);

        } else {
            Picasso.with(getActivity())
                    .load(R.drawable.default_placeholder)
                    .resize(200, 200)
                    .centerCrop()
                    .into(imgGambarSubKategoriMenu);
        }

        if (fileGambarSubKategoriMenu != null) {
            Glide.with(this)
                    .load(fileGambarSubKategoriMenu)
                    .asBitmap()
                    .override(200, 200)
                    .centerCrop()
                    .into(imgGambarSubKategoriMenu);
        }

    }


}