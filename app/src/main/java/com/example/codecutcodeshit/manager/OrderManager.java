package com.example.codecutcodeshit.manager;

import com.example.codecutcodeshit.model.CartItem;
import com.example.codecutcodeshit.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ORDER MANAGER - Quản lý danh sách đơn hàng
 *
 * Sử dụng Singleton Pattern
 */
public class OrderManager {

    private static OrderManager instance;
    private List<Order> orders;

    private OrderManager() {
        orders = new ArrayList<>();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    /**
     * Tạo đơn hàng mới
     * Tự động thêm điểm thưởng và stamp
     */
    public Order createOrder(List<CartItem> cartItems, double totalPrice) {
        Random random = new Random();
        int orderNumber = 10000 + random.nextInt(90000);

        List<CartItem> itemsCopy = new ArrayList<>(cartItems);
        Order order = new Order(orderNumber, itemsCopy, totalPrice);

        orders.add(0, order);

        // Thêm điểm thưởng và stamp
        RewardsManager.getInstance().addPointsFromOrder(orderNumber, totalPrice);

        return order;
    }

    /**
     * Lấy tất cả đơn hàng
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Lấy đơn hàng đang xử lý (Ongoing)
     */
    public List<Order> getOngoingOrders() {
        List<Order> ongoing = new ArrayList<>();
        for (Order order : orders) {
            if (order.isOngoing()) {
                ongoing.add(order);
            }
        }
        return ongoing;
    }

    /**
     * Lấy đơn hàng đã hoàn thành (History)
     */
    public List<Order> getCompletedOrders() {
        List<Order> completed = new ArrayList<>();
        for (Order order : orders) {
            if (order.isCompleted()) {
                completed.add(order);
            }
        }
        return completed;
    }

    /**
     * Đánh dấu đơn hàng hoàn thành
     */
    public void markOrderCompleted(int orderNumber) {
        for (Order order : orders) {
            if (order.getOrderNumber() == orderNumber) {
                order.markAsCompleted();
                break;
            }
        }
    }

    public boolean hasOrders() {
        return !orders.isEmpty();
    }

    public Order getLatestOrder() {
        if (orders.isEmpty()) return null;
        return orders.get(0);
    }

    public void clearOrders() {
        orders.clear();
    }
}

