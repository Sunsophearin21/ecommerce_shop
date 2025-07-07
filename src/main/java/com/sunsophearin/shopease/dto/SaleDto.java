package com.sunsophearin.shopease.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;
@Data
public class SaleDto {
    private List<SaleDetailDTO> saleDetailDTOS;
    private String phoneNumber;
    private String username;
    private String address;
    private double latitude;
    private double longitude;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
