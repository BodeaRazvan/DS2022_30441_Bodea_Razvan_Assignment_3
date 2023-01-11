package com.energy.dto;

import lombok.*;

import java.sql.Date;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasurementDTO {
    private Date date;
    private LocalTime time;
    private Double energy;
}
