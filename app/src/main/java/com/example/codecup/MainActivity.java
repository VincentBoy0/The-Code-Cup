package com.example.codecup;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;
import android.widget.Toast;
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

    private static final String TAG = "MainActivity";

    // ===== Khai báo các View =====
    private RecyclerView rvCoffeeList;
    private BottomNavigationView bottomNavigation;
    private ImageView ivCart;
    private TextView tvCartBadge;
    private TextView tvCustomerName;

    // Category chips
    private com.google.android.material.chip.ChipGroup chipGroupCategories;
    private com.google.android.material.chip.Chip chipAll, chipCoffee, chipTea, chipBreakfast, chipCake, chipDrinks;

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

        // If user is not logged in, redirect to LoginActivity
        android.content.SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        if (!isLoggedIn) {
            startActivity(new android.content.Intent(this, com.example.codecup.LoginActivity.class));
            finish();
            return;
        }

        // If logged in, load per-user data (so rewards/orders/cart are user-specific after restart)
        String loggedUser = prefs.getString("logged_in_user", null);
        if (loggedUser != null && !loggedUser.isEmpty()) {
            com.example.codecup.manager.RewardsManager.getInstance().loadDataForUser(this, loggedUser);
            com.example.codecup.manager.OrderManager.getInstance().loadDataForUser(this, loggedUser);
            com.example.codecup.manager.CartManager.getInstance().loadDataForUser(this, loggedUser);
        }

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

        // Category chips
        chipGroupCategories = findViewById(R.id.chip_group_categories);
        chipAll = findViewById(R.id.chip_all);
        chipCoffee = findViewById(R.id.chip_coffee);
        chipTea = findViewById(R.id.chip_tea);
        chipBreakfast = findViewById(R.id.chip_breakfast);
        chipCake = findViewById(R.id.chip_cake);
        chipDrinks = findViewById(R.id.chip_drinks);

        // Listen for chip selection changes
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String category = "All";
            if (checkedIds != null && !checkedIds.isEmpty()) {
                int id = checkedIds.get(0);
                if (id == R.id.chip_all) category = "All";
                else if (id == R.id.chip_coffee) category = "Coffee";
                else if (id == R.id.chip_tea) category = "Tea";
                else if (id == R.id.chip_breakfast) category = "Breakfast";
                else if (id == R.id.chip_cake) category = "Cake";
                else if (id == R.id.chip_drinks) category = "Drinks";
            }
            filterByCategory(category);
        });
    }

    /**
     * Lọc danh sách theo category và cập nhật adapter
     */
    private void filterByCategory(String category) {
        if (coffeeList == null) return;

        if (category == null || category.equalsIgnoreCase("All")) {
            // show all
            coffeeAdapter = new CoffeeAdapter(this, coffeeList, this);
            rvCoffeeList.setAdapter(coffeeAdapter);
            coffeeAdapter.notifyDataSetChanged();
            return;
        }

        List<Coffee> filtered = new ArrayList<>();
        for (Coffee c : coffeeList) {
            if (c.getCategory() != null && c.getCategory().equalsIgnoreCase(category)) {
                filtered.add(c);
            }
        }

        coffeeAdapter = new CoffeeAdapter(this, filtered, this);
        rvCoffeeList.setAdapter(coffeeAdapter);
        coffeeAdapter.notifyDataSetChanged();
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

        // Ensure adapter refreshes view
        coffeeAdapter.notifyDataSetChanged();

        // Diagnostic: log how many items are present
        Log.d(TAG, "Coffee items loaded: " + (coffeeList != null ? coffeeList.size() : 0));
        Toast.makeText(this, "Coffee items: " + (coffeeList != null ? coffeeList.size() : 0), Toast.LENGTH_SHORT).show();

        // Ensure initial category selection shows all
        filterByCategory("All");
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
                3.50, R.drawable.ic_coffee_cup, "Coffee"));

        list.add(new Coffee(2, "Americano",
                "Espresso diluted with hot water. Smooth and full-bodied taste.",
                4.00, R.drawable.ic_coffee_cup, "Coffee"));

        list.add(new Coffee(3, "Cappuccino",
                "Perfect balance of espresso, steamed milk, and milk foam.",
                4.50, R.drawable.ic_coffee_cup, "Coffee"));

        list.add(new Coffee(4, "Latte",
                "Creamy espresso with steamed milk and light foam on top.",
                4.50, R.drawable.ic_coffee_cup, "Coffee"));

        list.add(new Coffee(5, "Mocha",
                "Espresso with chocolate syrup, steamed milk and whipped cream.",
                5.00, R.drawable.ic_coffee_cup, "Coffee"));

        list.add(new Coffee(6, "Caramel Macchiato",
                "Vanilla-flavored latte with caramel drizzle on top.",
                5.50, R.drawable.ic_coffee_cup, "Coffee"));

        list.add(new Coffee(7, "Cold Brew",
                "Slow-steeped coffee for 12+ hours. Smooth and less acidic.",
                4.50, R.drawable.ic_coffee_cup, "Coffee"));

        list.add(new Coffee(8, "Vietnamese Coffee",
                "Strong drip coffee with sweetened condensed milk. Rich and sweet.",
                5.00, R.drawable.ic_coffee_cup, "Coffee"));

        // Additional home items (same style as existing ones)
        list.add(new Coffee(9, "Croissant",
                "Buttery, flaky croissant — perfect with your coffee.",
                2.75, R.drawable.ic_coffee_cup, "Breakfast"));

        list.add(new Coffee(10, "Blueberry Muffin",
                "Moist muffin loaded with fresh blueberries.",
                2.50, R.drawable.ic_coffee_cup, "Cake"));

        list.add(new Coffee(11, "Avocado Toast",
                "Smashed avocado on sourdough with a sprinkle of chili flakes.",
                5.25, R.drawable.ic_coffee_cup, "Breakfast"));

        list.add(new Coffee(12, "Ham & Cheese Sandwich",
                "Toasted sandwich with savory ham and melted cheese.",
                6.00, R.drawable.ic_coffee_cup, "Breakfast"));

        list.add(new Coffee(13, "Pancake Stack",
                "Fluffy pancakes served with maple syrup and butter.",
                7.00, R.drawable.ic_coffee_cup, "Breakfast"));

        list.add(new Coffee(14, "Fruit Smoothie",
                "Mixed seasonal fruit blended with yogurt for a refreshing boost.",
                4.75, R.drawable.ic_coffee_cup, "Drinks"));

        // Add tea items so Tea category has content
        list.add(new Coffee(15, "Green Tea",
                "Light and refreshing green tea served hot.",
                2.00, R.drawable.ic_coffee_cup, "Tea"));

        list.add(new Coffee(16, "Earl Grey",
                "Classic black tea with bergamot aroma.",
                2.25, R.drawable.ic_coffee_cup, "Tea"));

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
                    // Show home content (loyalty + coffee list)
                    // ensure coffee list and loyalty visible
                    findViewById(R.id.loyalty_card).setVisibility(View.VISIBLE);
                    findViewById(R.id.rv_coffee_list).setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.nav_menu) {
                    // No separate menu: behave same as Home
                    findViewById(R.id.loyalty_card).setVisibility(View.VISIBLE);
                    findViewById(R.id.rv_coffee_list).setVisibility(View.VISIBLE);
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
        // Pass category so detail screen can adjust customization UI
        intent.putExtra("coffee_category", coffee.getCategory());

        // Khởi chạy Activity mới
        startActivity(intent);
    }
}