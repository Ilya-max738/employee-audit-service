package org.service.pattern;

import org.service.model.Employee;
import org.service.model.ProfilesRole;
import org.service.service.Actions;
import org.service.service.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChangeParametersCommand implements Command{
    private Employee employee;
    private Actions action;

    public ChangeParametersCommand(Employee employee, Actions action){
        this.employee = employee;
        this.action = action;
    }
    @Override
    public void execute(){
        try {
            switch (action) {
                case ChangeName -> {
                    String newName = inputBuffer();
                    if (newName.matches("[A-Za-zА-Яа-я\\d\\s.,']+")){
                        employee.setName(newName);
                    } else throw new ServiceException("Name set problems");
                }
                case UpdateSalary -> {
                    String newSalaryS = inputBuffer();
                    double newSalary = Double.parseDouble(newSalaryS);
                    if (newSalary >= 0){
                        employee.setSalary(newSalary);
                    } else throw new ServiceException("Salary set problems");
                }
                case ChangePosition -> {
                    String newPosition = inputBuffer();
                    ProfilesRole profilesRole;
                    switch (newPosition){
                        case "HR" -> profilesRole = ProfilesRole.HR;
                        case "Admin" -> profilesRole = ProfilesRole.ADMIN;
                        case "Regular" -> profilesRole = ProfilesRole.REGULAR;
                        case "Manager" -> profilesRole = ProfilesRole.MANAGER;
                        default -> profilesRole = null;
                    }
                    if (profilesRole != null){
                        employee.setPosition(profilesRole);
                    } else throw new ServiceException("Position set problems");
                }
            }
        } catch (Exception e) {
            System.out.println("Problem: " + e);
        }
    }

    private String inputBuffer() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        return bufferedReader.readLine();
    }
}
