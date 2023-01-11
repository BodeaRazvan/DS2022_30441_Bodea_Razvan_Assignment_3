package com.energy.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EditUserDTO {
    private Long id;
    private String username;
    private String email;
    private String address;
    private String role;
}
