package com.example.codecutcodeshit.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codecutcodeshit.R;
import com.example.codecutcodeshit.model.Order;

import java.util.List;
import java.util.Locale;

/**
 * ADAPTER CHO DANH SÁCH ĐƠN HÀNG
 *
 * Click vào order để chuyển từ Ongoing sang Completed
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;
    private OnOrderClickListener listener;
    private boolean isOngoingSection; // true = ongoing, false = history

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(Context context, List<Order> orders, boolean isOngoingSection, OnOrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.isOngoingSection = isOngoingSection;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Hiển thị thông tin
        holder.tvOrderNumber.setText(String.format(Locale.US, "Order #%d", order.getOrderNumber()));
        holder.tvOrderDate.setText(order.getFormattedDate());
        holder.tvOrderItems.setText(order.getItemsDescription());
        holder.tvOrderTotal.setText(String.format(Locale.US, "$%.2f", order.getTotalPrice()));

        // Hiển thị status và màu
        if (order.isOngoing()) {
            holder.tvOrderStatus.setText("Ongoing");
            holder.tvOrderStatus.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.status_preparing)));
        } else {
            holder.tvOrderStatus.setText("Completed");
            holder.tvOrderStatus.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.status_completed)));
        }

        // Click listener - chỉ cho ongoing orders
        if (isOngoingSection) {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrderClick(order);
                }
            });
        } else {
            holder.itemView.setOnClickListener(null);
            holder.itemView.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvOrderStatus, tvOrderDate;
        TextView tvOrderItems, tvOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderItems = itemView.findViewById(R.id.tv_order_items);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
        }
    }
}

