package org.service.pattern;

import static org.service.pattern.Invoker.repository;

public class ShowAllCommand implements Command{
    public String[] arrayAllEmployees;
    @Override
    public void execute() {
        arrayAllEmployees = repository.shoeAll();
    }
}
