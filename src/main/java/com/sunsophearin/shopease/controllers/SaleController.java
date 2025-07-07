package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.dto.response.SaleDtoResponse;
import com.sunsophearin.shopease.enums.DeliveryStatus;
import com.sunsophearin.shopease.security.service.impl.RoleCheckerService;
import com.sunsophearin.shopease.security.utils.JwtUtil;
import com.sunsophearin.shopease.services.SaleService;
import com.sunsophearin.shopease.services.impl.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sales")
@RequiredArgsConstructor
@Slf4j
public class SaleController {

    private final PaymentServiceImpl paymentService;
    private final JwtUtil jwtUtil;
    private final RoleCheckerService roleCheckerService;
    private final SaleService saleService;

    @PostMapping("/payment-khqr")
    public ResponseEntity<?> createPayment(
            @RequestBody SaleDto paymentRequest,
            HttpServletRequest request
    ) throws AccessDeniedException {
        if (!roleCheckerService.hasAnyRole(request,"ROLE_USER","ROLE_ADMIN")) {
            throw new AccessDeniedException("User does not have admin role");
        }
        try {
            String email = jwtUtil.getEmailFromRequest(request);
            log.info("Processing payment for user: {}", email);
            Map<String, Object> response = paymentService.generateKhqr(paymentRequest, email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Payment creation failed: {}", e.getMessage(), e);
            throw new RuntimeException("Payment processing failed");
        }
    }

    @PostMapping("/check-payment")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @RequestBody Map<String, String> payload
    ) {
        String md5 = payload.get("md5");
        if (md5 == null || md5.isBlank()) {
            log.warn("Missing MD5 in payment verification");
            return ResponseEntity.badRequest().body(Map.of("error", "MD5 is required"));
        }
        Map<String, Object> response = paymentService.checkPayment(md5);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-order/delivery-status/{status}")
    public ResponseEntity<List<SaleDtoResponse>> getMyOrderByDeliveryStatus(
            @PathVariable String status,
            HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        log.info("Processing delivery for user: {}", email);
        DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(saleService.getSalesByUserNameAndDeliveryStatus(email, deliveryStatus));
    }

    @GetMapping("/saleall")
    public ResponseEntity<?> getSaleall() {
        return ResponseEntity.ok(saleService.getAllSale());
    }

}
