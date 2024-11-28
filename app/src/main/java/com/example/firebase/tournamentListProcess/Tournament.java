package com.example.firebase.tournamentListProcess;

public class Tournament {
    private String id;
    private String name;
    private String address;
    private String contact;
    private String email;
    private int price;
    private String endDate;
    private String imageUrl;

    public Tournament(String name, String address, String contact, String email, int price, String endDate, String imageUrl) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.price = price;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public int getPrice() {
        return price;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

//    private String name;
//    private String address;
//    private String contact;
//    private int price;
//    private String endDate;
//    private int imageResId;
//
//    public Tournament(String name, String address, String contact, int price, String endDate, int imageResId) {
//        this.name = name;
//        this.address = address;
//        this.contact = contact;
//        this.price = price;
//        this.endDate = endDate;
//        this.imageResId = imageResId; // Assign image resource
//    }
//
//    // Getters for all fields
//    public String getName() { return name; }
//    public String getAddress() { return address; }
//    public String getContact() { return contact; }
//    public int getPrice() { return price; }
//    public String getEndDate() { return endDate; }
//    public int getImageResId() { return imageResId; }