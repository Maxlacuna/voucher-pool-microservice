package com.boots.voucher.service;

import com.boots.voucher.model.Recipient;
import com.boots.voucher.model.SpecialOffer;
import com.boots.voucher.model.VoucherCode;
import com.boots.voucher.repository.RecipientRepository;
import com.boots.voucher.repository.SpecialOfferRepository;
import com.boots.voucher.repository.VoucherCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

    private final RecipientRepository recipientRepository;
    private final SpecialOfferRepository specialOfferRepository;
    private final VoucherCodeRepository voucherCodeRepository;

    public void generateVouchersForAllRecipients(String specialOfferName, LocalDate expirationDate) {
        logger.info("Generating vouchers for special offer: '{}'", specialOfferName);
        SpecialOffer specialOffer = specialOfferRepository.findByName(specialOfferName.trim());

        if (specialOffer == null) {
            logger.error("Special offer not found: '{}'", specialOfferName);
            return;
        }

        List<Recipient> recipients = recipientRepository.findAll();
        logger.info("Found {} recipients to generate vouchers for.", recipients.size());

        for (Recipient recipient : recipients) {
            VoucherCode voucherCode = VoucherCode.builder()
                    .code(generateUniqueCode())
                    .expirationDate(expirationDate)
                    .redeemed(false)
                    .recipient(recipient)
                    .specialOffer(specialOffer)
                    .build();
            voucherCodeRepository.save(voucherCode);
        }
        logger.info("Finished generating vouchers.");
    }

    public VoucherCode redeemVoucher(String code, String email) {
        VoucherCode voucherCode = voucherCodeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Voucher code not found"));

        if (!voucherCode.getRecipient().getEmail().equals(email)) {
            throw new RuntimeException("Voucher does not belong to this recipient");
        }

        if (voucherCode.isRedeemed()) {
            throw new RuntimeException("Voucher already redeemed");
        }

        if (voucherCode.getExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Voucher has expired");
        }

        voucherCode.setRedeemed(true);
        voucherCode.setRedeemedAt(LocalDateTime.now());
        return voucherCodeRepository.save(voucherCode);
    }

    public Page<VoucherCode> getValidVouchersForRecipient(String email, Pageable pageable) {
        return voucherCodeRepository.findAllByRecipientEmailAndRedeemedIsFalseAndExpirationDateIsAfter(email, LocalDate.now(), pageable);
    }

    public Page<VoucherCode> getRedeemedVouchersForOffer(String offerName, Pageable pageable) {
        return voucherCodeRepository.findAllBySpecialOfferNameAndRedeemedIsTrue(offerName, pageable);
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
