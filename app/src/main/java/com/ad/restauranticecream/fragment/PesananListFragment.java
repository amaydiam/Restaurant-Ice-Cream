package com.ad.restauranticecream.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.activity.DrawerActivity;
import com.ad.restauranticecream.activity.PesananDetailActivity;
import com.ad.restauranticecream.adapter.PesananAdapter;
import com.ad.restauranticecream.model.DataPesananMenunggu;
import com.ad.restauranticecream.model.Pesanan;
import com.ad.restauranticecream.model.Refresh;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.utils.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialCommunityIcons;
import com.joanzapata.iconify.widget.IconButton;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class PesananListFragment extends Fragment implements PesananAdapter.OnPesananItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, CustomVolley.OnCallbackResponse {

    private static final String TAG_MORE = "TAG_MORE";
    private static final String TAG_AWAL = "TAG_AWAL";
    private static final String TAG_NEW = "TAG_NEW";
    private static final String TAG_SIAP = "TAG_SIAP";
    private static final String TAG_NIKMAT = "TAG_NIKMAT";
    private static final String TAG_BATAL = "TAG_BATAL";
    private static final String TAG_DIBAYAR = "TAG_DIBAYAR";

    private static final String TAG_ATAS = "atas";
    private static final String TAG_BAWAH = "bawah";
    public PesananAdapter adapter;
    @BindBool(R.bool.is_tablet)
    boolean isTablet;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.progress_more_data)
    ProgressBar progressMoreData;
    @BindView(R.id.no_data)
    IconButton noData;
    @BindView(R.id.fab_scroll_up)
    FloatingActionButton fabScrollUp;
    @BindView(R.id.fab_action)
    com.github.clans.fab.FloatingActionButton fabAction;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    //error
    @BindView(R.id.error_message)
    View errorMessage;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.text_error)
    TextView textError;
    @BindView(R.id.try_again)
    TextView tryAgain;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.parent_search)
    CardView parentSearch;
    private ArrayList<Pesanan> data = new ArrayList<>();
    private GridLayoutManager mLayoutManager;
    private String keyword = null;
    private Integer position_action;
    private ProgressDialog dialogProgress;
    private FragmentActivity activity;
    private Unbinder butterknife;
    private boolean isFinishLoadingAwalData = true;
    private boolean isLoadingMoreData = false;
    private boolean isFinishMoreData = false;
    private int page = 1;
    private boolean isRefresh = false;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private int mPreviousVisibleItem;

    public PesananListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PesananListFragment newInstance() {
        return new PesananListFragment();
    }
    //  private String session_key;

    @OnClick(R.id.fab_scroll_up)
    void ScrollUp() {
        recyclerView.smoothScrollToPosition(0);
    }


    @OnClick(R.id.try_again)
    void TryAgain() {
        RefreshData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DrawerActivity) {
            // activity = (DrawerActivity) context;
        }
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        butterknife = ButterKnife.bind(this, rootView);
        customVolley = new CustomVolley(activity);
        customVolley.setOnCallbackResponse(this);
        try {
            keyword = getArguments().getString(RestaurantIceCream.KEYWORD);
        } catch (Exception e) {

        }


        // Configure the swipe refresh layout
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.blue_light,
                R.color.green_light, R.color.orange_light, R.color.red_light);
        TypedValue typed_value = new TypedValue();
        activity.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
        swipeContainer.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));

        //search
        parentSearch.setVisibility(View.GONE);

        hideSoftKeyboard();

        if (!TextUtils.isNullOrEmpty(keyword))
            parentSearch.setVisibility(View.GONE);
        //inisial adapterPesanan
        adapter = new PesananAdapter(activity, data, isTablet);
        adapter.setValSearchAlamat(keyword);
        adapter.setOnPesananItemClickListener(this);

        //recyclerView
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        //inisial layout manager
       /* int grid_column_count = getResources().getInteger(R.integer.grid_column_count);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(grid_column_count, StaggeredGridLayoutManager.VERTICAL);
*/

        //   final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //  mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mLayoutManager = new GridLayoutManager(activity, getNumberOfColumns());

        // set layout manager
        recyclerView.setLayoutManager(mLayoutManager);

        // set adapterPesanan
        recyclerView.setAdapter(adapter);

        //handle ringkas data
        Mugen.with(recyclerView, new MugenCallbacks() {
            @Override
            public void onLoadMore() {
                if (isFinishLoadingAwalData
                        && !isFinishMoreData
                        && adapter.getItemCount() > 0) {
                    getDataFromServer(TAG_MORE);
                }
            }

            @Override
            public boolean isLoading() {
                return isLoadingMoreData;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        }).start();


        fabAction.setVisibility(View.GONE);

        fabScrollUp.setImageDrawable(
                new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_arrow_up)
                        .colorRes(R.color.primary));


        noData.setText(Html.fromHtml("<center><h1>{mdi-calendar}</h1></br> Tidak ada pesanan ...</center>"));
        showNoData(false);

          /* =========================================================================================
        ==================== Get Data List (Pesanan) ================================================
        ============================================================================================*/
        if (savedInstanceState == null || !savedInstanceState.containsKey(RestaurantIceCream.PESANAN_ID)) {
            getDataFromServer(TAG_AWAL);
        } else {
            data = savedInstanceState.getParcelableArrayList(RestaurantIceCream.PESANAN_ID);
            page = savedInstanceState.getInt(RestaurantIceCream.PAGE);
            isLoadingMoreData = savedInstanceState.getBoolean(RestaurantIceCream.IS_LOADING_MORE_DATA);
            isFinishLoadingAwalData = savedInstanceState.getBoolean(RestaurantIceCream.IS_FINISH_LOADING_AWAL_DATA);

            if (!isFinishLoadingAwalData) {
                getDataFromServer(TAG_AWAL);
            } else if (isLoadingMoreData) {
                adapter.notifyDataSetChanged();
                checkData();
                getDataFromServer(TAG_MORE);
            } else {
                adapter.notifyDataSetChanged();
                checkData();
            }
        }
        /* =========================================================================================
        ==================== End Get Data List (Pesanan) ============================================
        ============================================================================================*/

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLayoutManager != null && adapter != null) {
            outState.putBoolean(RestaurantIceCream.IS_FINISH_LOADING_AWAL_DATA, isFinishLoadingAwalData);
            outState.putBoolean(RestaurantIceCream.IS_LOADING_MORE_DATA, isLoadingMoreData);
            outState.putInt(RestaurantIceCream.PAGE, page);
            outState.putParcelableArrayList(RestaurantIceCream.data, data);
        }
    }

    private void showProgresMore(boolean show) {
        if (show) {
            progressMoreData.setVisibility(View.VISIBLE);
        } else {
            progressMoreData.setVisibility(View.GONE);
        }
    }

    private void showNoData(boolean show) {
        if (show) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }
    }

    private void ProgresRefresh(boolean show) {
        if (show) {
            swipeContainer.setRefreshing(true);
            swipeContainer.setEnabled(false);
        } else {
            swipeContainer.setEnabled(true);
            swipeContainer.setRefreshing(false);
        }
    }

    private void getDataFromServer(final String TAG) {
        /*queue = customVolley.Rest(Request.Method.GET, RestaurantIceCream.api_test + "?" + RestaurantIceCream.app_key + "=" + RestaurantIceCream.value_app_key + "&session_key=" + session_key
                + "&PAGE=" + PAGE + "&limit="
                + RestaurantIceCream.LIMIT_DATA, null, TAG);*/
        queue = customVolley.Rest(Request.Method.GET, getUrlToDownload(page), null, TAG);

    }

    public String getUrlToDownload(int page) {
        if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_PELANGGAN) {
            return ApiHelper.getPesananModePelangganLink(getActivity(), Prefs.getIdMeja(getActivity()), page, keyword);
        } else if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_PELAYAN) {
            return ApiHelper.getPesananModePelayanLink(getActivity(), page, keyword);
        } else if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_KASIR) {
            return ApiHelper.getPesananModeKasirLink(getActivity(), page, keyword);
        }
        return null;
    }


    protected void DrawDataAllData(String position, String tag, String response) {


        try {
            if (isRefresh) {
                adapter.delete_all();
            }

            JSONObject json = new JSONObject(response);
            Boolean isSuccess = Boolean.parseBoolean(json.getString(RestaurantIceCream.isSuccess));
            String message = json.getString(RestaurantIceCream.message);
            if (isSuccess) {
                JSONArray items_obj = json.getJSONArray(RestaurantIceCream.pesanan);
                int jumlah_list_data = items_obj.length();
                if (jumlah_list_data > 0) {
                    for (int i = 0; i < jumlah_list_data; i++) {
                        JSONObject obj = items_obj.getJSONObject(i);
                        setDataObject(position, obj);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    switch (tag) {
                        case TAG_MORE:
                            isFinishMoreData = true;
                            //       TastyToast.makeText(activity, "tidak ada data lama...", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            break;
                        case TAG_AWAL:
                            //      TastyToast.makeText(activity, "tidak ada data...", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            break;
                        case TAG_NEW:
                            //     TastyToast.makeText(activity, "tidak ada data baru...", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            break;
                    }
                }

                if (isTablet && page == 1 && adapter.data.size() > 0) {
                    adapter.setSelected(0);
                    if (activity instanceof DrawerActivity) {
                        ((DrawerActivity) getActivity()).loadDetailPesananFragmentWith(adapter.data.get(0));
                    }
                }

                page = page + 1;
            } else {
                TastyToast.makeText(activity, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }

            checkData();

        } catch (JSONException e) {
            e.printStackTrace();
            TastyToast.makeText(activity, "Parsing data error ...", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }


    }

    private void checkData() {
        if (adapter.getItemCount() > 0) {
            showNoData(false);
        } else {
            showNoData(true);
        }

        EventBus.getDefault().postSticky(new DataPesananMenunggu(adapter.getItemCount()));
    }


    private void ResponeDelete(String response) {
        try {

            JSONObject json = new JSONObject(response);
            Boolean isSuccess = Boolean.parseBoolean(json.getString(RestaurantIceCream.isSuccess));
            String message = json.getString(RestaurantIceCream.message);
            if (isSuccess) {
                adapter.remove(position_action);
                checkData();
            } else {
                TastyToast.makeText(activity, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            TastyToast.makeText(activity, "Parsing data error ...", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    private void setDataObject(String position, JSONObject obj) throws JSONException {
        //parse object

        String id_pesanan = obj.getString(RestaurantIceCream.id_pesanan);
        String id_meja = obj.getString(RestaurantIceCream.id_meja);
        String kode_pesanan = obj.getString(RestaurantIceCream.kode_pesanan);
        String nama_pemesan = obj.getString(RestaurantIceCream.nama_pemesan);
        String waktu_pesan = obj.getString(RestaurantIceCream.waktu_pesan);
        String status_pesanan = obj.getString(RestaurantIceCream.status_pesanan);
        String catatan = obj.getString(RestaurantIceCream.catatan);
        String nama_meja = obj.getString(RestaurantIceCream.nama_meja);
        String total_harga = obj.getString(RestaurantIceCream.total_harga);
        //set map object
        Pesanan pesanan = new Pesanan(
                id_pesanan,
                id_meja,
                kode_pesanan,
                nama_pemesan,
                waktu_pesan,
                status_pesanan,
                catatan,
                nama_meja,
                total_harga

        );


        if (position.equals(TAG_BAWAH)) {
            data.add(pesanan);
        } else {
            data.add(0, pesanan);
        }

    }


    @Override
    public void onRefresh() {
        RefreshData();
    }

    public void RefreshData() {
        // if (adapterPesanan.getItemCount() > 1) {
        isRefresh = true;
        isLoadingMoreData = false;
        isFinishLoadingAwalData = true;
        isFinishMoreData = false;
        page = 1;
        showNoData(false);
       /* } else {
            isRefresh = false;
        }*/
        getDataFromServer(TAG_AWAL);
    }


    private void startProgress(String TAG) {
        if (TAG.equals(TAG_SIAP)) {
            TAG = "Pesanan Disiapkan";
        }
        if (TAG.equals(TAG_NIKMAT)) {
            TAG = "Pesanan Dinikmati";
        }
        if (TAG.equals(TAG_BATAL)) {
            TAG = "Pesanan Dibatalkan";
        }
        if (TAG.equals(TAG_DIBAYAR)) {
            TAG = "Pesanan Dibayarkan";
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
        if (TAG.equals(TAG_SIAP) || TAG.equals(TAG_NIKMAT) || TAG.equals(TAG_BATAL) || TAG.equals(TAG_DIBAYAR)) {
            startProgress(TAG);
        } else {
            showProgresMore(false);
            if (TAG.equals(TAG_AWAL)) {
                ProgresRefresh(true);
                isFinishLoadingAwalData = false;
                errorMessage.setVisibility(View.GONE);
                if (adapter.getItemCount() == 0) {
                    loading.setVisibility(View.VISIBLE);
                }

            } else {
                if (TAG.equals(TAG_MORE)) {
                    isLoadingMoreData = true;
                    isFinishMoreData = false;
                    showProgresMore(true);
                }
            }
        }
    }

    @Override
    public void onVolleyEnd(String TAG) {
        if (TAG.equals(TAG_SIAP) || TAG.equals(TAG_NIKMAT) || TAG.equals(TAG_BATAL) || TAG.equals(TAG_DIBAYAR)) {
            stopProgress(TAG);
        } else {
            ProgresRefresh(false);
            if (TAG.equals(TAG_AWAL)) {
                loading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {

        if (TAG.equals(TAG_SIAP)) {
            adapter.changeDisiapkan(position_action);
        } else if (TAG.equals(TAG_NIKMAT) || TAG.equals(TAG_BATAL) || TAG.equals(TAG_DIBAYAR)) {
            ResponeDelete(response);

        } else {

            if (TAG.equals(TAG_AWAL)) {
                errorMessage.setVisibility(View.GONE);
                DrawDataAllData(TAG_BAWAH, TAG, response);
                isFinishLoadingAwalData = true;
            }
            if (TAG.equals(TAG_MORE)) {
                DrawDataAllData(TAG_BAWAH, TAG, response);
                isLoadingMoreData = false;
            }
            if (TAG.equals(TAG_NEW)) {
                DrawDataAllData(TAG_ATAS, TAG, response);
            }

            isRefresh = false;
            showProgresMore(false);
        }
    }


    @Override
    public void onVolleyErrorResponse(String TAG, String response) {
        if (TAG.equals(TAG_SIAP) || TAG.equals(TAG_DIBAYAR)) {
            TastyToast.makeText(activity, "Error", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        } else {
            if (TAG.equals(TAG_AWAL)) {
                isFinishLoadingAwalData = false;
                if (adapter.getItemCount() == 0) {
                    errorMessage.setVisibility(View.VISIBLE);
                } else {
                    errorMessage.setVisibility(View.GONE);
                }
            }
            if (TAG.equals(TAG_MORE)) {
                isFinishMoreData = false;
                isLoadingMoreData = false;
                showProgresMore(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterknife.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_AWAL);
            queue.cancelAll(TAG_MORE);
            queue.cancelAll(TAG_NEW);
            queue.cancelAll(TAG_SIAP);
            queue.cancelAll(TAG_NIKMAT);
            queue.cancelAll(TAG_BATAL);
            queue.cancelAll(TAG_DIBAYAR);
        }
    }

    @Override
    public void onActionClick(View v, int position) {

        position_action = position;
        final String id_pesanan = adapter.data.get(position).id_pesanan;
        int viewId = v.getId();
        if (viewId == R.id.btn_siap) {
            queue = customVolley.Rest(Request.Method.GET, ApiHelper.getPesananSedangDisiapkanLink(getActivity(), id_pesanan), null, TAG_SIAP);
        } else if (viewId == R.id.btn_nikmati) {
            queue = customVolley.Rest(Request.Method.GET, ApiHelper.getPesananSedangDinikmatiLink(getActivity(), id_pesanan), null, TAG_NIKMAT);
        } else if (viewId == R.id.btn_batal) {
            new AlertDialog.Builder(getActivity())
                    .setIcon(
                            new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_alert_octagon)
                                    .colorRes(R.color.primary)
                                    .actionBarSize())
                    .setTitle("Batalkan Pesanan")
                    .setMessage("Apa anda yakin akan membatalkan pesanan ini?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            queue = customVolley.Rest(Request.Method.GET, ApiHelper.getPesananDibatalkanLink(getActivity(), id_pesanan), null, TAG_BATAL);
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        } else if (viewId == R.id.btn_bayar) {
            queue = customVolley.Rest(Request.Method.GET, ApiHelper.getPesananTelahDibayarLink(getActivity(), id_pesanan), null, TAG_DIBAYAR);
        }
    }

    @Override
    public void onRootClick(View v, int position) {

        if (isTablet) {
            adapter.setSelected(position);

            if (activity instanceof DrawerActivity) {
                ((DrawerActivity) getActivity()).loadDetailPesananFragmentWith(adapter.data.get(0));
            }
        } else {
            Intent intent = new Intent(activity, PesananDetailActivity.class);
            intent.putExtra(RestaurantIceCream.PESANAN_OBJECT, adapter.data.get(position));
            startActivity(intent);
        }

    }


    public int getNumberOfColumns() {
        // Get screen width
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float widthPx = displayMetrics.widthPixels;
        if (isTablet) {
            widthPx = widthPx / 3;
        }
        // Calculate desired width

        float desiredPx = getResources().getDimensionPixelSize(R.dimen.movie_list_card_width);
        int columns = Math.round(widthPx / desiredPx);
        return columns > 1 ? columns : 1;
    }


    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
    public void onRefreshDrawer(Refresh cp) {
        if (cp.isRefresh()) {
            // if (adapterPesanan.getItemCount() > 1) {
            isRefresh = true;
            isLoadingMoreData = false;
            isFinishLoadingAwalData = true;
            isFinishMoreData = false;
            page = 1;
            showNoData(false);
       /* } else {
            isRefresh = false;
        }*/
            getDataFromServer(TAG_AWAL);
        }

        Refresh stickyEvent = EventBus.getDefault().getStickyEvent(Refresh.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

}
