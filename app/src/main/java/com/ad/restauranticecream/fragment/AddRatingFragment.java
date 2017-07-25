package com.ad.restauranticecream.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.Mustahiq;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.utils.SnackBar;
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

public class AddRatingFragment extends DialogFragment implements CustomVolley.OnCallbackResponse{
    private static final String TAG_RATING= "TAG_RATING";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.addrating)
    AppCompatRatingBar addrating;
    @BindView(R.id.rating)
    Button rating;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private SnackBar snackbar;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private ProgressDialog dialogProgress;
    private Unbinder butterKnife;

    private int val_addrating = 0;


    private RatingListener callback;
    private Mustahiq mustahiq;

    public AddRatingFragment() {

    }

    public void setData(Mustahiq mustahiq) {
        this.mustahiq = mustahiq;
    }


    @OnClick(R.id.rating)
    void Rating() {
        getData();

        if (val_addrating == 0) {
            snackbar.show("Harap beri rating");
            return;
        }

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put(RestaurantIceCream.id_mustahiq,
                String.valueOf(mustahiq.id_mustahiq));
        jsonParams.put(RestaurantIceCream.rating,
                String.valueOf(val_addrating));
        if(Prefs.getLogin(getActivity()))
            jsonParams.put(RestaurantIceCream.id_user,
                    String.valueOf(Prefs.getIdUser(getActivity())));

        queue = customVolley.Rest(Request.Method.POST, ApiHelper.getAddRatingLink(getActivity()), jsonParams, TAG_RATING);


    }

    private void getData() {

        val_addrating = (int) addrating.getRating();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_RATING);
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

                JSONObject obj = new JSONObject(json.getString(RestaurantIceCream.mustahiq));
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
                        status_mustahiq,
                        jumlah_rating,id_amil_zakat, nama_amil_zakat, waktu_terakhir_donasi);
                callback.onFinishRating(mustahiq);
                dismiss();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
            callback = (RatingListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement KonfirmasiPendaftaranPesertaListener");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_rating, container);

        butterKnife = ButterKnife.bind(this, view);
        customVolley = new CustomVolley(getActivity());
        customVolley.setOnCallbackResponse(this);
        snackbar = new SnackBar(getActivity(), coordinatorLayout);
        toolbar.setTitle("Rating");
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




    public interface RatingListener {

        void onFinishRating(Mustahiq mustahiq);
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