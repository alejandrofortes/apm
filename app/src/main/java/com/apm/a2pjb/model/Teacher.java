package com.apm.a2pjb.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Teacher {

    private Long id;
    private String name;
    private String job;
    private String department;
    private String office;
    private Integer extension;
    private String email;


    public Teacher() {
    }

    public Teacher(String name, String job, String department, String office, Integer extension, String email) {
        this.name = name;
        this.job = job;
        this.department = department;
        this.office = office;
        this.extension = extension;
        this.email = email;
    }

    public Teacher(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.job = jsonObject.getString("job");
            this.department = jsonObject.getString("dept");
            this.office = jsonObject.getString("room");
            this.extension = jsonObject.getInt("ext");
            this.email = jsonObject.getString("email");
        }catch (JSONException e){
            new Teacher();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
