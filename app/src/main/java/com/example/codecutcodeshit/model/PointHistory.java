package com.example.codecutcodeshit.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Model class đại diện cho một mục lịch sử điểm thưởng
 *
 * Hỗ trợ Gson serialization:
 * - type: Enum được Gson serialize thành String
 * - dateTimestamp: Lưu Date dưới dạng timestamp (long)
 */
public class PointHistory {

    public enum Type {
        ORDER,          // Điểm từ đơn hàng
        LOYALTY_BONUS,  // Điểm thưởng khi hoàn thành 8 stamps
        REDEEM          // Đổi điểm lấy đồ uống (điểm âm)
    }

    private int points;
    private String description;
    private long dateTimestamp; // Lưu Date dưới dạng timestamp cho Gson
    private Type type;

    public PointHistory(int points, String description, Type type) {
        this.points = points;
        this.description = description;
        this.type = type;
        this.dateTimestamp = System.currentTimeMillis();
    }

    // Getters
    public int getPoints() { return points; }
    public String getDescription() { return description; }
    public Date getDate() { return new Date(dateTimestamp); }
    public Type getType() { return type; }

    // Format ngày
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return sdf.format(getDate());
    }

    // Format điểm với dấu + hoặc -
    public String getFormattedPoints() {
        if (points >= 0) {
            return "+" + points;
        } else {
            return String.valueOf(points); // Đã có dấu -
        }
    }

    // Kiểm tra điểm dương hay âm
    public boolean isPositive() {
        return points >= 0;
    }
}

