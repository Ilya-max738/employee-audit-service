package org.service.pattern;

import org.service.model.Employee;

import static org.service.pattern.Invoker.repository;

public class GetByIndexCommand implements Command{
    private int index;
    private Employee employee;
    public GetByIndexCommand(int index){
        this.index = index;
    }
    @Override
    public void execute() {
        employee = repository.getEmployeeByIndex(index);
    }

    public Employee getEmployee() {
        return employee;
    }
}
