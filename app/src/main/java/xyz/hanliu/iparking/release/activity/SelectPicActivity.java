package xyz.hanliu.iparking.release.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.io.File;

import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.release.utils.PhotoSelectUtils;
import xyz.hanliu.iparking.utils.ToastUtil;

public class SelectPicActivity extends AppCompatActivity {
    /*页面上的控件*/
    private Button mBtnTakePhoto;
    private Button mBtnSelectPic;
    private Button mBtnFinish;
    private ImageView mIvPic;
    private TextView mTvPath;
    private TextView mTvUri;
    /*用来申请拍照，存储权限*/
    private static final int REQUEST_CODE = 0x001;
    /*图片选择工具的实体类*/
    private PhotoSelectUtils mPhotoSelectUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpic);
        /*控件的绑定*/
        initView();
        /*图片选择工具类的初始化 && 权限申请*/
        init();
        /*控件绑定监听事件*/
        initListener();

    }


    /**
     * 控件的绑定
     */
    private void initView() {
        mBtnTakePhoto = findViewById(R.id.btn_take_photo);
        mBtnSelectPic = findViewById(R.id.btn_select_pic);
        mTvPath = findViewById(R.id.tvPath);
        mTvUri = findViewById(R.id.tvUri);
        mIvPic = findViewById(R.id.iv_pic);
        mBtnFinish = findViewById(R.id.btn_finish);
    }

    /*
     *图片选择工具类的初始化 && 权限申请
     */
    private void init() {
        /*创建PhotoSelectUtils（一个Activity对应一个PhotoSelectUtils）*/
        mPhotoSelectUtils = new PhotoSelectUtils(this,
                new PhotoSelectUtils.PhotoSelectListener() {
                    /*图片选择完成对应的回调事件*/
                    @Override
                    public void onFinish(File outputFile, Uri outputUri) {
                        mTvPath.setText(outputFile.getAbsolutePath());
                        mTvUri.setText(outputUri.toString());
                        /*根据图片的URI将图片加载进入ImageView*/
                        Glide.with(SelectPicActivity.this)
                                .load(outputUri).into(mIvPic);
                    }
                }, true);/*true裁剪，false不裁剪*/

        /*检查权限*/
        checkPermission();
    }


    /*
     * 检查用户权限，如果没有就申请
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(SelectPicActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(SelectPicActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(SelectPicActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SelectPicActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA}, REQUEST_CODE);
        } else {
            ToastUtil.showMsg(SelectPicActivity.this, "已经获得了所有权限");
        }
    }


    /***
     *申请权限完成的回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initListener();
            } else {
                Toast.makeText(this, "您已拒绝授予权限", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 点击事件的设定
     */
    private void initListener() {

        mBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoSelectUtils.takePhoto();
            }
        });

        mBtnSelectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoSelectUtils.selectPhoto();
            }
        });

        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*如果一张图片都没选*/
                if (mTvPath.getText().toString().equals("图片路径:")) {
                    ToastUtil.showMsg(getApplicationContext(), "您还没有选择图片呢!");
                }
                /*把已选图片的URI传到上一个界面*/
                else {
                    ToastUtil.showMsg(getApplicationContext(), "选择图片成功:" + mTvPath.getText().toString());
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent();
                    bundle.putString("uri", mTvUri.getText().toString());
                    bundle.putString("path", mTvPath.getText().toString());
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 启动其他Activity，得到回传数据后的处理事件
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在Activity中的onActivityResult()方法里与PhotoSelectUtils关联
        mPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }
}
