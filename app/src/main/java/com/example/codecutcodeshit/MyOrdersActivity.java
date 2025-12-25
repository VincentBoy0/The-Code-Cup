package com.example.codecutcodeshit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecutcodeshit.adapter.OrderAdapter;
import com.example.codecutcodeshit.manager.OrderManager;
import com.example.codecutcodeshit.model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * ACTIVITY MÀN HÌNH DANH SÁCH ĐƠN HÀNG (My Orders)
 *
 * 2 section: Ongoing Orders và Order History
 * Có bottom navigation giống các màn hình khác
 */
public class MyOrdersActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {

    // Views
    private RecyclerView rvOngoingOrders, rvHistoryOrders;
    private CardView cardNoOngoing, cardNoHistory;
    private TextView tvOngoingTitle, tvOngoingHint, tvHistoryTitle;
    private LinearLayout layoutEmptyOrders;
    private Button btnStartOrdering;
    private BottomNavigationView bottomNavigation;

    // Adapters
    private OrderAdapter ongoingAdapter, historyAdapter;

    // Manager
    private OrderManager orderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        orderManager = OrderManager.getInstance();

        initViews();
        setupRecyclerViews();
        setupBottomNavigation();
        setupListeners();
        updateUI();
    }

    /**
     * Refresh dữ liệu mỗi khi quay lại màn hình
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách orders
        if (ongoingAdapter != null) {
            ongoingAdapter.updateOrders(orderManager.getOngoingOrders());
        }
        if (historyAdapter != null) {
            historyAdapter.updateOrders(orderManager.getCompletedOrders());
        }
        updateUI();
        // Đảm bảo tab Orders được chọn
        bottomNavigation.setSelectedItemId(R.id.nav_orders);
    }

    private void initViews() {
        rvOngoingOrders = findViewById(R.id.rv_ongoing_orders);
        rvHistoryOrders = findViewById(R.id.rv_history_orders);
        cardNoOngoing = findViewById(R.id.card_no_ongoing);
        cardNoHistory = findViewById(R.id.card_no_history);
        tvOngoingTitle = findViewById(R.id.tv_ongoing_title);
        tvOngoingHint = findViewById(R.id.tv_ongoing_hint);
        tvHistoryTitle = findViewById(R.id.tv_history_title);
        layoutEmptyOrders = findViewById(R.id.layout_empty_orders);
        btnStartOrdering = findViewById(R.id.btn_start_ordering);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void setupRecyclerViews() {
        // Ongoing Orders
        rvOngoingOrders.setLayoutManager(new LinearLayoutManager(this));
        ongoingAdapter = new OrderAdapter(this, orderManager.getOngoingOrders(), true, this);
        rvOngoingOrders.setAdapter(ongoingAdapter);

        // History Orders
        rvHistoryOrders.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new OrderAdapter(this, orderManager.getCompletedOrders(), false, this);
        rvHistoryOrders.setAdapter(historyAdapter);
    }

    private void setupBottomNavigation() {
        // Đánh dấu Orders là tab đang được chọn
        bottomNavigation.setSelectedItemId(R.id.nav_orders);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_orders) {
                // Đang ở Orders rồi
                return true;
            } else if (itemId == R.id.nav_rewards) {
                startActivity(new Intent(this, RewardsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupListeners() {
        btnStartOrdering.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void updateUI() {
        if (!orderManager.hasOrders()) {
            // Không có đơn hàng nào
            layoutEmptyOrders.setVisibility(View.VISIBLE);
            tvOngoingTitle.setVisibility(View.GONE);
            tvOngoingHint.setVisibility(View.GONE);
            rvOngoingOrders.setVisibility(View.GONE);
            cardNoOngoing.setVisibility(View.GONE);
            tvHistoryTitle.setVisibility(View.GONE);
            rvHistoryOrders.setVisibility(View.GONE);
            cardNoHistory.setVisibility(View.GONE);
        } else {
            layoutEmptyOrders.setVisibility(View.GONE);

            // Ongoing section
            tvOngoingTitle.setVisibility(View.VISIBLE);
            tvOngoingHint.setVisibility(View.VISIBLE);
            if (orderManager.getOngoingOrders().isEmpty()) {
                rvOngoingOrders.setVisibility(View.GONE);
                cardNoOngoing.setVisibility(View.VISIBLE);
            } else {
                rvOngoingOrders.setVisibility(View.VISIBLE);
                cardNoOngoing.setVisibility(View.GONE);
            }

            // History section
            tvHistoryTitle.setVisibility(View.VISIBLE);
            if (orderManager.getCompletedOrders().isEmpty()) {
                rvHistoryOrders.setVisibility(View.GONE);
                cardNoHistory.setVisibility(View.VISIBLE);
            } else {
                rvHistoryOrders.setVisibility(View.VISIBLE);
                cardNoHistory.setVisibility(View.GONE);
            }
        }
    }

    // ===== Click vào Ongoing Order =====
    @Override
    public void onOrderClick(Order order) {
        new AlertDialog.Builder(this)
                .setTitle("Complete Order")
                .setMessage("Mark Order #" + order.getOrderNumber() + " as completed?")
                .setPositiveButton("Complete", (dialog, which) -> {
                    order.markAsCompleted();

                    ongoingAdapter.updateOrders(orderManager.getOngoingOrders());
                    historyAdapter.updateOrders(orderManager.getCompletedOrders());

                    updateUI();

                    Toast.makeText(this, "Order #" + order.getOrderNumber() + " completed!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

