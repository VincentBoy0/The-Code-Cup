package com.example.codecup.model;

/**
 * Model class đại diện cho một phần thưởng có thể đổi
 */
public class Reward {

    private int id;
    private String name;
    private String description;
    private int pointsRequired;
    private int iconResId;

    public Reward(int id, String name, String description, int pointsRequired, int iconResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.iconResId = iconResId;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPointsRequired() { return pointsRequired; }
    public int getIconResId() { return iconResId; }

    // Format points
    public String getFormattedPoints() {
        return pointsRequired + " points";
    }
}

