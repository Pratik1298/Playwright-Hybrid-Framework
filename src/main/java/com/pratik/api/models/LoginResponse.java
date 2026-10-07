package com.pratik.api.models;
public record LoginResponse(String accessToken, String tokenType, int expiresIn) {}