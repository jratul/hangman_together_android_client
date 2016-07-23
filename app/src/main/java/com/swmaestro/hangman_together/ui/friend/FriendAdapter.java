package com.swmaestro.hangman_together.ui.friend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.common.HangmanData;
import com.swmaestro.hangman_together.common.Util;
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.givecandy.GiveCandyResponse;
import com.swmaestro.hangman_together.rest.givecandy.GiveCandyService;

import java.util.ArrayList;

import retrofit2.Call;

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
                    requestGiveCandy(Util.getPreferences(mContext, HangmanData.KEY_USER_PHONE_NUM), dataUnit.nickname, i, holder.btnGiveCandy);
                }
            }
        });

        return view;
    }

    private void requestGiveCandy(String phoneNum, String friendNickname, int i, Button btnGiveCandy) {
        try {
            GiveCandyService giveCandyService = RetrofitManager.getInstance().getService(GiveCandyService.class);
            Call<JsonObject> call = giveCandyService.giveCandyRequest(phoneNum, friendNickname);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    String responseString = "n";

                    try {
                        Gson gson = new Gson();
                        String resultRaw = response.body().toString();
                        GiveCandyResponse obj = new GiveCandyResponse();
                        obj = gson.fromJson(resultRaw, GiveCandyResponse.class);
                        responseString = obj.getMessage();
                        //Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();

                        if(responseString.equals("y")) {
                            friendData.get(i).hasGivenCandy = "y";
                            btnGiveCandy.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                            Toast.makeText(mContext, friendNickname + "에게 캔디를 선물했습니다.", Toast.LENGTH_SHORT).show();
                        } else if(responseString.equals("f")) {
                            Toast.makeText(mContext, friendNickname + "의 캔디 보관함이 가득 차 있습니다.", Toast.LENGTH_SHORT).show();
                        } else if(responseString.equals("n")) {

                        }
                    } catch(Exception e) {
                        Toast.makeText(mContext, "서버와의 접속에 실패했습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());

                        return;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(mContext, "서버와의 접속에 실패했습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }catch(Exception e) {
            Log.d("intro", e.getMessage());
        }
    }

    private class ViewHolder {
        public TextView tvNickname;
        public Button btnGiveCandy;
    }
}
