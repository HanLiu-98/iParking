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

public class SpaceRecyclerViewAdapter extends RecyclerView.Adapter<SpaceRecyclerViewAdapter.ViewHolder> {


    private final List<ParkingSpace_General> space_list;
    private final Context mContext;

    public SpaceRecyclerViewAdapter(Context mContext, List<ParkingSpace_General> space_list) {
        this.mContext = mContext;
        this.space_list = space_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_space, null);

        final ViewHolder holder = new ViewHolder(itemView);

//        holder.spaceView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int positiion=holder.getAdapterPosition();
//                ParkingSpace space=space_list.get(positiion);
//
//
//                Intent intent = new Intent(mContext, SpaceDetail.class);
//                Bundle bundle = new Bundle();
//
//                bundle.putInt("pk",123);
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//                ToastUtil.showMsg(mContext,"aha!");
//
//            }
//        });


        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 根据位置进行数据绑定
        ParkingSpace_General space = space_list.get(position);
//        super(itemView);
////        iv_spaceImage=itemView.findViewById(R.id.iv_image_item);
////        tv_positionDetail=itemView.findViewById(R.id.tv_positiondetail_item);
////        tv_time=itemView.findViewById(R.id.tv_time_item);
////        tv_price= itemView.findViewById(R.id.tv_price_item);

        ;
        Glide.with(mContext)
                .load(Config.IMAGE_HOST + space.getImagePath()).into(holder.iv_spaceImage);
        holder.tv_positionDetail.setText(space.getPositionDetail());
        String starttime = DateUtil.dateToStrGeneral(space.getStartTime());
        String endtime = DateUtil.dateToStrGeneral(space.getEndTime());
        String time = starttime + "-" + endtime;
        holder.tv_time.setText(time);
        holder.tv_price.setText(String.valueOf(space.getPrice()));


//
//        holder.position.setText(space.getPosition());
//        int price=space.getPrice();
//        holder.price.setText(String.valueOf(price));
//        String time =space.getStarttime()+"-"+space.getEndtime();
//
//        Log.d("time", time);
//
//        holder.time.setText(time);

        // 绑定数据
//        ParkingSpace space=mSpaceList.get(position);
//        holder.spaceImage.setImageResource(space.getImage());
//        holder.position.setText(space.getPosition());
//        holder.time.setText(space.getStarttime()+"-"+space.getEndtime());
//        holder.price.setText(space.getPrice());
    }

    @Override
    public int getItemCount() {
        return space_list.size();
    }

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

    /*点击项目的监听事件*/
    public interface OnSpaceRecyclerView {
        public void OnItemClick(int position);
    }

    private OnSpaceRecyclerView onSpaceRecyclerView;

    public void setOnSpaceRecyclerView(OnSpaceRecyclerView onSpaceRecyclerView) {
        this.onSpaceRecyclerView = onSpaceRecyclerView;
    }


}
