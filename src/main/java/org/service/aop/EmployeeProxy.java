package org.service.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.service.service.ServiceException;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class EmployeeProxy {
    private Logger LOGGER = Logger.getLogger(EmployeeProxy.class.getName());

    public void validateConstructor(JoinPoint joinPoint){
        ConstructorSignature signature = (ConstructorSignature) joinPoint.getSignature();
        String[] strings = signature.getParameterNames();
        Object[] objects = joinPoint.getArgs();
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]){
                case "id" -> {
                    if (validateId((int) objects[i])){
                        String log = getLogging(Level.FINE, "Id was validated");
                        addLogIntoFile(log);
                    } else throw new ServiceException("Id wasn't validated");
                }
                case "salary" -> {
                    if (validateSalary((double) objects[i])){
                        String log = getLogging(Level.FINE, "Salary was validated");
                        addLogIntoFile(log);
                    } else throw new ServiceException("Salary wasn't validated");
                }
                case "name" -> {
                    if (validateName((String) objects[i])){
                        String log = getLogging(Level.FINE, "Name was validated");
                        addLogIntoFile(log);
                    } else throw new ServiceException("Name wasn't validated");
                }
            }
        }
    }

    @Before("execution(* org.service.repository.EmployeesRepository.create(..))")
    public void validateParameters(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if(validateName((String) args[0])){
            String log = getLogging(Level.INFO, "Name was validated");
            addLogIntoFile(log);
        } else throw new ServiceException("Name wasn't validated");
        if (validateSalary((double) args[2])){
            String log = getLogging(Level.INFO, "Salary was validated");
            addLogIntoFile(log);
        } else throw new ServiceException("Salary wasn't validated");
    }



    public void validateSetterName(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if (validateName((String) args[0])){
            String log = getLogging(Level.INFO, "Name was validated");
            System.out.println(log);
            addLogIntoFile(log);
        } else throw new ServiceException("Name wasn't validated");
    }

    public void validateSetterId(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if (validateId((int) args[0])){
            String log = getLogging(Level.INFO, "Id was validated");
            addLogIntoFile(log);
        } else throw new ServiceException("Id wasn't validated");
    }

    public void validateSetterSalary(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if (validateSalary((double) args[0])){
            String log = getLogging(Level.INFO, "Salary was validated");
            addLogIntoFile(log);
        } else throw new ServiceException("Salary wasn't validated");
    }

    private boolean validateId(int id){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(Integer.toString(id));
        return matcher.find();
    }
    private boolean validateName(String name){
        Pattern pattern = Pattern.compile("[A-Za-zА-Яа-я\\s\\-'.,]{1,255}");
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }
    private boolean validateSalary(double salary){
        return salary >= 0;
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

    private void addLogIntoFile(String log){
        try (FileWriter fileWriter = new FileWriter("C:/Users/rtd56/Desktop/Projects/employee-audit-service/Loggers.txt", true)){
            fileWriter.write(log);
            fileWriter.write('\n');
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Problem: " + e);
        }
    }
}
