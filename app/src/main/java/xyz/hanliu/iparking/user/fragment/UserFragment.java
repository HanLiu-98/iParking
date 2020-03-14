package xyz.hanliu.iparking.user.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.User;
import xyz.hanliu.iparking.base.BaseFragment;
import xyz.hanliu.iparking.data.GlobalData;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment {
    TextView tv;
    User user;
    String s;



    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        tv = (TextView) getActivity().findViewById(R.id.tv_display);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv.setText(GlobalData.user.toString());
    }


}
