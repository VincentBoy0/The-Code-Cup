package com.example.codecup.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.codecup.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * CART MANAGER - Quản lý giỏ hàng
 *
 * Sử dụng Singleton Pattern để đảm bảo chỉ có 1 instance
 * Giỏ hàng được chia sẻ giữa tất cả các Activity
 *
 * DATA PERSISTENCE:
 * - Sử dụng SharedPreferences để lưu trữ
 * - Gson để chuyển đổi List<CartItem> ↔ JSON string
 * - Dữ liệu được lưu mỗi khi có thay đổi
 * - Dữ liệu được load khi app khởi động
 */
public class CartManager {

    // ===== CONSTANTS =====
    private static final String PREF_NAME = "cart_data";
    private static final String KEY_CART_ITEMS = "cart_items";
    private static final String KEY_NEXT_ID = "next_id";

    // ===== SINGLETON =====
    private static CartManager instance;

    // ===== DATA =====
    private List<CartItem> cartItems;
    private int nextId = 1;

    // ===== PERSISTENCE =====
    private SharedPreferences sharedPreferences;
    private Gson gson;

    /**
     * Private constructor - không cho tạo từ bên ngoài
     * Khởi tạo với dữ liệu rỗng, cần gọi init() để load data
     */
    private CartManager() {
        cartItems = new ArrayList<>();
        gson = new Gson();
    }

    /**
     * Lấy instance duy nhất (Singleton Pattern)
     */
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    /**
     * QUAN TRỌNG: Phải gọi method này khi app khởi động
     * Thường gọi trong Application class hoặc MainActivity
     *
     * @param context Application context
     */
    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadFromStorage();
    }

    /**
     * Load dữ liệu từ SharedPreferences
     * Chuyển JSON string → List<CartItem>
     */
    private void loadFromStorage() {
        if (sharedPreferences == null) return;

        // Load cart items
        String json = sharedPreferences.getString(KEY_CART_ITEMS, "[]");
        Type type = new TypeToken<List<CartItem>>(){}.getType();
        cartItems = gson.fromJson(json, type);

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Load next ID
        nextId = sharedPreferences.getInt(KEY_NEXT_ID, 1);
    }

    /**
     * Lưu dữ liệu vào SharedPreferences
     * Chuyển List<CartItem> → JSON string
     */
    private void saveToStorage() {
        if (sharedPreferences == null) return;

        String json = gson.toJson(cartItems);
        sharedPreferences.edit()
                .putString(KEY_CART_ITEMS, json)
                .putInt(KEY_NEXT_ID, nextId)
                .apply();
    }

    /**
     * Thêm item vào giỏ hàng
     * Tự động lưu sau khi thêm
     */
    public void addItem(CartItem item) {
        // Kiểm tra xem đã có item giống không (cùng coffee, size, cup, ice)
        for (CartItem existingItem : cartItems) {
            if (existingItem.getCoffeeId() == item.getCoffeeId() &&
                existingItem.getSize().equals(item.getSize()) &&
                existingItem.getCupType().equals(item.getCupType()) &&
                existingItem.getIceLevel().equals(item.getIceLevel())) {

                // Nếu có, tăng số lượng
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                saveToStorage(); // Lưu thay đổi
                return;
            }
        }

        // Nếu không, thêm item mới
        cartItems.add(item);
        saveToStorage(); // Lưu thay đổi
    }

    /**
     * Xóa item khỏi giỏ hàng
     */
    public void removeItem(int itemId) {
        cartItems.removeIf(item -> item.getId() == itemId);
        saveToStorage(); // Lưu thay đổi
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void clearCart() {
        cartItems.clear();
        saveToStorage(); // Lưu thay đổi
    }

    // ===== NEW METHODS =====
    public void clearData() {
        cartItems = new ArrayList<>();
        nextId = 1;
        saveToStorage();
    }

    public void loadDataForUser(Context context, String userId) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME + "_" + userId, Context.MODE_PRIVATE);
        loadFromStorage();
    }

    // ===== GETTERS =====

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getNextId() {
        return nextId++;
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
