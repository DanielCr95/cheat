package dating.cheat.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dating.cheat.Adapters.CategoriesAdapter;
import dating.cheat.Adapters.OpenersAdapter;
import dating.cheat.Model.Openers;
import dating.cheat.Model.OpenersCategories;
import dating.cheat.R;
import dating.cheat.receiver.ConnectivityReceiver;
import dating.cheat.utils.AppController;
import dating.cheat.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener,
        SwipeRefreshLayout.OnRefreshListener {
    public LinearLayout coordinatorLayout;
    public boolean isConnected;
    public GridView gridView;
    public CategoriesAdapter adapterCategories;
    public Context c;
    ArrayList<OpenersCategories> postCategories = new ArrayList<>();
    public SwipeRefreshLayout swipeRefreshLayout;
    public String id,team1;
    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        checkConnectivity();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_two, container, false);
        coordinatorLayout = (LinearLayout) v.findViewById(R.id.coordinatorLayout);
        gridView = (GridView) v.findViewById(R.id.gv);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        //swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen,R.color.black,R.color.colorRed);


        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public void getData() throws Exception {
        if (checkConnectivity()){
            try {
                swipeRefreshLayout.setRefreshing(true);
                getTous();
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
    public void getTous() throws Exception{
        String TAG = "CATEGORIES";
        String url = Constants.CATEGORIES_URL;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("categoriesResponse", response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    swipeRefreshLayout.setRefreshing(false);


                    Log.e("erroCategories", "" +error.getMessage());
                }catch (NullPointerException e)
                {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }


            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG);

    }
    public void parseJson(String response){

        try {

            JSONArray array = new JSONArray(response);
            JSONObject jsonObject =null;
            postCategories.clear();
            OpenersCategories op;
            for(int i=0 ; i<array.length() ; i++)
            {
                jsonObject=array.getJSONObject(i);

                String idCategory= jsonObject.getString("id_category");
                String nameCategory =jsonObject.getString("name_category");


                op = new OpenersCategories();
                op.setIdCategory(idCategory);
                op.setNameCategory(nameCategory);

                postCategories.add(op);
            }

        }
        catch (JSONException e) {
            swipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
        }

        adapterCategories = new CategoriesAdapter(getActivity(), postCategories);
        gridView.setAdapter(adapterCategories);
        swipeRefreshLayout.setRefreshing(false);

    }




}

