package com.energy.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateDeviceDTO {
    private String name;
    private String description;
    private String address;
    private Double maxConsumption;
}
