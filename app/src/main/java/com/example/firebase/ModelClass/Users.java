package com.example.firebase.ModelClass;

import java.util.List;

public class Users {

    private String name;
    private String address;
    private String email;
    private String phone;
    private String gender;
    private List<String> registeredTournaments;

    public Users(String name, String address, String email, String phone, String gender, List<String> registeredTournaments) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.registeredTournaments = registeredTournaments;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getRegisteredTournaments() {
        return registeredTournaments;
    }
}
