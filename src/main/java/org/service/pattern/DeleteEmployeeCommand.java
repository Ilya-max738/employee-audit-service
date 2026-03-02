package org.service.pattern;

import org.service.model.Employee;

import static org.service.pattern.Invoker.repository;

public class DeleteEmployeeCommand implements Command{
    private Employee employee;

    public DeleteEmployeeCommand(Employee employee){
        this.employee = employee;
    }
    @Override
    public void execute() {
        repository.delete(employee);
    }
}
