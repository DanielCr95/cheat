package dating.cheat.fragments;


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

import dating.cheat.Adapters.OpenersAdapter;
import dating.cheat.Model.Openers;
import dating.cheat.R;
import dating.cheat.receiver.ConnectivityReceiver;
import dating.cheat.utils.AppController;
import dating.cheat.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener,
        SwipeRefreshLayout.OnRefreshListener {
        public LinearLayout coordinatorLayout;
        public boolean isConnected;
        public RecyclerView recyclerTous;
        public OpenersAdapter adapterOpeners;

        ArrayList<Openers> postOpeners = new ArrayList<>();
        public SwipeRefreshLayout swipeRefreshLayout;
        public String id,team1;
    public OneFragment() {
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
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        coordinatorLayout = (LinearLayout) v.findViewById(R.id.coordinatorLayout);
        recyclerTous = (RecyclerView) v.findViewById(R.id.recycler_tous);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        //swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen,R.color.black,R.color.colorRed);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerTous.setLayoutManager(layoutManager);
        recyclerTous.setNestedScrollingEnabled(false);
        recyclerTous.setItemAnimator(new DefaultItemAnimator());

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
        String TAG = "OPENERS";
        String url = Constants.OPENERS_URL;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("openersResponse", response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    swipeRefreshLayout.setRefreshing(false);


                    Log.e("errorOpeners", "" +error.getMessage());
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
            postOpeners.clear();
            Openers op;
            for(int i=0 ; i<array.length() ; i++)
            {
                jsonObject=array.getJSONObject(i);

                String idOpener= jsonObject.getString("id_opener");
                String refCategory=jsonObject.getString("ref_category");
                String message=jsonObject.getString("message");
                String explanation=jsonObject.getString("explanation");
                String lockStatus=jsonObject.getString("lock_status");

                op = new Openers();
                op.setIdOpener(idOpener);
                op.setRefCategory(refCategory);
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

        adapterOpeners = new OpenersAdapter(getContext(), postOpeners);
        recyclerTous.setAdapter(adapterOpeners);
        swipeRefreshLayout.setRefreshing(false);

    }




}

