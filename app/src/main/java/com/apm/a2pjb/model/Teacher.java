package com.apm.a2pjb.model;

import java.util.List;

public class Teacher {

    private String name;
    private String job;
    private String department;
    private String office;
    private Integer extension;
    private String email;
    private List<String> others;


    public Teacher() {
    }

    public Teacher(String name, String job, String department, String office, Integer extension, String email, List<String> others) {
        this.name = name;
        this.job = job;
        this.department = department;
        this.office = office;
        this.extension = extension;
        this.email = email;
        this.others = others;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Integer getExtension() {
        return extension;
    }

    public void setExtension(Integer extension) {
        this.extension = extension;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getOthers() {
        return others;
    }

    public void setOthers(List<String> others) {
        this.others = others;
    }
}
