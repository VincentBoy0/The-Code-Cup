package com.example.codecup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecup.R;
import com.example.codecup.model.CartItem;

import java.util.List;
import java.util.Locale;

/**
 * ADAPTER CHO GIỎ HÀNG
 *
 * Hiển thị danh sách items trong Cart Preview Dialog
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemActionListener listener;

    // Interface để xử lý sự kiện
    public interface OnCartItemActionListener {
        void onRemoveItem(CartItem item);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemActionListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvName.setText(item.getName());
        holder.tvDetails.setText(item.getDetailsText());
        holder.tvQuantity.setText(String.format(Locale.US, "Qty: %d", item.getQuantity()));
        holder.tvPrice.setText(String.format(Locale.US, "$%.2f", item.getTotalPrice()));
        holder.ivImage.setImageResource(item.getImageResId());

        // Xử lý nút xóa
        holder.ivRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveItem(item);
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

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivRemove;
        TextView tvName, tvDetails, tvQuantity, tvPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_cart_item);
            ivRemove = itemView.findViewById(R.id.iv_cart_item_remove);
            tvName = itemView.findViewById(R.id.tv_cart_item_name);
            tvDetails = itemView.findViewById(R.id.tv_cart_item_details);
            tvQuantity = itemView.findViewById(R.id.tv_cart_item_quantity);
            tvPrice = itemView.findViewById(R.id.tv_cart_item_price);
        }
    }
}

