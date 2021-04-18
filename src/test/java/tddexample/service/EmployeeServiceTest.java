package tddexample.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import tddexample.exception.EmployeeNotFoundException;
import tddexample.model.entity.Employee;
import tddexample.model.rest.EmployeeSaveRequest;
import tddexample.model.rest.EmployeeUpdateRequest;


@SpringBootTest
@AutoConfigureTestEntityManager
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void shouldSaveEmployee(){
        EmployeeSaveRequest request = new EmployeeSaveRequest("Frodo Baggins");
        Employee employee = employeeService.saveEmployee(request);

        Employee savedEmployee = transactionTemplate.execute(transactionStatus -> {
            return testEntityManager.find(Employee.class,employee.getId());
        });

        Assertions.assertEquals( "Frodo Baggins", savedEmployee.getFullName() );
        Assertions.assertNotNull( savedEmployee.getId() );
    }

    @Test
    public void shouldUpdateEmployee(){
        Employee employee = transactionTemplate.execute(transactionStatus -> {
            Employee e = testEntityManager.persistAndFlush(new Employee(null,"F. Baggins"));
            transactionStatus.flush();
            return e;
        });

        EmployeeUpdateRequest request = new EmployeeUpdateRequest(employee.getId(),"Frodo Baggins");
        Employee updatedEmployee = employeeService.updateEmployee(request);

        Assertions.assertEquals( "Frodo Baggins", updatedEmployee.getFullName() );
    }

    @Test
    public void shouldThrowEmployeeNotFoundExceptionFromUpdateWhenIdNotExists(){
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(1,"Frodo Baggins");
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(request));
    }

    @Test
    public void shouldGetEmployeeWhenIdExists(){
        Employee savedEmployee = transactionTemplate.execute(transactionStatus -> {
            Employee e = testEntityManager.persistAndFlush(new Employee(null,"Frodo Baggins"));
            transactionStatus.flush();
            return e;
        });
        Assertions.assertEquals(savedEmployee, employeeService.getEmployee(savedEmployee.getId()));
    }

    @Test
    public void shouldThrowEmployeeNotFoundExceptionFromGetWhenIdNotExist(){
        Assertions.assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployee(20));
    }

    @Test
    public void shouldReturnEmployeeWhenFoundByName(){
        Employee savedEmployee = transactionTemplate.execute(transactionStatus -> {
            Employee e = testEntityManager.persistAndFlush(new Employee(null,"Frodo Baggins"));
            transactionStatus.flush();
            return e;
        });

        Employee employeeFoundByName = employeeService.getEmployeeByName("Frodo Baggins");
        Assertions.assertEquals("Frodo Baggins",employeeFoundByName.getFullName());
    }

    @Test
    public void shouldThrowEmployeeNotFoundExceptionWhenNotFoundByName(){
        Assertions.assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeByName("Frodo Baggins"));
    }

    @AfterEach
    public void cleanUp(){
        transactionTemplate.execute(transactionStatus -> {
            testEntityManager.getEntityManager()
                    .createQuery("DELETE FROM Employee").executeUpdate();
            transactionStatus.flush();
            return null;
        });
    }
}
