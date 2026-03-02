package org.service;

import org.service.configuration.ConfigfApp;
import org.service.model.Employee;
import org.service.model.ProfilesRole;
import org.service.service.Actions;
import org.service.service.EmployeeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class Client {
    public static ProfilesRole setPosition(String string){
        switch (string){
            case "HR" -> {
                return ProfilesRole.HR;
            }
            case "Admin" -> {
                return ProfilesRole.ADMIN;
            }
            case "Manager" -> {
                return ProfilesRole.MANAGER;
            }
            case "Regular" -> {
                return ProfilesRole.REGULAR;
            }
            case null, default -> {
                return ProfilesRole.None;
            }
        }
    }
    public static void setUserPosition(PrintWriter printWriter, BufferedReader bufferedReader){
        try {
            String userPosition = bufferedReader.readLine();
            switch (userPosition) {
                case "HR" -> USERPOSITION = ProfilesRole.HR;
                case "Admin" -> USERPOSITION = ProfilesRole.ADMIN;
                case "Manager" -> USERPOSITION = ProfilesRole.MANAGER;
                case null, default -> USERPOSITION = ProfilesRole.REGULAR;
            }
        } catch (Exception e) {
            printWriter.println("Problem: " + e);
        }
    }
    public static ProfilesRole USERPOSITION;
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigfApp.class);
        EmployeeService service = context.getBean(EmployeeService.class);
        PrintWriter printWriter = new PrintWriter(System.out, true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        printWriter.println("You're welcome! Please, set your position: ");
        setUserPosition(printWriter, bufferedReader);
        printWriter.println("Your position: " + USERPOSITION);
        String userFlag = "yes";
        while (userFlag.contains("yes")){
            try {
                BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
                printWriter.println("Actions: 1)create employee 2)change parameters 3)get information 4)delete employee 5) get all employees");
                switch (bufferedReader1.readLine()) {
                    case "1" -> {
                        try {
                            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(System.in));
                            printWriter.println("Name:");
                            String name = bufferedReader2.readLine();
                            printWriter.println("Position:");
                            String positionS = bufferedReader2.readLine();
                            printWriter.println("Start salary: ");
                            String salary = bufferedReader2.readLine();
                            service.createEmployee(name, setPosition(positionS), Double.parseDouble(salary));
                        } catch (Exception e) {
                            printWriter.println("Problem: " + e);
                        }
                    }
                    case "2" -> {
                        try {
                            printWriter.println("Choice number of employee: ");
                            String[] allEmployees = service.getAllEmployees();
                            printWriter.println(Arrays.toString(allEmployees));
                            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(System.in));
                            String number = bufferedReader2.readLine();
                            int index = Integer.parseInt(number) - 1;
                            Employee employee = service.getByIndex(index);
                            printWriter.println("Set action: 1)update salary 2)change position 3)change name");
                            String actionS = bufferedReader2.readLine();
                            Actions action = null;
                            switch (actionS) {
                                case "1" -> action = Actions.UpdateSalary;
                                case "2" -> action = Actions.ChangePosition;
                                case "3" -> action = Actions.ChangeName;
                            }
                            if (action != null) {
                                printWriter.println("input new info: ");
                                service.changeParameters(action, employee);
                            } else throw new RuntimeException("Problem with the data");
                        } catch (Exception e) {
                            printWriter.println("Problem: " + e);
                        }
                    }
                    case "3" -> {
                        try {
                            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(System.in));
                            printWriter.println("Set information about employee: id, position, name, salary: ");
                            String idS = bufferedReader2.readLine();
                            String position = bufferedReader2.readLine();
                            String name = bufferedReader1.readLine();
                            String salaryS = bufferedReader2.readLine();
                            int id;
                            double salary;
                            if (idS.equals("-")){
                                id = -0;
                            } else id = Integer.parseInt(idS);
                            if (salaryS.equals("-")){
                                salary = -0;
                            } else salary = Double.parseDouble(salaryS);
                            String[] foundInfo = service.employeeInfo( id, setPosition(position), name, salary);
                            printWriter.println("Result: ");
                            printWriter.println(Arrays.toString(foundInfo));
                        } catch (Exception e) {
                            printWriter.println("Problem: " + e);
                        }
                    }
                    case "4" -> {
                        try {
                            printWriter.println("Choice number of employee: ");
                            String[] allEmployees = service.getAllEmployees();
                            printWriter.println(Arrays.toString(allEmployees));
                            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(System.in));
                            String number = bufferedReader2.readLine();
                            int index = Integer.parseInt(number) - 1;
                            Employee employee = service.getByIndex(index);
                            service.deleteEmployee(employee);
                        } catch (Exception e) {
                            printWriter.println("Problem: " + e);
                        }
                    }
                    case "5" -> {
                        printWriter.println("Repository of employees: ");
                        String[] allEmployees = service.getAllEmployees();
                        printWriter.println(Arrays.toString(allEmployees));
                    }
                }
                printWriter.println("Repeat?");
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(System.in));
                userFlag = bufferedReader2.readLine();
                printWriter.println("Change user rights?");
                String userDesition = bufferedReader2.readLine();
                if (userDesition.contains("yes")){
                    printWriter.println("Set new position: ");
                    setUserPosition(printWriter, bufferedReader2);
                }
            } catch (Exception e) {
                printWriter.println("Problem: " + e);
            }
        }
    }
}
