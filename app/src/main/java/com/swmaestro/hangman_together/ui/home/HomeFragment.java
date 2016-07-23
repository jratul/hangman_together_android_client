package com.swmaestro.hangman_together.ui.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.common.HangmanData;
import com.swmaestro.hangman_together.common.Util;
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.home.HomeResponse;
import com.swmaestro.hangman_together.rest.home.HomeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    @BindString(R.string.intro_value_connection_error) String valueConnectionErrorMessage;
    @BindString(R.string.home_value_myscore_label) String valueMyscoreLabel;
    @BindString(R.string.home_value_mycandy_label) String valueMycandyLabel;

    @BindView(R.id.home_tv_mynickname) TextView tvMynickname;
    @BindView(R.id.home_tv_myscore) TextView tvMyscore;
    @BindView(R.id.home_tv_mycandy) TextView tvMycandy;
    @BindView(R.id.home_lv_rank) ListView lvRank;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    static Unbinder butterKnifeUnbinder;

    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private RankAdapter rankAdapter = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        butterKnifeUnbinder = ButterKnife.bind(this, view);

        tvMynickname.setText(Util.getPreferences(getContext(), HangmanData.KEY_USER_NICKNAME) + "님의 정보");

        rankAdapter = new RankAdapter(mContext);

        requestHomeData(Util.getPreferences(mContext, HangmanData.KEY_USER_PHONE_NUM));

        lvRank.setAdapter(rankAdapter);

        return view;
    }

    private void requestHomeData(String phoneNum) {
        try {
            HomeService homeService = RetrofitManager.getInstance().getService(HomeService.class);
            Call<JsonObject> call = homeService.homeRequest(phoneNum);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    String responseString = "n";

                    try {
                        Gson gson = new Gson();
                        String resultRaw = response.body().toString();
                        HomeResponse obj = new HomeResponse();
                        obj = gson.fromJson(resultRaw, HomeResponse.class);
                        responseString = obj.getMessage();
                        //Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();

                        if(responseString.equals("y")) {
                            tvMyscore.setText(valueMyscoreLabel + obj.getMyscore());
                            tvMycandy.setText(valueMycandyLabel + obj.getMycandy());
                            List<String> rankNickname = obj.getRankNickname();
                            List<String> rankScore = obj.getRankScore();

                            ArrayList<RankData> dataList = new ArrayList<RankData>();

                            for(int i=0;i<rankNickname.size();i++) {
                                dataList.add(new RankData(rankNickname.get(i), rankScore.get(i)));
                            }

                            if(dataList.size() > 0) {
                                rankAdapter.setData(dataList);
                            }

                        } else if(responseString.equals("n")) {

                        }
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }catch(Exception e) {
            Log.d("intro", e.getMessage());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override public void onDestroy() {
        super.onDestroy();

        if(butterKnifeUnbinder!=null) butterKnifeUnbinder.unbind();
    }


}
