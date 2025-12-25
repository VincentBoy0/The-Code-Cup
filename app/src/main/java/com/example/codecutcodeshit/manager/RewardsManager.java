package com.example.codecutcodeshit.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.codecutcodeshit.model.PointHistory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * REWARDS MANAGER - Quản lý điểm thưởng và stamps
 *
 * Singleton Pattern
 *
 * Logic:
 * - Mỗi đơn hàng hoàn thành: +1 stamp, +điểm (dựa trên giá trị đơn hàng)
 * - Đủ 8 stamps: TỰ ĐỘNG cộng bonus points và reset stamps
 * - Điểm = giá trị đơn hàng * 10 (ví dụ: $5 = 50 điểm)
 * - Bonus khi hoàn thành 8 stamps: 100 điểm
 * - Điểm có thể dùng để Redeem đồ uống
 *
 * DATA PERSISTENCE:
 * - Sử dụng SharedPreferences để lưu trữ
 * - Gson để chuyển đổi List<PointHistory> ↔ JSON string
 * - Dữ liệu được lưu mỗi khi có thay đổi
 */
public class RewardsManager {

    // ===== CONSTANTS =====
    private static final String PREF_NAME = "rewards_data";
    private static final String KEY_STAMP_COUNT = "stamp_count";
    private static final String KEY_TOTAL_POINTS = "total_points";
    private static final String KEY_LOYALTY_BONUS_COUNT = "loyalty_bonus_count";
    private static final String KEY_POINT_HISTORY = "point_history";

    public static final int MAX_STAMPS = 8;
    public static final int LOYALTY_BONUS_POINTS = 100;
    public static final double POINTS_PER_DOLLAR = 10.0;

    // ===== SINGLETON =====
    private static RewardsManager instance;

    // ===== DATA =====
    private int stampCount = 0;
    private int totalPoints = 0;
    private int loyaltyBonusCount = 0;
    private List<PointHistory> pointHistory;

    // ===== PERSISTENCE =====
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private RewardsManager() {
        pointHistory = new ArrayList<>();
        gson = new Gson();
    }

    public static synchronized RewardsManager getInstance() {
        if (instance == null) {
            instance = new RewardsManager();
        }
        return instance;
    }

    /**
     * QUAN TRỌNG: Phải gọi method này khi app khởi động
     *
     * @param context Application context
     */
    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadFromStorage();
    }

    /**
     * Load dữ liệu từ SharedPreferences
     */
    private void loadFromStorage() {
        if (sharedPreferences == null) return;

        stampCount = sharedPreferences.getInt(KEY_STAMP_COUNT, 0);
        totalPoints = sharedPreferences.getInt(KEY_TOTAL_POINTS, 0);
        loyaltyBonusCount = sharedPreferences.getInt(KEY_LOYALTY_BONUS_COUNT, 0);

        // Load point history
        String json = sharedPreferences.getString(KEY_POINT_HISTORY, "[]");
        Type type = new TypeToken<List<PointHistory>>(){}.getType();
        pointHistory = gson.fromJson(json, type);

        if (pointHistory == null) {
            pointHistory = new ArrayList<>();
        }
    }

    /**
     * Lưu dữ liệu vào SharedPreferences
     */
    private void saveToStorage() {
        if (sharedPreferences == null) return;

        String historyJson = gson.toJson(pointHistory);
        sharedPreferences.edit()
                .putInt(KEY_STAMP_COUNT, stampCount)
                .putInt(KEY_TOTAL_POINTS, totalPoints)
                .putInt(KEY_LOYALTY_BONUS_COUNT, loyaltyBonusCount)
                .putString(KEY_POINT_HISTORY, historyJson)
                .apply();
    }

    /**
     * Thêm điểm từ đơn hàng hoàn thành
     * Tự động xử lý stamps và bonus
     *
     * @param orderNumber Mã đơn hàng
     * @param orderTotal Tổng giá trị đơn hàng
     * @return true nếu vừa hoàn thành chuỗi 8 stamps
     */
    public boolean addPointsFromOrder(int orderNumber, double orderTotal) {
        int points = (int) (orderTotal * POINTS_PER_DOLLAR);
        totalPoints += points;

        pointHistory.add(0, new PointHistory(
                points,
                "Order #" + orderNumber,
                PointHistory.Type.ORDER
        ));

        stampCount++;

        boolean completedLoyalty = false;
        if (stampCount >= MAX_STAMPS) {
            completedLoyalty = completeLoyaltyCard();
        }

        saveToStorage(); // Lưu thay đổi
        return completedLoyalty;
    }

    /**
     * Hoàn thành chuỗi 8 stamps
     */
    private boolean completeLoyaltyCard() {
        stampCount = 0;
        loyaltyBonusCount++;
        totalPoints += LOYALTY_BONUS_POINTS;

        pointHistory.add(0, new PointHistory(
                LOYALTY_BONUS_POINTS,
                "Loyalty Bonus #" + loyaltyBonusCount + " (8 stamps completed!)",
                PointHistory.Type.LOYALTY_BONUS
        ));

        return true;
    }

    /**
     * Đổi điểm lấy đồ uống
     */
    public boolean redeemPoints(int pointsToRedeem, String rewardName) {
        if (totalPoints >= pointsToRedeem) {
            totalPoints -= pointsToRedeem;

            pointHistory.add(0, new PointHistory(
                    -pointsToRedeem,
                    "Redeemed: " + rewardName,
                    PointHistory.Type.REDEEM
            ));

            saveToStorage(); // Lưu thay đổi
            return true;
        }
        return false;
    }

    // ===== GETTERS =====
    public int getStampCount() { return stampCount; }
    public int getTotalPoints() { return totalPoints; }
    public List<PointHistory> getPointHistory() { return pointHistory; }
    public int getLoyaltyBonusCount() { return loyaltyBonusCount; }

    public boolean canRedeem(int points) {
        return totalPoints >= points;
    }

    public boolean hasHistory() {
        return !pointHistory.isEmpty();
    }

    public int getStampsRemaining() {
        return MAX_STAMPS - stampCount;
    }

    /**
     * Reset tất cả dữ liệu (cho testing)
     */
    public void reset() {
        stampCount = 0;
        totalPoints = 0;
        loyaltyBonusCount = 0;
        pointHistory.clear();
        saveToStorage();
    }
}

