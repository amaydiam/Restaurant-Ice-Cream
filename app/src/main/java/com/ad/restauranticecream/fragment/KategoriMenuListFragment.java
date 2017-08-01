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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.activity.KategoriMenuListActivity;
import com.ad.restauranticecream.activity.SubKategoriMenuListActivity;
import com.ad.restauranticecream.adapter.KategoriMenuAdapter;
import com.ad.restauranticecream.model.KategoriMenu;
import com.ad.restauranticecream.utils.ApiHelper;
import com.ad.restauranticecream.utils.CustomVolley;
import com.ad.restauranticecream.utils.Prefs;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialCommunityIcons;
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

import static android.content.Context.INPUT_METHOD_SERVICE;


public class KategoriMenuListFragment extends Fragment implements KategoriMenuAdapter.OnKategoriMenuItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, CustomVolley.OnCallbackResponse, ManageKategoriMenuFragment.AddEditKategoriMenuListener {

    private static final String TAG_MORE = "TAG_MORE";
    private static final String TAG_AWAL = "TAG_AWAL";
    private static final String TAG_NEW = "TAG_NEW";
    private static final String TAG_BATAL = "TAG_BATAL";

    private static final String TAG_ATAS = "atas";
    private static final String TAG_BAWAH = "bawah";
    public KategoriMenuAdapter adapter;
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
    private ArrayList<KategoriMenu> data = new ArrayList<>();
    private GridLayoutManager mLayoutManager;
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

    public KategoriMenuListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static KategoriMenuListFragment newInstance() {
        return new KategoriMenuListFragment();
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

    @OnClick(R.id.fab_action)
    void AddKategoriMenu() {

        FragmentManager fragmentManager = getChildFragmentManager();
        ManageKategoriMenuFragment manageKategoriMenu = new ManageKategoriMenuFragment();
        manageKategoriMenu.setTargetFragment(this, 0);
        manageKategoriMenu.setCancelable(false);
        manageKategoriMenu.setDialogTitle("KategoriMenu");
        manageKategoriMenu.setAction("add");
        manageKategoriMenu.show(fragmentManager, "Manage KategoriMenu");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof KategoriMenuListActivity) {
            // activity = (KategoriMenuListActivity) context;
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

        //search
        parentSearch.setVisibility(View.GONE);

        hideSoftKeyboard();

        parentSearch.setVisibility(View.GONE);
        //inisial adapterKategoriMenu
        adapter = new KategoriMenuAdapter(activity, data, isTablet);
        adapter.setOnKategoriMenuItemClickListener(this);

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

        // set adapterKategoriMenu
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

        if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_PELAYAN)
            fabAction.setVisibility(View.VISIBLE);
        else
            fabAction.setVisibility(View.GONE);

        fabScrollUp.setImageDrawable(
                new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_arrow_up)
                        .colorRes(R.color.primary));


        noData.setText(Html.fromHtml("<center><h1>{mdi-calendar}</h1></br> Tidak ada kategori_menu ...</center>"));
        showNoData(false);

          /* =========================================================================================
        ==================== Get Data List (KategoriMenu) ================================================
        ============================================================================================*/
        if (savedInstanceState == null || !savedInstanceState.containsKey(RestaurantIceCream.KATEGORI_MENU_ID)) {
            getDataFromServer(TAG_AWAL);
        } else {
            data = savedInstanceState.getParcelableArrayList(RestaurantIceCream.KATEGORI_MENU_ID);
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
        ==================== End Get Data List (KategoriMenu) ============================================
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
        return ApiHelper.getKategoriMenuLink(getActivity(), page);
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
                JSONArray items_obj = json.getJSONArray(RestaurantIceCream.kategori_menu);
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
                    if (activity instanceof KategoriMenuListActivity) {
                        ((KategoriMenuListActivity) getActivity()).loadDetailKategoriMenuFragmentWith(adapter.data.get(0));
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

        String id_kategori_menu = obj.getString(RestaurantIceCream.id_kategori_menu);
        String nama_kategori_menu = obj.getString(RestaurantIceCream.nama_kategori_menu);
        String gambar_kategori_menu = obj.getString(RestaurantIceCream.gambar_kategori_menu);
        //set map object
        KategoriMenu kategori_menu = new KategoriMenu(
                id_kategori_menu,
                nama_kategori_menu,
                gambar_kategori_menu

        );


        if (position.equals(TAG_BAWAH)) {
            data.add(kategori_menu);
        } else {
            data.add(0, kategori_menu);
        }

    }


    @Override
    public void onRefresh() {
        RefreshData();
    }

    public void RefreshData() {
        // if (adapterKategoriMenu.getItemCount() > 1) {
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
        if (TAG.equals(TAG_BATAL)) {
            TAG = "KategoriMenu Dibatalkan";
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
        if (TAG.equals(TAG_BATAL)) {
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
        if (TAG.equals(TAG_BATAL)) {
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

        if (TAG.equals(TAG_BATAL)) {
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
        if (TAG.equals(TAG_BATAL)) {
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
            queue.cancelAll(TAG_BATAL);
        }
    }

    @Override
    public void onActionClick(View v, int position) {
        position_action = position;
        KategoriMenu ketegori_menu = adapter.data.get(position);
        FragmentManager fragmentManager = getChildFragmentManager();
        ManageKategoriMenuFragment manageKategoriMenu = new ManageKategoriMenuFragment();
        manageKategoriMenu.setTargetFragment(this, 0);
        manageKategoriMenu.setCancelable(false);
        manageKategoriMenu.setDialogTitle("KategoriMenu");
        manageKategoriMenu.setAction("edit");
        manageKategoriMenu.setData(ketegori_menu);
        manageKategoriMenu.show(fragmentManager, "Manage KategoriMenu");
    }

    @Override
    public void onRootClick(View v, int position) {

        if (isTablet) {
            adapter.setSelected(position);

            if (activity instanceof KategoriMenuListActivity) {
                ((KategoriMenuListActivity) getActivity()).loadDetailKategoriMenuFragmentWith(adapter.data.get(0));
            }
        } else {
            Intent intent = new Intent(activity, SubKategoriMenuListActivity.class);
            intent.putExtra(RestaurantIceCream.KATEGORI_MENU_OBJECT, adapter.data.get(position));
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
    public void onFinishEditKategoriMenu(KategoriMenu kategori_menu) {

    }

    @Override
    public void onFinishAddKategoriMenu(KategoriMenu kategori_menu) {
        adapter.data.add(0, kategori_menu);
        adapter.notifyDataSetChanged();
        if (isTablet) {
            adapter.setSelected(0);
            ((KategoriMenuListActivity) getActivity()).loadDetailKategoriMenuFragmentWith(adapter.data.get(0));
        }
    }

    @Override
    public void onFinishDeleteKategoriMenu(KategoriMenu kategori_menu) {
        adapter.remove(position_action);
    }
}
