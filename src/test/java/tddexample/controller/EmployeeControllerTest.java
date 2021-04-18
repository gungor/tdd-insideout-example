package tddexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.support.TransactionTemplate;
import tddexample.model.entity.Employee;
import tddexample.model.rest.EmployeeSaveRequest;
import tddexample.model.rest.EmployeeUpdateRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJson
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void shouldAddEmployee() throws Exception {
        EmployeeSaveRequest request = new EmployeeSaveRequest("Frodo Baggins");
        MvcResult result = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content( objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Employee savedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(),
                Employee.class );

        Assertions.assertEquals("Frodo Baggins",savedEmployee.getFullName());
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {
        Employee savedEmployee = transactionTemplate.execute(transactionStatus -> {
            Employee e = testEntityManager.persistAndFlush(new Employee(null,"F. Baggins"));
            transactionStatus.flush();
            return e;
        });
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(savedEmployee.getId(),"Frodo Baggins");
        MvcResult result = mockMvc.perform(put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content( objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Employee updatedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(),
                Employee.class );

        Assertions.assertEquals("Frodo Baggins",updatedEmployee.getFullName());
    }

    @Test
    public void shouldGetEmployeeWhenCalledWithExistingId() throws Exception {
        Employee employee = transactionTemplate.execute(transactionStatus -> {
            Employee e = testEntityManager.persistAndFlush(new Employee(null,"Frodo Baggins"));
            transactionStatus.flush();
            return e;
        });

        MvcResult result = mockMvc.perform(get("/employees/"+employee.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Employee savedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(),
                Employee.class );

        Assertions.assertEquals("Frodo Baggins",savedEmployee.getFullName());
    }

    @Test
    public void shouldGetEmployeeWhenCalledWithExistingEmployeeName() throws Exception {
        Employee employee = transactionTemplate.execute(transactionStatus -> {
            Employee e = testEntityManager.persistAndFlush(new Employee(null,"FBaggins"));
            transactionStatus.flush();
            return e;
        });

        MvcResult result = mockMvc.perform(get("/employees/name/"+employee.getFullName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Employee savedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(),
                Employee.class );

        Assertions.assertEquals("FBaggins",savedEmployee.getFullName());
    }

    @AfterEach
    public void cleanUp(){

        transactionTemplate.execute(transactionStatus -> {
            testEntityManager.getEntityManager().createQuery("DELETE FROM Employee")
                    .executeUpdate();
            transactionStatus.flush();
            return null;
        });

    }


}
