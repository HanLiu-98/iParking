package xyz.hanliu.iparking.user.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.User;
import xyz.hanliu.iparking.data.GlobalData;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    TextView tv;
    User user;
    String s;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) getActivity().findViewById(R.id.tv_display);
//        String s=GlobalData.user
        tv.setText(GlobalData.user.toString());
    }


}
