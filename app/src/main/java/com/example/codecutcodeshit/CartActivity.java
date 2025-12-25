package com.example.codecutcodeshit;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecutcodeshit.adapter.CartFullAdapter;
import com.example.codecutcodeshit.manager.CartManager;
import com.example.codecutcodeshit.manager.OrderManager;
import com.example.codecutcodeshit.model.CartItem;

import java.util.Locale;

/**
 * ACTIVITY MÀN HÌNH GIỎ HÀNG (My Cart)
 *
 * Hiển thị danh sách sản phẩm trong giỏ hàng
 * Cho phép:
 * - Điều chỉnh số lượng (+/-)
 * - Vuốt trái để xóa item
 * - Checkout để đặt hàng
 */
public class CartActivity extends AppCompatActivity implements CartFullAdapter.OnCartItemListener {

    // Views
    private RecyclerView rvCartItems;
    private LinearLayout layoutEmptyCart, layoutBottom;
    private TextView tvClearAll, tvSwipeHint;
    private TextView tvSubtotal, tvTax, tvTotal;
    private Button btnCheckout, btnBrowseMenu;
    private ImageView ivBack;

    // Adapter
    private CartFullAdapter cartAdapter;

    // Cart Manager
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartManager = CartManager.getInstance();

        initViews();
        setupRecyclerView();
        setupListeners();
        updateUI();
    }

    /**
     * Refresh dữ liệu mỗi khi quay lại màn hình
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại adapter với dữ liệu mới nhất
        if (cartAdapter != null) {
            cartAdapter.updateItems(cartManager.getCartItems());
        }
        updateUI();
    }

    /**
     * Ánh xạ các View
     */
    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        rvCartItems = findViewById(R.id.rv_cart_items);
        layoutEmptyCart = findViewById(R.id.layout_empty_cart);
        layoutBottom = findViewById(R.id.layout_bottom);
        tvClearAll = findViewById(R.id.tv_clear_all);
        tvSwipeHint = findViewById(R.id.tv_swipe_hint);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnBrowseMenu = findViewById(R.id.btn_browse_menu);
    }

    /**
     * Thiết lập RecyclerView với Swipe to Delete
     */
    private void setupRecyclerView() {
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartFullAdapter(this, cartManager.getCartItems(), this);
        rvCartItems.setAdapter(cartAdapter);

        // Thiết lập Swipe to Delete
        setupSwipeToDelete();
    }

    /**
     * Thiết lập gesture vuốt trái để xóa item
     */
    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {

            // Lấy màu từ colors.xml để dễ customize
            private final ColorDrawable background = new ColorDrawable(
                    ContextCompat.getColor(CartActivity.this, R.color.swipe_delete_background));
            private final Drawable deleteIcon = ContextCompat.getDrawable(CartActivity.this, R.drawable.ic_delete);

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false; // Không hỗ trợ kéo thả
            }

            /**
             * Định nghĩa ngưỡng vuốt đủ để xóa item
             *
             * @return 0.3f = vuốt 30% chiều rộng item là đủ
             *         Mặc định là 0.5f (50%)
             */
            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.3f; // Vuốt 30% là đủ để xóa
            }

            /**
             * Định nghĩa tốc độ vuốt tối thiểu
             *
             * @return Tốc độ vuốt tối thiểu (pixels/giây)
             *         Nếu vuốt nhanh hơn tốc độ này, dù chưa đủ 30% vẫn xóa
             */
            @Override
            public float getSwipeEscapeVelocity(float defaultValue) {
                return defaultValue * 0.5f; // Giảm 50% so với mặc định, dễ vuốt hơn
            }

            /**
             * Định nghĩa tốc độ vuốt tối đa để hủy
             *
             * @return Nếu vuốt chậm hơn tốc độ này và chưa đủ 30%, item sẽ quay lại
             */
            @Override
            public float getSwipeVelocityThreshold(float defaultValue) {
                return defaultValue * 0.8f;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                CartItem item = cartAdapter.getItem(position);

                if (item != null) {
                    // Xóa item khỏi CartManager
                    cartManager.removeItem(item.getId());
                    // Cập nhật adapter
                    cartAdapter.updateItems(cartManager.getCartItems());
                    // Cập nhật UI
                    updateUI();

                    Toast.makeText(CartActivity.this,
                            item.getName() + " removed from cart",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;

                // Vẽ background đỏ
                if (dX < 0) { // Vuốt trái
                    background.setBounds(
                            itemView.getRight() + (int) dX,
                            itemView.getTop(),
                            itemView.getRight(),
                            itemView.getBottom()
                    );
                    background.draw(c);

                    // Vẽ icon delete
                    if (deleteIcon != null) {
                        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + iconMargin;
                        int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
                        int iconRight = itemView.getRight() - iconMargin;
                        int iconLeft = iconRight - deleteIcon.getIntrinsicWidth();

                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        deleteIcon.draw(c);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(rvCartItems);
    }

    /**
     * Thiết lập các sự kiện
     */
    private void setupListeners() {
        // Nút quay lại
        ivBack.setOnClickListener(v -> finish());

        // Nút xóa tất cả
        tvClearAll.setOnClickListener(v -> showClearCartDialog());

        // Nút Browse Menu (khi giỏ trống)
        btnBrowseMenu.setOnClickListener(v -> finish());

        // Nút Checkout
        btnCheckout.setOnClickListener(v -> {
            if (!cartManager.isEmpty()) {
                // Tính tổng tiền (bao gồm thuế)
                double subtotal = cartManager.getTotalPrice();
                double tax = subtotal * 0.10;
                double total = subtotal + tax;

                // Tạo Order mới và lưu vào OrderManager
                OrderManager.getInstance().createOrder(cartManager.getCartItems(), total);

                // Xóa giỏ hàng sau khi checkout
                cartManager.clearCart();

                // Chuyển đến màn hình Order Success
                Intent intent = new Intent(this, OrderSuccessActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Hiển thị dialog xác nhận xóa tất cả
     */
    private void showClearCartDialog() {
        if (cartManager.isEmpty()) return;

        new AlertDialog.Builder(this)
                .setTitle("Clear Cart")
                .setMessage("Are you sure you want to remove all items from your cart?")
                .setPositiveButton("Clear", (dialog, which) -> {
                    cartManager.clearCart();
                    cartAdapter.updateItems(cartManager.getCartItems());
                    updateUI();
                    Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Cập nhật giao diện
     */
    private void updateUI() {
        if (cartManager.isEmpty()) {
            // Giỏ hàng trống
            rvCartItems.setVisibility(View.GONE);
            layoutEmptyCart.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.GONE);
            tvSwipeHint.setVisibility(View.GONE);
            tvClearAll.setVisibility(View.GONE);
        } else {
            // Có items
            rvCartItems.setVisibility(View.VISIBLE);
            layoutEmptyCart.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.VISIBLE);
            tvSwipeHint.setVisibility(View.VISIBLE);
            tvClearAll.setVisibility(View.VISIBLE);

            // Cập nhật giá
            updatePrices();
        }
    }

    /**
     * Cập nhật hiển thị giá
     */
    private void updatePrices() {
        double subtotal = cartManager.getTotalPrice();
        double tax = subtotal * 0.10; // 10% tax
        double total = subtotal + tax;

        tvSubtotal.setText(String.format(Locale.US, "$%.2f", subtotal));
        tvTax.setText(String.format(Locale.US, "$%.2f", tax));
        tvTotal.setText(String.format(Locale.US, "$%.2f", total));
    }

    // ===== Implement OnCartItemListener =====

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        // Cập nhật số lượng trong CartManager
        item.setQuantity(newQuantity);
        // Refresh adapter
        cartAdapter.notifyDataSetChanged();
        // Cập nhật giá
        updatePrices();
    }

    @Override
    public void onItemRemoved(CartItem item) {
        // Xóa khỏi CartManager
        cartManager.removeItem(item.getId());
        // Refresh adapter
        cartAdapter.updateItems(cartManager.getCartItems());
        // Cập nhật UI
        updateUI();

        Toast.makeText(this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
    }
}

