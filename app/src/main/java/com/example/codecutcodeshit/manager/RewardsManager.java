package com.example.codecutcodeshit.manager;

import com.example.codecutcodeshit.model.PointHistory;

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
 */
public class RewardsManager {

    private static RewardsManager instance;

    // Số stamps hiện tại (0-8)
    private int stampCount = 0;

    // Tổng điểm
    private int totalPoints = 0;

    // Lịch sử điểm
    private List<PointHistory> pointHistory;

    // Số lần đã hoàn thành chuỗi 8 stamps
    private int loyaltyBonusCount = 0;

    // Constants
    public static final int MAX_STAMPS = 8;
    public static final int LOYALTY_BONUS_POINTS = 100;
    public static final double POINTS_PER_DOLLAR = 10.0;

    private RewardsManager() {
        pointHistory = new ArrayList<>();
    }

    public static synchronized RewardsManager getInstance() {
        if (instance == null) {
            instance = new RewardsManager();
        }
        return instance;
    }

    /**
     * Thêm điểm từ đơn hàng hoàn thành
     * Tự động xử lý stamps và bonus
     *
     * @param orderNumber Mã đơn hàng
     * @param orderTotal Tổng giá trị đơn hàng
     * @return true nếu vừa hoàn thành chuỗi 8 stamps (để hiển thị thông báo)
     */
    public boolean addPointsFromOrder(int orderNumber, double orderTotal) {
        // Tính điểm: $1 = 10 điểm
        int points = (int) (orderTotal * POINTS_PER_DOLLAR);

        // Thêm điểm
        totalPoints += points;

        // Thêm vào lịch sử
        pointHistory.add(0, new PointHistory(
                points,
                "Order #" + orderNumber,
                PointHistory.Type.ORDER
        ));

        // Tăng stamp
        stampCount++;

        // Kiểm tra đủ 8 stamps → tự động cộng bonus và reset
        if (stampCount >= MAX_STAMPS) {
            return completeLoyaltyCard();
        }

        return false;
    }

    /**
     * Hoàn thành chuỗi 8 stamps
     * Tự động cộng bonus points và reset stamps
     */
    private boolean completeLoyaltyCard() {
        // Reset stamps
        stampCount = 0;

        // Tăng số lần hoàn thành
        loyaltyBonusCount++;

        // Thêm bonus points
        totalPoints += LOYALTY_BONUS_POINTS;

        // Thêm vào lịch sử
        pointHistory.add(0, new PointHistory(
                LOYALTY_BONUS_POINTS,
                "Loyalty Bonus #" + loyaltyBonusCount + " (8 stamps completed!)",
                PointHistory.Type.LOYALTY_BONUS
        ));

        return true; // Trả về true để thông báo cho user
    }

    /**
     * Đổi điểm lấy đồ uống
     *
     * @param pointsToRedeem Số điểm cần đổi
     * @param rewardName Tên phần thưởng
     * @return true nếu đổi thành công
     */
    public boolean redeemPoints(int pointsToRedeem, String rewardName) {
        if (totalPoints >= pointsToRedeem) {
            totalPoints -= pointsToRedeem;

            // Thêm vào lịch sử (điểm âm)
            pointHistory.add(0, new PointHistory(
                    -pointsToRedeem,
                    "Redeemed: " + rewardName,
                    PointHistory.Type.REDEEM
            ));

            return true;
        }
        return false;
    }

    // Getters
    public int getStampCount() { return stampCount; }
    public int getTotalPoints() { return totalPoints; }
    public List<PointHistory> getPointHistory() { return pointHistory; }
    public int getLoyaltyBonusCount() { return loyaltyBonusCount; }

    // Kiểm tra có đủ điểm để redeem không
    public boolean canRedeem(int points) {
        return totalPoints >= points;
    }

    // Kiểm tra có lịch sử không
    public boolean hasHistory() {
        return !pointHistory.isEmpty();
    }

    // Số stamps còn thiếu để hoàn thành chuỗi
    public int getStampsRemaining() {
        return MAX_STAMPS - stampCount;
    }

    // Reset (cho testing)
    public void reset() {
        stampCount = 0;
        totalPoints = 0;
        loyaltyBonusCount = 0;
        pointHistory.clear();
    }
}

