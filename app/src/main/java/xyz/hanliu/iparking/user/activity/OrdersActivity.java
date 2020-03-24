package xyz.hanliu.iparking.user.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.Date;
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
import xyz.hanliu.iparking.user.adapter.WaitPayOrderRecyclerViewAdapter;
import xyz.hanliu.iparking.user.bean.OrderPlus;
import xyz.hanliu.iparking.utils.AlertDialogUtil;
import xyz.hanliu.iparking.utils.DateUtil;
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
                public void OnShowDetailButtonClick(int position)
                {
                    showSpaceDetail(order_List.get(position).getId());
                }

                @Override
                public void OnFinishParkingButtonClick(int position)
                {
                    /*执行完成停车操作*/
                    ToastUtil.showMsg(OrdersActivity.this, "");
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
            WaitPayOrderRecyclerViewAdapter adapter = new WaitPayOrderRecyclerViewAdapter(order_List);
            adapter.setOnWaitPayOrderRecyclerView(new WaitPayOrderRecyclerViewAdapter.OnWaitPayOrderRecyclerView() {
                @Override
                public void OnPayButtonClick(int position)
                {
                    /**
                     * 进行支付操作
                     */
                    doPay(String.valueOf(order_List.get(position).getId()),GlobalData.user.getMobile(),
                            OrdersActivity.this,order_List.get(position).getPrice());
                }

                @Override
                public void OnShowDetailButtonClick(int position)
                {
                    /**
                     * 查看车位的详情
                     */

                    showSpaceDetail(order_List.get(position).getId());
                }
            });
            rv_orders.setAdapter(adapter);

        }
        /*如果当前用户没有待支付订单1.RecyclerView设置消失；2.TextView设置可见*/
        else
        {
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


    /**
     * 弹出支付界面，根据订单主键进行订单支付
     */
    public  void doPay(String spaceid, String tenantmobile, Context mContext,float price)
    {
        //弹出支付界面
        LayoutInflater inflater = getLayoutInflater();
        View payView = inflater.inflate(R.layout.paymoney, null);
        String titlemsg = "从钱包里支付" + String.valueOf(price) + "元";
        new AlertDialog.Builder(mContext).setIcon(R.drawable.rmb).setTitle(titlemsg)
                .setView(payView).setPositiveButton("支付", new DialogInterface.OnClickListener() {
            /*点击支付按钮后进行的操作*/
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取到用户输入的支付密码
                EditText ev_pass = payView.findViewById(R.id.et_password_pay);
                String password = ev_pass.getText().toString();
                //输入密码正确&&余额够用
                if (password.equals(GlobalData.user.getPassword()) && GlobalData.user.getBalance() >= price) {
                    //进行扣款操作
                    payOrder(spaceid, tenantmobile,mContext);
                }
                else
                {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext).
                            setTitle("支付失败").
                            setMessage("请确保：\n1.支付密码正确无误。\n2.钱包余额充足。").
                            setIcon(R.drawable.ic_payfail);
                    builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        }).create().show();

    }

    /**
     * 验证用户密码以及钱包余额后，进行扣款，修改双方账户余额，修改订单状态
     */
    public void payOrder(String spaceid, String tenantmobile,Context mContext)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("spaceid", String.valueOf(spaceid));
        params.put("tenantMobile", GlobalData.user.getMobile());
        params.put("payTime", DateUtil.dateToStr(new Date()));

        OkHttpUtils.post()
                .url(Config.URL_PAYORDER)
                .params(params)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AlertDialogUtil.showNetErrorAlertDialog(mContext, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        /*事务执行失败*/
                        if (response.equals("failure")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext).
                                    setTitle("扣款失败!").
                                    setMessage("可能这个车位已经被别人租走了！").
                                    setIcon(R.drawable.ic_payfail);
                            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                        /*事务执行成功！已经扣款了*/
                        else
                        {
                            String msg="已经通知业主为您开锁！\n\n" +
                                    "温馨提示：\n\n" +
                                    "1.在车位出租时间截止前，若因业主原因未能停车成功，可凭借证据发起退款！\n\n" +
                                    "2.成功停车后请记得去个人中心确认！";
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext).
                                    setTitle("付款成功!").
                                    setMessage(msg).
                                    setIcon(R.drawable.ic_paysuccess);
                            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    requestOrders(0);
                                }
                            });
                            builder.show();
                        }
                    }
                });
    }
}
