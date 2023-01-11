package com.energy.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "date")
    private Date date;

    @Column(name= "time")
    private LocalTime time;

    @Column(name="energy")
    private Double energy;

    @ManyToOne
    @JoinColumn(name="device_id")
    @JsonIgnoreProperties("measurements")
    private Device device;
}
