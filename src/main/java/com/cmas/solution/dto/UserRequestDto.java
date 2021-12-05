package com.cmas.solution.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDto {
    @JsonProperty("First Name")
    private String firstName;
    @JsonProperty("Last Name")
    private String lastName;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Age")
    private Integer age;
    @JsonProperty("Active")
    private Boolean active;
}
