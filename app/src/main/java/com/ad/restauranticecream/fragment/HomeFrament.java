package com.ad.restauranticecream.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.Meja;
import com.ad.restauranticecream.model.RefreshDrawer;
import com.ad.restauranticecream.utils.Prefs;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFrament#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFrament extends Fragment implements MasterPasswordFragment.MasterPasswordListener, DialogPickMejaFragment.PickMejaListener {

    Unbinder unbinder;
    private int MODE;

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
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onFinishMasterPassword() {
        Prefs.putModeApp(getActivity(), MODE);
        EventBus.getDefault().postSticky(new RefreshDrawer(true));
    }

    @Override
    public void onFinishPickMeja(Meja meja) {
        Prefs.putModeApp(getActivity(), MODE);
        Prefs.putIdMeja(getActivity(), meja.id_meja);
        Prefs.putNamaMeja(getActivity(), meja.nama_meja);
        EventBus.getDefault().postSticky(new RefreshDrawer(true));
    }


}
