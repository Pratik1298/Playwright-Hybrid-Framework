package com.pratik.api.models;

public record ProductRequest(String name, String description, double price,
                             String categoryId, String brandId, String productImageId,
                             boolean isLocationOffer, boolean isRental, String co2Rating) {

    public static ProductRequest basedOn(Product reference, String name, double price) {
        return new ProductRequest(name, "Created by Playwright API test", price,
                reference.category().id(), reference.brand().id(), reference.productImage().id(),
                false, false, "C");
    }
}
