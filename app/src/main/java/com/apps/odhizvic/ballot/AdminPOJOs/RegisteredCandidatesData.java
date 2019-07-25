package com.apps.odhizvic.ballot.AdminPOJOs;

public class RegisteredCandidatesData {
    private String leader_profile;
    private String leader_name;
    private String leader_regNo;
    private String leader_position;
    private String leader_year;
    private int voteCount;

    public RegisteredCandidatesData() {
    }


    public RegisteredCandidatesData(String leader_name, String leader_position, String leader_profile,
                                    String leader_regNo, String leader_year, int voteCount){
        this.leader_profile = leader_profile;
        this.leader_name = leader_name;
        this.leader_regNo = leader_regNo;
        this.leader_position = leader_position;
        this.leader_year = leader_year;
        this.voteCount = voteCount;
    }

    public String getLeader_profile() {
        return leader_profile;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public String getLeader_regNo() {
        return leader_regNo;
    }

    public String getLeader_position() {
        return leader_position;
    }

    public String getLeader_year() {
        return leader_year;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
