package xyz.hanliu.iparking.user.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.base.BaseFragment;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.user.activity.DepositActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment {
    TextView tv_nickname;
    Button btn_deposite;



    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_user, null);
        tv_nickname = view.findViewById(R.id.tv_nickname_user);
        tv_nickname.setText(GlobalData.user.getNickname());
        btn_deposite = view.findViewById(R.id.btn_deposite_user);
        return view;
    }


    /**
     * 控件点击事件的绑定
     */
    private void initListener() {
        /*钱包充值按钮的响应事件*/
        btn_deposite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DepositActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化界面数据
     */
    @Override
    public void initData() {
        super.initData();
        initListener();
    }
}
