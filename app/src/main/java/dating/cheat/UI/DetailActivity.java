package dating.cheat.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import dating.cheat.BottomFragments.ConversationFragment;
import dating.cheat.BottomFragments.MoreFragment;
import dating.cheat.BottomFragments.ThankyouFragment;
import dating.cheat.MainActivity;
import dating.cheat.R;
import dating.cheat.fragments.OneFragment;

public class DetailActivity extends AppCompatActivity {
    BottomNavigationView mBottomNavigationView;
    BottomNavigationView navigation;
    BottomNavigationItemView itemView;
    BottomNavigationMenuView bottomNavigationMenuView;
    View v2,view,badge;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int lastSelectedItem = -1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        navigation = (BottomNavigationView)findViewById(R.id.bottom_view);
        bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        v2 = bottomNavigationMenuView.getChildAt(2);
        itemView = (BottomNavigationItemView) v2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            disableShiftMode(navigation);
            navigation.setItemIconTintList(null);

        }

        final TextView message = (TextView) findViewById(R.id.message);
        final TextView explanation = (TextView) findViewById(R.id.explanation);
        message.setMovementMethod(new ScrollingMovementMethod());
        explanation.setMovementMethod(new ScrollingMovementMethod());
try {
        String stMessage = getIntent().getStringExtra("message");
        String stExplanation = getIntent().getStringExtra("explanation");
        message.setText(stMessage);
        explanation.setText(stExplanation);
}catch (Exception e)
{
    e.getMessage();
}
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragCategory = null;

                // init corresponding fragment
                switch (item.getItemId()) {
                    case R.id.menu_home:

                        if(navigation.getMenu().getItem(0).isChecked()) {
                        }
                        else
                        {
                            Intent i = new Intent(DetailActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        break;

                    case R.id.menu_update:
                        fragCategory = new ConversationFragment();
                        replaceFragment(fragCategory);
                        message.setVisibility(View.GONE);
                        explanation.setVisibility(View.GONE);
                        break;

                    case R.id.menu_notification:
                        fragCategory = new MoreFragment();
                        replaceFragment(fragCategory);
                        message.setVisibility(View.GONE);
                        explanation.setVisibility(View.GONE);
                        break;

                    case R.id.menu_account:
                        fragCategory = new ThankyouFragment();
                        replaceFragment(fragCategory);
                        message.setVisibility(View.GONE);
                        explanation.setVisibility(View.GONE);

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
      //  TabLayout.Tab tab = tabLayout.getTabAt(0);
       // tab.select();
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
