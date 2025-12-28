package com.example.codecup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Object> displayedEntries = new ArrayList<>();
    private List<com.example.codecup.Category> originalCategories = new ArrayList<>();

    public void setCategories(List<com.example.codecup.Category> categories) {
        this.originalCategories = categories != null ? new ArrayList<>(categories) : new ArrayList<>();
        rebuildDisplayed(null, null);
    }

    public void filter(String query) {
        filter(query, null);
    }

    public void filter(String query, String categoryFilter) {
        rebuildDisplayed(query, categoryFilter);
    }

    private void rebuildDisplayed(String query, String categoryFilter) {
        displayedEntries.clear();
        String q = query != null ? query.trim().toLowerCase() : null;

        for (com.example.codecup.Category cat : originalCategories) {
            if (categoryFilter != null && !categoryFilter.equalsIgnoreCase("All")
                    && !cat.getName().equalsIgnoreCase(categoryFilter)) {
                continue;
            }

            List<com.example.codecup.MenuItem> matchedItems = new ArrayList<>();
            for (com.example.codecup.MenuItem item : cat.getItems()) {
                if (q == null || q.isEmpty()) {
                    matchedItems.add(item);
                } else {
                    String title = item.getTitle() != null ? item.getTitle().toLowerCase() : "";
                    String desc = item.getDescription() != null ? item.getDescription().toLowerCase() : "";
                    if (title.contains(q) || desc.contains(q)) {
                        matchedItems.add(item);
                    }
                }
            }

            if (!matchedItems.isEmpty()) {
                displayedEntries.add(cat.getName());
                displayedEntries.addAll(matchedItems);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return displayedEntries.get(position) instanceof String ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            ((HeaderViewHolder) holder).bind((String) displayedEntries.get(position));
        } else {
            ((ItemViewHolder) holder).bind((com.example.codecup.MenuItem) displayedEntries.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return displayedEntries.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.headerTitle);
        }

        public void bind(String header) {
            title.setText(header);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
        }

        public void bind(com.example.codecup.MenuItem item) {
            title.setText(item.getTitle());
            description.setText(item.getDescription() != null ? item.getDescription() : "");
        }
    }
}
