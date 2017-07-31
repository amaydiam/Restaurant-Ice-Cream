package com.ad.restauranticecream.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.utils.SnackBar;
import com.ad.restauranticecream.utils.Utils;
import com.ad.restauranticecream.widget.RobotoRegularEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SetUrlFragment extends DialogFragment implements CustomVolley.OnCallbackResponse {
    private static final String TAG_TES = "TAG_TES";
    String title;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.url)
    RobotoRegularEditText url;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private SnackBar snackbar;
    private Unbinder butterKnife;

    private String val_url = "";
    private CustomVolley customVolley;
    private RequestQueue queue;

    public SetUrlFragment() {

    }

    void Action(int id) {
        Utils.HideKeyboard(getActivity(), url);
        switch (id) {

            case Menus.SEND:
                val_url = url.getText().toString().trim();

                if (val_url.length() == 0) {
                    snackbar.show("Harap isi form...");
                    return;
                }
                queue = customVolley.Rest(Request.Method.GET, ApiHelper.getTesUrl(getActivity(), val_url.replace("/", "").replace("http", "").replace(":", "")), null, TAG_TES);
                break;
            case Menus.DELETE:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_TES);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setDialogTitle(String title) {
        this.title = title;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_set_url, container);

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

        toolbar.setSubtitle("Set URL");
        _delete.setVisible(false);

        if (Prefs.getUrl(getActivity()) != null)
            url.setText(Prefs.getUrl(getActivity()).replace("http://", ""));

        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }


    @Override
    public void onVolleyStart(String TAG) {

    }

    @Override
    public void onVolleyEnd(String TAG) {

    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {
        try {

            JSONObject json = new JSONObject(response);
            String res = json.getString(RestaurantIceCream.isSuccess);
            if (TAG.equals(TAG_TES)) {
                Prefs.putUrl(getActivity(), "http://" + val_url.replace("/", "").replace("http", "").replace(":", ""));
            }
            dismiss();
        } catch (Exception e) {
            snackbar.show("error parsing data");
            Log.v("error", e.getMessage());
        }

    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {
        snackbar.show("Cek Koneksi Anda atau Apakah XAMPP Anda telah aktif?");
    }
}