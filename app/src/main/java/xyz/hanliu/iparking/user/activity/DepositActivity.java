package xyz.hanliu.iparking.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.release.activity.SelectPicActivity;
import xyz.hanliu.iparking.utils.ToastUtil;

public class DepositActivity extends AppCompatActivity {
    public static final int TO_SELECT_PHOTO = 3;

    String imagePath = null;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.iv_payimage_deposit)
    ImageView iv_payimage;
    @BindView(R.id.btn_uploadEvidence_deposit)
    Button btn_uploadEvidence_deposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        /*控件绑定*/
        ButterKnife.bind(this);
        initListener();
    }

    /**
     * 控件的点击事件的绑定
     */
    private void initListener() {
        //RadioGroup里面放两个radioButton，实现支付宝和微信支付两种支付方式的选择
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_alipay) {
                    iv_payimage.setImageResource(R.drawable.alipay);
                } else {
                    iv_payimage.setImageResource(R.drawable.weixinpay);
                }
            }
        });


        //上传支付凭证按钮的点击事件（即：选择一张图片）
        btn_uploadEvidence_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepositActivity.this, SelectPicActivity.class);
                //在ActivityA中启动activityB,在返回的时候携带来自B的数据
                startActivityForResult(intent, TO_SELECT_PHOTO);
            }
        });

    }


    /***
     * 启动选择照片的回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            //获取图片的路径
            imagePath = data.getExtras().getString("path");
            ToastUtil.showMsg(this, imagePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
