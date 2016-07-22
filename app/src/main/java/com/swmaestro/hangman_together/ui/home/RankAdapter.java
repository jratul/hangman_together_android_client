package com.swmaestro.hangman_together.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swmaestro.hangman_together.R;

import java.util.ArrayList;

public class RankAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<RankData> rankData = new ArrayList<RankData>();

    public RankAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void setData(ArrayList<RankData> dataList) {
        if(dataList != null) {
            for(RankData data : dataList) {
                rankData.add(data);
            }
            notifyDataSetChanged();
        }
    }
    @Override public int getCount() {
        return rankData.size();
    }

    @Override public Object getItem(int i) {
        return rankData.get(i);
    }

    @Override public long getItemId(int i) {
        return i;
    }

    @Override public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listrow_rank, null);

            holder.tvRank = (TextView) view.findViewById(R.id.rank_row_tv_rank);
            holder.tvNickname = (TextView) view.findViewById(R.id.rank_row_tv_nickname);
            holder.tvScore = (TextView) view.findViewById(R.id.rank_row_tv_score);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        RankData dataUnit = rankData.get(i);

        holder.tvRank.setText(Integer.toString(i+1));
        holder.tvNickname.setText(dataUnit.nickname);
        holder.tvScore.setText(dataUnit.score);

        return view;
    }

    private class ViewHolder {
        public TextView tvRank;
        public TextView tvNickname;
        public TextView tvScore;
    }
}
