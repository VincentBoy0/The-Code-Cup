package com.example.codecup;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecup.adapter.CoffeeAdapter;
import com.example.codecup.manager.CartManager;
import com.example.codecup.manager.RewardsManager;
import com.example.codecup.model.Coffee;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * MAIN ACTIVITY - Màn hình chính của ứng dụng The Code Cup
 *
 * Activity là gì?
 * - Activity đại diện cho MỘT màn hình trong ứng dụng Android
 * - Mỗi Activity có một file layout XML tương ứng (activity_main.xml)
 * - Activity xử lý logic và tương tác người dùng
 *
 * Cấu trúc màn hình:
 * 1. Header: Logo + Tên app + Icon giỏ hàng
 * 2. Loyalty Card: Thẻ khách hàng thân thiết
 * 3. Coffee List: Danh sách sản phẩm (RecyclerView)
 * 4. Bottom Navigation: Thanh điều hướng dưới cùng
 */
public class MainActivity extends AppCompatActivity implements CoffeeAdapter.OnCoffeeClickListener {

    // ===== Khai báo các View =====
    private RecyclerView rvCoffeeList;
    private BottomNavigationView bottomNavigation;
    private ImageView ivCart;
    private TextView tvCartBadge;
    private TextView tvCustomerName;

    // Loyalty Card Views
    private ImageView[] homeStamps = new ImageView[8];
    private TextView tvStampCount;
    private TextView tvLoyaltyMessage;

    // ===== Dữ liệu =====
    private List<Coffee> coffeeList;
    private CoffeeAdapter coffeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bật Edge-to-Edge (hiển thị full màn hình)
        EdgeToEdge.enable(this);

        // Gắn layout XML vào Activity
        setContentView(R.layout.activity_main);

