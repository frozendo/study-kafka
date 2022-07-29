package com.frozendo.study.source;

import com.frozendo.study.entity.Product;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class ProductSource {

    private static Map<Integer, Product> products;

    public static Product getProduct() {
        if (Objects.isNull(products) || products.isEmpty()) {
            buildList();
        }
        var random = new Random();
        var value = random.nextInt(20);
        return products.get(value);
    }

    private static void buildList() {
        products = Map.ofEntries(
                Map.entry(1, new Product("001", "Monitor LCD")),
                Map.entry(2, new Product("002", "Monitor Widescreen")),
                Map.entry(3, new Product("003", "Mouse")),
                Map.entry(4, new Product("004", "Mouse Wireless")),
                Map.entry(5, new Product("005", "Keyboard")),
                Map.entry(6, new Product("006", "Mechanical Keyboard")),
                Map.entry(7, new Product("007", "Headset")),
                Map.entry(8, new Product("008", "Headset Wireless")),
                Map.entry(9, new Product("009", "Webcam USB")),
                Map.entry(10, new Product("010", "Pen drive 16GB")),
                Map.entry(11, new Product("011", "SSD 256GB")),
                Map.entry(12, new Product("012", "HHD 1TB")),
                Map.entry(13, new Product("013", "Motherboard")),
                Map.entry(14, new Product("014", "Video Card")),
                Map.entry(15, new Product("015", "HDMI Adapter")),
                Map.entry(16, new Product("016", "PC Gamer")),
                Map.entry(17, new Product("017", "Notebook")),
                Map.entry(18, new Product("018", "Macbook")),
                Map.entry(19, new Product("019", "Smartphone")),
                Map.entry(20, new Product("020", "Iphone"))
        );
    }

}
