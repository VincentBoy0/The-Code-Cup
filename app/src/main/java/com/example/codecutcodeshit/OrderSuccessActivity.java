package com.example.codecutcodeshit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codecutcodeshit.manager.OrderManager;
import com.example.codecutcodeshit.model.Order;

/**
 * ACTIVITY MÀN HÌNH ĐẶT HÀNG THÀNH CÔNG
 *
 * Hiển thị thông báo đặt hàng thành công và mã đơn hàng
 */
public class OrderSuccessActivity extends AppCompatActivity {

    private TextView tvOrderNumber;
    private Button btnTrackOrder, btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        initViews();
        displayOrderNumber();
        setupListeners();
        setupBackPressHandler();
    }

    /**
     * Ánh xạ các View
     */
    private void initViews() {
        tvOrderNumber = findViewById(R.id.tv_order_number);
        btnTrackOrder = findViewById(R.id.btn_track_order);
        btnBackToHome = findViewById(R.id.btn_back_to_home);
    }

    /**
     * Hiển thị mã đơn hàng từ OrderManager
     */
    private void displayOrderNumber() {
        Order latestOrder = OrderManager.getInstance().getLatestOrder();
        if (latestOrder != null) {
            tvOrderNumber.setText(getString(R.string.order_number_format, latestOrder.getOrderNumber()));
        }
    }

    /**
     * Thiết lập sự kiện
     */
    private void setupListeners() {
        // Nút Track My Order - chuyển đến MyOrdersActivity
        btnTrackOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyOrdersActivity.class);
            startActivity(intent);
            finish();
        });

        // Nút quay về Home
        btnBackToHome.setOnClickListener(v -> navigateToHome());
    }

    /**
     * Xử lý nút Back - không cho quay lại Cart
     */
    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToHome();
            }
        });
    }

    /**
     * Chuyển về màn hình Home
     */
    private void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

