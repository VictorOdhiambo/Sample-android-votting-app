package com.apps.odhizvic.ballot.UserPOJOs;

public class VotingData {
    private String studentRegNo;
    private String candidatePosition;
    private String hasVoted;

    public VotingData() {
    }

    public VotingData(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public VotingData(String hasVoted, String studentRegNo) {
        this.studentRegNo = studentRegNo;
        this.hasVoted = hasVoted;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public String getCandidatePosition() {
        return candidatePosition;
    }

    public String getHasVoted() {
        return hasVoted;
    }
}
