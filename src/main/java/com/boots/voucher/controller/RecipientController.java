package com.boots.voucher.controller;

import com.boots.voucher.model.Recipient;
import com.boots.voucher.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipients")
@RequiredArgsConstructor
public class RecipientController {

    private final RecipientRepository recipientRepository;

    @PostMapping
    public ResponseEntity<Recipient> createRecipient(@RequestBody Recipient recipient) {
        return ResponseEntity.ok(recipientRepository.save(recipient));
    }

    @GetMapping
    public ResponseEntity<List<Recipient>> getAllRecipients() {
        return ResponseEntity.ok(recipientRepository.findAll());
    }
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteRecipient(@PathVariable String email) {
        recipientRepository.findAll().stream()
                .filter(r -> r.getEmail().equals(email))
                .findFirst()
                .ifPresent(recipientRepository::delete);
        return ResponseEntity.noContent().build();
    }
}
