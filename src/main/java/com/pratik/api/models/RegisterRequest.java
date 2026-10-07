package com.pratik.api.models;
public record RegisterRequest(String firstName, String lastName, String dob,
                              String phone, String email, String password) {}
