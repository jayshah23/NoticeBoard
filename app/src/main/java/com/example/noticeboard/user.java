package com.example.noticeboard;

public class user {
    public String name, phone, department, email,semester, type, designation, id_number;

    public user(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public user(String name, String phone, String department, String email, String semester, String type, String designation, String id_number) {
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.email = email;
        this.semester = semester;
        this.type = type;
        this.designation = designation;
        this.id_number = id_number;
    }
}
