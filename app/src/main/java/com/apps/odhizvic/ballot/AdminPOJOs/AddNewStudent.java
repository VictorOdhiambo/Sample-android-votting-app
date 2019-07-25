package com.apps.odhizvic.ballot.AdminPOJOs;

public class AddNewStudent {
    private String registration_number;

    public AddNewStudent() {
    }

    public AddNewStudent(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getRegistration_number() {
        return registration_number;
    }
}
