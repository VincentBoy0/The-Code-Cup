package com.example.codecutcodeshit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecutcodeshit.adapter.CoffeeAdapter;
import com.example.codecutcodeshit.model.Coffee;
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
 * 1. Header: Logo + Tên app + Icon thông báo
 * 2. Loyalty Card: Thẻ khách hàng thân thiết
 * 3. Coffee List: Danh sách sản phẩm (RecyclerView)
 * 4. Bottom Navigation: Thanh điều hướng dưới cùng
 */
public class MainActivity extends AppCompatActivity implements CoffeeAdapter.OnCoffeeClickListener {

    // ===== Khai báo các View =====
    // Các biến này sẽ được liên kết với View trong layout XML
    private RecyclerView rvCoffeeList;
    private BottomNavigationView bottomNavigation;
    private ImageView ivNotification;
    private TextView tvCustomerName;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
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
        ivNotification = findViewById(R.id.iv_notification);
        tvCustomerName = findViewById(R.id.tv_customer_name);
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
                } else if (itemId == R.id.nav_menu) {
                    Toast.makeText(MainActivity.this, "Menu - Coming Soon!", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_orders) {
                    Toast.makeText(MainActivity.this, "Orders - Coming Soon!", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Toast.makeText(MainActivity.this, "Profile - Coming Soon!", Toast.LENGTH_SHORT).show();
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
        // Click vào icon thông báo
        ivNotification.setOnClickListener(v -> {
            Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show();
        });
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