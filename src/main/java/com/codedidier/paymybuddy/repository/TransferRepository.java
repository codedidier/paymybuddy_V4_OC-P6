package com.codedidier.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codedidier.paymybuddy.entity.Transfer;

import java.util.List;

/**
 * Jpa repository for entity : transfer. Contains some custom method to find transfer in data base.
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

  /**
   * This method will return the list of transfer where the user is creditor (money++).
   *
   * @param creditorId the id of the user
   * @return the list of transfer
   */
  List<Transfer> findAllByCreditorId(int creditorId);

  /**
   * This method will return the list of transfer where the user is debtor (money--).
   *
   * @param debtorId the id of the user
   * @return the list of transfer
   */
  List<Transfer> findAllByDebtorId(int debtorId);
}
