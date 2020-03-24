package xyz.hanliu.iparking.user.activity;

import android.app.AlertDialog;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.ParkingSpace;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.home.activity.SpaceDetailActivity;
import xyz.hanliu.iparking.user.adapter.MyReleaseRecyclerViewAdapter;
import xyz.hanliu.iparking.utils.AlertDialogUtil;

public class MyReleaseActivity extends AppCompatActivity {

    @BindView(R.id.rv_myrelease)
    RecyclerView rv_myrelease;

    @BindView(R.id.tv_norelease)
    TextView tv_norelease;

    private List<ParkingSpace> mySpaceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrelease);
        /*控件绑定*/
        ButterKnife.bind(this);
        tv_norelease.setVisibility(View.GONE);

        requestMyReleaseData();
    }
    /**
     * 向服务器请求我发布的车位列表
     */
    private void requestMyReleaseData()
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", GlobalData.user.getMobile());
        OkHttpUtils.post()
                .url(Config.URL_GETMYRELEASE)
                .params(params)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AlertDialogUtil.showNetErrorAlertDialog(MyReleaseActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                            processData(response);
                    }
                });
    }
    /**
     * 处理返回的车位列表
     */
    private void processData(String json)
    {
        /*处理JSON数据*/
        mySpaceList = JSON.parseArray(json,ParkingSpace.class);
        /*请求回来的数据，可能会有已经过期的车位(在起租时间前，还没有被租出去！！！车位的状态改为2)*/
        for (ParkingSpace space:mySpaceList)
        {
            if(space.getStatus()==0&&space.getStartTime().getTime()<new Date().getTime())
                space.setStatus(2);
        }

        /**
         * 如果当前用户发布过车位
         * 1.隐藏TextView
         * 2.把订单List填充到RecyclerView里显示
         * 3.设置按钮的响应事件
         */

        if (mySpaceList.size() != 0) {
            tv_norelease.setVisibility(View.GONE);
            rv_myrelease.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rv_myrelease.setLayoutManager(layoutManager);
            MyReleaseRecyclerViewAdapter adapter=new MyReleaseRecyclerViewAdapter(mySpaceList);
            adapter.setOnMyReleaseRecyclerView(new MyReleaseRecyclerViewAdapter.OnMyReleaseRecyclerView() {

                /* 删除车位按钮的响应事件 */
                @Override
                public void OnDeleteButtonClick(int position)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyReleaseActivity.this).
                            setTitle("确认撤销发布？").
                            setIcon(R.drawable.ic_icon_waring).
                            setMessage("请仔细核对车位的具体位置以及可用时间后再进行操作！");
                    builder.setPositiveButton("确认撤销", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            /*车位已出租不能撤销发布*/
                            if(mySpaceList.get(position).getStatus()==1)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyReleaseActivity.this).
                                        setTitle("撤销失败").
                                        setMessage("这个车位已经被别人租下了，不能撤销！").
                                        setIcon(R.drawable.ic_payfail);
                                builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();

                            }
                            else/*车位处于待出租状态，进行删除操作*/
                            {
                                deleteSpace(mySpaceList.get(position).getId());
                            }

                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }

                /* 查看车位详情按钮的响应事件 */
                @Override
                public void OnShowDetailButtonClick(int position)
                {
                    Intent intent = new Intent(MyReleaseActivity.this, SpaceDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pk", mySpaceList.get(position).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

                /*修改价格按钮的响应事件*/
                @Override
                public void OnModifyPriceButtonClick(int position)
                {
                    /*车位已出租，不能修改价格*/
                    if(mySpaceList.get(position).getStatus()==1)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyReleaseActivity.this).
                                setTitle("修改失败").
                                setMessage("这个车位已经被别人租下了，不能再修改价格！").
                                setIcon(R.drawable.ic_payfail);
                        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                    else/*车位处于待出租状态，进行修改价格操作*/
                    {
                        //弹出修改价格界面
                        LayoutInflater inflater = getLayoutInflater();
                        View payView = inflater.inflate(R.layout.modifyprice, null);
                        new AlertDialog.Builder(MyReleaseActivity.this)
                                .setView(payView).setPositiveButton("确认修改",
                                new DialogInterface.OnClickListener() {
                            /*点击确认修改按钮后进行的操作*/
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //获取到用户输入的价格
                                EditText ev_price = payView.findViewById(R.id.et_price_modify);
                                String price = ev_price.getText().toString();
                                //进行修改操作
                                modifyPrice(mySpaceList.get(position).getId(),Float.valueOf(price));
                            }
                        }).create().show();
                    }
                }
            });
            rv_myrelease.setAdapter(adapter);
        }
        /*如果当前用户没有已支付订单1.RecyclerView设置消失；2.TextView设置可见*/
        else
        {
            rv_myrelease.setVisibility(View.GONE);
            tv_norelease.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 撤销发布
     */
    private void deleteSpace(int id)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        OkHttpUtils.post()
                .url(Config.URL_DELETESPACE)
                .params(params)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AlertDialogUtil.showNetErrorAlertDialog(MyReleaseActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        if(response.equals("success"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MyReleaseActivity.this).
                                    setTitle("空闲车位撤销成功!").
                                    setIcon(R.drawable.ic_paysuccess);
                            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                            /*撤销操作完成后，请求新的数据并且填充视图*/
                            requestMyReleaseData();

                        }

                    }
                });


    }
    /**
     * 修改价格
     */
    private void modifyPrice(int id,float price)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("price", String.valueOf(price));
        OkHttpUtils.post()
                .url(Config.URL_MODIFYPRICE)
                .params(params)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AlertDialogUtil.showNetErrorAlertDialog(MyReleaseActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        if(response.equals("success"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MyReleaseActivity.this).
                                    setTitle("价格修改成功!").
                                    setIcon(R.drawable.ic_paysuccess);
                            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                            /*价格修改成功操作完成后，请求新的数据并且填充视图*/
                            requestMyReleaseData();

                        }

                    }
                });

    }
}
