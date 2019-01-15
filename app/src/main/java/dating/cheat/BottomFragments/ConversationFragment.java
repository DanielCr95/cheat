package dating.cheat.BottomFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import dating.cheat.Adapters.ExpandableListAdapter;
import dating.cheat.Model.Conversation;
import dating.cheat.Model.Openers;
import dating.cheat.R;
import dating.cheat.receiver.ConnectivityReceiver;
import dating.cheat.utils.AppController;
import dating.cheat.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener,
        SwipeRefreshLayout.OnRefreshListener {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    ArrayList<Conversation> post_array = new ArrayList<>();
    ArrayList<Conversation> post_array2 = new ArrayList<>();
    ArrayList<Conversation> post_array3 = new ArrayList<>();
    ArrayList<Conversation> post_array4 = new ArrayList<>();
    ArrayList<Conversation> post_array5 = new ArrayList<>();
    ArrayList<Conversation> post_array6 = new ArrayList<>();

    HashMap<String, List<String>> listDataChild;
    List<String> listDataGroup ;
    public CoordinatorLayout coordinatorLayout;
    public boolean isConnected;
    public SwipeRefreshLayout swipeRefreshLayout;
    HashMap<String, ArrayList<Conversation>> listDataTeams ;

    public ConversationFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_conversation, container, false);
        // get the listview
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        try {
            getData();
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return v;
    }
    public void getData() throws Exception {
        listDataGroup = new ArrayList<String>();
        listDataTeams = new HashMap<String, ArrayList<Conversation>>();

        if (checkConnectivity()){
            try {
                swipeRefreshLayout.setRefreshing(true);
                getAllPosts();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }else {
            swipeRefreshLayout.setRefreshing(false);

            // getAllPosts();
            showSnack();

        }
    }
    ExpandableListView.OnGroupExpandListener onGroupExpandListenser = new ExpandableListView.OnGroupExpandListener() {
        int previousGroup =-1;
        @Override
        public void onGroupExpand(int groupPosition) {
            if(groupPosition!= previousGroup)
                expListView.collapseGroup(previousGroup);
            previousGroup = groupPosition;
        }
    };




    public boolean checkConnectivity() {
        return ConnectivityReceiver.isConnected();
    }

    public void showSnack() {

        Snackbar.make(coordinatorLayout, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)

                .show();
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

    }
    public void getAllPosts() throws Exception{
        String TAG = "CONVERSATIONS";
        String url = Constants.CONVERSATION_URL;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("conversationsResponse", response);
                listDataGroup.add("Divers");
                listDataGroup.add("Détails personnels");
                listDataGroup.add("Compliments");
                listDataGroup.add("Obtenir son numéro");
                listDataGroup.add("Date");
                listDataGroup.add("Récupération et Négativité");

                // listDataGroup.add(getString(R.string.group_standing3));
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    swipeRefreshLayout.setRefreshing(false);


                    Log.e("errorConversations", "" +error.getMessage());
                }catch (NullPointerException e)
                {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }


            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG);

    }
    private void parseJson(String response) {
        try {

            JSONArray array = new JSONArray(response);
            JSONObject jsonObject = null;
            post_array.clear();
            post_array2.clear();
            post_array3.clear();
            post_array4.clear();
            post_array5.clear();
            post_array6.clear();

            Conversation op;
            for (int i = 0; i < array.length(); i++) {
                jsonObject = array.getJSONObject(i);

                String idConversation = jsonObject.getString("id_conversation");
                String refConvCategory = jsonObject.getString("ref_conv_category");
                String convNameCategory = jsonObject.getString("name_conv_category");
                String message = jsonObject.getString("message");
                String explanation = jsonObject.getString("explanation");
                String lockStatus = jsonObject.getString("lock_status");

                op = new Conversation();
                if (convNameCategory.equals("Divers")) {
                    op.setIdConversation(idConversation);
                    op.setRefConvCategory(refConvCategory);
                    op.setConvNameCategory(convNameCategory);
                    op.setMessage(message);
                    op.setExplanation(explanation);
                    op.setLockStatus(lockStatus);
                    post_array.add(op);

                } else if (convNameCategory.equals("Détails personnels")) {
                    op.setIdConversation2(idConversation);
                    op.setRefConvCategory2(refConvCategory);
                    op.setConvNameCategory2(convNameCategory);
                    op.setMessage2(message);
                    op.setExplanation2(explanation);
                    op.setLockStatus2(lockStatus);
                    post_array2.add(op);

                }
                else if (convNameCategory.equals("Compliments")) {
                    op.setIdConversation3(idConversation);
                    op.setRefConvCategory3(refConvCategory);
                    op.setConvNameCategory3(convNameCategory);
                    op.setMessage3(message);
                    op.setExplanation3(explanation);
                    op.setLockStatus3(lockStatus);
                    post_array3.add(op);

                }

                else if (convNameCategory.equals("Obtenir son numéro")) {
                    op.setIdConversation4(idConversation);
                    op.setRefConvCategory4(refConvCategory);
                    op.setConvNameCategory4(convNameCategory);
                    op.setMessage4(message);
                    op.setExplanation4(explanation);
                    op.setLockStatus4(lockStatus);
                    post_array4.add(op);

                }

                else if (convNameCategory.equals("Date")) {
                    op.setIdConversation5(idConversation);
                    op.setRefConvCategory5(refConvCategory);
                    op.setConvNameCategory5(convNameCategory);
                    op.setMessage5(message);
                    op.setExplanation5(explanation);
                    op.setLockStatus5(lockStatus);
                    post_array5.add(op);

                }

                else if (convNameCategory.equals("Récupération et Négativité")) {
                    op.setIdConversation6(idConversation);
                    op.setRefConvCategory6(refConvCategory);
                    op.setConvNameCategory6(convNameCategory);
                    op.setMessage6(message);
                    op.setExplanation6(explanation);
                    op.setLockStatus6(lockStatus);
                    post_array6.add(op);

                }


            }
            listDataTeams.put(listDataGroup.get(0), post_array); // Group, Child data(Teams)
            listDataTeams.put(listDataGroup.get(1), post_array2); // Group, Child data(Teams)
            listDataTeams.put(listDataGroup.get(2), post_array3); // Group, Child data(Teams)
            listDataTeams.put(listDataGroup.get(3), post_array4); // Group, Child data(Teams)
            listDataTeams.put(listDataGroup.get(4), post_array5); // Group, Child data(Teams)
            listDataTeams.put(listDataGroup.get(5), post_array6); // Group, Child data(Teams)

        } catch (JSONException e) {
            swipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
        }
        listAdapter = new ExpandableListAdapter(getContext(),listDataGroup,listDataTeams);
        expListView.setAdapter(listAdapter);
        swipeRefreshLayout.setRefreshing(false);

    }

}
