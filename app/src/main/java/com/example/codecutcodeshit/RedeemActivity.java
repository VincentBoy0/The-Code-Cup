package com.example.codecutcodeshit;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecutcodeshit.adapter.RewardAdapter;
import com.example.codecutcodeshit.manager.RewardsManager;
import com.example.codecutcodeshit.model.Reward;

import java.util.ArrayList;
import java.util.List;

/**
 * ACTIVITY MÀN HÌNH REDEEM REWARDS
 *
 * Đổi điểm lấy đồ uống
 */
public class RedeemActivity extends AppCompatActivity implements RewardAdapter.OnRewardRedeemListener {

    private ImageView ivBack;
    private TextView tvPointsBalance;
    private RecyclerView rvRewards;

    private RewardAdapter rewardAdapter;
    private RewardsManager rewardsManager;
    private List<Reward> rewardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        rewardsManager = RewardsManager.getInstance();

        initViews();
        createRewardList();
        setupRecyclerView();
        setupListeners();
        updatePointsBalance();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvPointsBalance = findViewById(R.id.tv_points_balance);
        rvRewards = findViewById(R.id.rv_rewards);
    }

    /**
     * Tạo danh sách các phần thưởng có thể đổi
     */
    private void createRewardList() {
        rewardList = new ArrayList<>();

        rewardList.add(new Reward(1,
                "Free Espresso",
                "Any size espresso drink",
                50, R.drawable.ic_coffee_cup));

        rewardList.add(new Reward(2,
                "Free Cappuccino",
                "Regular size cappuccino",
                80, R.drawable.ic_coffee_cup));

        rewardList.add(new Reward(3,
                "Free Latte",
                "Any size latte drink",
                100, R.drawable.ic_coffee_cup));

        rewardList.add(new Reward(4,
                "Free Mocha",
                "Regular size mocha",
                120, R.drawable.ic_coffee_cup));

        rewardList.add(new Reward(5,
                "Free Cold Brew",
                "Large cold brew coffee",
                90, R.drawable.ic_coffee_cup));

        rewardList.add(new Reward(6,
                "Free Any Drink",
                "Any drink of your choice",
                150, R.drawable.ic_coffee_cup));

        rewardList.add(new Reward(7,
                "Free Pastry",
                "Any pastry from the menu",
                70, R.drawable.ic_coffee_cup));

        rewardList.add(new Reward(8,
                "Free Breakfast Combo",
                "Coffee + Pastry combo",
                200, R.drawable.ic_coffee_cup));
    }

    private void setupRecyclerView() {
        rvRewards.setLayoutManager(new LinearLayoutManager(this));
        rewardAdapter = new RewardAdapter(this, rewardList, this);
        rvRewards.setAdapter(rewardAdapter);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());
    }

    private void updatePointsBalance() {
        tvPointsBalance.setText(String.valueOf(rewardsManager.getTotalPoints()));
    }

    @Override
    public void onRedeemClick(Reward reward) {
        // Hiển thị dialog xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Redeem " + reward.getName() + "?")
                .setMessage("This will cost " + reward.getPointsRequired() + " points.\n\n" +
                        "Your balance: " + rewardsManager.getTotalPoints() + " points")
                .setPositiveButton("Redeem", (dialog, which) -> {
                    if (rewardsManager.redeemPoints(reward.getPointsRequired(), reward.getName())) {
                        // Thành công
                        updatePointsBalance();
                        rewardAdapter.notifyPointsChanged();

                        Toast.makeText(this,
                                "🎉 " + reward.getName() + " redeemed!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Không đủ điểm
                        Toast.makeText(this,
                                "Not enough points!",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePointsBalance();
        if (rewardAdapter != null) {
            rewardAdapter.notifyPointsChanged();
        }
    }
}

