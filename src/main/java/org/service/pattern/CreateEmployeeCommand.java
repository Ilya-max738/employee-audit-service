package org.service.pattern;

import org.service.model.ProfilesRole;

import static org.service.pattern.Invoker.repository;

public class CreateEmployeeCommand implements Command{
    private String name;
    private ProfilesRole position;
    private double salary;

    public CreateEmployeeCommand(String name, ProfilesRole position, double salary){
        this.name = name;
        this.position = position;
        this.salary = salary;
    }
    @Override
    public void execute() {
        if (name.matches("[A-Za-zА-Яа-я\\s\\-'.,]{1,225}") && Double.toString(salary).matches("[\\d.,]+")){
            repository.create(name, position, salary);
        }
    }
}
