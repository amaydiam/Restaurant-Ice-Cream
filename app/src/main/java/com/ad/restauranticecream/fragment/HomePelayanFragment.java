package com.ad.restauranticecream.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.activity.KategoriMenuListActivity;
import com.ad.restauranticecream.activity.PesananListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePelayanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePelayanFragment extends Fragment {


    Unbinder unbinder;

    public HomePelayanFragment() {
        // Required empty public constructor
    }

    public static HomePelayanFragment newInstance() {
        HomePelayanFragment fragment = new HomePelayanFragment();
        return fragment;
    }

    @OnClick({R.id.btn_menu, R.id.btn_pesanan})
    void ClickMode(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_menu:
                Intent intent = new Intent(getActivity(), KategoriMenuListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_pesanan:
                Intent pesanan = new Intent(getActivity(), PesananListActivity.class);
                startActivity(pesanan);
                break;
            default:
                break;

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_pelayan, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
