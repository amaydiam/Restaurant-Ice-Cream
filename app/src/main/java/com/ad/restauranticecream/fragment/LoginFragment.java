package com.ad.restauranticecream.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.utils.SnackBar;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.joanzapata.iconify.fonts.MaterialModule;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends DialogFragment implements CustomVolley.OnCallbackResponse, RegisterFragment.RegisterListener {
    private static final String TAG_LOGIN = "TAG_LOGIN";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.username)
    RobotoRegularEditText username;
    @BindView(R.id.password)
    RobotoRegularEditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private SnackBar snackbar;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private ProgressDialog dialogProgress;
    private Unbinder butterKnife;

    private String val_username = "";
    private String val_password = "";


    private LoginListener callback;

    public LoginFragment() {

    }


    @OnClick(R.id.register)
    void register() {
        FragmentManager fragmentManager = getChildFragmentManager();
        RegisterFragment register = new RegisterFragment();
        register.setTargetFragment(this, 0);
        register.show(fragmentManager, "Register");
        }

    @OnClick(R.id.login)
    void Login() {
        Utils.HideKeyboard(getActivity(), username);
        Utils.HideKeyboard(getActivity(), password);
        getData();

        if (val_username.length() == 0
                || val_password.length() == 0) {
            snackbar.show("Harap isi semua form...");
            return;
        }

        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put(RestaurantIceCream.username,
                val_username);
        jsonParams.put(RestaurantIceCream.password,
                val_password);

        queue = customVolley.Rest(Request.Method.POST, ApiHelper.getLoginLink(getActivity()), jsonParams, TAG_LOGIN);


    }

    private void getData() {

        val_username = username.getText().toString().trim();
        val_password = password.getText().toString().trim();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_LOGIN);
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
                JSONObject jsUser = new JSONObject(json.getString(RestaurantIceCream.user));
                String id_user = jsUser.getString(RestaurantIceCream.id_user);
                String tipe_user = jsUser.getString(RestaurantIceCream.tipe_user);
                String nama = jsUser.getString(RestaurantIceCream.nama);
                String alamat = jsUser.getString(RestaurantIceCream.alamat);
                String no_telp = jsUser.getString(RestaurantIceCream.no_telp);
                String no_identitas = jsUser.getString(RestaurantIceCream.no_identitas);
                callback.onFinishLogin(id_user,tipe_user,nama,alamat,no_telp,no_identitas);
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
            callback = (LoginListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement KonfirmasiPendaftaranPesertaListener");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_login, container);

        butterKnife = ButterKnife.bind(this, view);
        customVolley = new CustomVolley(getActivity());
        customVolley.setOnCallbackResponse(this);
        snackbar = new SnackBar(getActivity(), coordinatorLayout);
        toolbar.setTitle("Login");
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

    private void progress(boolean status) {
        if (status) {
            dialogProgress = ProgressDialog.show(getActivity(), "Submit",
                    "Harap menunggu...", true);
        } else {

            if (dialogProgress.isShowing())
                dialogProgress.dismiss();
        }
    }

    @Override
    public void onFinishRegister(String id_user, String tipe_user, String nama, String alamat, String no_telp, String no_identitas) {

        callback.onFinishLogin(id_user,tipe_user,nama,alamat,no_telp,no_identitas);
        dismiss();
    }


    public interface LoginListener {

        void onFinishLogin(String id_user, String tipe_user, String nama, String alamat, String no_telp, String no_identitas);
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