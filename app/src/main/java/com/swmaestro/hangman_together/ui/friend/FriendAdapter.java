package com.swmaestro.hangman_together.ui.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.swmaestro.hangman_together.R;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter{
    private Context mContext = null;
    private ArrayList<FriendData> friendData = new ArrayList<FriendData>();

    public FriendAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void setData(ArrayList<FriendData> dataList) {
        if(dataList != null) {
            for(FriendData data : dataList) {
                friendData.add(data);
            }
            notifyDataSetChanged();
        }
    }

    public void removeData() {
        friendData.clear();
        notifyDataSetChanged();
    }

    @Override public int getCount() {
        return friendData.size();
    }

    @Override public Object getItem(int i) {
        return friendData.get(i);
    }

    @Override public long getItemId(int i) {
        return i;
    }

    @Override public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listrow_friend, null);

            holder.tvNickname = (TextView) view.findViewById(R.id.friend_row_tv_nickname);
            holder.btnGiveCandy = (Button) view.findViewById(R.id.friend_row_btn_give_candy);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        FriendData dataUnit = friendData.get(i);

        holder.tvNickname.setText(dataUnit.nickname);

        if(dataUnit.hasGivenCandy.equals("y")) {
            holder.btnGiveCandy.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
        }

        holder.btnGiveCandy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataUnit.hasGivenCandy.equals("n")) {
                    //TODO : 캔디 주기
                }
            }
        });

        return view;
    }

    private class ViewHolder {
        public TextView tvNickname;
        public Button btnGiveCandy;
    }
}
