package com.apps.odhizvic.ballot.AdminPOJOs;

public class ShowCurrentLeadership {
    private int leader_profile_image;
    private String leader_name;
    private String leader_reg_no;
    private String leader_position;
    private String leader_course;
    private String leader_year;

    public ShowCurrentLeadership(){}

    public ShowCurrentLeadership(int leader_profile_image, String leader_name) {
        this.leader_profile_image = leader_profile_image;
        this.leader_name = leader_name;
    }

    public ShowCurrentLeadership(int leader_profile_image, String leader_name, String leader_reg_no,
                                 String leader_position, String leader_year, String leader_faculty) {
        this.leader_profile_image = leader_profile_image;
        this.leader_name = leader_name;
        this.leader_reg_no = leader_reg_no;
        this.leader_position = leader_position;
        this.leader_course = leader_faculty;
        this.leader_year = leader_year;
    }

    public int getLeader_profile_image() {
        return leader_profile_image;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public String getLeader_reg_no() {
        return leader_reg_no;
    }

    public String getLeader_position() {
        return leader_position;
    }

    public String getLeader_course() {
        return leader_course;
    }

    public String getLeader_year() {
        return leader_year;
    }
}
