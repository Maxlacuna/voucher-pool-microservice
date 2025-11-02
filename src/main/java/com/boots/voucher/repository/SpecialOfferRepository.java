package com.boots.voucher.repository;

import com.boots.voucher.model.SpecialOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpecialOfferRepository extends JpaRepository<SpecialOffer, UUID> {
    SpecialOffer findByName(String name);
}
