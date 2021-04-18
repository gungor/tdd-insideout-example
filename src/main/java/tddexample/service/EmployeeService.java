package tddexample.service;

import org.springframework.stereotype.Service;
import tddexample.model.entity.Employee;
import tddexample.model.rest.EmployeeSaveRequest;
import tddexample.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(EmployeeSaveRequest request) {
        Employee employee = new Employee(null,request.getFullName());
        return employeeRepository.save(employee);
    }

}
