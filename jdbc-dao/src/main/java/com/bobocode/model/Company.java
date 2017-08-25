package com.bobocode.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
public class Company {
    private Long id;
    private String name;
    private String phone;
}
