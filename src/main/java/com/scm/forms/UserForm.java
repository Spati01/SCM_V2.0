package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

@NotBlank(message = "Username is required!")
@Size(min=3,message = "Min 3 Character is required!")
private String name;
@Email(message = "Invalid Email Address!")
@NotBlank(message = "Email is required!")
@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
private String email;
@NotBlank(message = "Password is required!")
@Size(min=6, message = "Min 6 Character is required!")
private String password;
@NotBlank(message = "About is repuired!")
private String about;
@NotBlank(message = "Phone number is required!")
@Size(min = 10, max = 12, message = "Invalid Phone Number")
private String phoneNumber;

}
