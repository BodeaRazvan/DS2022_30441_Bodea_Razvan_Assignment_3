package com.energy.dto;

import com.energy.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TokenDTO {
    User user;
    String token;
}
