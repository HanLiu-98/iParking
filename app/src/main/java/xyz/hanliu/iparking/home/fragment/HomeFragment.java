package xyz.hanliu.iparking.home.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.ParkingSpace_General;
import xyz.hanliu.iparking.base.BaseFragment;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.home.activity.SpaceDetail;
import xyz.hanliu.iparking.home.adapter.SpaceRecyclerViewAdapter;
import xyz.hanliu.iparking.utils.AlertDialogUtil;
import xyz.hanliu.iparking.utils.JsonBean;
import xyz.hanliu.iparking.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {
    /*界面上的控件*/
    private Button btn_chooseposition;
    private RecyclerView recyclerView;
    private Button btn_search;
    private TextView tv_blankmsg;
    private String area = "";

    /*界面上的空闲车位数据*/
    private List<ParkingSpace_General> spaceList = new ArrayList<>();

    /**
     * 填充布局，控件绑定
     */
    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        btn_chooseposition = view.findViewById(R.id.btn_chooseposition_home);
        btn_search = view.findViewById(R.id.btn_search_home);
        recyclerView = view.findViewById(R.id.rv_spacelist_home);
        tv_blankmsg = view.findViewById(R.id.tv_blankmsg_home);
        tv_blankmsg.setText("这个地区暂时没有空闲的车位哦~");
        tv_blankmsg.setVisibility(View.GONE);
        return view;
    }

    /**
     * 初始化界面数据
     */
    public void initData() {
        super.initData();
        initListener();
        requestSpaceList("北京市北京市东城区");
    }

    /**
     * 控件点击事件的绑定
     */
    private void initListener() {
        /*选择位置按钮的点击事件*/
        btn_chooseposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView(GlobalData.options1Items, GlobalData.options2Items, GlobalData.options3Items);
            }
        });

        /*搜索按钮的实现*/
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果用户还没选择位置
                if (area.equals("")) {
                    ToastUtil.showMsg(mContext, "必须要选择一个位置才能进行搜索！");
                } else {   /*已经选择地区的话，就请求数据，填充RecyclerView*/
                    requestSpaceList(area);
                }
            }
        });

    }

    /**
     * 获取一个地区内所有空闲车位列表
     */
    public void requestSpaceList(String area) {
        OkHttpUtils.post()
                .url(Config.URL_GETSPACELIST)
                .addParams("area", area)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AlertDialogUtil.showNetErrorAlertDialog(mContext, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processData(response);
                    }
                });
    }


    /**
     * 处理服务器传回来的JSON数据，并且设置RecyclerView的adapter
     */
    private void processData(String json) {
        /*处理JSON数据*/
        spaceList = JSON.parseArray(json, ParkingSpace_General.class);
        /*该地区有可用空闲车位就显示RecyclerView，没有可用空闲车位就显示TextView*/
        if (spaceList.size() != 0) {
            tv_blankmsg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            /*添加Android自带的分割线*/
            recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            SpaceRecyclerViewAdapter adapter = new SpaceRecyclerViewAdapter(mContext, spaceList);
            /*设置Item的点击事件*/
            adapter.setOnSpaceRecyclerView(new SpaceRecyclerViewAdapter.OnSpaceRecyclerView() {
                @Override
                public void OnItemClick(int position) {
                    Intent intent = new Intent(mContext, SpaceDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pk", spaceList.get(position).getId());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            tv_blankmsg.setVisibility(View.VISIBLE);
        }
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

                String tx = opt1tx + opt2tx + opt3tx;
                area = tx;
                btn_chooseposition.setText(tx);
            }
        }).setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

}
