package com.codedidier.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codedidier.paymybuddy.constants.Fare;
import com.codedidier.paymybuddy.dto.GetTransferDto;
import com.codedidier.paymybuddy.dto.NewTransferDto;
import com.codedidier.paymybuddy.entity.Transfer;
import com.codedidier.paymybuddy.entity.User;
import com.codedidier.paymybuddy.exception.BadArgumentException;
import com.codedidier.paymybuddy.exception.IllegalContactException;
import com.codedidier.paymybuddy.exception.InvalidBalanceException;
import com.codedidier.paymybuddy.repository.TransferRepository;
import com.codedidier.paymybuddy.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    UserRepository userRepositoryMock;
    @Mock
    TransferRepository transferRepositoryMock;

    @InjectMocks
    TransferServiceImpl transferService;

    private int amount = 50;
    private BigDecimal balance = BigDecimal.valueOf(100);
    private final String userEmail = "testEmail";
    private final String debtor = "debtorTest";
    private final String creditor = "creditorTest";
    private final String descriptionTest = "Test0018574685";
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(userEmail, "userLastName", "userFirstName", "pass");
        user.setBalance(BigDecimal.ZERO);
    }

    @Test
    void addCashValid() {

        // GIVEN
        // valeur à ajouter = 50
        // valeur déjà présente = 100
        user.setBalance(balance);
        // WHEN
        // mock des appels au repo
        Mockito.when(userRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        // appel du service
        transferService.addCash(amount, userEmail);
        // THEN
        // verif que résult = add + init
        Assertions.assertThat(user.getBalance())
                .isEqualTo(balance.add(BigDecimal.valueOf(amount).setScale(3)));
    }

    @Test
    public void addCashWhenAdd0_ShouldThrowIllegalArgumentException() {
        // GIVEN
        // valeur à ajouter == 0

        // valeur déjà présente
        user.setBalance(balance);
        // WHEN
        // mock des appels au repo

        // appel du service

        // THEN
        // verif que l'exception est bien lancée
        org.junit.jupiter.api.Assertions.assertThrows(
                BadArgumentException.class, () -> transferService.addCash(0, userEmail));
    }

    @Test
    void removeCashValid() {

        // GIVEN
        // valeur à soustraire 50
        String amountD = "50.5";
        // valeur déjà présente 100
        user.setBalance(balance);

        // WHEN
        // Mock des appels au repo
        Mockito.when(userRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        // appel du service
        transferService.removeCash(amountD, userEmail);
        // THEN
        // vérif que result = 100 - 50
        Assertions.assertThat(user.getBalance())
                .isEqualTo(balance.subtract(new BigDecimal(amountD).setScale(3, RoundingMode.HALF_DOWN)));
    }

    @Test
    public void removeCashWhenBalanceEqualsRemove_ResultBalanceEqual0() {

        // GIVEN
        // valeur à soustraire 100
        String amount = "100";
        // valeur déjà présente 100
        user.setBalance(balance);

        // WHEN
        // Mock des appels au repo
        Mockito.when(userRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        // appel du service
        transferService.removeCash(amount, userEmail);
        // THEN
        // vérif que result = 100 - 100
        Assertions.assertThat(user.getBalance().signum()).isEqualTo(0);
    }

    @Test
    public void removeCashWhenBalanceIsInferiorToRemove_ShouldThrowAnException() {

        // GIVEN
        // valeur à soustraire 50
        String amount = "50.5";
        // valeur déjà présente 20

        user.setBalance(BigDecimal.valueOf(20));

        // WHEN
        // Mock des appels au repo
        Mockito.when(userRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        // appel du service

        // THEN
        // vérif qu'on jette bien l'exception
        org.junit.jupiter.api.Assertions.assertThrows(
                InvalidBalanceException.class, () -> transferService.removeCash(amount, userEmail));
    }

    // quand tout est OK
    @Test
    public void createTransferValid() {

        // GIVEN
        double amountD = 50.5;

        NewTransferDto newTransfer = new NewTransferDto();
        newTransfer.setDebtorEmail(debtor);
        newTransfer.setCreditorEmail(creditor);
        newTransfer.setAmount(amountD);
        newTransfer.setDescription(descriptionTest);

        User user = new User(debtor, "lastNameUser", "firstNameUser", "pass");
        user.setBalance(balance);
        user.setId(1);
        User contact = new User(creditor, "lastNameContact", "firstNameContact", "pass");
        contact.setBalance(balance);
        // contact.setId(2);
        user.getContacts().add(contact);

        // WHEN
        Mockito.when(userRepositoryMock.findByEmail(debtor)).thenReturn(Optional.of(user));
        Mockito.when(userRepositoryMock.getById(1)).thenReturn(new User());
        GetTransferDto expected = new GetTransferDto(contact.getFirstName(), descriptionTest,
                BigDecimal.valueOf(amountD));
        GetTransferDto actual = transferService.createTransfer(newTransfer);

        BigDecimal charge = Fare.TRANSACTION_FARE.multiply(BigDecimal.valueOf(amountD));

        // THEN
        Assertions.assertThat(user.getBalance())
                .isEqualTo(balance.subtract(BigDecimal.valueOf(amountD).add(charge)));
        Assertions.assertThat(contact.getBalance()).isEqualTo(balance.add(BigDecimal.valueOf(amountD)));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    // when contactEmail is not part of user's contacts
    @Test
    void createTransferWhenCreditorIsNotAContact_ShouldThrowIllegalContactException() {

        // GIVEN
        NewTransferDto newTransfer = new NewTransferDto();
        newTransfer.setDebtorEmail(debtor);
        newTransfer.setCreditorEmail(creditor);
        newTransfer.setAmount(amount);
        newTransfer.setDescription(descriptionTest);

        User user = new User(debtor, "lastNameUser", "firstNameUser", "pass");
        user.setBalance(balance);

        // WHEN
        Mockito.when(userRepositoryMock.findByEmail(debtor)).thenReturn(Optional.of(user));
        // GetTransferDto(String contactName, String description, int amount)

        // THEN
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalContactException.class, () -> transferService.createTransfer(newTransfer));
    }

    // when debtor have not enough money
    @Test
    void createTransferWhenAmountIsOverDebtorBalance_ShouldThrowException() {

        // GIVEN
        NewTransferDto newTransfer = new NewTransferDto();
        newTransfer.setDebtorEmail(debtor);
        newTransfer.setCreditorEmail(creditor);
        newTransfer.setAmount(balance.intValue() + 500);
        newTransfer.setDescription(descriptionTest);

        User user = new User(debtor, "lastNameUser", "firstNameUser", "pass");
        user.setBalance(balance);
        User contact = new User(creditor, "lastNameContact", "firstNameContact", "pass");
        user.getContacts().add(contact);

        // WHEN
        Mockito.when(userRepositoryMock.findByEmail(debtor)).thenReturn(Optional.of(user));
        // GetTransferDto(String contactName, String description, int amount)

        // THEN
        org.junit.jupiter.api.Assertions.assertThrows(
                InvalidBalanceException.class, () -> transferService.createTransfer(newTransfer));
    }

    // email -> List<Transfer>
    @Test
    void getTransfersValid() {

        // GIVEN
        User user = new User();
        user.setId(5);
        List<Transfer> transfers1 = new ArrayList<Transfer>();
        for (int i = 1; i < 5; i++) {
            Transfer transfer = new Transfer();
            transfer.setId(i);
            transfer.setDebtorId(i);
            transfer.setCreditorId(5);
            transfer.setAmount(BigDecimal.valueOf(1.5 + i));
            transfer.setDescription("");
            User debtor = new User();
            debtor.setFirstName("debtor" + i);
            transfer.setDebtor(debtor);
            transfers1.add(transfer);
        }
        List<Transfer> transfers2 = new ArrayList<Transfer>();
        for (int i = 5; i < 7; i++) {
            Transfer transfer = new Transfer();
            transfer.setId(i);
            transfer.setDebtorId(5);
            transfer.setCreditorId(i);
            transfer.setAmount(BigDecimal.valueOf(1.5 + i));
            transfer.setDescription("");
            User creditor = new User();
            creditor.setFirstName("creditor" + i);
            transfer.setCreditor(creditor);
            transfers2.add(transfer);
        }
        // WHEN
        Mockito.when(userRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(transferRepositoryMock.findAllByCreditorId(Mockito.anyInt()))
                .thenReturn(transfers1);
        Mockito.when(transferRepositoryMock.findAllByDebtorId(Mockito.anyInt())).thenReturn(transfers2);
        List<GetTransferDto> actual = transferService.getTransfers(userEmail);
        // THEN
        Assertions.assertThat(actual.size()).isEqualTo(transfers1.size() + transfers2.size());

    }

    @Test
    void getTransfersWhenThereIsNoTransfer_ShouldReturnEmptyList() {

        // GIVEN
        User user = new User();
        user.setId(15);

        // WHEN
        Mockito.when(userRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(transferRepositoryMock.findAllByCreditorId(Mockito.anyInt()))
                .thenReturn(new ArrayList<>());
        Mockito.when(transferRepositoryMock.findAllByDebtorId(Mockito.anyInt())).thenReturn(new ArrayList<>());
        List<GetTransferDto> actual = transferService.getTransfers(userEmail);
        // THEN
        Assertions.assertThat(actual.isEmpty()).isTrue();

    }
}
