package com.swmaestro.hangman_together.ui.stash;

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
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.takecandy.TakeCandyResponse;
import com.swmaestro.hangman_together.rest.takecandy.TakeCandyService;

import java.util.ArrayList;

import retrofit2.Call;

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
            requestTakeCandy(dataUnit.stashIdx, i);

        });

        return view;
    }

    private void requestTakeCandy(String stashIdx, int i) {
        try {
            TakeCandyService takeCandyService = RetrofitManager.getInstance().getService(TakeCandyService.class);
            Call<JsonObject> call = takeCandyService.takeCandyRequest(stashIdx);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    String responseString = "n";

                    try {
                        Gson gson = new Gson();
                        String resultRaw = response.body().toString();
                        TakeCandyResponse obj = new TakeCandyResponse();
                        obj = gson.fromJson(resultRaw, TakeCandyResponse.class);
                        responseString = obj.getMessage();
                        //Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();

                        if(responseString.equals("y")) {
                            stashData.remove(i);
                            notifyDataSetChanged();
                            Toast.makeText(mContext, "친구에게 받은 캔디를 얻었습니다.", Toast.LENGTH_SHORT).show();
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
        public Button btnTakeCandy;
    }
}
