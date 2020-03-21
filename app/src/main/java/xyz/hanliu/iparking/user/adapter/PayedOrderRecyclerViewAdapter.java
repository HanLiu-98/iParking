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

public class PayedOrderRecyclerViewAdapter extends RecyclerView.Adapter<PayedOrderRecyclerViewAdapter.ViewHolder> {


    private List<OrderPlus> list;

    public PayedOrderRecyclerViewAdapter(List<OrderPlus> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*Activity里用RecyclerView用这个代码！！！*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payedorder, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderPlus orderPlus = list.get(position);

        holder.tv_area.setText(orderPlus.getArea());
        holder.tv_positondetail.setText(orderPlus.getPositionDetail());
        String starttime = DateUtil.dateToStrGeneral(orderPlus.getStartTime());
        String endtime = DateUtil.dateToStrGeneral(orderPlus.getEndTime());
        String time = starttime + " —— " + endtime;
        holder.tv_time.setText(time);
        holder.tv_orderpaytime.setText(DateUtil.dateToStrDetail(orderPlus.getPayTime()));
        holder.tv_ordercreatetime.setText(DateUtil.dateToStrDetail(orderPlus.getCreateTime()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_area;
        public TextView tv_positondetail;
        public TextView tv_time;
        public TextView tv_ordercreatetime;
        public TextView tv_orderpaytime;

        public Button btn_showdetail;
        public Button btn_moneyback;
        public Button btn_finishparking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_area = itemView.findViewById(R.id.tv_area_item_payed);
            tv_positondetail = itemView.findViewById(R.id.tv_positiondetail_item_payed);
            tv_time = itemView.findViewById(R.id.tv_availabletime_item_payed);
            tv_ordercreatetime = itemView.findViewById(R.id.tv_createtime_item_payed);
            tv_orderpaytime = itemView.findViewById(R.id.tv_paytime_item_payed);

            btn_showdetail = itemView.findViewById(R.id.btn_showspacedetail_item_payed);
            btn_finishparking = itemView.findViewById(R.id.btn_finish_item_payed);
            btn_moneyback = itemView.findViewById(R.id.btn_moneyback_item_payed);

            /**
             * 三个按钮的监听事件
             * 1.查看车位详情
             * 2.退款
             * 3.确认停车成功
             */

            btn_moneyback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPayedOrderRecyclerView != null) {
                        onPayedOrderRecyclerView.OnMoneyBackButtonClick(getLayoutPosition());
                    }
                }
            });

            btn_showdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPayedOrderRecyclerView != null) {
                        onPayedOrderRecyclerView.OnShowDetailButtonClick(getLayoutPosition());
                    }
                }
            });

            btn_finishparking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPayedOrderRecyclerView != null) {
                        onPayedOrderRecyclerView.OnFinishParkingButtonClick(getLayoutPosition());
                    }
                }
            });

        }
    }


    public interface OnPayedOrderRecyclerView {
        void OnMoneyBackButtonClick(int position);

        void OnShowDetailButtonClick(int position);

        void OnFinishParkingButtonClick(int position);
    }

    private OnPayedOrderRecyclerView onPayedOrderRecyclerView;

    public void setOnPayedOrderRecyclerView(OnPayedOrderRecyclerView onPayedOrderRecyclerView) {
        this.onPayedOrderRecyclerView = onPayedOrderRecyclerView;
    }
}
