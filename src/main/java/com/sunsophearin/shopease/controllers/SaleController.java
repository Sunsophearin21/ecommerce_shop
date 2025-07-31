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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.EnumMap;
import java.util.HashMap;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPayment(
            @RequestBody SaleDto paymentRequest,
            HttpServletRequest request
    ) throws AccessDeniedException {
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
    public ResponseEntity<?> getMyOrderByDeliveryStatus(
            @PathVariable String status,
            HttpServletRequest request
    ) {
        String email = jwtUtil.getEmailFromRequest(request);
        log.info("Processing delivery for user: {}", email);

        DeliveryStatus deliveryStatus;
        try {
            deliveryStatus = DeliveryStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid delivery status: " + status);
        }

        List<SaleDtoResponse> orders = saleService.getSalesByUserNameAndDeliveryStatus(email, deliveryStatus);

        // Even if orders is empty, it's safe to return 200 OK
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/grouped")
    @PreAuthorize("isAuthenticated()") // Ensures endpoint is protected
    public ResponseEntity<?> getMyOrdersGrouped(HttpServletRequest request) {
        try {
            // 🔐 Extract email from JWT
            String email = jwtUtil.getEmailFromRequest(request);
            log.info("Processing delivery get user: {}", email);
            if (email == null) {
                log.warn("Unauthorized access attempt to /my-order/grouped (missing or invalid token).");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Unauthorized"));
            }

            // 📦 Fetch orders grouped by DeliveryStatus (enum map)
            Map<DeliveryStatus, List<SaleDtoResponse>> grouped = saleService.getSalesGroupedByStatus(email);

            // 🔄 Convert DeliveryStatus enum to String keys for frontend
            Map<String, List<SaleDtoResponse>> result = new HashMap<>();
            for (Map.Entry<DeliveryStatus, List<SaleDtoResponse>> entry : grouped.entrySet()) {
                result.put(entry.getKey().name(), entry.getValue());
            }

            return ResponseEntity.ok(result);

        } catch (Exception ex) {
            log.error("❌ Failed to fetch grouped orders", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }



    @GetMapping("/saleall")
    public ResponseEntity<?> getSaleall() {
        return ResponseEntity.ok(saleService.getAllSale());
    }

}
