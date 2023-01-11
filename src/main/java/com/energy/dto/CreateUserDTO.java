package com.energy.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateUserDTO {
    private String username;
    private String password1;
    private String password2;
    private String email;
    private String address;
    private String role;

}
