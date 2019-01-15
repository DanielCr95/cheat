package dating.cheat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dating.cheat.Model.Openers;
import dating.cheat.Model.OpenersCategories;
import dating.cheat.R;

public class CategoriesAdapter extends BaseAdapter {

    public Context mContext;
    public int index=0;
    LayoutInflater layoutInflater;
    public ArrayList<OpenersCategories> originalItems = new ArrayList<>();

    public CategoriesAdapter(Context mContext, ArrayList<OpenersCategories> postList) {
        this.mContext = mContext;
        this.originalItems = postList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return originalItems.size();
    }

    @Override
    public Object getItem(int i) {
        return originalItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView = layoutInflater.inflate(R.layout.rows_categories,parent,false);
        }
        TextView txtCategory = (TextView) convertView.findViewById(R.id.title_category);
        OpenersCategories post = originalItems.get(position);

        txtCategory.setText(post.getNameCategory());
        return convertView;
    }




}
