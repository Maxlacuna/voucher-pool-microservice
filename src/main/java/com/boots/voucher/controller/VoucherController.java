package com.boots.voucher.controller;

import com.boots.voucher.model.VoucherCode;
import com.boots.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/generate")
    public ResponseEntity<Void> generateVouchers(@RequestParam String specialOfferName,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate) {
        voucherService.generateVouchersForAllRecipients(specialOfferName, expirationDate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redeem")
    public ResponseEntity<VoucherCode> redeemVoucher(@RequestParam String code, @RequestParam String email) {
        return ResponseEntity.ok(voucherService.redeemVoucher(code, email));
    }

    @GetMapping("/recipient/{email}")
    public ResponseEntity<Page<VoucherCode>> getValidVouchersForRecipient(@PathVariable String email, Pageable pageable) {
        return ResponseEntity.ok(voucherService.getValidVouchersForRecipient(email, pageable));
    }

    @GetMapping("/offer/{offerName}")
    public ResponseEntity<Page<VoucherCode>> getRedeemedVouchersForOffer(@PathVariable String offerName, Pageable pageable) {
        return ResponseEntity.ok(voucherService.getRedeemedVouchersForOffer(offerName, pageable));
    }
}
