package com.energy.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO {
    private String username;
    private String password1;
    private String password2;
    private String address;
    private String email;
}
