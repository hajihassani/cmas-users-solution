package com.cmas.solution.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "user")
@Relation(itemRelation = "user", collectionRelation = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto extends RepresentationModel<UserResponseDto> {
    @JsonProperty("User ID")
    private Long userId;
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
