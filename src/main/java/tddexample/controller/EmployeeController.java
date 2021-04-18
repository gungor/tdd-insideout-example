package tddexample.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tddexample.model.entity.Employee;
import tddexample.model.rest.EmployeeSaveRequest;
import tddexample.service.EmployeeService;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody EmployeeSaveRequest request) {
        return employeeService.saveEmployee(request);
    }

}
