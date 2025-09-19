package com.mytlx.arcane.utils.json.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private int age;
    private boolean active;
    private List<String> tags;
    private Map<String, Object> attributes;
    private Address address;

    public User(String name, int age) {
        this.name = name;
        this.age = age;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String city;
        private String street;
        private String zipCode;

        public Address(String city, String street) {
            this.city = city;
            this.street = street;
        }
    }
}