package org.service.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.service.Client;
import org.service.model.Employee;
import org.service.service.Actions;
import org.service.service.ServiceException;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.*;

@Aspect
@Component
public class BaseAdvices {
    private Logger LOGGER = Logger.getLogger(BaseAdvices.class.getName());
    private String beforeEmployee;

    @After("execution(* org.service.service.EmployeeService.createEmployee(..))")
    public void adviceCreate(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(args[i].toString());
            stringBuilder.append(", ");
        }
        String user = getPosition();
        String action = joinPoint.getSignature().getName();
        String info = "User: " + user + " action: " + action + " parameters: " + stringBuilder;
        String log = getLogging(Level.INFO, info);
        addLogIntoFile(log);
    }

    @Before("execution(* org.service.service.EmployeeService.changeParameters(..))")
    public void setBeforeEmployee(JoinPoint joinPoint) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        Object[] args = joinPoint.getArgs();
        Employee beforeEmployeeE = (Employee) args[1];
        beforeEmployee = objectMapper.writeValueAsString(beforeEmployeeE);
    }

    @After("execution(* org.service.service.EmployeeService.changeParameters(..))")
    public void adviceChange(JoinPoint joinPoint){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object[] args = joinPoint.getArgs();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameters = signature.getParameterNames();
            Employee after = null;
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].contains("employee")) {
                    after = (Employee) args[i];
                }
            }
            String user = getPosition();
            String action = joinPoint.getSignature().getName();
            String info = "User: " + user + " action: " + action + " early data: " + beforeEmployee + " changed data: " + objectMapper.writeValueAsString(after);
            String log = getLogging(Level.INFO, info);
            addLogIntoFile(log);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Problem: " + e);
        }
    }

    @After("execution(* org.service.service.EmployeeService.deleteEmployee(..))")
    public void adviceDelete(JoinPoint joinPoint){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object[] args = joinPoint.getArgs();
            Employee employee = (Employee) args[0];
            String user = getPosition();
            String action = joinPoint.getSignature().getName();
            String info = "User: " + user + " action: " + action + " data user: " + objectMapper.writeValueAsString(employee);
            String log = getLogging(Level.INFO, info);
            addLogIntoFile(log);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Problem: " + e);
        }
    }

    @After("execution(* org.service.service.EmployeeService.employeeInfo(..))")
    public void adviceGetInfo(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String user = getPosition();
        String action = joinPoint.getSignature().getName();
        String info = "User: " + user + " action: " + action + " parameters: " + Arrays.toString(args);
        String log = getLogging(Level.INFO, info);
        addLogIntoFile(log);
    }

    @Before("execution(* org.service.service.EmployeeService.createEmployee(..))")
    public void adviceCreateCheckUser(){
        if (getPosition().contains("HR") || getPosition().contains("Manager")){
            String log = getLogging(Level.INFO, "Create employee user was validated");
            addLogIntoFile(log);
        } else throw new ServiceException("Access is denied");
    }

    @Before("execution(* org.service.service.EmployeeService.deleteEmployee(..))")
    public void adviceDeletedCheckUser(){
        if (getPosition().contains("Admin")){
            String log = getLogging(Level.INFO, "Deleted employee user was validated");
            addLogIntoFile(log);
        } else throw new ServiceException("Access is denied");
    }

    @Before("execution(* org.service.service.EmployeeService.changeParameters(..))")
    public void adviceControlPosition(JoinPoint joinPoint){
        Actions action;
        Object[] args = joinPoint.getArgs();
        action = (Actions) args[0];
        if (action != null) {
            switch (getPosition()) {
                case "HR" -> {
                    if (action.equals(Actions.UpdateSalary)) {
                        String log = getLogging(Level.INFO, "Update salary user was validated");
                        addLogIntoFile(log);
                    } else throw new ServiceException("Access is denied!");
                }
                case "Manager" -> {
                    if (action.equals(Actions.ChangePosition)){
                        String log = getLogging(Level.INFO, "Change position user was validated");
                        addLogIntoFile(log);
                    } else throw new ServiceException("Access is denied");
                }
                case "Admin" -> {
                    if (action.equals(Actions.ChangeName)){
                        String log = getLogging(Level.INFO, "Change name use was validated");
                        addLogIntoFile(log);
                    } else throw new ServiceException("Access is denied");
                }
            }
        } else throw new ServiceException("Problem with actio case in the soviet");
    }

    private String getLogging(Level level, String info){
        StringWriter stringWriter = new StringWriter();
        LOGGER.setUseParentHandlers(false);
        Handler handler = new StreamHandler(){
            {
                setOutputStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        stringWriter.write(b);
                    }
                });
            }
        };
        handler.setFormatter(new SimpleFormatter(){
            private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public String format(LogRecord record) {
                return String.format(
                        "[%s] %s: %s%n",
                        LocalDateTime.now().format(dateTimeFormatter),
                        record.getLevel(),
                        record.getMessage()
                );
            }
        });
        LOGGER.addHandler(handler);
        LOGGER.log(level, info);
        handler.flush();
        return stringWriter.toString();
    }

    private String getPosition(){
        switch (Client.USERPOSITION){
            case HR -> {
                return "HR";
            }
            case ADMIN -> {
                return "Admin";
            }
            case MANAGER -> {
                return "Manager";
            }
            case REGULAR -> {
                return "Regular";
            }
            case null, default -> {
                return  "No info";
            }
        }
    }

    private void addLogIntoFile(String log){
        try (FileWriter fileWriter = new FileWriter("C:/Users/rtd56/Desktop/Projects/employee-audit-service/Loggers.txt", true)){
            fileWriter.write(log);
            fileWriter.write('\n');
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Problem: " + e);
        }
    }
}
