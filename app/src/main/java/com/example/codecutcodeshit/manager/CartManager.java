package com.example.codecutcodeshit.manager;

import com.example.codecutcodeshit.model.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * CART MANAGER - Quản lý giỏ hàng
 *
 * Sử dụng Singleton Pattern để đảm bảo chỉ có 1 instance
 * Giỏ hàng được chia sẻ giữa tất cả các Activity
 */
public class CartManager {

    // Instance duy nhất (Singleton)
    private static CartManager instance;

    // Danh sách items trong giỏ hàng
    private List<CartItem> cartItems;

    // ID tự tăng cho cart item
    private int nextId = 1;

    // Private constructor - không cho tạo từ bên ngoài
    private CartManager() {
        cartItems = new ArrayList<>();
    }

    // Lấy instance duy nhất
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Thêm item vào giỏ hàng
    public void addItem(CartItem item) {
        // Kiểm tra xem đã có item giống không (cùng coffee, size, cup, ice)
        for (CartItem existingItem : cartItems) {
            if (existingItem.getCoffeeId() == item.getCoffeeId() &&
                existingItem.getSize().equals(item.getSize()) &&
                existingItem.getCupType().equals(item.getCupType()) &&
                existingItem.getIceLevel().equals(item.getIceLevel())) {

                // Nếu có, tăng số lượng
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }

        // Nếu không, thêm item mới
        cartItems.add(item);
    }

    // Xóa item khỏi giỏ hàng
    public void removeItem(int itemId) {
        cartItems.removeIf(item -> item.getId() == itemId);
    }

    // Xóa toàn bộ giỏ hàng
    public void clearCart() {
        cartItems.clear();
    }

    // Lấy danh sách items
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // Lấy số lượng items
    public int getItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    // Lấy tổng tiền
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Lấy ID tiếp theo
    public int getNextId() {
        return nextId++;
    }

    // Kiểm tra giỏ hàng trống
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}

