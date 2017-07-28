package com.ad.restauranticecream.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.RefreshDrawer;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.MaterialCommunityIcons;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;

public class DrawerFragment extends Fragment implements OnMenuItemClickListener, OnNavigationItemSelectedListener, MasterPasswordFragment.MasterPasswordListener {

    @BindView(R.id.drawer_layout)
    public
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    private Fragment fragment;
    private Unbinder unbinder;
    private MenuItem prevMenuItem;
    private int MODE;

    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drawer, container, false);
        unbinder = ButterKnife.bind(this, v);

        // Setup toolbar
        toolbar.inflateMenu(R.menu.menu_zakat);
        toolbar.setTitle(getActivity().getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        // Setup navigation drawer
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        SetMenuDrawer();

        if (Prefs.getUrl(getActivity()) == null)
            SetUrl();

        actionBarDrawerToggle.syncState();

        int view_type;
        if (Prefs.getModeApp(getActivity()) != RestaurantIceCream.MODE_HOME) {

            view_type = Prefs.getLastSelected(getActivity());
        } else {
            view_type = RestaurantIceCream.VIEW_TYPE_HOME;
        }

        if (savedInstanceState == null) {
            setSelectedDrawerItem(view_type);
        } else {
            fragment = getActivity().getSupportFragmentManager().findFragmentByTag(RestaurantIceCream.TAG_GRID_FRAGMENT);
            if (savedInstanceState.containsKey(RestaurantIceCream.TOOLBAR_TITLE)) {
                toolbar.setSubtitle(savedInstanceState.getString(RestaurantIceCream.TOOLBAR_TITLE));
            } else {
                toolbar.setSubtitle(navigationView.getMenu().findItem(view_type).getTitle());
            }
        }
        return v;
    }

    private void SetMenuDrawer() {


        View header = navigationView.getHeaderView(0);
        RobotoBoldTextView ket = (RobotoBoldTextView) header.findViewById(R.id.ket);

        // ============ list menu drawer ==============
        Menu menu = navigationView.getMenu();

        MenuItem drawer_home = menu.findItem(R.id.drawer_home);
        drawer_home.setIcon(new IconDrawable(getActivity(), MaterialIcons.md_attach_money).actionBarSize());
        drawer_home.setVisible(false);

        MenuItem drawer_home_pelanggan = menu.findItem(R.id.drawer_home_pelanggan);
        drawer_home_pelanggan.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_user).actionBarSize());

        MenuItem drawer_home_pelayan = menu.findItem(R.id.drawer_home_pelayan);
        drawer_home_pelayan.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_group).actionBarSize());

        MenuItem drawer_home_kasir = menu.findItem(R.id.drawer_home_kasir);
        drawer_home_kasir.setIcon(new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_file_document).actionBarSize());

        MenuItem drawer_home_exit = menu.findItem(R.id.drawer_home_exit);
        drawer_home_exit.setIcon(new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_logout).actionBarSize());

        if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_PELANGGAN) {
            drawer_home.setVisible(false);
            drawer_home_pelanggan.setVisible(false);
            drawer_home_pelayan.setVisible(false);
            drawer_home_kasir.setVisible(false);
            drawer_home_exit.setVisible(true);
            ket.setText(getResources().getString(R.string.mode_pelanggan));
        } else if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_PELAYAN) {
            drawer_home.setVisible(false);
            drawer_home_pelanggan.setVisible(false);
            drawer_home_pelayan.setVisible(false);
            drawer_home_kasir.setVisible(false);
            drawer_home_exit.setVisible(true);
            ket.setText(getResources().getString(R.string.mode_pelayan));
        } else if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_KASIR) {
            drawer_home.setVisible(false);
            drawer_home_pelanggan.setVisible(false);
            drawer_home_pelayan.setVisible(false);
            drawer_home_kasir.setVisible(false);
            drawer_home_exit.setVisible(true);
            ket.setText(getResources().getString(R.string.mode_kasir));
        } else {
            drawer_home.setVisible(false);
            drawer_home_pelanggan.setVisible(false);
            drawer_home_pelayan.setVisible(false);
            drawer_home_kasir.setVisible(false);
            drawer_home_exit.setVisible(false);
            ket.setText(getResources().getString(R.string.app_name));
        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RestaurantIceCream.TOOLBAR_TITLE, toolbar.getTitle().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Toolbar action menu
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_set_url:
                SetUrl();
                return true;

            default:
                return false;
        }
    }

    private void SetUrl() {

        FragmentManager fragmentManager = getChildFragmentManager();
        SetUrlFragment setUrlFragment = new SetUrlFragment();
        setUrlFragment.setTargetFragment(this, 0);
        setUrlFragment.setDialogTitle("Set Url");
        setUrlFragment.show(fragmentManager, "Set Url");
    }


    // Drawer item selection
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawers();
        int id = item.getItemId();
        switch (id) {
            case Menus.DRAWER_HOME:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME);
                return true;
            case Menus.DRAWER_HOME_PELANGGAN:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME_PELANGGAN);
                return true;
            case Menus.DRAWER_HOME_PELAYAN:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME_PELAYAN);
                return true;
            case Menus.DRAWER_HOME_KASIR:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME_KASIR);
                return true;
            case Menus.DRAWER_HOME_EXIT:

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(true);
                }

                FragmentManager fragmentManager = getChildFragmentManager();
                MasterPasswordFragment mp = new MasterPasswordFragment();
                MODE = RestaurantIceCream.MODE_HOME;
                mp.setTargetFragment(this, 0);
                mp.show(fragmentManager, "mp");

                return true;
            default:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME);
                return false;
        }
    }


    public void setSelectedDrawerItem(int view_type) {
        int id;
        switch (view_type) {
            case RestaurantIceCream.VIEW_TYPE_HOME:
                id = Menus.DRAWER_HOME;
                fragment = new HomeFrament();
                break;
            case RestaurantIceCream.VIEW_TYPE_HOME_PELANGGAN:
                id = Menus.DRAWER_HOME_PELANGGAN;
                fragment = new HomePelangganFragment();
                break;
            case RestaurantIceCream.VIEW_TYPE_HOME_PELAYAN:
                id = Menus.DRAWER_HOME_PELAYAN;
                fragment = new HomePelayanFragment();
                break;
            case RestaurantIceCream.VIEW_TYPE_HOME_KASIR:
                id = Menus.DRAWER_HOME_KASIR;
                fragment = new PesananListFragment();
                break;
            default:
                id = Menus.DRAWER_HOME;
                fragment = new HomeFrament();
                break;
        }
        MenuItem item = navigationView.getMenu().findItem(id);
        if (prevMenuItem != null) {
            prevMenuItem.setChecked(false);
        }

        item.setChecked(true);
        prevMenuItem = item;
        toolbar.setSubtitle(item.getTitle());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment, RestaurantIceCream.TAG_GRID_FRAGMENT);
        transaction.commitAllowingStateLoss();
        Prefs.putLastSelected(getActivity(), view_type);
    }


    @Override
    public void onResume() {
        super.onResume();
        SetMenuDrawer();

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
    public void onRefreshDrawer(RefreshDrawer cp) {
        if (cp.isRefresh()) {
            SetMenuDrawer();
            if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_PELANGGAN) {
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME_PELANGGAN);
            } else if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_PELAYAN) {
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME_PELAYAN);
            } else if (Prefs.getModeApp(getActivity()) == RestaurantIceCream.MODE_KASIR) {
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME_KASIR);
            } else {
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME);
            }
        }

        RefreshDrawer stickyEvent = EventBus.getDefault().getStickyEvent(RefreshDrawer.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    @Override
    public void onFinishMasterPassword() {
        if (MODE == RestaurantIceCream.MODE_HOME) {
            Prefs.putIdMeja(getActivity(), null);
            Prefs.putNamaMeja(getActivity(), null);
        }
        Prefs.putModeApp(getActivity(), MODE);
        setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_HOME);
        SetMenuDrawer();
    }
}
