package tddexample.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class EmployeeUpdateRequest {

    @NotNull
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("fullName")
    @NotEmpty
    private String fullName;
}
