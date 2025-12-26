package com.example.codecup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecup.R;
import com.example.codecup.model.PointHistory;

import java.util.List;

/**
 * ADAPTER CHO DANH SÁCH LỊCH SỬ THƯỞNG (Reward History)
 * Hiển thị cả điểm nhận được và lịch sử đổi thưởng
 */
public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.PointViewHolder> {

    private Context context;
    private List<PointHistory> pointHistoryList;

    public PointHistoryAdapter(Context context, List<PointHistory> pointHistoryList) {
        this.context = context;
        this.pointHistoryList = pointHistoryList;
    }

    @NonNull
    @Override
    public PointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_point_history, parent, false);
        return new PointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointViewHolder holder, int position) {
        PointHistory item = pointHistoryList.get(position);

        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getFormattedDate());
        holder.tvPoints.setText(item.getFormattedPoints());

        // Đổi màu và label dựa trên loại
        switch (item.getType()) {
            case LOYALTY_BONUS:
                holder.tvType.setText("BONUS");
                holder.tvType.setTextColor(ContextCompat.getColor(context, R.color.loyalty_gold));
                holder.ivIcon.setImageResource(R.drawable.ic_loyalty);
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.loyalty_gold));
                holder.tvPoints.setTextColor(ContextCompat.getColor(context, R.color.loyalty_gold));
                break;
            case REDEEM:
                holder.tvType.setText("REDEEMED");
                holder.tvType.setTextColor(ContextCompat.getColor(context, R.color.swipe_delete_background));
                holder.ivIcon.setImageResource(R.drawable.ic_redeem);
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.swipe_delete_background));
                holder.tvPoints.setTextColor(ContextCompat.getColor(context, R.color.swipe_delete_background));
                break;
            default: // ORDER
                holder.tvType.setText("EARNED");
                holder.tvType.setTextColor(ContextCompat.getColor(context, R.color.success_green));
                holder.ivIcon.setImageResource(R.drawable.ic_points);
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.coffee_accent));
                holder.tvPoints.setTextColor(ContextCompat.getColor(context, R.color.success_green));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return pointHistoryList != null ? pointHistoryList.size() : 0;
    }

    public void updateHistory(List<PointHistory> newHistory) {
        this.pointHistoryList = newHistory;
        notifyDataSetChanged();
    }

    public static class PointViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvType, tvDescription, tvDate, tvPoints;

        public PointViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_point_icon);
            tvType = itemView.findViewById(R.id.tv_point_type);
            tvDescription = itemView.findViewById(R.id.tv_point_description);
            tvDate = itemView.findViewById(R.id.tv_point_date);
            tvPoints = itemView.findViewById(R.id.tv_point_value);
        }
    }
}

