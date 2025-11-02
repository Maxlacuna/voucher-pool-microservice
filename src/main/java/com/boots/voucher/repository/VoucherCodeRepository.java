package com.boots.voucher.repository;

import com.boots.voucher.model.VoucherCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoucherCodeRepository extends JpaRepository<VoucherCode, UUID> {

    Optional<VoucherCode> findByCode(String code);

    Page<VoucherCode> findAllByRecipientEmailAndRedeemedIsFalseAndExpirationDateIsAfter(String email, LocalDate date, Pageable pageable);

    Page<VoucherCode> findAllBySpecialOfferNameAndRedeemedIsTrue(String offerName, Pageable pageable);

    List<VoucherCode> findAllByExpirationDateBefore(LocalDate date);
}
