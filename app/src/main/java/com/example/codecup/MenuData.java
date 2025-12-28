package com.example.codecup;

import java.util.Arrays;
import java.util.List;

public class MenuData {
    public static List<Category> getCategories() {
        return Arrays.asList(
            new Category("Coffee", Arrays.asList(
                new MenuItem("1", "Espresso", "Strong single shot of coffee"),
                new MenuItem("2", "Americano", "Espresso diluted with hot water"),
                new MenuItem("3", "Cappuccino", "Espresso with steamed milk and foam"),
                new MenuItem("4", "Iced Coffee", "Cold brewed or shaken over ice"),
                new MenuItem("5", "Cold Brew", "Slow-steeped for a smooth taste"),
                new MenuItem("6", "Affogato", "Espresso poured over ice cream"),
                new MenuItem("27", "Ristretto", "Shorter, more concentrated espresso shot"),
                new MenuItem("28", "Flat White", "Espresso with thin layer of steamed milk")
            )),
            new Category("Latte", Arrays.asList(
                new MenuItem("7", "Vanilla Latte", "Espresso with steamed milk and vanilla syrup"),
                new MenuItem("8", "Caramel Macchiato", "Vanilla latte with caramel drizzle"),
                new MenuItem("9", "Mocha Latte", "Latte with chocolate syrup"),
                new MenuItem("10", "Pumpkin Spice Latte", "Seasonal spiced latte"),
                new MenuItem("29", "Hazelnut Latte", "Latte with hazelnut syrup"),
                new MenuItem("30", "Matcha Latte", "Green tea powder with steamed milk")
            )),
            new Category("Tea", Arrays.asList(
                new MenuItem("11", "Green Tea", "Light and grassy"),
                new MenuItem("12", "Black Tea", "Bold and robust"),
                new MenuItem("13", "Chai Latte", "Spiced tea with steamed milk"),
                new MenuItem("14", "Herbal Tea", "Caffeine-free blends"),
                new MenuItem("31", "Oolong Tea", "Floral and toasty notes"),
                new MenuItem("32", "Earl Grey", "Black tea scented with bergamot")
            )),
            new Category("Food", Arrays.asList(
                new MenuItem("15", "Ham Sandwich", "Toasted sandwich with ham and cheese"),
                new MenuItem("16", "Veggie Wrap", "Fresh vegetables in a whole wheat wrap"),
                new MenuItem("17", "Avocado Toast", "Smashed avocado on sourdough"),
                new MenuItem("18", "Quiche", "Savory egg pie with spinach and cheese"),
                new MenuItem("33", "Grilled Panini", "Pressed sandwich with melted cheese"),
                new MenuItem("34", "Chicken Caesar Salad", "Crisp romaine with grilled chicken")
            )),
            new Category("Desserts", Arrays.asList(
                new MenuItem("19", "Cheesecake", "Creamy New York-style cheesecake"),
                new MenuItem("20", "Brownie", "Fudgy chocolate brownie"),
                new MenuItem("21", "Tiramisu", "Coffee-soaked ladyfingers with mascarpone"),
                new MenuItem("22", "Ice Cream", "Rotating flavors"),
                new MenuItem("35", "Panna Cotta", "Silky cream dessert with berry sauce"),
                new MenuItem("36", "Macarons", "Assorted flavored macarons")
            )),
            new Category("Drinks", Arrays.asList(
                new MenuItem("23", "Smoothie", "Fruit blended with yogurt"),
                new MenuItem("24", "Milkshake", "Creamy milkshake with ice cream"),
                new MenuItem("25", "Lemonade", "Fresh-squeezed lemonade"),
                new MenuItem("26", "Hot Chocolate", "Rich chocolate with steamed milk"),
                new MenuItem("37", "Iced Tea", "Refreshing brewed tea over ice"),
                new MenuItem("38", "Sparkling Water", "Chilled carbonated water")
            )),
            new Category("Breakfast", Arrays.asList(
                new MenuItem("39", "Pancake Stack", "Fluffy pancakes with maple syrup"),
                new MenuItem("40", "Breakfast Burrito", "Eggs, cheese, and sausage in a tortilla"),
                new MenuItem("41", "Oatmeal Bowl", "Warm oats with fruits and nuts")
            )),
            new Category("Bakery", Arrays.asList(
                new MenuItem("42", "Croissant", "Buttery flaky croissant"),
                new MenuItem("43", "Blueberry Muffin", "Moist muffin with fresh blueberries"),
                new MenuItem("44", "Banana Bread", "Homestyle banana loaf")
            )),
            new Category("Specials", Arrays.asList(
                new MenuItem("45", "Barista Special", "Daily rotating specialty drink"),
                new MenuItem("46", "Seasonal Pie", "Limited-time pie flavor"),
                new MenuItem("47", "Chef's Platter", "A selection of our favorites for sharing")
            ))
        );
    }
}

