package com.pratik.api.models;
public record Product(String id, String name, String description, double price,
                      Brand brand, Category category, ProductImage productImage) {}