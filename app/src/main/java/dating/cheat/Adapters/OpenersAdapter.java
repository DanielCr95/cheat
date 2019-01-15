package dating.cheat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dating.cheat.Model.Openers;
import dating.cheat.R;

public class OpenersAdapter extends RecyclerView.Adapter<OpenersAdapter.ViewHolder>{

    public Context mContext;
    public int index=0;

    public ArrayList<Openers> originalItems = new ArrayList<>();

    public OpenersAdapter(Context mContext, ArrayList<Openers> postList) {
        this.mContext = mContext;
        this.originalItems = postList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tous_fragment, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {

            final Openers post = originalItems.get(position);

            if(index%2==0)
            {
                ((RelativeLayout) holder.relativeLayout).setBackgroundColor(Color.parseColor("#E0E0E0"));

            }
            else
            {
                ((RelativeLayout) holder.relativeLayout).setBackgroundColor(Color.WHITE);

            }
            index=index+1;


            holder.txtTous.setText(post.getMessage());




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return originalItems.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTous;
        private ImageView imgTous;
        private RelativeLayout relativeLayout;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTous = (TextView) itemView.findViewById(R.id.title_tous);
            imgTous=(ImageView) itemView.findViewById(R.id.icon_tous);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relative_tous);



        }



    }

}
