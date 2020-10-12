package com.android.hq.androiddbdemo.single;

/**
 * Created by heqiang on 17-9-1.
 */

public class StudentBean {

    private String name;
    private String gender;
    private int grade;
    private int cls;
    private String country;
    private String province;
    private String specialty;
    private boolean isBoarder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getCls() {
        return cls;
    }

    public void setCls(int cls) {
        this.cls = cls;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public boolean isBoarder() {
        return isBoarder;
    }

    public void setBoarder(boolean boarder) {
        isBoarder = boarder;
    }

}
