package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    //create employee
    public Employee createEmployee(Employee employee){
        return employeeRepository.save(employee);
    }
//Get all employees
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    // get employee By Id
    public Employee getEmployeeByID(Long id){
        return employeeRepository.findById(id).orElse(null);
    }
    //update the existing employee
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee emp = existingEmployee.get();
            emp.setName(updatedEmployee.getName());
            emp.setEmail(updatedEmployee.getEmail());
            emp.setDepartment(updatedEmployee.getDepartment());
            return employeeRepository.save(emp);
        } else {
            return null;
        }
    }

    // Delete Employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}