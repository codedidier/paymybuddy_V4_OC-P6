package com.codedidier.paymybuddy.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.codedidier.paymybuddy.dto.GetTransferDto;
import com.codedidier.paymybuddy.dto.NewTransferDto;
import com.codedidier.paymybuddy.exception.BadArgumentException;
import com.codedidier.paymybuddy.exception.IllegalContactException;
import com.codedidier.paymybuddy.exception.InvalidBalanceException;
import com.codedidier.paymybuddy.service.TransferServiceImpl;
import com.codedidier.paymybuddy.service.UserServiceImpl;

@WebMvcTest(controllers = TransferControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransferControllerImplTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserServiceImpl userServiceMock;
    @MockBean
    TransferServiceImpl transferServiceMock;
    @Mock
    Principal principalMock;

    private final String addCashUrl = "/home/balance/add";
    private final String removeCashUrl = "/home/balance/remove";
    private final String createTransferUrl = "/home/transfer";
    private final String getTransferUrl = "/home/transfer";
    private final String debtorTest = "PrenomTest";
    private final String creditorTest = "NomTest";
    private final String descriptionTest = "testPaiement";
    private final int amount = 50;
    private final String dateTest = "dateTest";
    private final int balance = 100;
    private final String userEmail = "mailtest@mail.com";

    @Test
    public void addCashValid() throws Exception {

        // GIVEN

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        // THEN

        mockMvc
                .perform(
                        post(addCashUrl + "?amount=" + amount)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isAccepted())
                .andExpect(view().name("success"));
    }

    @Test
    public void addCashInvalid() throws Exception {

        // GIVEN

        // WHEN

        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        // THEN

        mockMvc
                .perform(
                        post(addCashUrl + "?amount=" + 0.1)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCashWhenAmountEquals0_ShouldThrowIllegalArgumentException() throws Exception {

        // GIVEN

        // WHEN

        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.doThrow(BadArgumentException.class)
                .when(transferServiceMock)
                .addCash(Mockito.anyInt(), Mockito.anyString());
        // THEN

        mockMvc
                .perform(
                        post(addCashUrl + "?amount=" + 0)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCashWhenFinalBalanceIsOverMax_ShouldThrowAnException() throws Exception {

        // GIVEN

        // WHEN

        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.doThrow(InvalidBalanceException.class)
                .when(transferServiceMock)
                .addCash(Mockito.anyInt(), Mockito.anyString());
        // THEN

        mockMvc
                .perform(
                        post(addCashUrl + "?amount=" + 50)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeCashValid() throws Exception {

        // GIVEN

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.doNothing()
                .when(transferServiceMock)
                .removeCash(Mockito.anyString(), Mockito.anyString());
        // THEN
        mockMvc
                .perform(
                        post(removeCashUrl + "?amount=" + amount)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isAccepted())
                .andExpect(view().name("success"));
    }

    @Test
    public void removeCashInvalid() throws Exception {

        // GIVEN

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.doThrow(BadArgumentException.class)
                .when(transferServiceMock)
                .removeCash(Mockito.anyString(), Mockito.anyString());
        // THEN
        mockMvc
                .perform(
                        post(removeCashUrl + "?amount=" + -0.1)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeCashWhenAmountIsOverBalance_ShouldThrowException() throws Exception {

        // GIVEN

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.doThrow(InvalidBalanceException.class)
                .when(transferServiceMock)
                .removeCash(Mockito.anyString(), Mockito.anyString());
        // THEN
        mockMvc
                .perform(
                        post(removeCashUrl + "?amount=" + amount)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createTransferValid() throws Exception {

        // GIVEN
        // NewTransferDto(String creditorEmail, int amount, String description)
        NewTransferDto transferDto = new NewTransferDto(creditorTest, amount, descriptionTest);

        String urlEncoded = "creditorEmail="
                + transferDto.getCreditorEmail()
                + "&amount="
                + transferDto.getAmount()
                + "&description="
                + transferDto.getDescription();

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        // THEN
        mockMvc
                .perform(
                        post(createTransferUrl)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(urlEncoded))
                .andExpect(status().isCreated())
                .andExpect(view().name("success"));
    }

    @Test
    public void createTransferInvalidEmail() throws Exception {

        // GIVEN
        // NewTransferDto(String creditorEmail, int amount, String description)
        NewTransferDto transferDto = new NewTransferDto("", amount, descriptionTest);

        String urlEncoded = "creditorEmail="
                + transferDto.getCreditorEmail()
                + "&amount="
                + transferDto.getAmount()
                + "&description="
                + transferDto.getDescription();

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        // THEN
        mockMvc
                .perform(
                        post(createTransferUrl)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(urlEncoded))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createTransferInvalidAmount() throws Exception {
        // GIVEN

        NewTransferDto transferDto = new NewTransferDto(creditorTest, -0.01, descriptionTest);

        String urlEncoded = "creditorEmail="
                + transferDto.getCreditorEmail()
                + "&amount="
                + transferDto.getAmount()
                + "&description="
                + transferDto.getDescription();

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        // THEN
        mockMvc
                .perform(
                        post(createTransferUrl)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(urlEncoded))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createTransferWhenContactIsInvalid_ShouldThrowIllegalContactException()
            throws Exception {

        // GIVEN
        NewTransferDto transferDto = new NewTransferDto(creditorTest, amount, descriptionTest);

        String urlEncoded = "creditorEmail="
                + transferDto.getCreditorEmail()
                + "&amount="
                + transferDto.getAmount()
                + "&description="
                + transferDto.getDescription();

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.doThrow(IllegalContactException.class)
                .when(transferServiceMock)
                .createTransfer(Mockito.any());
        // THEN
        mockMvc
                .perform(
                        post(createTransferUrl)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(urlEncoded))
                .andExpect(status().isConflict());
    }

    @Test
    public void createTransferWhenAmountVerificationFail_ShouldThrowException() throws Exception {

        // GIVEN
        NewTransferDto transferDto = new NewTransferDto(creditorTest, amount, descriptionTest);

        String urlEncoded = "creditorEmail="
                + transferDto.getCreditorEmail()
                + "&amount="
                + transferDto.getAmount()
                + "&description="
                + transferDto.getDescription();

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.doThrow(InvalidBalanceException.class)
                .when(transferServiceMock)
                .createTransfer(Mockito.any());
        // THEN
        mockMvc
                .perform(
                        post(createTransferUrl)
                                .principal(principalMock)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .content(urlEncoded))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTransferValid() throws Exception {

        // GIVEN

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.when(transferServiceMock.getTransfers(Mockito.anyString()))
                .thenReturn(
                        asList(
                                new GetTransferDto("contactTest1", "descriptionTest", new BigDecimal("5.2")),
                                new GetTransferDto("contactTest2", "descriptionTest", new BigDecimal("10"))));
        // THEN
        mockMvc
                .perform(get(getTransferUrl).principal(principalMock))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("transfers", iterableWithSize(2)))
                .andExpect(view().name("transfer-home"));
    }

    @Test
    void getTransferWhenThereIsNoTransfer_ShouldReturnEmptyList() throws Exception {

        // GIVEN

        // WHEN
        Mockito.when(principalMock.getName()).thenReturn(userEmail);
        Mockito.when(transferServiceMock.getTransfers(Mockito.anyString()))
                .thenReturn(new ArrayList<>());
        // THEN
        mockMvc
                .perform(get(getTransferUrl).principal(principalMock))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("transfers", iterableWithSize(0)))
                .andExpect(view().name("transfer-home"));
    }

}
