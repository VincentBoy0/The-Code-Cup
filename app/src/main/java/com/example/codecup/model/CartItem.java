package com.example.codecup.model;

/**
 * Model class đại diện cho một item trong giỏ hàng
 */
public class CartItem {

    private int id;               // ID duy nhất của cart item
    private int coffeeId;         // ID của sản phẩm cà phê
    private String name;          // Tên cà phê
    private String size;          // Size (S, M, L)
    private String cupType;       // Loại ly (regular, plastic)
    private String iceLevel;      // Mức đá (none, less, more)
    private int quantity;         // Số lượng
    private double unitPrice;     // Giá mỗi đơn vị (đã tính size)
    private int imageResId;       // ID hình ảnh

    public CartItem(int id, int coffeeId, String name, String size, String cupType,
                    String iceLevel, int quantity, double unitPrice, int imageResId) {
        this.id = id;
        this.coffeeId = coffeeId;
        this.name = name;
        this.size = size;
        this.cupType = cupType;
        this.iceLevel = iceLevel;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.imageResId = imageResId;
    }

    // Getters
    public int getId() { return id; }
    public int getCoffeeId() { return coffeeId; }
    public String getName() { return name; }
    public String getSize() { return size; }
    public String getCupType() { return cupType; }
    public String getIceLevel() { return iceLevel; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public int getImageResId() { return imageResId; }

    // Setters
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Tính tổng giá
    public double getTotalPrice() {
        return unitPrice * quantity;
    }

    // Lấy text hiển thị chi tiết
    public String getDetailsText() {
        String cup;
        String ice;
        // Map values for drinks
        if (cupType.equals("regular") || cupType.equals("plastic")) {
            cup = cupType.equals("regular") ? "Regular Cup" : "Plastic Cup";
        } else if (cupType.equals("with_jam") || cupType.equals("no_jam")) {
            cup = cupType.equals("with_jam") ? "With Jam" : "No Jam";
        } else {
            cup = cupType; // fallback
        }

        if (iceLevel.equals("none") || iceLevel.equals("less") || iceLevel.equals("more")) {
            ice = iceLevel.equals("none") ? "No Ice" : (iceLevel.equals("less") ? "Less Ice" : "More Ice");
        } else if (iceLevel.equals("spicy") || iceLevel.equals("mild") || iceLevel.equals("non_spicy")) {
            if (iceLevel.equals("spicy")) ice = "Spicy";
            else if (iceLevel.equals("mild")) ice = "Mild";
            else ice = "Non-Spicy";
        } else {
            ice = iceLevel; // fallback
        }

        return String.format("Size %s | %s | %s", size, cup, ice);
    }
}
