package xyz.hanliu.iparking.home.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.ParkingSpace;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.utils.AlertDialogUtil;
import xyz.hanliu.iparking.utils.DateUtil;
import xyz.hanliu.iparking.utils.ToastUtil;

public class SpaceDetailActivity extends AppCompatActivity {

    int spaceid;

    @BindView(R.id.tv_area_detail)
    TextView tv_area;
    @BindView(R.id.tv_positiondetail_detail)
    TextView tv_positionDetail;
    @BindView(R.id.tv_starttime_detail)
    TextView tv_starttime;
    @BindView(R.id.tv_endtime_detail)
    TextView tv_endtime;
    @BindView(R.id.tv_price_detail)
    TextView tv_price;
    @BindView(R.id.tv_ownermobile_detail)
    TextView tv_ownermobile;
    @BindView(R.id.tv_remark_detail)
    TextView tv_remark;
    @BindView(R.id.iv_image_detail)
    ImageView iv_image;

    @BindView(R.id.btn_contact_detail)
    Button btn_contact;
    @BindView(R.id.btn_rent_detail)
    Button btn_rent;
    @BindView(R.id.btn_navigation_detail)
    Button btn_navigation;

    ParkingSpace space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacedetail);
        /*控件绑定*/
        ButterKnife.bind(this);
        /*将传递过来的主键信息取出来*/
        spaceid = getIntent().getExtras().getInt("pk", -1);
        /*根据主键内容，请求网络，并且填充视图*/
        requestSpaceDetailAndFillData(spaceid);
    }


    /**
     * 根据主键内容，请求网络，并且填充视图
     */
    private void requestSpaceDetailAndFillData(int pk) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(pk));
        OkGo.<String>post(Config.URL_GETDETAIL)
                .params(map)
                .execute(new StringCallback() {//派生子线程执行耗时的网络操作，回调接口
                    @Override   //网络请求成功的回调函数
                    public void onSuccess(Response<String> response) {
                        //获取服务器响应的JSON字符串
                        String json = response.body();
                        //利用fastjson库,将字符串转换成ParkingSpace对象
                        space = JSON.parseObject
                                (json, ParkingSpace.class);

                        /*初始化监听器*/
                        /*如果这个车位属于自己*/
                        if (space.getOwnerMobile().equals(GlobalData.user.getMobile())) {
                            initOwnListeners();
                            intiPublicListener();
                        } else {
                            initOtherListeners();
                            intiPublicListener();
                        }
                        fillData(space);
                    }

                    @Override   //网络请求失败的回调函数
                    public void onError(Response<String> response) {
                        super.onError(response);
                        String message = response.getException().getMessage();
                        AlertDialogUtil.showNetErrorAlertDialog(SpaceDetailActivity.this, message);
                    }
                });
    }


    /**
     * 用停车位对象填充视图数据
     */
    private void fillData(ParkingSpace space) {
        tv_area.setText(space.getArea());
        tv_positionDetail.setText(space.getPositionDetail());
        tv_starttime.setText(DateUtil.dateToStr(space.getStartTime()));
        tv_endtime.setText(DateUtil.dateToStr(space.getEndTime()));
        tv_price.setText("￥" + String.valueOf(space.getPrice()));
        tv_ownermobile.setText(space.getOwnerMobile());
        tv_remark.setText(space.getRemark());
        /*利用Glide进行图片的加载*/
        Glide.with(SpaceDetailActivity.this)
                .load(Config.IMAGE_HOST + space.getImagePath()).into(iv_image);
    }

    /**
     * 如果车位属于别人，按钮的点击事件
     */
    private void initOtherListeners() {
        btn_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpaceDetailActivity.this).
                        setTitle("确认出租").
                        setIcon(R.drawable.ic_sentiment_very_satisfied_black_24dp).
                        setMessage("请仔细核对空闲车位具体位置以及可用时间！");
                builder.setPositiveButton("提交订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        submitOrder();
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
        });

    }

    /*提交订单按钮被点击后的操作*/
    private void submitOrder() {
        HashMap<String, String> params = new HashMap<>();
        params.put("spaceid", String.valueOf(spaceid));
        params.put("tenantMobile", GlobalData.user.getMobile());
        params.put("createTime", DateUtil.dateToStr(new Date()));
        params.put("status", String.valueOf(0));

        OkHttpUtils.post()
                .url(Config.URL_SUBMITORDER)
                .params(params)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AlertDialogUtil.showNetErrorAlertDialog(SpaceDetailActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        /*订单提交失败，必定是insert语句违反实体完整性，用户已经执行过提交订单操作了*/
                        if (response.equals("failure")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SpaceDetailActivity.this).
                                    setTitle("你已经提交过订单了!").
                                    setMessage("请到个人中心的\n------待支付订单------\n栏完成支付操作！").
                                    setIcon(R.drawable.ic_icon_waring);
                            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                        /*订单提交成功，传回主键*/
                        else {
                            String[] strs = response.split("###");
                            doPay(strs[1], strs[2],SpaceDetailActivity.this);
                        }


                    }
                });


    }

    /**
     * 弹出支付界面，根据订单主键进行订单支付
     */
    public  void doPay(String spaceid, String tenantmobile,Context mContext) {
        //弹出支付界面
        LayoutInflater inflater = getLayoutInflater();
        View payView = inflater.inflate(R.layout.paymoney, null);
        String titlemsg = "从钱包里支付" + String.valueOf(space.getPrice()) + "元";
        new AlertDialog.Builder(mContext).setIcon(R.drawable.rmb).setTitle(titlemsg)
                .setView(payView).setPositiveButton("支付", new DialogInterface.OnClickListener() {
            /*点击支付按钮后进行的操作*/
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取到用户输入的支付密码
                EditText ev_pass = payView.findViewById(R.id.et_password_pay);
                String password = ev_pass.getText().toString();
                //输入密码正确&&余额够用
                if (password.equals(GlobalData.user.getPassword()) && GlobalData.user.getBalance() >= space.getPrice()) {
                    //进行扣款操作
                    payOrder(spaceid, tenantmobile,mContext);
                } else {
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
     * 验证用户密码以及钱包余额后，进行扣款，修改车位，修改订单状态
     */
    public void payOrder(String spaceid, String tenantmobile,Context mContext) {
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
                        else {
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
                                }
                            });
                            builder.show();
                        }


                    }
                });

    }

    /**
     * 如果车位属于自己，按钮的点击事件
     */
    private void initOwnListeners() {
        btn_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(SpaceDetailActivity.this, "不能出租自己的车位哦~");
            }
        });

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(SpaceDetailActivity.this, "这个车位的主人是你自己哦~");
            }
        });


    }

    /**
     * 导航去那儿的按钮响应事件
     */
    private void intiPublicListener() {
        btn_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkApkExist(SpaceDetailActivity.this, "com.autonavi.minimap")) {
                    ToastUtil.showMsg(SpaceDetailActivity.this, "请安装高德地图！");

                } else {
                    String des = space.getArea() + space.getPositionDetail();
                    /**
                     * 查看高德地图API，按照API拼链接
                     */
                    Uri uri = Uri.parse("amapuri://route/plan/?dlat=" + "" + "&dlon=" + "" + "&dname=" + des + "&dev=0&t=0");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));

                }


            }
        });
    }

    /**
     * 判断是否安装应用
     */
    private boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
