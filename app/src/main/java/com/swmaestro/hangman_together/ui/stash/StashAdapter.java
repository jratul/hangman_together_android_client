package com.swmaestro.hangman_together.ui.stash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.swmaestro.hangman_together.R;

import java.util.ArrayList;

public class StashAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<StashData> stashData = new ArrayList<StashData>();

    public StashAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void setData(ArrayList<StashData> dataList) {
        if(dataList != null) {
            for(StashData data : dataList) {
                stashData.add(data);
            }
            notifyDataSetChanged();
        }
    }

    @Override public int getCount() {
        return stashData.size();
    }

    @Override public Object getItem(int i) {
        return stashData.get(i);
    }

    @Override public long getItemId(int i) {
        return i;
    }

    @Override public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listrow_stash, null);

            holder.tvNickname = (TextView) view.findViewById(R.id.stash_row_tv_nickname);
            holder.btnTakeCandy = (Button) view.findViewById(R.id.stash_row_btn_take_candy);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        StashData dataUnit = stashData.get(i);

        holder.tvNickname.setText(dataUnit.nickname);

        holder.btnTakeCandy.setOnClickListener(v -> {
            //TODO : 캔디 받기
        });

        return view;
    }

    private class ViewHolder {
        public TextView tvNickname;
        public Button btnTakeCandy;
    }
}
