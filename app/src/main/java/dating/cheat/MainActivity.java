package dating.cheat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dating.cheat.BottomFragments.ConversationFragment;
import dating.cheat.BottomFragments.MoreFragment;
import dating.cheat.BottomFragments.ThankyouFragment;
import dating.cheat.fragments.FourFragment;
import dating.cheat.fragments.OneFragment;
import dating.cheat.fragments.ThreeFragment;
import dating.cheat.fragments.TwoFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    BottomNavigationView mBottomNavigationView;
    BottomNavigationView navigation;
    BottomNavigationItemView itemView;
    BottomNavigationMenuView bottomNavigationMenuView;
    View v2,view,badge;
    private int lastSelectedItem = -1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        navigation = (BottomNavigationView)findViewById(R.id.bottom_view);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("TOUS"));
        tabLayout.addTab(tabLayout.newTab().setText("CATÉGORIES"));
        tabLayout.addTab(tabLayout.newTab().setText("FAVORIS"));
        tabLayout.addTab(tabLayout.newTab().setText("RECHERCHE"));
        replaceFragment(new OneFragment());
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new OneFragment());
                } else if (tab.getPosition() == 1) {
                    replaceFragment(new TwoFragment());
                } else if (tab.getPosition() == 2) {
                    replaceFragment(new ThreeFragment());
                }else if (tab.getPosition() == 3) {
                    replaceFragment(new FourFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        v2 = bottomNavigationMenuView.getChildAt(2);
        itemView = (BottomNavigationItemView) v2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            disableShiftMode(navigation);
            navigation.setItemIconTintList(null);

        }


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragCategory = null;

                // init corresponding fragment
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragCategory = new OneFragment();
                        replaceFragment2(fragCategory);
                        tabLayout.setVisibility(View.VISIBLE);

                        break;

                    case R.id.menu_update:
                        fragCategory = new ConversationFragment();
                        replaceFragment(fragCategory);
                        tabLayout.setVisibility(View.GONE);
                        break;

                    case R.id.menu_notification:
                        fragCategory = new MoreFragment();
                        replaceFragment(fragCategory);
                        break;

                    case R.id.menu_account:
                        fragCategory = new ThankyouFragment();
                        replaceFragment(fragCategory);
                        break;

                }
                //Set bottom menu selected item text in toolbar
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(item.getTitle());
                }
                return true;
            }
        });

        navigation.setSelectedItemId(R.id.menu_home);
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
    private void replaceFragment2(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        transaction.commit();
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                item.setPadding(0, 10,0, 0);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }


}
