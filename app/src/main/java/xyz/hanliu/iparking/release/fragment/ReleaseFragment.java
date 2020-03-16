package xyz.hanliu.iparking.release.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.ParkingSpace;
import xyz.hanliu.iparking.base.BaseFragment;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.home.activity.SpaceDetailActivity;
import xyz.hanliu.iparking.release.activity.SelectPicActivity;
import xyz.hanliu.iparking.utils.AlertDialogUtil;
import xyz.hanliu.iparking.utils.DateUtil;
import xyz.hanliu.iparking.utils.JsonBean;
import xyz.hanliu.iparking.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReleaseFragment extends BaseFragment {

    /*发布车位界面内的所有控件*/
    private Button btn_position_add;
    private Button btn_starttime_add;
    private Button btn_endtime_add;
    private ImageView iv_addpic_add;
    private Button btn_startRelease;
    private EditText et_positiondetail;
    private EditText et_price;
    private EditText et_remark;

    String area = null;
    Date starttime = null;
    Date endtime = null;
    private Uri imageUri = null;
    private String imagePath = null;


    public static final int TO_SELECT_PHOTO = 3;


    @Override
    public View initView() {

        View view = View.inflate(mContext, R.layout.fragment_release, null);
        btn_position_add = view.findViewById(R.id.btn_chooseposition_add);
        btn_starttime_add = view.findViewById(R.id.btn_choosestarttime_add);
        btn_endtime_add = view.findViewById(R.id.btn_chooseendtime_add);
        iv_addpic_add = view.findViewById(R.id.iv_addpic_add);
        btn_startRelease = view.findViewById(R.id.btn_startrelease_add);
        et_positiondetail = view.findViewById(R.id.et_positiondetail_add);
        et_price = view.findViewById(R.id.et_price_add);
        et_remark = view.findViewById(R.id.et_remark_add);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();

    }

    /**
     * 点击事件的绑定
     */
    private void initListener() {
        /* “选择位置” 按钮的点击事件绑定*/
        btn_position_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView(GlobalData.options1Items, GlobalData.options2Items,
                        GlobalData.options3Items);
            }
        });

        /* “选择起租时间” 按钮的点击事件绑定*/
        btn_starttime_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(mContext,
                        new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        btn_starttime_add.setText(DateUtil.dateToStr(date));
                        starttime = date;
                    }/*设置时间的格式*/
                }).setType(new boolean[]{true, true, true, true, true, false}).build();
                pvTime.show();
            }
        });
        /* “选择截止时间” 按钮的点击事件绑定*/
        btn_endtime_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(mContext,
                        new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        btn_endtime_add.setText(DateUtil.dateToStr(date));
                        endtime = date;
                    }/*设置时间的格式*/
                }).setType(new boolean[]{true, true, true, true, true, false}).build();
                pvTime.show();
            }
        });

        /* “添加照片” 的点击事件绑定*/
        iv_addpic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectPicActivity.class);
                //在ActivityA中启动activityB,在返回的时候携带来自B的数据
                startActivityForResult(intent, TO_SELECT_PHOTO);
            }
        });

        /* “确认发布” 的点击事件绑定*/
        btn_startRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid()) {
                    return;
                }
                /*输入合法后创建对象*/
                String positionDetail = et_positiondetail.getText().toString().trim();
                float price = Float.valueOf(et_price.getText().toString().trim());
                String remark = et_remark.getText().toString().trim();
                /**
                 * 一定要先new，再set，否则会崩溃
                 * 想要使用fastjson转对象，不能搞有参构造！！！
                 */
                ParkingSpace space = new ParkingSpace();
                space.setRemark(remark);
                space.setArea(area);
                space.setPositionDetail(positionDetail);
                space.setStartTime(starttime);
                space.setEndTime(endtime);
                space.setPrice(price);
                space.setImagePath(imagePath);
                space.setStatus(0);
                space.setOwnerMobile(GlobalData.user.getMobile());

                releaseSpace(space);
            }
        });
    }


    /**
     * 向服务器上传车位信息
     */
    private void releaseSpace(ParkingSpace space) {
        btn_startRelease.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        //进度条采用不明确显示进度的模糊模式
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在把空闲车位信息上传到服务器...");
        progressDialog.show();

        File file = new File(imagePath);
        /*车位的参数放到Map里*/
        HashMap<String, String> params = new HashMap<>();
        params.put("area", space.getArea());
        params.put("positionDetail", space.getPositionDetail());
        params.put("startTime", DateUtil.dateToStr(space.getStartTime()));
        params.put("endTime", DateUtil.dateToStr(space.getEndTime()));
        params.put("price", String.valueOf(space.getPrice()));
        params.put("remark", space.getRemark());
        params.put("status", String.valueOf(space.getStatus()));
        params.put("ownerMobile", space.getOwnerMobile());
        /*根据路径找到文件*/
        File imagefile = new File(space.getImagePath());
        /*图片在服务器的名字*/
        String filename = space.getOwnerMobile() + String.valueOf(System.currentTimeMillis()) + ".jpg";
        /*提交表单信息*/
        OkHttpUtils.post()
                .addFile("ImgFile", filename, file)
                .url(Config.URL_UPLOAD)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String message = e.getMessage();
                        progressDialog.dismiss();
                        btn_startRelease.setEnabled(true);
                        AlertDialogUtil.showNetErrorAlertDialog(mContext, message);

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        String[] strs = response.split("###");
                        if (strs[0].equals("success"))/*服务器会返回主键*/ {
                            progressDialog.dismiss();
                            onReleaseSuccess(strs[1]);
                        } else {

                        }
                    }
                });
    }

    /**
     * 判断数据输入是否合法
     */
    public boolean isValid() {
        boolean valid = true;
        String area = btn_position_add.getText().toString();
        String positionDetail = et_positiondetail.getText().toString().trim();
        String price = et_price.getText().toString().trim();


        if (positionDetail.isEmpty()) {
            et_positiondetail.setError("请输入车位的详细地址");
            valid = false;
        } else {
            et_positiondetail.
                    setError(null);
        }

        if (price.isEmpty()) {
            et_price.setError("请输入出租的价格");
            valid = false;
        } else {
            et_price.setError(null);
        }

        if (area.equals("选择位置") || starttime == null
                || endtime == null || imageUri == null
                || !(starttime.getTime() > System.currentTimeMillis()
                && starttime.getTime() < endtime.getTime())) {
            String msg = "确认发布前请确保：\n1.所有信息项填写完整；\n" +
                    "2.起租时间是一个未来时间;\n" +
                    "3.起租时间早于截止时间；\n4.选择一张车位照片；";

            AlertDialogUtil.showDataInvaidAlertDialog(mContext, msg);
            valid = false;
        }
        return valid;

    }


    /**
     * 发布成功
     */
    public void onReleaseSuccess(String spacekey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).
                setTitle("提示").
                setIcon(R.drawable.ic_sentiment_very_satisfied_black_24dp).
                setMessage("恭喜你，发布成功！");
        builder.setPositiveButton("去看看我发布的车位", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), SpaceDetailActivity.class);
                /*给空闲车位详情Activity添加主键数据，即id*/
                Bundle bundle = new Bundle();
                bundle.putInt("pk", Integer.valueOf(spacekey));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        builder.show();

        /*把输入框清空*/
        btn_startRelease.setEnabled(true);
        et_positiondetail.setText("");
        et_remark.setText("");
        et_price.setText("");
        btn_position_add.setText("选择位置");
        btn_starttime_add.setText("选择开始时间");
        btn_endtime_add.setText("选择结束时间");
        iv_addpic_add.setImageResource(R.drawable.ic_addpic);
        /*注意：全局的变量 开始时间 结束时间 图片路径 都要清空 否则下次若不填写默认将上次数据提交*/
        starttime = null;
        endtime = null;
        imageUri = null;
        imagePath = null;
    }

    /**
     * 发布失败的情况
     */
    public void onReleaseFailed(String failmsg) {
        ToastUtil.showMsg(mContext, failmsg);
        btn_startRelease.setEnabled(true);
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
            ToastUtil.showMsg(getContext(), "onActivityResult" + imageUri);
            iv_addpic_add.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 弹出地址选择框
     */
    private void showPickerView(List<JsonBean> options1Items, ArrayList<ArrayList<String>> options2Items,
                                ArrayList<ArrayList<ArrayList<String>>> options3Items) {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt2tx + opt3tx;
                btn_position_add.setText(tx);
                area = opt1tx + tx;
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


}
