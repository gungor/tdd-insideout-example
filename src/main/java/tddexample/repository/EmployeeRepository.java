package tddexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tddexample.model.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {


}
