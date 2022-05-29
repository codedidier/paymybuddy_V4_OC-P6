package com.codedidier.paymybuddy.Repository.TestIT;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codedidier.paymybuddy.entity.Transfer;

public interface TransferRepo extends JpaRepository<Transfer, Integer> {

    Transfer getTransferByDescription(String description);
}