        // Xử lý padding cho thanh trạng thái và thanh điều hướng hệ thống
        // Chỉ áp dụng padding cho top (status bar), không padding bottom
        // vì BottomNavigationView đã nằm sát đáy
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Khởi tạo các thành phần
        initViews();
        setupCoffeeList();
        setupBottomNavigation();
        setupListeners();
    }

    /**
     * Ánh xạ các View từ layout XML
     *
     * findViewById() là gì?
     * - Tìm View trong layout theo ID
     * - Trả về View để ta có thể thao tác (setText, setOnClickListener...)
     */
    private void initViews() {
        rvCoffeeList = findViewById(R.id.rv_coffee_list);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        ivCart = findViewById(R.id.iv_cart);
        tvCartBadge = findViewById(R.id.tv_cart_badge);
        tvCustomerName = findViewById(R.id.tv_customer_name);

        // Loyalty Card Views
        tvStampCount = findViewById(R.id.tv_stamp_count);
        tvLoyaltyMessage = findViewById(R.id.tv_loyalty_message);
        homeStamps[0] = findViewById(R.id.home_stamp_1);
        homeStamps[1] = findViewById(R.id.home_stamp_2);
        homeStamps[2] = findViewById(R.id.home_stamp_3);
        homeStamps[3] = findViewById(R.id.home_stamp_4);
        homeStamps[4] = findViewById(R.id.home_stamp_5);
        homeStamps[5] = findViewById(R.id.home_stamp_6);
        homeStamps[6] = findViewById(R.id.home_stamp_7);
        homeStamps[7] = findViewById(R.id.home_stamp_8);
    }

    /**
     * Thiết lập danh sách cà phê và RecyclerView
     */
    private void setupCoffeeList() {
        // Tạo danh sách sản phẩm (dữ liệu mẫu)
        coffeeList = createSampleCoffeeList();

        // Tạo Adapter - cầu nối giữa dữ liệu và RecyclerView
        // 'this' ở đây là OnCoffeeClickListener vì MainActivity implement interface đó
        coffeeAdapter = new CoffeeAdapter(this, coffeeList, this);

        // Thiết lập LayoutManager - quyết định cách sắp xếp các item
        // LinearLayoutManager: sắp xếp theo danh sách dọc
        rvCoffeeList.setLayoutManager(new LinearLayoutManager(this));

        // Gắn Adapter vào RecyclerView
        rvCoffeeList.setAdapter(coffeeAdapter);
    }

    /**
     * Tạo dữ liệu mẫu cho danh sách cà phê
     *
     * @return List<Coffee> danh sách sản phẩm
     */
    private List<Coffee> createSampleCoffeeList() {
        List<Coffee> list = new ArrayList<>();

        // Thêm các sản phẩm vào danh sách
        // Coffee(id, name, description, price, imageResId)
        list.add(new Coffee(1, "Espresso",
                "Strong and bold single shot of coffee. The foundation of all coffee drinks.",
                3.50, R.drawable.ic_coffee_cup));

        list.add(new Coffee(2, "Americano",
                "Espresso diluted with hot water. Smooth and full-bodied taste.",
                4.00, R.drawable.ic_coffee_cup));

        list.add(new Coffee(3, "Cappuccino",
                "Perfect balance of espresso, steamed milk, and milk foam.",
                4.50, R.drawable.ic_coffee_cup));

        list.add(new Coffee(4, "Latte",
                "Creamy espresso with steamed milk and light foam on top.",
                4.50, R.drawable.ic_coffee_cup));

        list.add(new Coffee(5, "Mocha",
                "Espresso with chocolate syrup, steamed milk and whipped cream.",
                5.00, R.drawable.ic_coffee_cup));

        list.add(new Coffee(6, "Caramel Macchiato",
                "Vanilla-flavored latte with caramel drizzle on top.",
                5.50, R.drawable.ic_coffee_cup));

        list.add(new Coffee(7, "Cold Brew",
                "Slow-steeped coffee for 12+ hours. Smooth and less acidic.",
                4.50, R.drawable.ic_coffee_cup));

        list.add(new Coffee(8, "Vietnamese Coffee",
                "Strong drip coffee with sweetened condensed milk. Rich and sweet.",
                5.00, R.drawable.ic_coffee_cup));

        return list;
    }

    /**
     * Thiết lập Bottom Navigation
     */
    private void setupBottomNavigation() {
        // Đặt Home là tab mặc định
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        // Xử lý sự kiện khi chọn tab
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Đang ở Home rồi, không cần làm gì
                    return true;
                } else if (itemId == R.id.nav_orders) {
                    // Chuyển đến màn hình My Orders
                    startActivity(new Intent(MainActivity.this, MyOrdersActivity.class));
                    return true;
                } else if (itemId == R.id.nav_rewards) {
                    // Chuyển đến màn hình Rewards
                    startActivity(new Intent(MainActivity.this, RewardsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Chuyển đến màn hình Profile
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    return true;
                }

                return false;
            }
        });
    }

    /**
     * Thiết lập các sự kiện click khác
     */
    private void setupListeners() {
        // Click vào icon giỏ hàng - chuyển đến CartActivity
        ivCart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });
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
     * Cập nhật Loyalty Card từ RewardsManager
     * Đồng bộ với Rewards Screen
     */
    private void updateLoyaltyCard() {
        RewardsManager rewardsManager = RewardsManager.getInstance();
        int stampCount = rewardsManager.getStampCount();

        // Cập nhật stamp count text
        tvStampCount.setText(stampCount + "/" + RewardsManager.MAX_STAMPS);

        // Cập nhật stamps visual
        for (int i = 0; i < homeStamps.length; i++) {
            if (i < stampCount) {
                homeStamps[i].setImageResource(R.drawable.ic_stamp_filled);
            } else {
                homeStamps[i].setImageResource(R.drawable.ic_stamp_empty);
            }
        }

        // Cập nhật message
        int remaining = rewardsManager.getStampsRemaining();
        if (remaining == 0) {
            tvLoyaltyMessage.setText("🎉 Bonus points earned!");
        } else {
            tvLoyaltyMessage.setText(remaining + " more stamp" + (remaining > 1 ? "s" : "") + " for +"
                    + RewardsManager.LOYALTY_BONUS_POINTS + " bonus points!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật badge khi quay lại màn hình
        updateCartBadge();
        // Cập nhật loyalty card
        updateLoyaltyCard();
        // Đặt lại selected item cho bottom navigation
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    /**
     * Xử lý khi user click vào một sản phẩm cà phê
     * Method này được gọi từ CoffeeAdapter thông qua interface
     *
     * @param coffee Sản phẩm được click
     */
    @Override
    public void onCoffeeClick(Coffee coffee) {
        // Tạo Intent để chuyển sang CoffeeDetailActivity
        // Intent là "tin nhắn" để giao tiếp giữa các Activity
        Intent intent = new Intent(this, CoffeeDetailActivity.class);

        // Truyền dữ liệu qua Intent bằng putExtra()
        intent.putExtra("coffee_id", coffee.getId());
        intent.putExtra("coffee_name", coffee.getName());
        intent.putExtra("coffee_description", coffee.getDescription());
        intent.putExtra("coffee_price", coffee.getPrice());
        intent.putExtra("coffee_image", coffee.getImageResId());

        // Khởi chạy Activity mới
        startActivity(intent);
    }
}