package com.example.codecup.model;

/**
 * Model class đại diện cho một sản phẩm cà phê
 *
 * MODEL là gì?
 * - Model là class chứa dữ liệu của ứng dụng
 * - Nó giống như "bản thiết kế" mô tả một đối tượng có những thuộc tính gì
 * - Ví dụ: Coffee có id, tên, mô tả, giá, hình ảnh
 */
public class Coffee {

    // Các thuộc tính (fields) của cà phê
    private int id;           // ID duy nhất để phân biệt các sản phẩm
    private String name;       // Tên cà phê (vd: "Espresso")
    private String description;// Mô tả ngắn
    private double price;      // Giá tiền
    private int imageResId;    // ID của hình ảnh trong thư mục drawable

    /**
     * Constructor - Hàm khởi tạo để tạo đối tượng Coffee mới
     */
    public Coffee(int id, String name, String description, double price, int imageResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    // ===== GETTER methods =====
    // Getter là các method để LẤY giá trị của thuộc tính
    // Tại sao cần getter? Vì các thuộc tính là private (bảo mật)

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    // ===== SETTER methods =====
    // Setter là các method để THAY ĐỔI giá trị của thuộc tính

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}

