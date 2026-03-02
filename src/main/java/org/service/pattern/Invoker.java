package org.service.pattern;

import org.service.repository.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Invoker {
    private Command command;
    public static EmployeesRepository repository;

    public static Invoker getInstance(){
        return new Invoker();
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void push(){
        command.execute();
    }

    @Autowired
    public void setSetRepository(EmployeesRepository setRepository) {
        Invoker.repository = setRepository;
    }
}
