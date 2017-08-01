package com.ad.restauranticecream.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.Meja;
import com.ad.restauranticecream.model.Refresh;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Prefs;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.onesignal.OneSignal;
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFrament#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFrament extends Fragment implements MasterPasswordFragment.MasterPasswordListener, CustomVolley.OnCallbackResponse, DialogPickMejaFragment.PickMejaListener {

    private static final String TAG_LOG_MODE = "TAG_LOG_MODE";
    Unbinder unbinder;
    private int MODE;
    private CustomVolley customVolley;
    private ProgressDialog dialogProgress;
    private RequestQueue queue;

    public HomeFrament() {
        // Required empty public constructor
    }

    public static HomeFrament newInstance() {
        HomeFrament fragment = new HomeFrament();
        return fragment;
    }

    @OnClick({R.id.btn_mode_pelanggan, R.id.btn_mode_pelayan, R.id.btn_mode_kasir})
    void ClickMode(View v) {

        FragmentManager fragmentManager = getChildFragmentManager();
        MasterPasswordFragment mp = new MasterPasswordFragment();
        int id = v.getId();
        switch (id) {
            case R.id.btn_mode_pelanggan:
                MODE = RestaurantIceCream.MODE_PELANGGAN;
                DialogPickMejaFragment dialogPickMejaFragment = new DialogPickMejaFragment();
                dialogPickMejaFragment.setTargetFragment(this, 0);
                dialogPickMejaFragment.show(fragmentManager, "pm");
                break;
            case R.id.btn_mode_pelayan:
                MODE = RestaurantIceCream.MODE_PELAYAN;
                mp.setTargetFragment(this, 0);
                mp.show(fragmentManager, "mp");
                break;
            case R.id.btn_mode_kasir:
                MODE = RestaurantIceCream.MODE_KASIR;
                mp.setTargetFragment(this, 0);
                mp.show(fragmentManager, "mp");
                break;
            default:
                break;

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        customVolley = new CustomVolley(getActivity());
        customVolley.setOnCallbackResponse(this);
        return view;
    }


    @Override
    public void onFinishMasterPassword() {

        if (MODE == RestaurantIceCream.MODE_PELAYAN || MODE == RestaurantIceCream.MODE_KASIR) {

            final Map<String, String> jsonParams = new HashMap<>();

            jsonParams.put(RestaurantIceCream.tipe_pegawai,
                    MODE == RestaurantIceCream.MODE_PELAYAN ? "1" : "2");

            OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                @Override
                public void idsAvailable(String userId, String registrationId) {
                    if (userId != null)
                        jsonParams.put(RestaurantIceCream.id_one_signal,
                                userId);
                }
            });

            queue = customVolley.Rest(Request.Method.POST, ApiHelper.getAbsensiPegawaiLink(getActivity()), jsonParams, TAG_LOG_MODE);

        } else {

            Prefs.putModeApp(getActivity(), MODE);
            EventBus.getDefault().postSticky(new Refresh(true));
        }
    }

    @Override
    public void onFinishPickMeja(Meja meja) {
        Prefs.putModeApp(getActivity(), MODE);
        Prefs.putIdMeja(getActivity(), meja.id_meja);
        Prefs.putNamaMeja(getActivity(), meja.nama_meja);
        EventBus.getDefault().postSticky(new Refresh(true));
    }


    private void startProgress(String TAG) {
        if (TAG.equals(TAG_LOG_MODE)) {
            TAG = "Moding";
        }
        dialogProgress = ProgressDialog.show(getActivity(), TAG,
                "Please wait...", true);
    }

    private void stopProgress(String TAG) {
        if (dialogProgress != null)
            dialogProgress.dismiss();
    }

    @Override
    public void onVolleyStart(String TAG) {
        if (TAG.equals(TAG_LOG_MODE)) {
            startProgress(TAG_LOG_MODE);
        }
    }

    @Override
    public void onVolleyEnd(String TAG) {
        if (TAG.equals(TAG_LOG_MODE)) {
            stopProgress(TAG_LOG_MODE);
        }
    }


    @Override
    public void onVolleySuccessResponse(String TAG, String response) {
        if (TAG.equals(TAG_LOG_MODE)) {
            try {
                JSONObject json = new JSONObject(response);
                Boolean isSuccess = Boolean.parseBoolean(json.getString(RestaurantIceCream.isSuccess));
                String message = json.getString(RestaurantIceCream.message);
                if (isSuccess) {
                    Prefs.putModeApp(getActivity(), MODE);
                    EventBus.getDefault().postSticky(new Refresh(true));
                } else {
                    TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                TastyToast.makeText(getActivity(), "Parsing data error ...", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }
    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {
        if (TAG.equals(TAG_LOG_MODE)) {
            TastyToast.makeText(getActivity(), "Error ..", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_LOG_MODE);
        }
    }
}
