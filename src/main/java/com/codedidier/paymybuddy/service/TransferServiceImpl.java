package com.codedidier.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codedidier.paymybuddy.constants.Fare;
import com.codedidier.paymybuddy.dto.GetTransferDto;
import com.codedidier.paymybuddy.dto.NewTransferDto;
import com.codedidier.paymybuddy.entity.Transfer;
import com.codedidier.paymybuddy.entity.User;
import com.codedidier.paymybuddy.exception.BadArgumentException;
import com.codedidier.paymybuddy.exception.DataNotFindException;
import com.codedidier.paymybuddy.exception.IllegalContactException;
import com.codedidier.paymybuddy.exception.InvalidBalanceException;
import com.codedidier.paymybuddy.repository.TransferRepository;
import com.codedidier.paymybuddy.repository.UserRepository;

/**
 * Implementation for transferService. Contain some method to add/remove
 * currency from user balance. Contain some method to create/get transfer.
 */
@Service
public class TransferServiceImpl implements TransferService {

    private final UserRepository userRepository;
    private final TransferRepository transferRepository;
    private final Logger log = LogManager.getLogger(getClass().getName());

    @Autowired
    public TransferServiceImpl(UserRepository userRepository, TransferRepository transferRepository) {
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
    }

    /**
     * This method will add some cash to User balance.
     *
     * @param theAmount the integer to add
     */
    @Override
    public void addCash(int theAmount, String email) {

        // Check amount > 0
        if (theAmount < 1) {
            log.warn("KO - Amount must be > 0. Amount : " + theAmount);
            throw new BadArgumentException("KO - Amount must be > 0.");
        }
        // Get User

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            log.warn("KO - Can't find user: " + email);
            throw new DataNotFindException("KO - Can't find user: " + email);
        }

        BigDecimal amount = BigDecimal.valueOf(theAmount).setScale(3, RoundingMode.HALF_DOWN);
        User user = userOptional.get();
        log.info("Adding amount : " + amount + " to user balance : " + email);
        // Add Amount
        BigDecimal userBalance = user.getBalance();

        user.setBalance(amount.add(userBalance));
        log.trace("UserBalance : " + userBalance);
        log.trace("Final user balance : " + user.getBalance());
        // save
        userRepository.save(user);
        log.info("Succressfully added " + amount + " to user : " + email);
    }

    /**
     * This method will remove some cash to User balance.
     *
     * @param theAmount the integer to subtract
     */
    @Override
    public void removeCash(String theAmount, String email) {
        // Check theAmount > 0
        BigDecimal amount = new BigDecimal(theAmount).setScale(3, RoundingMode.HALF_DOWN);

        if (amount.signum() <= 0) {
            log.warn("KO - Amount must be > 0.");
            throw new BadArgumentException("KO - Amount must be > 0.");
        }
        // Get User
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.warn("KO - Can't find user: " + email);
            throw new DataNotFindException("KO - Can't find user: " + email);
        }
        // Check User Balance
        User user = userOptional.get();
        BigDecimal userBalance = user.getBalance().setScale(3, RoundingMode.HALF_DOWN);
        log.trace("User balance = " + userBalance);
        BigDecimal checkResult = userBalance.subtract(amount);
        // Add Amount and Check if initialBalance + theAmount < Integer.MAX
        if (checkResult.signum() >= 0) {
            user.setBalance(checkResult.setScale(3, RoundingMode.HALF_DOWN));

            // save
            userRepository.save(user);
            log.info("Successfully removed " + theAmount + " from balance.");
        } else {
            log.warn("Error - Balance can't be negative. Balance : " + checkResult);
            throw new InvalidBalanceException("Error - Balance can't be negative.");
        }
    }

    /**
     * This method will create a new Transfer between two user.
     *
     * @param newTransferDto dto with creditorEmail, debtorEmail, amount and
     *                       description
     */
    @Override
    @Transactional
    public GetTransferDto createTransfer(NewTransferDto newTransferDto) {

        log.info("Creating new transfer - " + newTransferDto);
        // get user (debtor)
        Optional<User> opUser = userRepository.findByEmail(newTransferDto.getDebtorEmail());
        User debtor = new User();
        if (opUser.isPresent()) {
            debtor = opUser.get();
        }

        BigDecimal amount = BigDecimal.valueOf(newTransferDto.getAmount());

        BigDecimal charge = amount.multiply(Fare.TRANSACTION_FARE);

        // check user balance
        if (debtor.getBalance().compareTo(amount.add(charge)) < 0) {
            log.warn("Error InvalidBalanceException - Amount > balance for user : " + debtor.getEmail());
            throw new InvalidBalanceException("Error - Amount > balance for user : " + debtor.getEmail());
        }

        // check if creditor is part of contacts
        User creditor = new User();
        for (User contact : debtor.getContacts()) {
            if (contact.getEmail().equals(newTransferDto.getCreditorEmail())) {
                creditor = contact;
            }
        }

        if (creditor.getEmail() == null) {
            log.warn("Error - User: " + newTransferDto.getCreditorEmail() + " NOT in contacts.");
            throw new IllegalContactException(
                    "Error - User: " + newTransferDto.getCreditorEmail() + " NOT in contacts.");
        }
        // add charge to appAccount User1
        User app = userRepository.getById(1);
        app.setBalance(app.getBalance().add(charge));

        // add amount to contact
        creditor.setBalance(creditor.getBalance().add(amount));

        // remove amount from user
        debtor.setBalance(debtor.getBalance().subtract(amount.add(charge)));

        // create entity Transfer
        Transfer transfer = new Transfer();
        transfer.setCreditorId(creditor.getId());
        transfer.setDebtorId(debtor.getId());
        transfer.setAmount(amount);
        transfer.setCharge(charge);
        transfer.setDescription(newTransferDto.getDescription());

        // save in repository
        log.info("Saving Charge - charge : " + charge);
        userRepository.save(app);

        log.info("Updating creditor and debtor.");
        userRepository.save(creditor);
        userRepository.save(debtor);

        log.info("Saving transfer.");
        transferRepository.save(transfer);

        return new GetTransferDto(
                creditor.getFirstName(), transfer.getDescription(), transfer.getAmount());
    }

    /**
     * This method will get all transfer for the given user.
     *
     * @param userEmail the email of the current user.
     */
    @Override
    public List<GetTransferDto> getTransfers(String userEmail) {

        log.info("Getting transfer for user : " + userEmail);

        Optional<User> userOp = userRepository.findByEmail(userEmail);

        int userId = 0;
        if (userOp.isPresent()) {
            userId = userOp.get().getId();
        }

        List<GetTransferDto> transactions = new ArrayList<>();
        log.info("Getting transaction as creditor - id: " + userId);
        for (Transfer transfer : transferRepository.findAllByCreditorId(userId)) {
            GetTransferDto temp = new GetTransferDto(
                    transfer.getDebtor().getFirstName(), transfer.getDescription(), transfer.getAmount());
            transactions.add(temp);
        }
        log.info("Getting transaction as debtor - id: " + userId);
        for (Transfer transfer : transferRepository.findAllByDebtorId(userId)) {
            GetTransferDto temp = new GetTransferDto(
                    transfer.getCreditor().getFirstName(),
                    transfer.getDescription(),
                    transfer.getAmount().negate());
            transactions.add(temp);
        }
        log.info("User: " + userId + " - " + transactions.size() + " transactions.");
        return transactions;
    }
}