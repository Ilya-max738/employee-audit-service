package org.service.service;

import org.service.model.Employee;
import org.service.model.ProfilesRole;
import org.service.pattern.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final Invoker invoker;

    @Autowired
    public EmployeeService(Invoker invoker){
        this.invoker = invoker;
    }

    public void createEmployee(String name, ProfilesRole position, double salary){
        CreateEmployeeCommand createEmployeeCommand = new CreateEmployeeCommand(name, position, salary);
        invoker.setCommand(createEmployeeCommand);
        invoker.push();
    }

    public void changeParameters(Actions action, Employee employee){
        ChangeParametersCommand changeParametersCommand = new ChangeParametersCommand(employee, action);
        invoker.setCommand(changeParametersCommand);
        invoker.push();
    }

    public String[] employeeInfo(int id, ProfilesRole position, String name, double salary){
        EmployeeInfoCommand employeeInfoCommand = new EmployeeInfoCommand(id, position, name, salary);
        invoker.setCommand(employeeInfoCommand);
        invoker.push();
        return employeeInfoCommand.info;
    }

    public void deleteEmployee(Employee employee){
        DeleteEmployeeCommand deleteEmployeeCommand = new DeleteEmployeeCommand(employee);
        invoker.setCommand(deleteEmployeeCommand);
        invoker.push();
    }

    public String[] getAllEmployees(){
        ShowAllCommand showAllCommand = new ShowAllCommand();
        invoker.setCommand(showAllCommand);
        invoker.push();
        return showAllCommand.arrayAllEmployees;
    }

    public Employee getByIndex(int index){
        GetByIndexCommand getByIndexCommand = new GetByIndexCommand(index);
        invoker.setCommand(getByIndexCommand);
        invoker.push();
        return getByIndexCommand.getEmployee();
    }
}
