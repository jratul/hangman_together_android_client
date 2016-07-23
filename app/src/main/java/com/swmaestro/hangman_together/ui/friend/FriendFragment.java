package com.swmaestro.hangman_together.ui.friend;

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
import com.swmaestro.hangman_together.rest.getfriend.GetFriendResponse;
import com.swmaestro.hangman_together.rest.getfriend.GetFriendService;

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
 * {@link FriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
    @BindString(R.string.intro_value_connection_error) String valueConnectionErrorMessage;

    @BindView(R.id.friend_tv_no_friend) TextView tvNoFriend;
    @BindView(R.id.friend_lv_friend) ListView lvFriend;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    static Unbinder butterKnifeUnbinder;

    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private FriendAdapter friendAdapter = null;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        butterKnifeUnbinder = ButterKnife.bind(this, view);

        requestGetFriend(Util.getPreferences(mContext, HangmanData.KEY_USER_PHONE_NUM));

        friendAdapter = new FriendAdapter(mContext);
        lvFriend.setAdapter(friendAdapter);

        return view;
    }

    private void requestGetFriend(String phoneNum) {
        try {
            GetFriendService getFriendService = RetrofitManager.getInstance().getService(GetFriendService.class);
            Call<JsonObject> call = getFriendService.getFriendRequest(phoneNum);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    String responseString = "n";

                    try {
                        Gson gson = new Gson();
                        String resultRaw = response.body().toString();
                        GetFriendResponse obj = new GetFriendResponse();
                        obj = gson.fromJson(resultRaw, GetFriendResponse.class);
                        responseString = obj.getMessage();
                        //Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();

                        if(responseString.equals("y")) {
                            List<String> friendNickname = obj.getFriendNickname();
                            List<String> hasGivenCandy = obj.getHasGivenCandy();

                            ArrayList<FriendData> dataList = new ArrayList<FriendData>();

                            for(int i=0;i<friendNickname.size();i++) {
                                dataList.add(new FriendData(friendNickname.get(i), hasGivenCandy.get(i)));
                            }

                            if(dataList.size() > 0) {
                                friendAdapter.setData(dataList);
                                tvNoFriend.setVisibility(View.GONE);
                            }
                        } else if(responseString.equals("n")) {

                        }
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());

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
