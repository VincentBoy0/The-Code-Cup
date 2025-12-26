package com.example.codecup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecup.R;
import com.example.codecup.manager.RewardsManager;
import com.example.codecup.model.Reward;

import java.util.List;

/**
 * ADAPTER CHO DANH SÁCH REWARDS CÓ THỂ ĐỔI
 */
public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private Context context;
    private List<Reward> rewards;
    private OnRewardRedeemListener listener;

    public interface OnRewardRedeemListener {
        void onRedeemClick(Reward reward);
    }

    public RewardAdapter(Context context, List<Reward> rewards, OnRewardRedeemListener listener) {
        this.context = context;
        this.rewards = rewards;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reward, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward reward = rewards.get(position);

        holder.tvName.setText(reward.getName());
        holder.tvDescription.setText(reward.getDescription());
        holder.tvPointsRequired.setText(reward.getFormattedPoints());
        holder.ivIcon.setImageResource(reward.getIconResId());

        // Kiểm tra đủ điểm để redeem không
        boolean canRedeem = RewardsManager.getInstance().canRedeem(reward.getPointsRequired());

        if (canRedeem) {
            holder.btnRedeem.setEnabled(true);
            holder.btnRedeem.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.coffee_primary));
            holder.btnRedeem.setAlpha(1.0f);
        } else {
            holder.btnRedeem.setEnabled(false);
            holder.btnRedeem.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.text_secondary));
            holder.btnRedeem.setAlpha(0.5f);
        }

        holder.btnRedeem.setOnClickListener(v -> {
            if (listener != null && canRedeem) {
                listener.onRedeemClick(reward);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewards != null ? rewards.size() : 0;
    }

    public void notifyPointsChanged() {
        notifyDataSetChanged();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvDescription, tvPointsRequired;
        Button btnRedeem;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_reward_icon);
            tvName = itemView.findViewById(R.id.tv_reward_name);
            tvDescription = itemView.findViewById(R.id.tv_reward_description);
            tvPointsRequired = itemView.findViewById(R.id.tv_points_required);
            btnRedeem = itemView.findViewById(R.id.btn_redeem);
        }
    }
}

