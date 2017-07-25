package com.ad.restauranticecream.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.activity.DrawerActivity;
import com.ad.restauranticecream.activity.MustahiqDetailActivity;
import com.ad.restauranticecream.adapter.MustahiqAdapter;
import com.ad.restauranticecream.model.AmilZakat;
import com.ad.restauranticecream.model.Mustahiq;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialCommunityIcons;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.joanzapata.iconify.widget.IconButton;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MustahiqListFragment extends Fragment implements MustahiqAdapter.OnMustahiqItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, CustomVolley.OnCallbackResponse, ManageMustahiqFragment.AddEditMustahiqListener {

    private static final String TAG_MORE = "TAG_MORE";
    private static final String TAG_AWAL = "TAG_AWAL";
    private static final String TAG_NEW = "TAG_NEW";
    private static final String TAG_DELETE = "TAG_DELETE";
    private static final String TAG_AMIL_ZAKAT = "TAG_AMIL_ZAKAT";


    private static final String TAG_ATAS = "atas";
    private static final String TAG_BAWAH = "bawah";
    public MustahiqAdapter adapterMustahiq;
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
    @BindView(R.id.parent_search)
    CardView parentSearch;
    private ArrayList<Mustahiq> dataMustahiqs = new ArrayList<>();
    private GridLayoutManager mLayoutManager;
    private Integer position_delete;
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

    public MustahiqListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MustahiqListFragment newInstance() {
        MustahiqListFragment fragment = new MustahiqListFragment();
        return fragment;

    }
    //  private String session_key;

    void ScrollUp() {
        recyclerView.smoothScrollToPosition(0);
    }

    @OnClick(R.id.fab_action)
    void AddMustahiq() {

        queue = customVolley.Rest(Request.Method.GET, ApiHelper.getAmilZakatLink(getActivity()), null, TAG_AMIL_ZAKAT);
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

        // Configure the swipe refresh layout
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.blue_light,
                R.color.green_light, R.color.orange_light, R.color.red_light);
        TypedValue typed_value = new TypedValue();
        activity.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
        swipeContainer.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));


        parentSearch.setVisibility(View.GONE);
        //inisial adapterMustahiq
        adapterMustahiq = new MustahiqAdapter(activity, dataMustahiqs, isTablet);
        adapterMustahiq.setOnMustahiqItemClickListener(this);

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

        // set adapterMustahiq
        recyclerView.setAdapter(adapterMustahiq);

        //handle ringkas dataMustahiqs
        Mugen.with(recyclerView, new MugenCallbacks() {
            @Override
            public void onLoadMore() {
                if (isFinishLoadingAwalData
                        && !isFinishMoreData
                        && adapterMustahiq.getItemCount() > 0) {
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


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem > mPreviousVisibleItem) {
                    fabAction.hide(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    fabAction.show(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });


        //setup fab
        fabAction.setImageDrawable(
                new IconDrawable(getActivity(), MaterialIcons.md_add)
                        .colorRes(R.color.white)
                        .actionBarSize());

        fabScrollUp.setImageDrawable(
                new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_arrow_up)
                        .colorRes(R.color.primary));

        fabAction.setVisibility(View.VISIBLE);

        noData.setText(Html.fromHtml("<center><h1>{mdi-calendar}</h1></br> Tidak ada mustahiq ...</center>"));
        showNoData(false);

          /* =========================================================================================
        ==================== Get Data List (Mustahiq) ================================================
        ============================================================================================*/
        if (savedInstanceState == null || !savedInstanceState.containsKey(RestaurantIceCream.CALON_MUSTAHIQ_ID)) {
            getDataFromServer(TAG_AWAL);
        } else {
            dataMustahiqs = savedInstanceState.getParcelableArrayList(RestaurantIceCream.CALON_MUSTAHIQ_ID);
            page = savedInstanceState.getInt(RestaurantIceCream.PAGE);
            isLoadingMoreData = savedInstanceState.getBoolean(RestaurantIceCream.IS_LOADING_MORE_DATA);
            isFinishLoadingAwalData = savedInstanceState.getBoolean(RestaurantIceCream.IS_FINISH_LOADING_AWAL_DATA);

            if (!isFinishLoadingAwalData) {
                getDataFromServer(TAG_AWAL);
            } else if (isLoadingMoreData) {
                adapterMustahiq.notifyDataSetChanged();
                checkData();
                getDataFromServer(TAG_MORE);
            } else {
                adapterMustahiq.notifyDataSetChanged();
                checkData();
            }
        }
        /* =========================================================================================
        ==================== End Get Data List (Mustahiq) ============================================
        ============================================================================================*/

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLayoutManager != null && adapterMustahiq != null) {
            outState.putBoolean(RestaurantIceCream.IS_FINISH_LOADING_AWAL_DATA, isFinishLoadingAwalData);
            outState.putBoolean(RestaurantIceCream.IS_LOADING_MORE_DATA, isLoadingMoreData);
            outState.putInt(RestaurantIceCream.PAGE, page);
            outState.putParcelableArrayList(RestaurantIceCream.data, dataMustahiqs);
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
        return ApiHelper.getMustahiqLink(getActivity(), page);
    }


    protected void DrawDataAllData(String position, String tag, String response) {


        try {
            if (isRefresh) {
                adapterMustahiq.delete_all();
            }

            JSONObject json = new JSONObject(response);
            Boolean isSuccess = Boolean.parseBoolean(json.getString(RestaurantIceCream.isSuccess));
            String message = json.getString(RestaurantIceCream.message);
            if (isSuccess) {
                JSONArray items_obj = json.getJSONArray(RestaurantIceCream.mustahiq);
                int jumlah_list_data = items_obj.length();
                if (jumlah_list_data > 0) {
                    for (int i = 0; i < jumlah_list_data; i++) {
                        JSONObject obj = items_obj.getJSONObject(i);
                        setDataObject(position, obj);
                    }
                    adapterMustahiq.notifyDataSetChanged();
                } else {
                    switch (tag) {
                        case TAG_MORE:
                            isFinishMoreData = true;
                            //       TastyToast.makeText(activity, "tidak ada dataMustahiqs lama...", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            break;
                        case TAG_AWAL:
                            //      TastyToast.makeText(activity, "tidak ada dataMustahiqs...", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            break;
                        case TAG_NEW:
                            //     TastyToast.makeText(activity, "tidak ada dataMustahiqs baru...", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            break;
                    }
                }

                if (isTablet && page == 1 && adapterMustahiq.data.size() > 0) {
                    adapterMustahiq.setSelected(0);
                    ((DrawerActivity) getActivity()).loadDetailMustahiqFragmentWith(adapterMustahiq.data.get(0).id_mustahiq);
                }

                page = page + 1;
            } else {
                TastyToast.makeText(activity, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }

            checkData();

        } catch (JSONException e) {
            e.printStackTrace();
            TastyToast.makeText(activity, "Parsing dataMustahiqs error ...", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }


    }

    private void checkData() {
        if (adapterMustahiq.getItemCount() > 0) {

            showNoData(false);
        } else {
            showNoData(true);
        }
    }


    private void ResponeDelete(String response) {
        try {

            JSONObject json = new JSONObject(response);
            Boolean isSuccess = Boolean.parseBoolean(json.getString(RestaurantIceCream.isSuccess));
            String message = json.getString(RestaurantIceCream.message);
            if (isSuccess) {
                adapterMustahiq.remove(position_delete);
                checkData();
            } else {
                TastyToast.makeText(activity, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            TastyToast.makeText(activity, "Parsing dataMustahiqs error ...", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    private void setDataObject(String position, JSONObject obj) throws JSONException {
        //parse object
        //parse object
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
        //set map object
        AddAndSetMapData(
                position,
                id_mustahiq,
                id_calon_mustahiq,
                nama_calon_mustahiq,
                latitude_calon_mustahiq,
                longitude_calon_mustahiq,
                alamat_calon_mustahiq,
                no_identitas_calon_mustahiq,
                no_telp_calon_mustahiq,
                nama_perekomendasi_calon_mustahiq,
                alasan_perekomendasi_calon_mustahiq,
                status_mustahiq,
                jumlah_rating,
                id_amil_zakat,
                nama_amil_zakat,
                waktu_terakhir_donasi
        );

    }

    private void AddAndSetMapData(
            String position,
            String id_mustahiq,
            String id_calon_mustahiq,
            String nama_calon_mustahiq,
            String alamat_calon_mustahiq,
            String latitude_calon_mustahiq,
            String longitude_calon_mustahiq,
            String no_identitas_calon_mustahiq,
            String no_telp_calon_mustahiq,
            String nama_perekomendasi_calon_mustahiq,
            String alasan_perekomendasi_calon_mustahiq,
            String status_mustahiq,
            String jumlah_rating,
            String id_amil_zakat,
            String nama_amil_zakat,
            String waktu_terakhir_donasi) {

        Mustahiq mustahiq = new Mustahiq(
                id_mustahiq,
                id_calon_mustahiq,
                nama_calon_mustahiq,
                alamat_calon_mustahiq,
                latitude_calon_mustahiq,
                longitude_calon_mustahiq,
                no_identitas_calon_mustahiq,
                no_telp_calon_mustahiq,
                nama_perekomendasi_calon_mustahiq,
                alasan_perekomendasi_calon_mustahiq,
                status_mustahiq,
                jumlah_rating,
                id_amil_zakat,
                nama_amil_zakat,
                waktu_terakhir_donasi);

        if (position.equals(TAG_BAWAH)) {
            dataMustahiqs.add(mustahiq);
        } else {
            dataMustahiqs.add(0, mustahiq);
        }
    }


    @Override
    public void onRefresh() {
        RefreshData();
    }

    public void RefreshData() {
        // if (adapterMustahiq.getItemCount() > 1) {
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
        if (TAG.equals(TAG_DELETE)) {
            TAG = "Delete Mustahiq";
        }
        if (TAG.equals(TAG_AMIL_ZAKAT)) {
            TAG = "Prepare";
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
        if (TAG.equals(TAG_DELETE)) {
            startProgress(TAG_DELETE);
        } else if (TAG.equals(TAG_AMIL_ZAKAT)) {
            startProgress(TAG_AMIL_ZAKAT);
        } else {
            showProgresMore(false);
            if (TAG.equals(TAG_AWAL)) {
                ProgresRefresh(true);
                isFinishLoadingAwalData = false;
                errorMessage.setVisibility(View.GONE);
                if (adapterMustahiq.getItemCount() == 0) {
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
        if (TAG.equals(TAG_DELETE)) {
            stopProgress(TAG_DELETE);
        } else if (TAG.equals(TAG_AMIL_ZAKAT)) {
            stopProgress(TAG_AMIL_ZAKAT);
        } else {
            ProgresRefresh(false);
            if (TAG.equals(TAG_AWAL)) {
                loading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {
        if (TAG.equals(TAG_DELETE)) {
            ResponeDelete(response);

        } else if (TAG.equals(TAG_AMIL_ZAKAT)) {
            ResponeAmilZakat(response);

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

    private void ResponeAmilZakat(String response) {

        try {

            JSONObject json = new JSONObject(response);
            Boolean isSuccess = Boolean.parseBoolean(json.getString(RestaurantIceCream.isSuccess));
            String message = json.getString(RestaurantIceCream.message);
            if (isSuccess) {
                JSONArray items_obj = json.getJSONArray(RestaurantIceCream.amil_zakat);
                int jumlah_list_data = items_obj.length();
                if (jumlah_list_data > 0) {

                    ArrayList<AmilZakat> dataAmilRestaurant Ice Cream= new ArrayList<>();
                    for (int i = 0; i < jumlah_list_data; i++) {
                        JSONObject obj = items_obj.getJSONObject(i);
                        String id_amil_zakat = obj.getString(RestaurantIceCream.id_amil_zakat);
                        String nama_amil_zakat = obj.getString(RestaurantIceCream.nama_amil_zakat);

                        dataAmilRestaurantIceCream.add(new AmilZakat(id_amil_zakat, nama_amil_zakat));
                    }

                    FragmentManager fragmentManager = getChildFragmentManager();
                    ManageMustahiqFragment manageMustahiq = new ManageMustahiqFragment();
                    manageMustahiq.setTargetFragment(this, 0);
                    manageMustahiq.setCancelable(false);
                    manageMustahiq.setDialogTitle("Mustahiq");
                    manageMustahiq.setAction("add");
                    manageMustahiq.setAmilZakat(dataAmilZakat);
                    manageMustahiq.show(fragmentManager, "Manage Mustahiq");

                } else {
                    TastyToast.makeText(activity, "tidak ada dataMustahiqs...", TastyToast.LENGTH_LONG, TastyToast.INFO);
                }
            } else {
                TastyToast.makeText(activity, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }

        } catch (JSONException e)

        {
            e.printStackTrace();
            TastyToast.makeText(activity, "Parsing dataMustahiqs error ...", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }

    }


    @Override
    public void onVolleyErrorResponse(String TAG, String response) {
        if (TAG.equals(TAG_DELETE)) {
            TastyToast.makeText(activity, "Error hapus mustahiq...", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        } else if (TAG.equals(TAG_AMIL_ZAKAT)) {
            TastyToast.makeText(activity, "Error ..", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        } else {
            if (TAG.equals(TAG_AWAL)) {
                isFinishLoadingAwalData = false;
                if (adapterMustahiq.getItemCount() == 0) {
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
            queue.cancelAll(TAG_DELETE);
            queue.cancelAll(TAG_AMIL_ZAKAT);
        }
    }

    @Override
    public void onActionClick(View v, int position) {
        int viewId = v.getId();
       /* if (viewId == R.id.btn_action) {
            OpenAtion(v, position);
        }*/
    }

    @Override
    public void onRootClick(View v, int position) {

        if (isTablet) {
            adapterMustahiq.setSelected(position);
            ((DrawerActivity) getActivity()).loadDetailMustahiqFragmentWith(adapterMustahiq.data.get(position).id_mustahiq);
        } else {
            Intent intent = new Intent(activity, MustahiqDetailActivity.class);
            intent.putExtra(RestaurantIceCream.MUSTAHIQ_ID, adapterMustahiq.data.get(position).id_mustahiq);
            startActivity(intent);
        }

    }
/*
    public void OpenAtion(View v, final int position) {

        final String id_mustahiq = adapterMustahiq.dataMustahiqs.get(position).id_mustahiq

        PopupMenu popup = new PopupMenu(activity, v, Gravity.RIGHT);
        popup.getMenuInflater().inflate(R.menu.action_manage, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int which = item.getItemId();
                if (which == R.id.action_edit) {
                    Intent myIntent = new Intent(getActivity(), actionEditActivity.class);
                    activity.startActivityForResult(myIntent, 1);
                }
                if (which == R.id.action_delete) {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(
                                    new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_alert_octagon)
                                            .colorRes(R.color.primary)
                                            .actionBarSize())
                            .setTitle("Hapus Mustahiq")
                            .setMessage("Apa anda yakin akan menghapus mustahiq ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    position_delete = position;
                                    queue = customVolley.Rest(Request.Method.GET, ApiHelper.getDeleteMustahiqLink(getActivity(), idgambar), null, TAG_DELETE);
                                }
                            })
                            .setNegativeButton("Tidak", null)
                            .show();
                }
                return true;
            }
        });

        // Force icons to show
        try {
            Field mFieldPopup = popup.getClass().getDeclaredField("mPopup");
            mFieldPopup.setAccessible(true);

            MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
            mPopup.setForceShowIcon(true);

        } catch (Exception e) {
            Log.w("TAG", "error forcing menu icons to show", e);
            return;
        }

        popup.show();
    }*/


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

    @Override
    public void onFinishEditMustahiq(Mustahiq mustahiq) {

    }

    @Override
    public void onFinishAddMustahiq(Mustahiq mustahiq) {
        adapterMustahiq.data.add(0, mustahiq);
        adapterMustahiq.notifyDataSetChanged();
        if (isTablet) {
            adapterMustahiq.setSelected(0);
            ((DrawerActivity) getActivity()).loadDetailMustahiqFragmentWith(adapterMustahiq.data.get(0).id_mustahiq);
        }
    }

    @Override
    public void onFinishDeleteMustahiq(Mustahiq mustahiq) {

    }
}
