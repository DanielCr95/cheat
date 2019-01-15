package dating.cheat.Adapters;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import dating.cheat.Model.Conversation;
import dating.cheat.R;
import dating.cheat.UI.DetailActivity;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Conversation>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, ArrayList<Conversation>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Conversation childText = (Conversation) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        if(groupPosition == 0)
        {
            txtListChild.setText(childText.getMessage());

        }
        else if(groupPosition == 1)
        {
            txtListChild.setText(childText.getMessage2());

        }
        else if(groupPosition == 2)
        {
            txtListChild.setText(childText.getMessage3());

        }
        else if(groupPosition == 3)
        {
            txtListChild.setText(childText.getMessage4());

        }
        else if(groupPosition == 4)
        {
            txtListChild.setText(childText.getMessage5());

        }
        else if(groupPosition == 5)
        {
            txtListChild.setText(childText.getMessage6());

        }
        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(_context,DetailActivity.class);
                switch (groupPosition)
                {
                    case 0 :
                    {
                        i.putExtra("message",childText.getMessage());
                        i.putExtra("explanation",childText.getExplanation());
                        break;
                    }

                    case 1 :
                    {
                        i.putExtra("message",childText.getMessage2());
                        i.putExtra("explanation",childText.getExplanation2());
                        break;
                    }

                    case 2 :
                    {
                        i.putExtra("message",childText.getMessage3());
                        i.putExtra("explanation",childText.getExplanation3());
                        break;
                    }

                    case 3 :
                    {
                        i.putExtra("message",childText.getMessage4());
                        i.putExtra("explanation",childText.getExplanation4());
                        break;
                    }

                    case 4 :
                    {
                        i.putExtra("message",childText.getMessage5());
                        i.putExtra("explanation",childText.getExplanation5());
                        break;
                    }

                    case 5 :
                    {
                        i.putExtra("message",childText.getMessage6());
                        i.putExtra("explanation",childText.getExplanation6());
                        break;
                    }


                }

                _context.startActivity(i);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}