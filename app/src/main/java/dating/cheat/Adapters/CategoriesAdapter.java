package dating.cheat.Adapters;

import android.content.Context;
import android.content.Intent;
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
import dating.cheat.UI.CategoryDetailActivity;

public class CategoriesAdapter extends BaseAdapter {

    public Context mContext;
    public int index=0;
    LayoutInflater layoutInflater;
    public ArrayList<OpenersCategories> originalItems = new ArrayList<>();

    public CategoriesAdapter(Context mContext, ArrayList<OpenersCategories> postList) {
        this.mContext = mContext;
        this.originalItems = postList;
        if(layoutInflater == null)
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
        final OpenersCategories post = originalItems.get(position);
        txtCategory.setText(post.getNameCategory());
        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,CategoryDetailActivity.class);
                i.putExtra("id",post.getIdCategory());
                mContext.startActivity(i);
            }
        });
        return convertView;
    }




}
