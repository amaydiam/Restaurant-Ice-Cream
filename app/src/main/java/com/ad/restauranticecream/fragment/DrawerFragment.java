package com.ad.restauranticecream.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.utils.Menus;
import com.ad.restauranticecream.utils.Prefs;
import com.ad.restauranticecream.widget.RobotoBoldTextView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.MaterialCommunityIcons;
import com.joanzapata.iconify.fonts.MaterialIcons;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DrawerFragment extends Fragment implements OnMenuItemClickListener, OnNavigationItemSelectedListener, LoginFragment.LoginListener {

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

        // Load previously selected drawer item
        int view_type = Prefs.getLastSelected(getActivity());
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
        Menu menu = navigationView.getMenu();//donasi
        MenuItem drawer_donasi = menu.findItem(R.id.drawer_donasi);
        drawer_donasi.setIcon(
                new IconDrawable(getActivity(), MaterialIcons.md_attach_money)
                        .actionBarSize());
//donasi
        MenuItem drawer_laporan_donasi = menu.findItem(R.id.drawer_laporan_donasi);
        drawer_laporan_donasi.setIcon(new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_file_document).actionBarSize());
//mustahiq
        MenuItem drawer_mustahiq = menu.findItem(R.id.drawer_mustahiq);
        drawer_mustahiq.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_group).actionBarSize());
        //calon_mustahiq
        MenuItem drawer_calon_mustahiq = menu.findItem(R.id.drawer_calon_mustahiq);
        drawer_calon_mustahiq.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_user).actionBarSize());

        MenuItem drawer_logout_login = menu.findItem(R.id.drawer_logout_login);
        if (Prefs.getLogin(getActivity())) {
            drawer_donasi.setVisible(false);
            drawer_mustahiq.setVisible(true);
            drawer_calon_mustahiq.setVisible(true);
            drawer_logout_login.setTitle("Logout");
            drawer_logout_login.setIcon(new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_logout).actionBarSize());
            if (Prefs.getTipeUser(getActivity()).equalsIgnoreCase("1")) {
                ket.setText("Admin");
                ket.setVisibility(VISIBLE);
            } else {
                drawer_donasi.setVisible(true);
                drawer_mustahiq.setVisible(false);
                ket.setVisibility(GONE);
            }
        } else {

            drawer_calon_mustahiq.setVisible(true);
            if (!Prefs.getTipeUser(getActivity()).equalsIgnoreCase("2")) {
                drawer_calon_mustahiq.setVisible(false);
            }
            drawer_donasi.setVisible(true);
            drawer_mustahiq.setVisible(false);
            drawer_logout_login.setTitle("Login");
            drawer_logout_login.setIcon(new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_login).actionBarSize());
            ket.setText(getResources().getString(R.string.app_name));

            ket.setVisibility(VISIBLE);
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
            case Menus.DRAWER_DONASI:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_DONASI);
                return true;
            case Menus.DRAWER_LAPORAN_DONASI:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_LAPORAN_DONASI);
                return true;
            case Menus.DRAWER_MUSTAHIQ:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_MUSTAHIQ);
                return true;
            case Menus.DRAWER_CALON_MUSTAHIQ:
                setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_CALON_MUSTAHIQ);
                return true;
            case Menus.DRAWER_LOGOUT_LOGIN:
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(true);
                }
                if (Prefs.getLogin(getActivity())) {
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
                    alt_bld.setMessage("Apakah anda akan keluar?")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Prefs.putLogin(getActivity(), false);
                                    SetMenuDrawer();
                                    setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_DONASI);
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alert.show();
                } else {
//ket Aktifity Login
                    FragmentManager fragmentManager = getChildFragmentManager();
                    LoginFragment Login = new LoginFragment();
                    Login.setTargetFragment(this, 0);
                    Login.show(fragmentManager, "Login");
                }
                return true;
            default:
                return false;
        }
    }


    public void setSelectedDrawerItem(int view_type) {
        int id;
        switch (view_type) {
            case RestaurantIceCream.VIEW_TYPE_DONASI:
                id = Menus.DRAWER_DONASI;
                fragment = new DonasiMapFragment();
                break;
            case RestaurantIceCream.VIEW_TYPE_LAPORAN_DONASI:
                id = Menus.DRAWER_LAPORAN_DONASI;
                fragment = new LaporanDonasiListFragment();
                break;
            case RestaurantIceCream.VIEW_TYPE_MUSTAHIQ:
                id = Menus.DRAWER_MUSTAHIQ;
                fragment = MustahiqListFragment.newInstance();
                break;
            case RestaurantIceCream.VIEW_TYPE_CALON_MUSTAHIQ:
                id = Menus.DRAWER_CALON_MUSTAHIQ;
                fragment = CalonMustahiqListFragment.newInstance();
                break;
            default:
                id = Menus.DRAWER_MUSTAHIQ;
                fragment = MustahiqListFragment.newInstance();
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
    public void onFinishLogin(String id_user, String tipe_user, String nama, String alamat, String no_telp, String no_identitas) {
        Prefs.putLogin(getActivity(), true);
        Prefs.putIdUser(getActivity(), id_user);
        Prefs.putTipeUser(getActivity(), tipe_user);
        Prefs.putNamaUser(getActivity(), nama);
        Prefs.putAlamatUser(getActivity(), alamat);
        Prefs.putNomorTelpUser(getActivity(), no_telp);
        Prefs.putNomorIdentitasUser(getActivity(), no_identitas);
        SetMenuDrawer();
        if(Prefs.getIdUser(getActivity()).equalsIgnoreCase("2"))
            setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_DONASI);
            else
        setSelectedDrawerItem(RestaurantIceCream.VIEW_TYPE_MUSTAHIQ);
    }
}
