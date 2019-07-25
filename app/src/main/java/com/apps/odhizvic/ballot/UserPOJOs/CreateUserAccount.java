package com.apps.odhizvic.ballot.UserPOJOs;

public class CreateUserAccount {
    private String studentName;
    private String studentRegNo;
    private String studentFaculty;
    private String studentPassword;

    public CreateUserAccount(){}

    public CreateUserAccount( String studentFaculty, String studentName,
                              String studentPassword, String studentRegNo
            ) {
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
        this.studentFaculty = studentFaculty;
        this.studentPassword = studentPassword;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public String getStudentFaculty() {
        return studentFaculty;
    }

    public String getStudentPassword() {
        return studentPassword;
    }
}
