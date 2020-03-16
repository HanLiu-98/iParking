package xyz.hanliu.iparking.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.ParkingSpace;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.utils.AlertDialogUtil;
import xyz.hanliu.iparking.utils.DateUtil;
import xyz.hanliu.iparking.utils.ToastUtil;

public class SpaceDetailActivity extends AppCompatActivity {
    int spaceid = 0;

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
                        ParkingSpace space = JSON.parseObject
                                (json, ParkingSpace.class);

                        /*初始化监听器*/
                        /*如果这个车位属于自己*/
                        if (space.getOwnerMobile().equals(GlobalData.user.getMobile())) {
                            initOwnListeners();
                        } else {
                            initOtherListeners();
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
     *
     */
    private void initOtherListeners() {
        btn_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SpaceDetailActivity.this, DepositActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("pk", spaceid);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

    }

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


}
