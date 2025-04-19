package com.scm.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactSearchForm {

    @NotBlank(message = "Please select your field")
    private String field;

    @NotBlank(message = "Please enter your name or email or phone number")
    private String value;
}
