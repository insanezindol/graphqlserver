package com.example.graphqlserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorInput {
    private String name;
    private String email;
    private String bio;
    private String birthDate;
    private String nationality;
}
