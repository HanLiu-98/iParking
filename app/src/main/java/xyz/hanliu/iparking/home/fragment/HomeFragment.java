package xyz.hanliu.iparking.home.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.ParkingSpace_General;
import xyz.hanliu.iparking.base.BaseFragment;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.home.activity.SpaceDetail;
import xyz.hanliu.iparking.home.adapter.SpaceRecyclerViewAdapter;
import xyz.hanliu.iparking.utils.DateUtil;
import xyz.hanliu.iparking.utils.JsonBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {
//    private Context mContext=getContext();

    private Button btn_chooseposition;


    /*界面上的空闲车位数据*/
    private List<ParkingSpace_General> spaceList = new ArrayList<>();
    //    private List<ParkingSpace> SpaceList=new ArrayList<>();
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView();


        return view;
    }


    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);

        btn_chooseposition = view.findViewById(R.id.btn_chooseposition);

        //初始化车位数据
//        intiSpaceData();
//        requestHomeSpaceData();
        String areastr = "北京市北京市东城区";
        requestSpaceData(areastr);
        recyclerView = view.findViewById(R.id.rv_spacelist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        SpaceRecyclerViewAdapter adapter = new SpaceRecyclerViewAdapter(mContext, spaceList);
        adapter.setOnSpaceRecyclerView(new SpaceRecyclerViewAdapter.OnSpaceRecyclerView() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(mContext, SpaceDetail.class);
                Bundle bundle = new Bundle();

                bundle.putInt("pk", spaceList.get(position).getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
//                ToastUtil.showMsg(mContext,"aha!");
            }
        });
        recyclerView.setAdapter(adapter);


        return view;

    }

    private void requestSpaceData(String area) {
        HashMap<String, String> map = new HashMap<>();
        map.put("area", area);
        map.put("pk", "啦啦啦");
        //进行网络请求
        OkGo.<String>post(Config.URL_GETSPACELIST)
                .params(map)
                .execute(new StringCallback() {//派生子线程执行耗时的网络操作，回调接口
                    @Override   //网络请求成功的回调函数
                    public void onSuccess(Response<String> response) {
                        //获取服务器响应的JSON字符串
                        String json = response.body();
                        //利用GSON库,将字符串转换成User对象
                        List<ParkingSpace_General> list = JSON.parseArray(json,
                                ParkingSpace_General.class);
                        spaceList = list;


                    }

                    @Override   //网络请求成功的回调函数
                    public void onError(Response<String> response) {
                        super.onError(response);
                        String message = response.getException().getMessage();
                        //利用Toast显示错误信息
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void intiSpaceData() throws ParseException {
        for (int i = 0; i < 10; i++) {
            ParkingSpace_General space = new ParkingSpace_General();
            space.setPositionDetail("固镇县振亚华府12栋号楼楼下");

            String starttimestr = "3月5日15:00:00";
            String endtimestr = "3月5日18:00:00";

            Date starttime = DateUtil.strToDate(starttimestr);
            Date endtime = DateUtil.strToDate(endtimestr);
            space.setStartTime(starttime);
            space.setEndTime(endtime);

//            space.setImagePath(R.drawable.test1);
            space.setPrice(100);
            spaceList.add(space);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        btn_chooseposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView(GlobalData.options1Items, GlobalData.options2Items, GlobalData.options3Items);
            }
        });
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
