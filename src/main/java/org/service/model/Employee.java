package org.service.model;

public class Employee {
    private int id;
    private ProfilesRole position;
    private double salary;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(ProfilesRole position) {
        this.position = position;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProfilesRole getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }
}
