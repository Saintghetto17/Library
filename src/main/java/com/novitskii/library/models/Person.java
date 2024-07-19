package com.novitskii.library.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person {
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 100, message = "Full name should be between 3 and 100 characters")
    private String fullName;

    @Min(value=1906, message = "Birth year should be greater than 1906 a.d")
    private int birthYear;
}
