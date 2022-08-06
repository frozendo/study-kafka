package com.frozendo.study.source;

import com.frozendo.study.entity.Product;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class ProductSource {

    public static final String FURNITURE = "Furniture";
    public static final String ELECTRONIC = "Electronic";
    public static final String HOME_APPLIANCE = "Home Appliance";
    private static Map<Integer, Product> products;

    private static final Random random = new Random();

    private ProductSource() {}

    public static Product getProduct() {
        if (Objects.isNull(products) || products.isEmpty()) {
            buildList();
        }
        var value = random.nextInt(19);
        return products.get(value);
    }

    private static void buildList() {
        products = Map.ofEntries(
                Map.entry(0, new Product("001", "Monitor LCD", ELECTRONIC)),
                Map.entry(1, new Product("002", "Monitor Widescreen", ELECTRONIC)),
                Map.entry(2, new Product("003", "Mouse", ELECTRONIC)),
                Map.entry(3, new Product("004", "Iphone", ELECTRONIC)),
                Map.entry(4, new Product("005", "Keyboard", ELECTRONIC)),
                Map.entry(5, new Product("006", "Macbook", ELECTRONIC)),
                Map.entry(6, new Product("007", "Headset", ELECTRONIC)),
                Map.entry(7, new Product("008", "Couch", FURNITURE)),
                Map.entry(8, new Product("009", "Bookcase", FURNITURE)),
                Map.entry(9, new Product("010", "Bed", FURNITURE)),
                Map.entry(10, new Product("011", "Nightstand", FURNITURE)),
                Map.entry(11, new Product("012", "Chair", FURNITURE)),
                Map.entry(12, new Product("013", "Closet", FURNITURE)),
                Map.entry(13, new Product("014", "Armchair", FURNITURE)),
                Map.entry(14, new Product("015", "Freezer", HOME_APPLIANCE)),
                Map.entry(15, new Product("016", "Stove", HOME_APPLIANCE)),
                Map.entry(16, new Product("017", "Refrigerator", HOME_APPLIANCE)),
                Map.entry(17, new Product("018", "Microwave", HOME_APPLIANCE)),
                Map.entry(18, new Product("019", "Washing Machine", HOME_APPLIANCE)),
                Map.entry(19, new Product("020", "Vacuum Cleaner", HOME_APPLIANCE))
        );
    }

}
