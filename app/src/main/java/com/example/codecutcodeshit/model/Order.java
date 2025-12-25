package com.example.codecutcodeshit.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Model class đại diện cho một đơn hàng
 *
 * Hỗ trợ Gson serialization:
 * - status: Enum được Gson serialize thành String
 * - orderDate: Lưu dưới dạng timestamp (long) để tránh lỗi parsing
 */
public class Order {

    // 2 trạng thái đơn giản
    public enum Status {
        ONGOING,      // Đang xử lý
        COMPLETED     // Đã hoàn thành
    }

    private int orderNumber;
    private List<CartItem> items;
    private double totalPrice;
    private Status status;
    private long orderDateTimestamp; // Lưu Date dưới dạng timestamp cho Gson

    // Constructor
    public Order(int orderNumber, List<CartItem> items, double totalPrice) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = Status.ONGOING; // Mặc định là đang xử lý
        this.orderDateTimestamp = System.currentTimeMillis();
    }

    // Getters
    public int getOrderNumber() { return orderNumber; }
    public List<CartItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public Status getStatus() { return status; }

    public Date getOrderDate() {
        return new Date(orderDateTimestamp);
    }

    // Setters
    public void setStatus(Status status) { this.status = status; }

    // Kiểm tra trạng thái
    public boolean isOngoing() { return status == Status.ONGOING; }
    public boolean isCompleted() { return status == Status.COMPLETED; }

    // Chuyển sang hoàn thành
    public void markAsCompleted() { this.status = Status.COMPLETED; }

    // Lấy text mô tả items
    public String getItemsDescription() {
        if (items == null || items.isEmpty()) {
            return "No items";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            sb.append(item.getQuantity())
              .append("x ")
              .append(item.getName())
              .append(" (")
              .append(item.getSize())
              .append(")");

            if (i < items.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    // Lấy ngày đặt hàng dạng text
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.US);
        return sdf.format(getOrderDate());
    }
}

