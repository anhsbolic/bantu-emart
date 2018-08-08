package id.anhs.bantuemart.ui.activity.main;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.anhs.bantuemart.R;
import id.anhs.bantuemart.ui.fragment.home.HomeFragment;
import id.anhs.bantuemart.ui.fragment.iniBaruHome.IniBaruHomeFragment;
import id.anhs.bantuemart.ui.fragment.mainHome.MainHomeFragment;
import id.anhs.bantuemart.ui.fragment.mainSearch.MainSearchFragment;
import id.anhs.bantuemart.ui.fragment.product.ProductFragment;
import id.anhs.bantuemart.ui.fragment.searchOne.SearchOneFragment;
import id.anhs.bantuemart.ui.fragment.uyah.UyahFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MainHomeFragment.OnFragmentInteractionListener,
        MainSearchFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        SearchOneFragment.OnFragmentInteractionListener,
        IniBaruHomeFragment.OnFragmentInteractionListener,
        ProductFragment.OnFragmentInteractionListener,
        UyahFragment.OnFragmentInteractionListener{

    @BindView(R.id.mainToolbar) Toolbar toolbar;
    @BindView(R.id.mainNavView) NavigationView navigationView;
    @BindView(R.id.mainDrawerLayout) DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ActionBar actionBar;
    private Runnable mPendingRunnable = null;
    private Handler mHandler = new Handler();
    private Fragment currentFragment;
    private Fragment lastFragment;
    private boolean isFirstVisit = true;
    private boolean isSecondBackPressed = false;

    private static final String FRAGMENT_MAIN_HOME = "MainHomeFragment";
    private static final String FRAGMENT_MAIN_SEARCH = "MainSearchFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding
        ButterKnife.bind(this);

        // Setup View
        setSupportActionBar(toolbar);
        actionBar = this.getSupportActionBar();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                    mPendingRunnable = null;
                }
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Fragment backStack management
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                lastFragment = fragments.get(fragments.size()-1);
                setMenuItemCheck(lastFragment.getClass().getSimpleName());
            }else{
                uncheckAllMenuItems();
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                lastFragment = fragments.get(0);
            }
        });

        // init selected page : Main Sales Page
        displaySelectedPage(R.id.drawerNavHome);

        // If mPendingRunnable is not null, then add to the message queue
        if(mPendingRunnable != null){
            mHandler.post(mPendingRunnable);
            mPendingRunnable = null;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // ini kalo butuh komunikasi direct dgn main activity
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount == 0) {
                if (isSecondBackPressed) {
                    super.onBackPressed();
                    return;
                }
                this.isSecondBackPressed = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> isSecondBackPressed = false, 3000);
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    // Navigation Configuration
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    // Navigation Configuration
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    // Navigation Configuration
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // Navigation Configuration
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedPage(item.getItemId());
        return true;
    }

    private void displaySelectedPage(int menuItemId) {
        switch (menuItemId) {
            case R.id.drawerNavHome:
                currentFragment = new MainHomeFragment();
                goToFragment();
                break;
            case R.id.drawerNavSearch:
                currentFragment = new MainSearchFragment();
                goToFragment();
                break;
        }
    }

    private void goToFragment() {
        mPendingRunnable = () -> {
            //replacing the fragment
            if(isFirstVisit){
                isFirstVisit = false;
                lastFragment = currentFragment;
                navigationView.setCheckedItem(R.id.drawerNavHome);
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.mainFrameLayout, currentFragment,
                                currentFragment.getClass().getSimpleName())
                        .commit();
            }else{
                if(!Objects.equals(currentFragment.getClass().getSimpleName(),
                        lastFragment.getClass().getSimpleName())){
                    getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.mainFrameLayout, currentFragment,
                                    currentFragment.getClass().getSimpleName())
                            .addToBackStack(lastFragment.getClass().getSimpleName())
                            .commit();
                }
            }
        };
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setMenuItemCheck(String fragmentName) {
        switch (fragmentName) {
            case FRAGMENT_MAIN_HOME:
                navigationView.setCheckedItem(R.id.drawerNavHome);
                break;
            case FRAGMENT_MAIN_SEARCH:
                navigationView.setCheckedItem(R.id.drawerNavSearch);
                break;
            default:
                //do nothing
                break;
        }
    }

    private void uncheckAllMenuItems() {
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }

    // public method, so fragment can call it
    public void setTitle(int titleId) {
        actionBar.setTitle(titleId);
    }
}
