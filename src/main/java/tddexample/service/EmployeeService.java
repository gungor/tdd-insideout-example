package tddexample.service;

import org.springframework.stereotype.Service;
import tddexample.exception.EmployeeNotFoundException;
import tddexample.model.entity.Employee;
import tddexample.model.rest.EmployeeSaveRequest;
import tddexample.model.rest.EmployeeUpdateRequest;
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

    public Employee updateEmployee(EmployeeUpdateRequest request) {
        return employeeRepository.findById(request.getId())
                .map( employee -> {
                    employee.setFullName(request.getFullName());
                    return employeeRepository.save(employee);
                })
                .orElseThrow(() -> new EmployeeNotFoundException());
    }

    public Employee getEmployee(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException());
    }

}
