package xyz.hanliu.iparking.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.home.activity.SpaceDetailActivity;
import xyz.hanliu.iparking.user.adapter.PayedOrderRecyclerViewAdapter;
import xyz.hanliu.iparking.user.adapter.WaitPayOrderRecycleViewAdapter;
import xyz.hanliu.iparking.user.bean.OrderPlus;
import xyz.hanliu.iparking.utils.AlertDialogUtil;
import xyz.hanliu.iparking.utils.ToastUtil;

public class OrdersActivity extends AppCompatActivity {
    /*界面上的title信息*/
    @BindView(R.id.tv_msg_orders)
    TextView tv_title;
    /*显示订单列表的RecyclerView*/
    @BindView(R.id.rv_orders)
    RecyclerView rv_orders;
    /*没有相关订单时显示的TextView*/
    @BindView(R.id.tv_noorder_orders)
    TextView tv_noorder;

    /*界面上的订单数据*/
    private List<OrderPlus> order_List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        /*控件绑定*/
        ButterKnife.bind(this);
        /*初始化控件时，没有orders时才显示的TextView设置为不可见*/
        tv_noorder.setVisibility(View.GONE);

        /*将传递过来的信息取出来*/
        int status = getIntent().getExtras().getInt("status", -1);
        /**
         * 0：待支付订单
         * 1：已支付订单
         * 根据flag值，设置不同的标题，空白显示，请求不同的数据
         */
        if (status == 0) {
            tv_title.setText("我的所有待支付订单");
            tv_noorder.setText("没有待支付订单哦~");
            requestOrders(0);
        } else {
            tv_title.setText("我的所有已支付订单");
            tv_noorder.setText("没有已支付订单哦~");
            requestOrders(1);
        }
    }


    /**
     * 请求处于不同状态的订单列表
     */
    public void requestOrders(int status) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", GlobalData.user.getMobile());
        params.put("status", String.valueOf(status));
        OkHttpUtils.post()
                .url(Config.URL_GETORDERS)
                .params(params)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AlertDialogUtil.showNetErrorAlertDialog(OrdersActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (status == 0)/*处理待支付订单数据*/ {
                            processData_waitPayList(response);
                        }
                        if (status == 1)/*处理已支付订单数据*/ {
                            processData_payedList(response);
                        }


                    }
                });
    }


    /**
     * 处理已支付订单数据
     */
    private void processData_payedList(String json) {
        /*处理JSON数据*/
        order_List = JSON.parseArray(json, OrderPlus.class);
        /**
         * 如果当前用户有已支付订单
         * 1.隐藏TextView
         * 2.把订单List填充到RecyclerView里显示
         * 3.设置按钮的响应事件
         */
        if (order_List.size() != 0) {
            tv_noorder.setVisibility(View.GONE);
            rv_orders.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rv_orders.setLayoutManager(layoutManager);
            PayedOrderRecyclerViewAdapter adapter = new PayedOrderRecyclerViewAdapter(order_List);
            adapter.setOnPayedOrderRecyclerView(new PayedOrderRecyclerViewAdapter.OnPayedOrderRecyclerView() {
                @Override
                public void OnMoneyBackButtonClick(int position) {
                    ToastUtil.showMsg(OrdersActivity.this, "执行退款操作");
                }

                @Override
                public void OnShowDetailButtonClick(int position) {
//                    ToastUtil.showMsg(OrdersActivity.this,"执行查看详情操作");
                    showSpaceDetail(order_List.get(position).getId());
                }

                @Override
                public void OnFinishParkingButtonClick(int position) {
                    ToastUtil.showMsg(OrdersActivity.this, "执行完成停车操作");
                }
            });
            rv_orders.setAdapter(adapter);
        }
        /*如果当前用户没有已支付订单1.RecyclerView设置消失；2.TextView设置可见*/
        else {
            rv_orders.setVisibility(View.GONE);
            tv_noorder.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 处理待支付订单数据
     */
    private void processData_waitPayList(String json) {
        /*处理JSON数据*/
        order_List = JSON.parseArray(json, OrderPlus.class);
        /**
         * 如果当前用户有待支付订单
         * 1.隐藏TextView
         * 2.把订单List填充到RecyclerView里显示
         * 3.设置按钮的响应事件
         */
        if (order_List.size() != 0) {
            tv_noorder.setVisibility(View.GONE);
            rv_orders.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rv_orders.setLayoutManager(layoutManager);
            WaitPayOrderRecycleViewAdapter adapter = new WaitPayOrderRecycleViewAdapter(order_List);
            adapter.setOnWaitPayOrderRecyclerView(new WaitPayOrderRecycleViewAdapter.OnWaitPayOrderRecyclerView() {
                @Override
                public void OnPayButtonClick(int position) {
                    ToastUtil.showMsg(OrdersActivity.this, "okk1");
                }

                @Override
                public void OnShowDetailButtonClick(int position) {

                    showSpaceDetail(order_List.get(position).getId());
                }
            });
            rv_orders.setAdapter(adapter);

        }
        /*如果当前用户没有待支付订单1.RecyclerView设置消失；2.TextView设置可见*/
        else {
            rv_orders.setVisibility(View.GONE);
            tv_noorder.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 查看车位详情
     */
    private void showSpaceDetail(int id) {
        Intent intent = new Intent(OrdersActivity.this, SpaceDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pk", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
