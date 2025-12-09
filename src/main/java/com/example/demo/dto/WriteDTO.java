package com.example.demo.dto;

import lombok.Data;

@Data
public class WriteDTO {
    private String userName;
    private String text;
    private Integer repeatTimes;
    private Long delay;
}
