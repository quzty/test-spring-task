package com.example.demo.dto;

import lombok.Data;

@Data
public class ReadDTO {
    private String text;
    private Integer repeatTimes;
    private Long delay;
}
