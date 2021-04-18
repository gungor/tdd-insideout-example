package tddexample.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import tddexample.model.entity.Employee;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldReturnEmployee(){
        entityManager.persistAndFlush(new Employee(null,"Frodo Baggins"));

        entityManager.clear();
        Employee employee = employeeRepository.findByFullName("Frodo Baggins").get();
        Assertions.assertEquals( "Frodo Baggins", employee.getFullName() );
    }

}
