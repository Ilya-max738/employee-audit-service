package org.service.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.service.model.Employee;
import org.service.model.ProfilesRole;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.*;

@Component
public class EmployeesRepository {
    private List<Employee> employeeList;
    private int elements;

    public EmployeesRepository(){
        employeeList = new ArrayList<>();
        elements = 0;
    }

    public void create(String name, ProfilesRole position, double salary){
        Random random = new Random();
        int id = random.nextInt(100, 999);
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPosition(position);
        employee.setSalary(salary);
        employee.setId(id);
        employeeList.add(employee);
        elements++;
    }

    public ArrayList<ArrayList<Employee>> search(int id, ProfilesRole position, double salary, String name){
        ArrayList<ArrayList<Employee>> employeesFound = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            employeesFound.add(new ArrayList<>());
        }
        for (int i = 0; i < elements; i++) {
            if (id == employeeList.get(i).getId()){
                employeesFound.get(0).add(employeeList.get(i));
            }
            if (position.equals(employeeList.get(i).getPosition())){
                employeesFound.get(1).add(employeeList.get(i));
            }
            if (salary == employeeList.get(i).getSalary()){
                employeesFound.get(2).add(employeeList.get(i));
            }
            if (name.equals(employeeList.get(i).getName())){
                employeesFound.get(3).add(employeeList.get(i));
            }
        }
        return employeesFound;
    }

    public void delete(Employee employee){
        employeeList.remove(employee);
        elements--;
    }

    public Employee getEmployeeByIndex(int index){
        return employeeList.get(index);
    }

    public String[] shoeAll(){
        PrintWriter printWriter = new PrintWriter(System.out, true);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < elements; i++) {
                strings.add(objectMapper.writeValueAsString(employeeList.get(i)));
            }
            return strings.toArray(new String[0]);
        } catch (Exception e) {
            printWriter.println("Problem: " + e);
            return null;
        }
    }
}
