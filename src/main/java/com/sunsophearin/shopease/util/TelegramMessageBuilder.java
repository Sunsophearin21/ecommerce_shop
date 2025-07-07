package com.sunsophearin.shopease.util;

import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.entities.Sale;
import java.math.BigDecimal;

public class TelegramMessageBuilder {
    public static String buildOrderNotification(
            Sale sale,
            SaleDto saleDto,
            String userEmail,
            String paymentStatus,
            BigDecimal totalAmount
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>🛒 New Order Received!</b>\n\n");
        sb.append("<b>Customer :</b> \n");
        sb.append("<b>Email:</b> ").append(userEmail).append("\n");
        sb.append("<b>Name:</b> ").append(saleDto.getUsername()).append("\n");
        sb.append("<b>Address:</b> ").append(saleDto.getAddress()).append("\n");
        sb.append("<b>Phone Number:</b> ").append(saleDto.getPhoneNumber()).append("\n");
        sb.append("<b>Order ID:</b> ").append(sale.getOrderId()).append("\n");
        sb.append("<b>Date:</b> ").append(DateFormatUtil.format(sale.getCreateAt())).append("\n");
        sb.append("<b>Payment Status:</b> ").append(paymentStatus).append("\n\n");
        sb.append("<b>Items:</b>\n");

        if (saleDto.getSaleDetailDTOS() != null) {
            saleDto.getSaleDetailDTOS().forEach(item -> {
                sb.append("• ").append(item.getProductName())
                        .append(" (x").append(item.getQuantity()).append(") - $")
                        .append(item.getUnitPrice());
                if (item.getColorName() != null && !item.getColorName().isEmpty()) {
                    sb.append(" | Color: ").append(item.getColorName());
                }
                if (item.getSizeName() != null && !item.getSizeName().isEmpty()) {
                    sb.append(" | Size: ").append(item.getSizeName());
                }
                sb.append("\n");
            });
        }

        sb.append("\n<b>Total:</b> $").append(totalAmount).append("\n");
        sb.append("\n<i>Thank you for your business!</i>");
        return sb.toString();
    }
}
