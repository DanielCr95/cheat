package dating.cheat.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dating.cheat.Adapters.OpenersAdapter;
import dating.cheat.BottomFragments.ConversationFragment;
import dating.cheat.BottomFragments.MoreFragment;
import dating.cheat.BottomFragments.ThankyouFragment;
import dating.cheat.MainActivity;
import dating.cheat.Model.CatDetails;
import dating.cheat.Model.Openers;
import dating.cheat.R;
import dating.cheat.receiver.ConnectivityReceiver;
import dating.cheat.utils.AppController;
import dating.cheat.utils.Constants;

public class CategoryDetailActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        SwipeRefreshLayout.OnRefreshListener {
    public LinearLayout coordinatorLayout;
    public boolean isConnected;
    public RecyclerView recyclerTous;
    public OpenersAdapter adapterOpeners;
    BottomNavigationView mBottomNavigationView;
    BottomNavigationView navigation;
    BottomNavigationItemView itemView;
    BottomNavigationMenuView bottomNavigationMenuView;
    View v2,view,badge;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int lastSelectedItem = -1;
    ArrayList<Openers> postOpeners = new ArrayList<>();
    public SwipeRefreshLayout swipeRefreshLayout;
    public String id;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
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
        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinatorLayout);
        recyclerTous = (RecyclerView) findViewById(R.id.recycler_tous);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        //swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen,R.color.black,R.color.colorRed);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTous.setLayoutManager(layoutManager);
        recyclerTous.setNestedScrollingEnabled(false);
        recyclerTous.setItemAnimator(new DefaultItemAnimator());

        try {
             id = getIntent().getStringExtra("id");
            getData();
        } catch (Exception e) {
            e.printStackTrace();
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
                            Intent i = new Intent(CategoryDetailActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        break;

                    case R.id.menu_update:
                        fragCategory = new ConversationFragment();
                        replaceFragment(fragCategory);
                        recyclerTous.setVisibility(View.GONE);
                        break;

                    case R.id.menu_notification:
                        fragCategory = new MoreFragment();
                        replaceFragment(fragCategory);
                        recyclerTous.setVisibility(View.GONE);

                        break;

                    case R.id.menu_account:
                        fragCategory = new ThankyouFragment();
                        replaceFragment(fragCategory);
                        recyclerTous.setVisibility(View.GONE);


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

    public void getData() throws Exception {
        if (checkConnectivity()){
            try {
                swipeRefreshLayout.setRefreshing(true);
                getCatDetails();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }else {
            swipeRefreshLayout.setRefreshing(false);
            showSnack();

        }
    }

    public boolean checkConnectivity() {
        return ConnectivityReceiver.isConnected();
    }

    public void showSnack() {

        Snackbar.make(coordinatorLayout, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityReceiver(this);
    }


    @Override
    public void onRefresh() {
        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onNetworkChange(boolean inConnected) {
        this.isConnected = inConnected;
        // Toast.makeText(getContext(),"the app network have been changed",Toast.LENGTH_SHORT).show();

    }
    public void getCatDetails() throws Exception{
        String TAG = "getCatDetails";
        String url = Constants.CATDETAILS_URL;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("getCatDetailsResponse", response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    swipeRefreshLayout.setRefreshing(false);


                    Log.e("errorgetCatDetails", "" +error.getMessage());
                }catch (NullPointerException e)
                {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }


            }
        })   {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params= new HashMap<String,String>();
                params.put("id",id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG);

    }
    public void parseJson(String response){

        try {

            JSONArray array = new JSONArray(response);
            JSONObject jsonObject =null;
            postOpeners.clear();
            Openers op;
            for(int i=0 ; i<array.length() ; i++)
            {
                jsonObject=array.getJSONObject(i);

                String idOpener= jsonObject.getString("id_opener");
                String message=jsonObject.getString("message");
                String explanation=jsonObject.getString("explanation");
                String lockStatus=jsonObject.getString("lock_status");

                op = new Openers();
                op.setIdOpener(idOpener);
                op.setMessage(message);
                op.setExplanation(explanation);
                op.setLockStatus(lockStatus);
                postOpeners.add(op);
            }

        }
        catch (JSONException e) {
            swipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
        }

        adapterOpeners = new OpenersAdapter(this, postOpeners);
        recyclerTous.setAdapter(adapterOpeners);
        swipeRefreshLayout.setRefreshing(false);

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
