package com.sunsophearin.shopease.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

@Data
public class SaleDto {
    private List<SaleDetailDTO> saleDetailDTOS;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
