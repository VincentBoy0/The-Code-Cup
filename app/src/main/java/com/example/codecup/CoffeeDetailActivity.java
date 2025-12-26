package com.example.codecup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecup.adapter.CartAdapter;
import com.example.codecup.manager.CartManager;
import com.example.codecup.model.CartItem;

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
    private ImageView ivBack, ivCoffeeDetail, ivCart;
    private TextView tvDetailName, tvDetailPrice, tvDetailDescription;
    private TextView tvQuantity, tvCartBadge;
    private Button btnSizeS, btnSizeM, btnSizeL;
    private ImageButton btnDecrease, btnIncrease;
    private Button btnAddToCart;

    // Buttons cho tùy chỉnh sản phẩm
    private Button btnCupRegular, btnCupPlastic;
    private Button btnIceNone, btnIceLess, btnIceMore;

    // ===== Biến lưu trữ dữ liệu =====
    private int coffeeId;
    private String coffeeName;
    private String coffeeDescription;
    private double coffeePrice;
    private int coffeeImageResId;

    private int quantity = 1;           // Số lượng mặc định
    private String selectedSize = "S";  // Size mặc định
    private double sizeMultiplier = 1.0; // Hệ số giá theo size

    // Tùy chỉnh sản phẩm
    private String selectedCup = "regular";  // Loại ly: regular, plastic
    private String selectedIce = "less";     // Mức đá: none, less, more

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
     * Cập nhật cart badge mỗi khi quay lại màn hình này
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
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
        ivCart = findViewById(R.id.iv_cart);
        tvCartBadge = findViewById(R.id.tv_cart_badge);

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

        // Tùy chỉnh sản phẩm
        btnCupRegular = findViewById(R.id.btn_cup_regular);
        btnCupPlastic = findViewById(R.id.btn_cup_plastic);
        btnIceNone = findViewById(R.id.btn_ice_none);
        btnIceLess = findViewById(R.id.btn_ice_less);
        btnIceMore = findViewById(R.id.btn_ice_more);
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

        // Cập nhật badge giỏ hàng
        updateCartBadge();
    }

    /**
     * Cập nhật badge số lượng trên icon giỏ hàng
     */
    private void updateCartBadge() {
        int itemCount = CartManager.getInstance().getItemCount();
        if (itemCount > 0) {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(itemCount > 99 ? "99+" : itemCount));
        } else {
            tvCartBadge.setVisibility(View.GONE);
        }
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
            // Tạo CartItem
            double unitPrice = coffeePrice * sizeMultiplier;
            CartItem cartItem = new CartItem(
                    CartManager.getInstance().getNextId(),
                    coffeeId,
                    coffeeName,
                    selectedSize,
                    selectedCup,
                    selectedIce,
                    quantity,
                    unitPrice,
                    coffeeImageResId
            );

            // Thêm vào giỏ hàng
            CartManager.getInstance().addItem(cartItem);

            // Cập nhật badge
            updateCartBadge();

            // Hiển thị thông báo
            String cupType = selectedCup.equals("regular") ? "Regular Cup" : "Plastic Cup";
            String iceLevel = selectedIce.equals("none") ? "No Ice" :
                    (selectedIce.equals("less") ? "Less Ice" : "More Ice");

            String message = String.format(Locale.US,
                    "Added %d x %s\nSize: %s | %s | %s",
                    quantity, coffeeName, selectedSize, cupType, iceLevel);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            // Chuyển đến màn hình My Cart
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        // ===== Icon giỏ hàng - Hiển thị Cart Preview =====
        ivCart.setOnClickListener(v -> showCartPreviewDialog());

        // ===== Các nút chọn loại ly =====
        btnCupRegular.setOnClickListener(v -> selectCupType("regular"));
        btnCupPlastic.setOnClickListener(v -> selectCupType("plastic"));

        // ===== Các nút chọn mức đá =====
        btnIceNone.setOnClickListener(v -> selectIceLevel("none"));
        btnIceLess.setOnClickListener(v -> selectIceLevel("less"));
        btnIceMore.setOnClickListener(v -> selectIceLevel("more"));
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

    /**
     * Xử lý khi user chọn loại ly
     *
     * @param cupType Loại ly: "regular" hoặc "plastic"
     */
    private void selectCupType(String cupType) {
        selectedCup = cupType;

        // Cập nhật UI - đổi màu nút được chọn
        btnCupRegular.setBackgroundTintList(getResources().getColorStateList(
                cupType.equals("regular") ? R.color.coffee_primary : R.color.text_secondary, null));
        btnCupPlastic.setBackgroundTintList(getResources().getColorStateList(
                cupType.equals("plastic") ? R.color.coffee_primary : R.color.text_secondary, null));
    }

    /**
     * Xử lý khi user chọn mức đá
     *
     * @param iceLevel Mức đá: "none", "less", hoặc "more"
     */
    private void selectIceLevel(String iceLevel) {
        selectedIce = iceLevel;

        // Cập nhật UI - đổi màu nút được chọn
        btnIceNone.setBackgroundTintList(getResources().getColorStateList(
                iceLevel.equals("none") ? R.color.coffee_primary : R.color.text_secondary, null));
        btnIceLess.setBackgroundTintList(getResources().getColorStateList(
                iceLevel.equals("less") ? R.color.coffee_primary : R.color.text_secondary, null));
        btnIceMore.setBackgroundTintList(getResources().getColorStateList(
                iceLevel.equals("more") ? R.color.coffee_primary : R.color.text_secondary, null));
    }

    /**
     * Hiển thị Dialog xem trước giỏ hàng
     */
    private void showCartPreviewDialog() {
        // Tạo AlertDialog với custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cart_preview, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Ánh xạ các View trong dialog
        RecyclerView rvCartItems = dialogView.findViewById(R.id.rv_cart_items);
        LinearLayout layoutEmptyCart = dialogView.findViewById(R.id.layout_empty_cart);
        LinearLayout layoutTotal = dialogView.findViewById(R.id.layout_total);
        View dividerTotal = dialogView.findViewById(R.id.divider_total);
        TextView tvCartItemCount = dialogView.findViewById(R.id.tv_cart_item_count);
        TextView tvCartTotal = dialogView.findViewById(R.id.tv_cart_total);
        Button btnGoToCart = dialogView.findViewById(R.id.btn_go_to_cart);
        ImageView ivCloseDialog = dialogView.findViewById(R.id.iv_close_dialog);

        // Xử lý nút đóng dialog
        ivCloseDialog.setOnClickListener(v -> dialog.dismiss());

        // Lấy dữ liệu giỏ hàng
        CartManager cartManager = CartManager.getInstance();

        if (cartManager.isEmpty()) {
            // Giỏ hàng trống
            rvCartItems.setVisibility(View.GONE);
            layoutEmptyCart.setVisibility(View.VISIBLE);
            layoutTotal.setVisibility(View.GONE);
            dividerTotal.setVisibility(View.GONE);
            btnGoToCart.setVisibility(View.GONE);
            tvCartItemCount.setText("0 items");
        } else {
            // Có items trong giỏ
            rvCartItems.setVisibility(View.VISIBLE);
            layoutEmptyCart.setVisibility(View.GONE);
            layoutTotal.setVisibility(View.VISIBLE);
            dividerTotal.setVisibility(View.VISIBLE);
            btnGoToCart.setVisibility(View.VISIBLE);

            // Setup RecyclerView
            rvCartItems.setLayoutManager(new LinearLayoutManager(this));
            CartAdapter cartAdapter = new CartAdapter(this, cartManager.getCartItems(),
                    item -> {
                        // Xử lý xóa item
                        cartManager.removeItem(item.getId());
                        updateCartBadge();

                        // Refresh dialog
                        dialog.dismiss();
                        showCartPreviewDialog();
                    });
            rvCartItems.setAdapter(cartAdapter);

            // Cập nhật thông tin
            int itemCount = cartManager.getItemCount();
            tvCartItemCount.setText(itemCount + (itemCount == 1 ? " item" : " items"));
            tvCartTotal.setText(String.format(Locale.US, "$%.2f", cartManager.getTotalPrice()));
        }

        // Xử lý nút Go to My Cart - chuyển đến CartActivity
        btnGoToCart.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, CartActivity.class));
        });

        dialog.show();
    }
}

