package com.example.demo.owner.bank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "owner_bank")
@Getter
@Setter
public class OwnerBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long bankId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    public OwnerBank(){}


    public OwnerBank(Long ownerId, OwnerBankDTO ownerBankDTO) {
        this.ownerId = ownerId;
        this.name = ownerBankDTO.getName();
        this.accountNumber = ownerBankDTO.getAccountNumber();
    }
    

}
