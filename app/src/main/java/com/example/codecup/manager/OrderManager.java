package com.example.codecup.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.codecup.model.CartItem;
import com.example.codecup.model.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ORDER MANAGER - Quản lý danh sách đơn hàng
 *
 * Sử dụng Singleton Pattern
 *
 * DATA PERSISTENCE:
 * - Sử dụng SharedPreferences để lưu trữ
 * - Gson để chuyển đổi List<Order> ↔ JSON string
 * - Lịch sử đơn hàng được lưu trữ vĩnh viễn
 */
public class OrderManager {

    // ===== CONSTANTS =====
    private static final String PREF_NAME = "orders_data";
    private static final String KEY_ORDERS = "orders_list";

    // ===== SINGLETON =====
    private static OrderManager instance;

    // ===== DATA =====
    private List<Order> orders;

    // ===== PERSISTENCE =====
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private OrderManager() {
        orders = new ArrayList<>();
        gson = new Gson();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
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

        String json = sharedPreferences.getString(KEY_ORDERS, "[]");
        Type type = new TypeToken<List<Order>>(){}.getType();
        orders = gson.fromJson(json, type);

        if (orders == null) {
            orders = new ArrayList<>();
        }
    }

    /**
     * Lưu dữ liệu vào SharedPreferences
     */
    private void saveToStorage() {
        if (sharedPreferences == null) return;

        String json = gson.toJson(orders);
        sharedPreferences.edit()
                .putString(KEY_ORDERS, json)
                .apply();
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
        saveToStorage(); // Lưu thay đổi

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
                saveToStorage(); // Lưu thay đổi
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

    /**
     * Xóa tất cả đơn hàng (cho testing)
     */
    public void clearOrders() {
        orders.clear();
        saveToStorage();
    }
}

