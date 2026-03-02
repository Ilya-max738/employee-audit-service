package org.service.pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.service.model.Employee;
import org.service.model.ProfilesRole;

import java.util.ArrayList;

import static org.service.pattern.Invoker.repository;

public class EmployeeInfoCommand implements Command{
    public String[] info;
    private int id;
    private ProfilesRole position;
    private String name;
    private double salary;

    public EmployeeInfoCommand(int id, ProfilesRole position, String name, double salary){
        this.id = id;
        this.position = position;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public void execute() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<String> infos = new ArrayList<>();
            ArrayList<ArrayList<Employee>> typesEmployee;
            typesEmployee = repository.search(id, position, salary, name);
            for (int i = 0; i < 4; ) {
                for (int j = 0; j < typesEmployee.get(i).size(); j++) {
                    infos.add(objectMapper.writeValueAsString(typesEmployee.get(i).get(j)));
                }
                i++;
            }
            if (!infos.isEmpty()) {
                info = infos.toArray(new String[0]);
            } else info[0] = "No searched information";
        } catch (Exception e) {
            System.out.println("Problem" + e);
        }
    }
}
