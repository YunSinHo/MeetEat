package com.example.demo.owner.bank;

import org.springframework.stereotype.Service;

@Service
public class OwnerBankService {

    private final OwnerBankRepository ownerBankRepository;

    public OwnerBankService(OwnerBankRepository ownerBankRepository) {
        this.ownerBankRepository = ownerBankRepository;
    }
    public void saveBank(OwnerBank bank) {
        ownerBankRepository.save(bank);
    }

}
