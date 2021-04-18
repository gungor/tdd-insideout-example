package tddexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tddexample.model.entity.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    Optional<Employee> findByFullName(String fullName);
}
