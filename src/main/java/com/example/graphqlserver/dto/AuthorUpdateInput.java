package com.example.graphqlserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorUpdateInput {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String birthDate;
    private String nationality;
}
