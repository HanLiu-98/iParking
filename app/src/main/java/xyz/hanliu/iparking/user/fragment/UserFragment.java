package xyz.hanliu.iparking.user.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.User;
import xyz.hanliu.iparking.base.BaseFragment;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.user.activity.DepositActivity;
import xyz.hanliu.iparking.user.activity.MyReleaseActivity;
import xyz.hanliu.iparking.user.activity.OrdersActivity;
import xyz.hanliu.iparking.user.activity.WithdrawalActivity;
import xyz.hanliu.iparking.utils.AlertDialogUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment {
    TextView tv_nickname;
    Button btn_deposite;
    Button btn_withdrawal;
    TextView tv_balance;

    Button btn_waitpayorder;
    Button btn_payedorder;

    Button btn_myrelease;


   /**
     * 填充布局，控件绑定
     */
    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_user, null);
        tv_nickname = view.findViewById(R.id.tv_nickname_user);
        tv_balance = view.findViewById(R.id.tv_balance_user);
        btn_deposite = view.findViewById(R.id.btn_deposite_user);
        btn_withdrawal = view.findViewById(R.id.btn_withdrawal_user);
        btn_waitpayorder = view.findViewById(R.id.btn_waitpayorder_user);
        btn_payedorder = view.findViewById(R.id.btn_payedorder_user);
        btn_myrelease=view.findViewById(R.id.btn_myrelease_user);
        return view;
    }


    /**
     * 初始化界面数据
     */
    @Override
    public void initData() {
        super.initData();
        initListener();
        requestLatestUserData(GlobalData.user.getMobile());
    }


    /**
     * 获取当前登录用户最新信息（主要是余额和昵称）
     */
    public void requestLatestUserData(String mobile) {
        OkHttpUtils.post()
                .url(Config.URL_GETUSERDATA)
                .addParams("mobile", mobile)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AlertDialogUtil.showNetErrorAlertDialog(mContext, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processData(response);
                    }
                });
    }

    /**
     * 处理服务器传回来的JSON数据，并且更新界面
     */
    private void processData(String json) {
        /*处理JSON数据*/
        User user = JSON.parseObject(json, User.class);
        /*界面数据设置*/
        tv_nickname.setText(user.getNickname());
        tv_balance.setText(String.valueOf(user.getBalance()));

        /*更新本地的GlobalData里的余额*/
        GlobalData.user = user;


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

        /*钱包提现按钮的响应事件*/
        btn_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WithdrawalActivity.class);
                startActivity(intent);
            }
        });

        /*"待支付订单"按钮的响应事件*/
        btn_waitpayorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrdersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("status", 0);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        /*已支付订单*/
        btn_payedorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrdersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("status", 1);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        /*我发布的车位的响应事件*/
        btn_myrelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyReleaseActivity.class);
                mContext.startActivity(intent);
            }
        });


    }


}
