package com.example.codecup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecup.R;
import com.example.codecup.model.CartItem;

import java.util.List;
import java.util.Locale;

/**
 * ADAPTER CHO MÀN HÌNH GIỎ HÀNG (CartActivity)
 *
 * Hiển thị danh sách items với khả năng điều chỉnh số lượng
 */
public class CartFullAdapter extends RecyclerView.Adapter<CartFullAdapter.CartFullViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemListener listener;

    // Interface xử lý các sự kiện
    public interface OnCartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemRemoved(CartItem item);
    }

    public CartFullAdapter(Context context, List<CartItem> cartItems, OnCartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartFullViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_full, parent, false);
        return new CartFullViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartFullViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Hiển thị thông tin
        holder.tvName.setText(item.getName());
        holder.tvDetails.setText(item.getDetailsText());
        holder.tvUnitPrice.setText(String.format(Locale.US, "$%.2f each", item.getUnitPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvTotal.setText(String.format(Locale.US, "$%.2f", item.getTotalPrice()));
        holder.ivImage.setImageResource(item.getImageResId());

        // Xử lý nút giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            if (currentQty > 1) {
                // Giảm số lượng
                if (listener != null) {
                    listener.onQuantityChanged(item, currentQty - 1);
                }
            } else {
                // Xóa item khi số lượng = 0
                if (listener != null) {
                    listener.onItemRemoved(item);
                }
            }
        });

        // Xử lý nút tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            if (currentQty < 10) { // Giới hạn tối đa 10
                if (listener != null) {
                    listener.onQuantityChanged(item, currentQty + 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    // Cập nhật danh sách
    public void updateItems(List<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }

    // Xóa item tại vị trí (dùng cho swipe)
    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            CartItem removedItem = cartItems.get(position);
            cartItems.remove(position);
            notifyItemRemoved(position);

            if (listener != null) {
                listener.onItemRemoved(removedItem);
            }
        }
    }

    // Lấy item tại vị trí
    public CartItem getItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            return cartItems.get(position);
        }
        return null;
    }

    public static class CartFullViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvDetails, tvUnitPrice, tvQuantity, tvTotal;
        ImageButton btnDecrease, btnIncrease;

        public CartFullViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_cart_item_image);
            tvName = itemView.findViewById(R.id.tv_cart_item_name);
            tvDetails = itemView.findViewById(R.id.tv_cart_item_details);
            tvUnitPrice = itemView.findViewById(R.id.tv_cart_item_unit_price);
            tvQuantity = itemView.findViewById(R.id.tv_cart_item_quantity);
            tvTotal = itemView.findViewById(R.id.tv_cart_item_total);
            btnDecrease = itemView.findViewById(R.id.btn_item_decrease);
            btnIncrease = itemView.findViewById(R.id.btn_item_increase);
        }
    }
}

