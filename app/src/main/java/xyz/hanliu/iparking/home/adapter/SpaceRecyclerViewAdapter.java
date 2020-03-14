package xyz.hanliu.iparking.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.app.bean.ParkingSpace_General;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.utils.DateUtil;

/**
 * RecyclerView的适配器
 * 使用心得：一定要选择合适的时候进行适配器的设置，否则会闪退，而且数据list为空。
 */
public class SpaceRecyclerViewAdapter extends RecyclerView.Adapter<SpaceRecyclerViewAdapter.ViewHolder> {
    /*在RecyclerView里显示的空闲车位列表*/
    private List<ParkingSpace_General> space_list;
    private Context mContext;

    /*适配器的构造函数*/
    public SpaceRecyclerViewAdapter(Context mContext, List<ParkingSpace_General> space_list) {
        this.mContext = mContext;
        this.space_list = space_list;
    }


    /*创建视图时调用*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_space, null);
        return new ViewHolder(itemView);
    }


    /*根据位置进行数据绑定*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ParkingSpace_General space = space_list.get(position);

        Glide.with(mContext)
                .load(Config.IMAGE_HOST + space.getImagePath()).into(holder.iv_spaceImage);
        holder.tv_positionDetail.setText(space.getPositionDetail());
        String starttime = DateUtil.dateToStrGeneral(space.getStartTime());
        String endtime = DateUtil.dateToStrGeneral(space.getEndTime());
        String time = starttime + "  ——  " + endtime;
        holder.tv_time.setText(time);
        holder.tv_price.setText(String.valueOf(space.getPrice()));
    }


    /*获取列表项的总条数*/
    @Override
    public int getItemCount() {
        return space_list.size();
    }


    /*一个列表项*/
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_spaceImage;
        public TextView tv_positionDetail;
        public TextView tv_time;
        public TextView tv_price;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_spaceImage = itemView.findViewById(R.id.iv_image_item);
            tv_positionDetail = itemView.findViewById(R.id.tv_positiondetail_item);
            tv_time = itemView.findViewById(R.id.tv_time_item);
            tv_price = itemView.findViewById(R.id.tv_price_item);
            /*列表项的点击事件*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSpaceRecyclerView != null) {
                        onSpaceRecyclerView.OnItemClick(getLayoutPosition());
                    }
                }
            });
        }
    }

    /*点击列表项的监听事件*/
    public interface OnSpaceRecyclerView {
        public void OnItemClick(int position);
    }

    private OnSpaceRecyclerView onSpaceRecyclerView;

    public void setOnSpaceRecyclerView(OnSpaceRecyclerView onSpaceRecyclerView) {
        this.onSpaceRecyclerView = onSpaceRecyclerView;
    }


}
