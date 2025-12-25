package com.example.codecutcodeshit;

import android.app.Application;

import com.example.codecutcodeshit.manager.CartManager;
import com.example.codecutcodeshit.manager.OrderManager;
import com.example.codecutcodeshit.manager.RewardsManager;

/**
 * APPLICATION CLASS - Điểm khởi đầu của ứng dụng
 *
 * Class này được gọi khi app khởi động, TRƯỚC tất cả Activity
 *
 * CHỨC NĂNG:
 * 1. Khởi tạo Data Persistence cho các Manager
 * 2. Load dữ liệu đã lưu từ storage
 *
 * LƯU Ý:
 * - Phải đăng ký trong AndroidManifest.xml với android:name=".CodeCupApplication"
 * - Sử dụng Application Context (không phải Activity Context) để tránh memory leak
 */
public class CodeCupApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // ========== KHỞI TẠO DATA PERSISTENCE ==========
        // Gọi init() cho mỗi Manager với Application Context
        // Điều này sẽ load dữ liệu đã lưu từ SharedPreferences

        // 1. Cart Manager - Giỏ hàng
        CartManager.getInstance().init(this);

        // 2. Rewards Manager - Điểm thưởng và Stamps
        RewardsManager.getInstance().init(this);

        // 3. Order Manager - Lịch sử đơn hàng
        OrderManager.getInstance().init(this);

        // Log để debug
        android.util.Log.d("CodeCupApp", "✅ Data Persistence initialized");
        android.util.Log.d("CodeCupApp", "📦 Cart items: " + CartManager.getInstance().getItemCount());
        android.util.Log.d("CodeCupApp", "🎁 Reward points: " + RewardsManager.getInstance().getTotalPoints());
        android.util.Log.d("CodeCupApp", "📋 Orders: " + OrderManager.getInstance().getOrders().size());
    }
}

