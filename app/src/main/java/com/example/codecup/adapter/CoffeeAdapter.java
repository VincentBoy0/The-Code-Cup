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
import com.example.codecup.model.Coffee;

import java.util.List;

/**
 * ADAPTER là gì?
 * - Adapter là "cầu nối" giữa dữ liệu (List<Coffee>) và giao diện (RecyclerView)
 * - Nó lấy dữ liệu và "gắn" vào từng item trong danh sách
 *
 * RecyclerView là gì?
 * - Là view hiển thị danh sách có thể cuộn được
 * - "Recycle" = tái sử dụng: Khi bạn cuộn, các item cũ được tái sử dụng thay vì tạo mới
 * - Tiết kiệm bộ nhớ hơn ListView cũ
 */
public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder> {

    // Danh sách cà phê sẽ hiển thị
    private List<Coffee> coffeeList;

    // Context để truy cập resources (hình ảnh, text...)
    private Context context;

    // Interface để xử lý sự kiện click
    private OnCoffeeClickListener listener;

    /**
     * INTERFACE là gì?
     * - Là "hợp đồng" định nghĩa các method mà class khác phải thực hiện
     * - Dùng để giao tiếp giữa Adapter và Activity
     * - Khi user click item, Adapter sẽ gọi method này để báo cho Activity biết
     */
    public interface OnCoffeeClickListener {
        void onCoffeeClick(Coffee coffee);
    }

    /**
     * Constructor - Hàm khởi tạo Adapter
     */
    public CoffeeAdapter(Context context, List<Coffee> coffeeList, OnCoffeeClickListener listener) {
        this.context = context;
        this.coffeeList = coffeeList;
        this.listener = listener;
    }

    /**
     * onCreateViewHolder - Được gọi khi RecyclerView cần tạo ViewHolder MỚI
     *
     * ViewHolder là gì?
     * - Là class giữ tham chiếu đến các View trong item layout
     * - Giúp tránh gọi findViewById() nhiều lần (tốn hiệu năng)
     */
    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate = "thổi phồng" layout XML thành View thực tế
        View view = LayoutInflater.from(context).inflate(R.layout.item_coffee, parent, false);
        return new CoffeeViewHolder(view);
    }

    /**
     * onBindViewHolder - Được gọi để gắn dữ liệu vào ViewHolder
     *
     * @param holder ViewHolder chứa các View
     * @param position Vị trí trong danh sách (0, 1, 2...)
     */
    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {
        // Lấy coffee tại vị trí hiện tại
        Coffee coffee = coffeeList.get(position);

        // Gắn dữ liệu vào các View
        holder.tvName.setText(coffee.getName());
        holder.tvDescription.setText(coffee.getDescription());
        holder.tvPrice.setText(String.format("$%.2f", coffee.getPrice()));
        holder.ivCoffee.setImageResource(coffee.getImageResId());

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCoffeeClick(coffee);
            }
        });
    }

    /**
     * getItemCount - Trả về số lượng item trong danh sách
     */
    @Override
    public int getItemCount() {
        return coffeeList != null ? coffeeList.size() : 0;
    }

    /**
     * ViewHolder - Class giữ tham chiếu đến các View trong item layout
     */
    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCoffee;    // Hình ảnh cà phê
        TextView tvName;       // Tên cà phê
        TextView tvDescription;// Mô tả
        TextView tvPrice;      // Giá

        public CoffeeViewHolder(@NonNull View itemView) {
            super(itemView);
            // Tìm các View bằng ID
            ivCoffee = itemView.findViewById(R.id.iv_coffee);
            tvName = itemView.findViewById(R.id.tv_coffee_name);
            tvDescription = itemView.findViewById(R.id.tv_coffee_description);
            tvPrice = itemView.findViewById(R.id.tv_coffee_price);
        }
    }
}

