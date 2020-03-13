package xyz.hanliu.iparking.home.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import xyz.hanliu.iparking.utils.DateUtil;
import xyz.hanliu.iparking.utils.ToastUtil;

public class SpaceDetail extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacedetail);
        /*控件绑定*/
        ButterKnife.bind(this);
        /*将传递过来的主键信息取出来*/
        int pk = getIntent().getExtras().getInt("pk", -1);
        /*根据主键内容，请求网络，并且填充视图*/
        requestSpaceDetailAndFillData(pk);

//       ToastUtil.showMsg(SpaceDetail.this,"图片加载耗时，请耐心等待...");

    }

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
                        //利用fastjson库,将字符串转换成SpaceInfo_Detail对象
                        ParkingSpace parkingSpace_detail = JSON.parseObject(json, ParkingSpace.class);
                        fillData(parkingSpace_detail);

                    }

                    @Override   //网络请求成功的回调函数
                    public void onError(Response<String> response) {
                        super.onError(response);
                        String message = response.getException().getMessage();
                        //利用Toast显示错误信息
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void fillData(ParkingSpace space) {
        tv_area.setText(space.getArea());
        tv_positionDetail.setText(space.getPositionDetail());
        tv_starttime.setText(DateUtil.dateToStr(space.getStartTime()));
        tv_endtime.setText(DateUtil.dateToStr(space.getEndTime()));
//        String pricestr="￥"+String.valueOf(space.getPrice());
        tv_price.setText("￥" + String.valueOf(space.getPrice()));
        tv_ownermobile.setText(space.getOwnerMobile());
        tv_remark.setText(space.getRemark());
        ToastUtil.showMsg(SpaceDetail.this, "图片加载耗时，请耐心等待...");
        Glide.with(SpaceDetail.this)
                .load(Config.IMAGE_HOST + space.getImagePath()).into(iv_image);


    }
}
