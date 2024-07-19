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
public class Book {
    private int id;

    @NotEmpty(message = "Name of book should exist")
    private String name;

    @NotEmpty(message = "Name of author should exist")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100")
    private String author;

    @Min(value = 0, message = "Year should be greater than 0 a.d")
    private int year;

    private Integer personId;
}
