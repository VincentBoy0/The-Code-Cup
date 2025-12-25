package com.example.codecutcodeshit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecutcodeshit.adapter.PointHistoryAdapter;
import com.example.codecutcodeshit.manager.RewardsManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * ACTIVITY MÀN HÌNH REWARDS
 *
 * Hiển thị:
 * - Loyalty Card với 8 stamps (tự động reset và cộng bonus khi đủ)
 * - Tổng điểm thưởng
 * - Nút Redeem Drinks
 * - Lịch sử điểm
 */
public class RewardsActivity extends AppCompatActivity {

    // Views
    private TextView tvStampCount, tvLoyaltyMessage, tvTotalPoints;
    private Button btnRedeemDrinks;
    private ImageView[] stamps = new ImageView[8];
    private RecyclerView rvPointsHistory;
    private CardView cardNoHistory;
    private BottomNavigationView bottomNavigation;

    // Adapter
    private PointHistoryAdapter historyAdapter;

    // Manager
    private RewardsManager rewardsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        rewardsManager = RewardsManager.getInstance();

        initViews();
        setupRecyclerView();
        setupBottomNavigation();
        setupListeners();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (historyAdapter != null) {
            historyAdapter.updateHistory(rewardsManager.getPointHistory());
        }
        updateUI();
        bottomNavigation.setSelectedItemId(R.id.nav_rewards);
    }

    private void initViews() {
        tvStampCount = findViewById(R.id.tv_stamp_count);
        tvLoyaltyMessage = findViewById(R.id.tv_loyalty_message);
        tvTotalPoints = findViewById(R.id.tv_total_points);
        btnRedeemDrinks = findViewById(R.id.btn_redeem_drinks);
        rvPointsHistory = findViewById(R.id.rv_points_history);
        cardNoHistory = findViewById(R.id.card_no_history);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        stamps[0] = findViewById(R.id.stamp_1);
        stamps[1] = findViewById(R.id.stamp_2);
        stamps[2] = findViewById(R.id.stamp_3);
        stamps[3] = findViewById(R.id.stamp_4);
        stamps[4] = findViewById(R.id.stamp_5);
        stamps[5] = findViewById(R.id.stamp_6);
        stamps[6] = findViewById(R.id.stamp_7);
        stamps[7] = findViewById(R.id.stamp_8);
    }

    private void setupRecyclerView() {
        rvPointsHistory.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new PointHistoryAdapter(this, rewardsManager.getPointHistory());
        rvPointsHistory.setAdapter(historyAdapter);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_rewards);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(this, MyOrdersActivity.class));
                return true;
            } else if (itemId == R.id.nav_rewards) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupListeners() {
        // Nút Redeem Drinks - chuyển sang màn hình Redeem
        btnRedeemDrinks.setOnClickListener(v -> {
            startActivity(new Intent(this, RedeemActivity.class));
        });
    }

    private void updateUI() {
        int stampCount = rewardsManager.getStampCount();
        tvStampCount.setText(stampCount + "/" + RewardsManager.MAX_STAMPS);

        for (int i = 0; i < stamps.length; i++) {
            if (i < stampCount) {
                stamps[i].setImageResource(R.drawable.ic_stamp_filled);
            } else {
                stamps[i].setImageResource(R.drawable.ic_stamp_empty);
            }
        }

        int remaining = rewardsManager.getStampsRemaining();
        if (remaining == 0) {
            tvLoyaltyMessage.setText("🎉 Bonus points earned!");
        } else {
            tvLoyaltyMessage.setText(remaining + " more stamp" + (remaining > 1 ? "s" : "") + " for +"
                    + RewardsManager.LOYALTY_BONUS_POINTS + " bonus points!");
        }

        tvTotalPoints.setText(String.valueOf(rewardsManager.getTotalPoints()));

        if (rewardsManager.hasHistory()) {
            rvPointsHistory.setVisibility(View.VISIBLE);
            cardNoHistory.setVisibility(View.GONE);
        } else {
            rvPointsHistory.setVisibility(View.GONE);
            cardNoHistory.setVisibility(View.VISIBLE);
        }
    }
}
