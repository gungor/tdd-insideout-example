package tddexample.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import tddexample.model.entity.Employee;
import tddexample.model.rest.EmployeeSaveRequest;


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
