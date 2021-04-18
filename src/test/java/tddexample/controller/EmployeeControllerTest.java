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
