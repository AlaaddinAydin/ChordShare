package com.example.chordshare;

public class EducationDetail {

    String educationName,educationDescription,educationLink;

    public EducationDetail(String educationName, String educationDescription, String educationLink) {
        this.educationName = educationName;
        this.educationDescription = educationDescription;
        this.educationLink = educationLink;
    }

    public String getEducationName() {
        return educationName;
    }

    public String getEducationDescription() {
        return educationDescription;
    }

    public String getEducationLink() {
        return educationLink;
    }
}
