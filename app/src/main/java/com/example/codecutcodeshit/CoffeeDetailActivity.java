package com.example.codecutcodeshit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * ACTIVITY CHI TIẾT SẢN PHẨM CÀ PHÊ
 *
 * Activity này hiển thị khi user click vào một sản phẩm trong danh sách
 * Cho phép xem chi tiết, chọn size, số lượng và thêm vào giỏ hàng
 *
 * INTENT là gì?
 * - Intent là "tin nhắn" để giao tiếp giữa các Activity
 * - Có thể truyền dữ liệu qua Intent bằng putExtra() và getExtra()
 */
public class CoffeeDetailActivity extends AppCompatActivity {

    // ===== Khai báo các View =====
    private ImageView ivBack, ivCoffeeDetail;
    private TextView tvDetailName, tvDetailPrice, tvDetailDescription;
    private TextView tvQuantity;
    private Button btnSizeS, btnSizeM, btnSizeL;
    private ImageButton btnDecrease, btnIncrease;
    private Button btnAddToCart;

    // ===== Biến lưu trữ dữ liệu =====
    private int coffeeId;
    private String coffeeName;
    private String coffeeDescription;
    private double coffeePrice;
    private int coffeeImageResId;

    private int quantity = 1;           // Số lượng mặc định
    private String selectedSize = "S";  // Size mặc định
    private double sizeMultiplier = 1.0; // Hệ số giá theo size

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_detail);

        // Lấy dữ liệu từ Intent
        getIntentData();

        // Ánh xạ View
        initViews();

        // Hiển thị dữ liệu
        displayCoffeeDetails();

        // Xử lý sự kiện
        setupListeners();
    }

    /**
     * Lấy dữ liệu được truyền từ MainActivity thông qua Intent
     */
    private void getIntentData() {
        // getIntent() lấy Intent đã gửi Activity này
        // getXXXExtra() lấy dữ liệu từ Intent
        coffeeId = getIntent().getIntExtra("coffee_id", 0);
        coffeeName = getIntent().getStringExtra("coffee_name");
        coffeeDescription = getIntent().getStringExtra("coffee_description");
        coffeePrice = getIntent().getDoubleExtra("coffee_price", 0.0);
        coffeeImageResId = getIntent().getIntExtra("coffee_image", R.drawable.ic_coffee_cup);

        // Nếu không có mô tả, dùng mô tả mặc định
        if (coffeeDescription == null || coffeeDescription.isEmpty()) {
            coffeeDescription = "A delicious coffee crafted with love at The Code Cup. " +
                    "Made from premium beans, perfectly roasted to bring out the rich flavors.";
        }
    }

    /**
     * Ánh xạ các View từ layout XML
     */
    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivCoffeeDetail = findViewById(R.id.iv_coffee_detail);

        tvDetailName = findViewById(R.id.tv_detail_name);
        tvDetailPrice = findViewById(R.id.tv_detail_price);
        tvDetailDescription = findViewById(R.id.tv_detail_description);
        tvQuantity = findViewById(R.id.tv_quantity);

        btnSizeS = findViewById(R.id.btn_size_s);
        btnSizeM = findViewById(R.id.btn_size_m);
        btnSizeL = findViewById(R.id.btn_size_l);

        btnDecrease = findViewById(R.id.btn_decrease);
        btnIncrease = findViewById(R.id.btn_increase);

        btnAddToCart = findViewById(R.id.btn_add_to_cart);
    }

    /**
     * Hiển thị thông tin cà phê lên giao diện
     */
    private void displayCoffeeDetails() {
        tvDetailName.setText(coffeeName);
        tvDetailDescription.setText(coffeeDescription);
        ivCoffeeDetail.setImageResource(coffeeImageResId);

        // Cập nhật giá
        updatePrice();
    }

    /**
     * Cập nhật giá hiển thị dựa trên size và số lượng
     */
    private void updatePrice() {
        double totalPrice = coffeePrice * sizeMultiplier * quantity;
        tvDetailPrice.setText(String.format(Locale.US, "$%.2f", coffeePrice * sizeMultiplier));
        btnAddToCart.setText(String.format(Locale.US, "Add to Cart - $%.2f", totalPrice));
    }

    /**
     * Thiết lập các sự kiện click
     */
    private void setupListeners() {
        // ===== Nút quay lại =====
        ivBack.setOnClickListener(v -> {
            // finish() đóng Activity hiện tại và quay về Activity trước
            finish();
        });

        // ===== Các nút chọn size =====
        btnSizeS.setOnClickListener(v -> selectSize("S", 1.0));
        btnSizeM.setOnClickListener(v -> selectSize("M", 1.3));
        btnSizeL.setOnClickListener(v -> selectSize("L", 1.5));

        // ===== Nút giảm số lượng =====
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updatePrice();
            }
        });

        // ===== Nút tăng số lượng =====
        btnIncrease.setOnClickListener(v -> {
            if (quantity < 10) { // Giới hạn tối đa 10
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
                updatePrice();
            }
        });

        // ===== Nút thêm vào giỏ hàng =====
        btnAddToCart.setOnClickListener(v -> {
            // Tạo thông báo Toast
            String message = String.format(Locale.US, "Added %d x %s (Size %s) to cart!",
                    quantity, coffeeName, selectedSize);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            // TODO: Thực sự thêm vào giỏ hàng (sẽ làm sau)
        });
    }

    /**
     * Xử lý khi user chọn size
     *
     * @param size Size được chọn (S, M, L)
     * @param multiplier Hệ số nhân giá (1.0, 1.3, 1.5)
     */
    private void selectSize(String size, double multiplier) {
        selectedSize = size;
        sizeMultiplier = multiplier;

        // Cập nhật UI - đổi màu nút được chọn
        // Màu được chọn: coffee_primary
        // Màu không được chọn: text_secondary
        btnSizeS.setBackgroundTintList(getResources().getColorStateList(
                size.equals("S") ? R.color.coffee_primary : R.color.text_secondary, null));
        btnSizeM.setBackgroundTintList(getResources().getColorStateList(
                size.equals("M") ? R.color.coffee_primary : R.color.text_secondary, null));
        btnSizeL.setBackgroundTintList(getResources().getColorStateList(
                size.equals("L") ? R.color.coffee_primary : R.color.text_secondary, null));

        // Cập nhật giá
        updatePrice();
    }
}

