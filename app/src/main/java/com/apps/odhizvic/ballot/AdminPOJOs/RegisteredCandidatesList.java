package com.apps.odhizvic.ballot.AdminPOJOs;

public class RegisteredCandidatesList {
    private String candidatePosition;

    public RegisteredCandidatesList() {
    }

    public RegisteredCandidatesList(String candidatePosition) {
        this.candidatePosition = candidatePosition;
    }

    public String getCandidatePosition() {
        return candidatePosition;
    }

}
