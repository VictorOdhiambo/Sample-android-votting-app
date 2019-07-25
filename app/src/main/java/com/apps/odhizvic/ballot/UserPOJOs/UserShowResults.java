package com.apps.odhizvic.ballot.UserPOJOs;

public class UserShowResults {
    private String candidateReg;
    private String voteCount;

    public UserShowResults() {
    }

    public UserShowResults(String candidateReg, String voteCount) {
        this.candidateReg = candidateReg;
        this.voteCount = voteCount;
    }

    public String getCandidateReg() {
        return candidateReg;
    }

    public String getVoteCount() {
        return voteCount;
    }
}
