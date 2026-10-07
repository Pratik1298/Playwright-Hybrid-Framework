package com.pratik.api.models;

import java.util.List;

public record ProductPage(int currentPage, List<Product> data, int total) {}
