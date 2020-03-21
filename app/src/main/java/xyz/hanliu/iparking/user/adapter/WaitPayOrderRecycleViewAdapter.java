package xyz.hanliu.iparking.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.user.bean.OrderPlus;
import xyz.hanliu.iparking.utils.DateUtil;

public class WaitPayOrderRecycleViewAdapter extends RecyclerView.Adapter<WaitPayOrderRecycleViewAdapter.ViewHolder> {
    /*在RecyclerView里显示的待支付订单列表*/
    private List<OrderPlus> waitpayorders_list;

    /*适配器的构造函数*/
    public WaitPayOrderRecycleViewAdapter(List<OrderPlus> list) {
        this.waitpayorders_list = list;
    }

    /*创建视图时调用*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*Activity里用RecyclerView用这个代码！！！*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waitpayorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderPlus orderPlus = waitpayorders_list.get(position);

        holder.tv_area.setText(orderPlus.getArea());
        holder.tv_positondetail.setText(orderPlus.getPositionDetail());
        String starttime = DateUtil.dateToStrGeneral(orderPlus.getStartTime());
        String endtime = DateUtil.dateToStrGeneral(orderPlus.getEndTime());
        String time = starttime + " —— " + endtime;
        holder.tv_time.setText(time);
        holder.tv_price.setText(String.valueOf(orderPlus.getPrice()));
        holder.tv_ordercreatetime.setText(DateUtil.dateToStrDetail(orderPlus.getCreateTime()));

    }

    @Override
    public int getItemCount() {
        return waitpayorders_list.size();
    }

    /*一个列表项*/
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_area;
        public TextView tv_positondetail;
        public TextView tv_time;
        public TextView tv_price;
        public TextView tv_ordercreatetime;
        public Button btn_showdetail;
        public Button btn_paynow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_area = itemView.findViewById(R.id.tv_area_item_waitpay);
            tv_positondetail = itemView.findViewById(R.id.tv_positiondetail_item_waitpay);
            tv_time = itemView.findViewById(R.id.tv_availabletime_item_waitpay);
            tv_price = itemView.findViewById(R.id.tv_price_item_waitpay);
            tv_ordercreatetime = itemView.findViewById(R.id.tv_createtime_item_waitpay);

            btn_paynow = itemView.findViewById(R.id.btn_pay_item_waitpay);
            btn_showdetail = itemView.findViewById(R.id.btn_showspacedetail_item_waitpay);

            /*视图里两个按钮的点击事件*/
            btn_paynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onWaitPayOrderRecyclerView != null) {
                        onWaitPayOrderRecyclerView.OnPayButtonClick(getLayoutPosition());

                    }
                }
            });

            btn_showdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onWaitPayOrderRecyclerView != null) {
                        onWaitPayOrderRecyclerView.OnShowDetailButtonClick(getLayoutPosition());

                    }
                }
            });
        }
    }


    /*点击立即支付的监听事件*/
    public interface OnWaitPayOrderRecyclerView {
        public void OnPayButtonClick(int position);

        public void OnShowDetailButtonClick(int position);
    }

    private OnWaitPayOrderRecyclerView onWaitPayOrderRecyclerView;

    public void setOnWaitPayOrderRecyclerView(OnWaitPayOrderRecyclerView onWaitPayOrderRecyclerView) {
        this.onWaitPayOrderRecyclerView = onWaitPayOrderRecyclerView;
    }
}

