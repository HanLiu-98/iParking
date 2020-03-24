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
import xyz.hanliu.iparking.app.bean.ParkingSpace;
import xyz.hanliu.iparking.utils.DateUtil;

public class MyReleaseRecyclerViewAdapter extends
        RecyclerView.Adapter<MyReleaseRecyclerViewAdapter.ViewHolder>
{

    private List<ParkingSpace> list;

    public MyReleaseRecyclerViewAdapter(List<ParkingSpace> list)
    {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myrelease, parent, false);
        return new MyReleaseRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        ParkingSpace space=list.get(position);
        holder.tv_area.setText(space.getArea());
        holder.tv_positondetail.setText(space.getPositionDetail());
        String starttime = DateUtil.dateToStrGeneral(space.getStartTime());
        String endtime = DateUtil.dateToStrGeneral(space.getEndTime());
        String time = starttime + " —— " + endtime;
        holder.tv_time.setText(time);

        holder.tv_price.setText(String.valueOf(space.getPrice()));
        String msg;
        if(space.getStatus()==0)
            msg="待出租";
        else if(space.getStatus()==1)
            msg="已出租";
        else
            msg="已过期";
        holder.tv_status.setText(msg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView tv_area;
        public TextView tv_positondetail;
        public TextView tv_time;
        public TextView tv_price;
        public TextView tv_status;

        public Button btn_showdetail;
        public Button btn_delete;
        public Button btn_modifyprice;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_area = itemView.findViewById(R.id.tv_area_item_myrelease);
            tv_positondetail = itemView.findViewById(R.id.tv_positiondetail_item_myrelease);
            tv_time = itemView.findViewById(R.id.tv_availabletime_item_myrelease);
            tv_price = itemView.findViewById(R.id.tv_price_item_myrelease);
            tv_status = itemView.findViewById(R.id.tv_status_item_myrelease);
            btn_showdetail = itemView.findViewById(R.id.btn_showspacedetail_item_myrelease);
            btn_delete = itemView.findViewById(R.id.btn_delete_item_myrelease);
            btn_modifyprice = itemView.findViewById(R.id.btn_modifyprice_item_myrelease);
            /**
             * 三个按钮的监听事件
             * 1.查看车位详情
             * 2.撤销发布
             * 3.修改价格
             */
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMyReleaseRecyclerView!=null)
                    {
                        onMyReleaseRecyclerView.OnDeleteButtonClick(getLayoutPosition());
                    }

                }
            });

            btn_showdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMyReleaseRecyclerView!=null)
                    {
                        onMyReleaseRecyclerView.OnShowDetailButtonClick(getLayoutPosition());
                    }

                }
            });

            btn_modifyprice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMyReleaseRecyclerView!=null)
                    {
                        onMyReleaseRecyclerView.OnModifyPriceButtonClick(getLayoutPosition());
                    }

                }
            });
        }
    }


    public interface OnMyReleaseRecyclerView
    {
        void OnDeleteButtonClick(int position);

        void OnShowDetailButtonClick(int position);

        void OnModifyPriceButtonClick(int position);
    }

    private OnMyReleaseRecyclerView onMyReleaseRecyclerView;

    public void setOnMyReleaseRecyclerView(OnMyReleaseRecyclerView onMyReleaseRecyclerView)
    {
        this.onMyReleaseRecyclerView = onMyReleaseRecyclerView;
    }
}
