package com.boots.voucher.controller;

import com.boots.voucher.model.SpecialOffer;
import com.boots.voucher.repository.SpecialOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/special-offers")
@RequiredArgsConstructor
public class SpecialOfferController {

    private final SpecialOfferRepository specialOfferRepository;

    @PostMapping
    public ResponseEntity<SpecialOffer> createSpecialOffer(@RequestBody SpecialOffer specialOffer) {
        return ResponseEntity.ok(specialOfferRepository.save(specialOffer));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteSpecialOffer(@PathVariable String name) {
        SpecialOffer specialOffer = specialOfferRepository.findByName(name);
        if (specialOffer != null) {
            specialOfferRepository.delete(specialOffer);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SpecialOffer>> getAllSpecialOffers() {
        return ResponseEntity.ok(specialOfferRepository.findAll());
    }
}
