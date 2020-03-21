package xyz.hanliu.iparking.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.release.activity.SelectPicActivity;

public class WithdrawalActivity extends AppCompatActivity {

    public static final int TO_SELECT_PHOTO = 3;

    private Uri imageUri = null;
    private String imagePath = null;

    @BindView(R.id.iv_addpic_withdrawal)
    ImageView iv_paypicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        /*控件绑定*/
        ButterKnife.bind(this);
        initListener();
    }

    /**
     * 控件的点击事件的绑定
     */
    private void initListener() {
        //上传支付包或者微信付款码的点击事件（即：选择一张图片）
        iv_paypicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WithdrawalActivity.this, SelectPicActivity.class);
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
            //获取图片的URI
            String uri = data.getExtras().getString("uri");
            imageUri = Uri.parse(uri);
            imagePath = data.getExtras().getString("path");
            iv_paypicture.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
